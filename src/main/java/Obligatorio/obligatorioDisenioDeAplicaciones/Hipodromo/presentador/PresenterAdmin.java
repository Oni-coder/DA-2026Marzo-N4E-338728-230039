package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.presentador;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.excepciones.HipodromoException;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test.Administrador;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.observador.Observable;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.observador.Observador;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.servicios.fachada.FachadaServicios;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@Scope("session")
@RequestMapping("/admin")
public class PresenterAdmin implements Observador {

    private final FachadaServicios fachada;
    private LocalDate jornadaActual;

    public PresenterAdmin(FachadaServicios fachada) {
        this.fachada = fachada;
        fachada.agregarObservador(this);
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
        if (FachadaServicios.Eventos.cambioEnCarreras.equals(evento))
            System.out.println("PresenterAdmin: notificado de cambio en carreras.");
    }
}
