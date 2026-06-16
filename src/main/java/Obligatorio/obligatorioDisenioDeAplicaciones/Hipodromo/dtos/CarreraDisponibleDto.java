package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.dtos;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Carrera;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Jornada;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Participacion;

@Getter
public class CarreraDisponibleDto {

    private String fechaJornada;
    private int numero;
    private String nombre;
    private String estado;
    private List<ParticipacionDisponibleDto> participaciones;

    public CarreraDisponibleDto(Jornada jornada, Carrera carrera) {
        this.fechaJornada = jornada.getFecha().toString();
        this.numero = carrera.getNumero();
        this.nombre = carrera.getNombre();
        this.estado = carrera.getEstado().name();
        this.participaciones = new ArrayList<>();
        for (Participacion p : carrera.getParticipaciones()) {
            participaciones.add(new ParticipacionDisponibleDto(p));
        }
    }
}
