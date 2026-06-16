package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.dtos;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Carrera;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Participacion;

@Getter
public class CarreraProximaDto {

    private int numero;
    private String nombre;
    private String estado;
    private int cantCaballos;
    private double totalApostado;
    private int cantApuestas;
    private List<ParticipacionDto> participaciones;

    public CarreraProximaDto(Carrera carrera) {
        this.numero = carrera.getNumero();
        this.nombre = carrera.getNombre();
        this.estado = carrera.getEstado().name();
        this.cantCaballos = carrera.getParticipaciones().size();
        this.totalApostado = carrera.getPozo();
        this.cantApuestas = carrera.getCantApuestas();
        this.participaciones = new ArrayList<>();
        for (Participacion p : carrera.getParticipaciones()) {
            participaciones.add(new ParticipacionDto(p));
        }
    }
}
