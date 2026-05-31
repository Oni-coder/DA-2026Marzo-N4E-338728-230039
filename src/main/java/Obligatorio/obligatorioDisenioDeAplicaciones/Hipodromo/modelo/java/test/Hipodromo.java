package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test;

import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Hipodromo {

    private double porcentajeComision;
    private List<Jornada> jornadas = new ArrayList<>();
    private List<Caballo> caballos = new ArrayList<>();

    public Hipodromo(double porcentajeComision) {
        this.porcentajeComision = porcentajeComision;
    }

    public void setComision(double porcentaje) {
        this.porcentajeComision = porcentaje;
    }

    public Caballo agregarCaballo(String nombre) {
        Caballo caballo = new Caballo(nombre);
        caballos.add(caballo);
        return caballo;
    }

    public Jornada getOCrearJornada(java.time.LocalDate fecha) {
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
