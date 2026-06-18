package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.presentador;

import java.time.LocalDate;

import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.excepciones.HipodromoException;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Administrador;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.observador.Observable;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.observador.Observador;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.servicios.fachada.FachadaServicios;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.utils.ConexionNavegador;
import jakarta.annotation.PreDestroy;

@RestController
@Scope("session")
@RequestMapping("/admin")
public class PresenterAdmin implements Observador {

    private final FachadaServicios fachada;
    private final ConexionNavegador conexionNavegador;
    private LocalDate jornadaActual;

    public PresenterAdmin(FachadaServicios fachada, ConexionNavegador conexionNavegador) {
        this.fachada = fachada;
        this.conexionNavegador = conexionNavegador;
        fachada.agregarObservador(this);
    }

    @GetMapping(value = "/registrarSSE", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter registrarSSE() {
        conexionNavegador.conectarSSE();
        return conexionNavegador.getUltimaConexion();
    }

    @GetMapping("/tablero")
    public Commands tablero(@SessionAttribute(name = "administrador", required = false) Administrador admin) throws HipodromoException {
        if (admin == null) return Commands.create(new Command("usuarioNoAutenticado", "loginAdmin.html"));
        if (jornadaActual == null) jornadaActual = fachada.getFechaJornadaActualOAnterior();
        if (jornadaActual == null) return Commands.create(new Command("sinJornadas", "No hay jornadas definidas en el sistema"));
        return Commands.create(tableroCommand());
    }

    @PostMapping("/jornadaAnterior")
    public Commands jornadaAnterior(@SessionAttribute(name = "administrador", required = false) Administrador admin) throws HipodromoException {
        if (admin == null) return Commands.create(new Command("usuarioNoAutenticado", "loginAdmin.html"));
        LocalDate anterior = fachada.getFechaJornadaAnterior(jornadaActual);
        if (anterior == null) throw new HipodromoException("No hay jornada anterior");
        jornadaActual = anterior;
        return Commands.create(tableroCommand());
    }

    @PostMapping("/jornadaSiguiente")
    public Commands jornadaSiguiente(@SessionAttribute(name = "administrador", required = false) Administrador admin) throws HipodromoException {
        if (admin == null) return Commands.create(new Command("usuarioNoAutenticado", "loginAdmin.html"));
        LocalDate siguiente = fachada.getFechaJornadaSiguiente(jornadaActual);
        if (siguiente == null) throw new HipodromoException("No hay jornada siguiente");
        jornadaActual = siguiente;
        return Commands.create(tableroCommand());
    }

    @PostMapping("/abrir")
    public Commands abrirCarrera(@SessionAttribute(name = "administrador", required = false) Administrador admin,
                                  @RequestParam int numeroCarrera) throws HipodromoException {
        if (admin == null) return Commands.create(new Command("usuarioNoAutenticado", "loginAdmin.html"));
        fachada.abrirCarrera(jornadaActual, numeroCarrera);
        return Commands.create(new Command("mensaje", "Carrera abierta exitosamente"), tableroCommand());
    }

    @PostMapping("/cerrar")
    public Commands cerrarCarrera(@SessionAttribute(name = "administrador", required = false) Administrador admin,
                                   @RequestParam int numeroCarrera) throws HipodromoException {
        if (admin == null) return Commands.create(new Command("usuarioNoAutenticado", "loginAdmin.html"));
        fachada.cerrarCarrera(jornadaActual, numeroCarrera);
        return Commands.create(new Command("mensaje", "Carrera cerrada exitosamente"), tableroCommand());
    }

    @PostMapping("/finalizar")
    public Commands finalizarCarrera(@SessionAttribute(name = "administrador", required = false) Administrador admin,
                                      @RequestParam int numeroCarrera,
                                      @RequestParam int numeroParticipacionGanadora) throws HipodromoException {
        if (admin == null) return Commands.create(new Command("usuarioNoAutenticado", "loginAdmin.html"));
        fachada.finalizarCarrera(jornadaActual, numeroCarrera, numeroParticipacionGanadora);
        return Commands.create(new Command("mensaje", "Carrera finalizada y apuestas liquidadas"), tableroCommand());
    }

    private Command tableroCommand() throws HipodromoException {
        return new Command("tablero", fachada.getTableroAdmin(jornadaActual));
    }

    @Override
    public void actualizar(Object evento, Observable origen) {
        if (FachadaServicios.Eventos.cambioEnCarreras.equals(evento) && jornadaActual != null) {
            try {
                conexionNavegador.enviarJSON(Commands.create(tableroCommand()));
            } catch (HipodromoException e) {
                System.out.println("PresenterAdmin: error al construir tablero para push SSE: " + e.getMessage());
            }
        }
    }

    // Se ejecuta cuando la sesión (y por lo tanto este bean @Scope("session")) se destruye:
    // logout, invalidate() o expiración de sesión. Evita observadores "fantasma" en la fachada.
    @PreDestroy
    public void quitarObservador() {
        fachada.quitarObservador(this);
    }
}
