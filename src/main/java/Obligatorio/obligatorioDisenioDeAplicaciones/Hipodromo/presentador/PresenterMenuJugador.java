package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.presentador;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Jugador;

@RestController
@Scope("session")
@RequestMapping("/menuJugador")
public class PresenterMenuJugador {

    @GetMapping("/vistaConectada")
    public Commands inicializarVista(@SessionAttribute(name = "jugador", required = false) Jugador jugador) {
        if (jugador == null)
            return Commands.create(new Command("usuarioNoAutenticado", "loginJugador.html"));
        return Commands.create(
                new Command("nombreCompleto", jugador.getNombreCompleto()),
                new Command("saldo", jugador.getSaldo())
        );
    }
}
