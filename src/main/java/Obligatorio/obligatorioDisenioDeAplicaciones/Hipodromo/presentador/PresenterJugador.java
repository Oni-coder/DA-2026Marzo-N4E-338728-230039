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
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Jugador;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.observador.Observable;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.observador.Observador;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.servicios.fachada.FachadaServicios;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.utils.ConexionNavegador;
import jakarta.annotation.PreDestroy;

@RestController
@Scope("session")
@RequestMapping("/jugador")
public class PresenterJugador implements Observador {

    private final FachadaServicios fachada;
    private final ConexionNavegador conexionNavegador;
    private Jugador jugadorActual;

    public PresenterJugador(FachadaServicios fachada, ConexionNavegador conexionNavegador) {
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
    public Commands tablero(@SessionAttribute(name = "jugador", required = false) Jugador jugador) {
        if (jugador == null) return Commands.create(new Command("usuarioNoAutenticado", "loginJugador.html"));
        jugadorActual = jugador;
        return Commands.create(new Command("tablero", fachada.getTableroJugador(jugador)));
    }

    @PostMapping("/realizarApuesta")
    public Commands realizarApuesta(@SessionAttribute(name = "jugador", required = false) Jugador jugador,
                                     @RequestParam String fechaJornada,
                                     @RequestParam int numeroCarrera,
                                     @RequestParam int numeroParticipacion,
                                     @RequestParam double monto,
                                     @RequestParam String nombreModalidad,
                                     @RequestParam String contrasena) throws HipodromoException {
        if (jugador == null) return Commands.create(new Command("usuarioNoAutenticado", "loginJugador.html"));
        jugadorActual = jugador;
        fachada.realizarApuesta(jugador, LocalDate.parse(fechaJornada), numeroCarrera,
                numeroParticipacion, monto, nombreModalidad, contrasena);
        return Commands.create(
                new Command("mensaje", "Apuesta realizada exitosamente"),
                new Command("tablero", fachada.getTableroJugador(jugador))
        );
    }

    @Override
    public void actualizar(Object evento, Observable origen) {
        if (FachadaServicios.Eventos.cambioEnCarreras.equals(evento) && jugadorActual != null) {
            conexionNavegador.enviarJSON(Commands.create(new Command("tablero", fachada.getTableroJugador(jugadorActual))));
        }
    }

    // Se ejecuta al destruirse este bean de sesión (logout, invalidate() o expiración).
    @PreDestroy
    public void quitarObservador() {
        fachada.quitarObservador(this);
    }
}
