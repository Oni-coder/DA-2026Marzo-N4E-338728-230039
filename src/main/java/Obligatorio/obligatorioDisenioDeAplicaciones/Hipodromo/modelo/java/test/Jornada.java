package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test;

import lombok.Getter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Jornada {

    private LocalDate fecha;
    private List<Carrera> carreras = new ArrayList<>();
    private int proximoNumero = 1;

    public Jornada(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Carrera agregarCarrera(String nombre) {
        Carrera carrera = new Carrera(proximoNumero++, nombre);
        carreras.add(carrera);
        return carrera;
    }

}
