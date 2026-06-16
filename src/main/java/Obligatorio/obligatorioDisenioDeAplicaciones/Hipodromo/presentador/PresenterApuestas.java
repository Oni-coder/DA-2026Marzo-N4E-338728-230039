package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.presentador;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.dtos.JornadaDto;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.dtos.JugadorDto;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.dtos.ModalidadDto;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.excepciones.HipodromoException;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Carrera;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Jornada;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Jugador;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Modalidad;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.servicios.fachada.FachadaServicios;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Scope("session")
@RequestMapping("/apuestas")
public class PresenterApuestas {

    private final FachadaServicios fachada;

    public PresenterApuestas(FachadaServicios fachada) {
        this.fachada = fachada;
    }

    @GetMapping("/vistaConectada")
    public Commands inicializarVista(@SessionAttribute(name = "jugador", required = false) Jugador jugador) {
        if (jugador == null)
            return Commands.create(new Command("usuarioNoAutenticado", "loginJugador.html"));
        return Commands.create(
                new Command("jugador", new JugadorDto(jugador)),
                carrerasDisponibles(),
                modalidades()
        );
    }

    @PostMapping("/realizar")
    public Commands realizarApuesta(@SessionAttribute(name = "jugador", required = false) Jugador jugador,
                                     @RequestParam String fechaJornada,
                                     @RequestParam int numeroCarrera,
                                     @RequestParam int numeroParticipacion,
                                     @RequestParam double monto,
                                     @RequestParam String nombreModalidad,
                                     @RequestParam(defaultValue = "") String contrasena) throws HipodromoException {
        if (jugador == null)
            return Commands.create(new Command("usuarioNoAutenticado", "loginJugador.html"));
        fachada.realizarApuesta(jugador, LocalDate.parse(fechaJornada), numeroCarrera, numeroParticipacion, monto, nombreModalidad, contrasena);
        return Commands.create(
                new Command("mensaje", "Apuesta realizada exitosamente"),
                new Command("jugador", new JugadorDto(jugador)),
                carrerasDisponibles()
        );
    }

    private Command carrerasDisponibles() {
        List<JornadaDto> dto = new ArrayList<>();
        for (Jornada jornada : fachada.getJornadas()) {
            for (Carrera carrera : jornada.getCarreras()) {
                if (carrera.aceptaApuestas()) {
                    dto.add(new JornadaDto(jornada));
                    break;
                }
            }
        }
        return new Command("carrerasDisponibles", dto);
    }

    private Command modalidades() {
        List<ModalidadDto> dto = new ArrayList<>();
        for (Modalidad modalidad : fachada.getModalidades()) {
            dto.add(new ModalidadDto(modalidad.getNombre()));
        }
        return new Command("modalidades", dto);
    }
}
