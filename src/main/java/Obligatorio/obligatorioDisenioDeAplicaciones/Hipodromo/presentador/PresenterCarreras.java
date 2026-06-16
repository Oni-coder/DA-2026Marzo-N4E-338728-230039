package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.presentador;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.dtos.JornadaDto;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.excepciones.HipodromoException;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Administrador;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Jornada;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.observador.Observable;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.observador.Observador;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.servicios.fachada.FachadaServicios;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Scope("session")
@RequestMapping("/carreras")
public class PresenterCarreras implements Observador {

    private final FachadaServicios fachada;

    public PresenterCarreras(FachadaServicios fachada) {
        this.fachada = fachada;
        this.fachada.agregarObservador(this);
    }

    @GetMapping("/vistaConectada")
    public Commands inicializarVista(@SessionAttribute(name = "administrador", required = false) Administrador admin) {
        if (admin == null)
            return Commands.create(new Command("usuarioNoAutenticado", "loginAdmin.html"));
        return Commands.create(
                new Command("nombreCompleto", admin.getNombreCompleto()),
                jornadas()
        );
    }

    @GetMapping("/jornadas")
    public Commands getJornadas(@SessionAttribute(name = "administrador", required = false) Administrador admin) {
        if (admin == null)
            return Commands.create(new Command("usuarioNoAutenticado", "loginAdmin.html"));
        return Commands.create(jornadas());
    }

    @PostMapping("/abrir")
    public Commands abrirCarrera(@SessionAttribute(name = "administrador", required = false) Administrador admin,
                                  @RequestParam String fechaJornada,
                                  @RequestParam int numeroCarrera) throws HipodromoException {
        if (admin == null)
            return Commands.create(new Command("usuarioNoAutenticado", "loginAdmin.html"));
        fachada.abrirCarrera(LocalDate.parse(fechaJornada), numeroCarrera);
        return Commands.create(new Command("mensaje", "Carrera abierta exitosamente"), jornadas());
    }

    @PostMapping("/cerrar")
    public Commands cerrarCarrera(@SessionAttribute(name = "administrador", required = false) Administrador admin,
                                   @RequestParam String fechaJornada,
                                   @RequestParam int numeroCarrera) throws HipodromoException {
        if (admin == null)
            return Commands.create(new Command("usuarioNoAutenticado", "loginAdmin.html"));
        fachada.cerrarCarrera(LocalDate.parse(fechaJornada), numeroCarrera);
        return Commands.create(new Command("mensaje", "Carrera cerrada exitosamente"), jornadas());
    }

    @PostMapping("/finalizar")
    public Commands finalizarCarrera(@SessionAttribute(name = "administrador", required = false) Administrador admin,
                                      @RequestParam String fechaJornada,
                                      @RequestParam int numeroCarrera,
                                      @RequestParam int numeroParticipacionGanadora) throws HipodromoException {
        if (admin == null)
            return Commands.create(new Command("usuarioNoAutenticado", "loginAdmin.html"));
        fachada.finalizarCarrera(LocalDate.parse(fechaJornada), numeroCarrera, numeroParticipacionGanadora);
        return Commands.create(new Command("mensaje", "Carrera finalizada y apuestas liquidadas"), jornadas());
    }

    private Command jornadas() {
        List<JornadaDto> dto = new ArrayList<>();
        for (Jornada jornada : fachada.getJornadas()) {
            dto.add(new JornadaDto(jornada));
        }
        return new Command("jornadas", dto);
    }

    @Override
    public void actualizar(Object evento, Observable origen) {
        if (FachadaServicios.Eventos.cambioEnCarreras.equals(evento)) {
            System.out.println("PresenterCarreras: cambio en carreras notificado.");
        }
    }
}
