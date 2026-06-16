package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.presentador;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.excepciones.HipodromoException;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Administrador;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Jugador;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.servicios.fachada.FachadaServicios;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

@RestController
@Scope("session")
@RequestMapping("/acceso")
public class PresenterLogin {

    private final FachadaServicios fachada;

    public PresenterLogin(FachadaServicios fachada) {
        this.fachada = fachada;
    }

    @PostMapping("/loginJugador")
    public Commands loginJugador(HttpSession sesionHttp,
                                  @RequestParam String nombreUsuario,
                                  @RequestParam String contrasena) throws HipodromoException {
        Jugador jugador = fachada.loginJugador(nombreUsuario, contrasena);
        sesionHttp.setAttribute("jugador", jugador);
        return Commands.create(new Command("loginExitoso", "menuJugador.html"));
    }

    @PostMapping("/logoutJugador")
    public Commands logoutJugador(HttpSession sesionHttp) {
        sesionHttp.removeAttribute("jugador");
        sesionHttp.invalidate();
        return Commands.create(new Command("usuarioNoAutenticado", "index.html"));
    }

    @PostMapping("/loginAdmin")
    public Commands loginAdmin(HttpSession sesionHttp,
                                @RequestParam String nombreUsuario,
                                @RequestParam String contrasena) throws HipodromoException {
        Administrador admin = fachada.loginAdministrador(nombreUsuario, contrasena);
        sesionHttp.setAttribute("administrador", admin);
        return Commands.create(new Command("loginExitoso", "menuAdmin.html"));
    }

    @PostMapping("/logoutAdmin")
    public Commands logoutAdmin(HttpSession sesionHttp) {
        Administrador admin = (Administrador) sesionHttp.getAttribute("administrador");
        if (admin != null) {
            fachada.logoutAdministrador(admin.getNombreUsuario());
            sesionHttp.removeAttribute("administrador");
            sesionHttp.invalidate();
        }
        return Commands.create(new Command("usuarioNoAutenticado", "index.html"));
    }
}
