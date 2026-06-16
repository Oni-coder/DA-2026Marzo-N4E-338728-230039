package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.dtos;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Carrera;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Participacion;

@Getter
public class CarreraDto {

    private int numero;
    private String nombre;
    private String estado;
    private double pozo;
    private boolean aceptaApuestas;
    private List<ParticipacionDto> participaciones;
    private Integer numeroGanador;

    public CarreraDto(Carrera carrera) {
        this.numero = carrera.getNumero();
        this.nombre = carrera.getNombre();
        this.estado = carrera.getEstado().name();
        this.pozo = carrera.getPozo();
        this.aceptaApuestas = carrera.aceptaApuestas();

        this.participaciones = new ArrayList<>();
        for (Participacion p : carrera.getParticipaciones()) {
            this.participaciones.add(new ParticipacionDto(p));
        }

        Participacion ganador = carrera.getGanador();
        this.numeroGanador = (ganador != null) ? ganador.getNumero() : null;
    }
}
