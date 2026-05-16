package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test;

import lombok.Getter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Hipodromo {

    private double porcentajeComision;
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Jornada> jornadas = new ArrayList<>();

    public Hipodromo(double porcentajeComision) {
        this.porcentajeComision = porcentajeComision;
    }

    public void setComision(double porcentaje) {
        this.porcentajeComision = porcentaje;
    }

    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public Jornada getOCrearJornada(LocalDate fecha) {
        return jornadas.stream()
                .filter(j -> j.getFecha().equals(fecha))
                .findFirst()
                .orElseGet(() -> {
                    Jornada j = new Jornada(fecha);
                    jornadas.add(j);
                    return j;
                });
    }

}
