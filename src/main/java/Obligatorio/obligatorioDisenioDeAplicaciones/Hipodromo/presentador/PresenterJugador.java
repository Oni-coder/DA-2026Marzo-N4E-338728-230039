package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.presentador;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.excepciones.HipodromoException;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test.Jugador;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.servicios.fachada.FachadaServicios;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@Scope("session")
@RequestMapping("/jugador")
public class PresenterJugador {

    private final FachadaServicios fachada;

    public PresenterJugador(FachadaServicios fachada) {
        this.fachada = fachada;
    }

    @GetMapping("/tablero")
    public Commands tablero(@SessionAttribute(name = "jugador", required = false) Jugador jugador) {
        if (jugador == null) return Commands.create(new Command("usuarioNoAutenticado", "loginJugador.html"));
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
        fachada.realizarApuesta(jugador, LocalDate.parse(fechaJornada), numeroCarrera,
                numeroParticipacion, monto, nombreModalidad, contrasena);
        return Commands.create(
                new Command("mensaje", "Apuesta realizada exitosamente"),
                new Command("tablero", fachada.getTableroJugador(jugador))
        );
    }
}
