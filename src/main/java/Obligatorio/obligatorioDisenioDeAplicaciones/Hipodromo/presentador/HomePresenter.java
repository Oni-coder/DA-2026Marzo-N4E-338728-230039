package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.presentador;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Scope("session")
public class HomePresenter {

    @GetMapping("/")
    public String home() {
        return "redirect:/index.html";
    }
}
