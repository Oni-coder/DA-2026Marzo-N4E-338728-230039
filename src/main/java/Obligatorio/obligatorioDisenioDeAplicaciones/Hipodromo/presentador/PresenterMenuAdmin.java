package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.presentador;

import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Administrador;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.utils.ConexionNavegador;

@RestController
@Scope("session")
@RequestMapping("/menuAdmin")
public class PresenterMenuAdmin {

    private final ConexionNavegador conexionNavegador;

    public PresenterMenuAdmin(ConexionNavegador conexionNavegador) {
        this.conexionNavegador = conexionNavegador;
    }

    @GetMapping(value = "/registrarSSE", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter registrarSSE() {
        conexionNavegador.conectarSSE();
        return conexionNavegador.getUltimaConexion();
    }

    @GetMapping("/vistaConectada")
    public Commands inicializarVista(@SessionAttribute(name = "administrador", required = false) Administrador admin) {
        if (admin == null)
            return Commands.create(new Command("usuarioNoAutenticado", "loginAdmin.html"));
        return Commands.create(new Command("nombreCompleto", admin.getNombreCompleto()));
    }
}
