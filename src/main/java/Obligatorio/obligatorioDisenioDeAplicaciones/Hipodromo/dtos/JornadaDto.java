package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.dtos;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Carrera;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Jornada;

@Getter
public class JornadaDto {

    private String fecha;
    private List<CarreraDto> carreras;

    public JornadaDto(Jornada jornada) {
        this.fecha = jornada.getFecha().toString();
        this.carreras = new ArrayList<>();
        for (Carrera carrera : jornada.getCarreras()) {
            this.carreras.add(new CarreraDto(carrera));
        }
    }
}
