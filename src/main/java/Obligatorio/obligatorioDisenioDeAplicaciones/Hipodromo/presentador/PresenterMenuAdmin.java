package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.presentador;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Administrador;

@RestController
@Scope("session")
@RequestMapping("/menuAdmin")
public class PresenterMenuAdmin {

    @GetMapping("/vistaConectada")
    public Commands inicializarVista(@SessionAttribute(name = "administrador", required = false) Administrador admin) {
        if (admin == null)
            return Commands.create(new Command("usuarioNoAutenticado", "loginAdmin.html"));
        return Commands.create(new Command("nombreCompleto", admin.getNombreCompleto()));
    }
}
