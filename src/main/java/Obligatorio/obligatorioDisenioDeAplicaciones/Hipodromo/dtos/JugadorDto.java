package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.dtos;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test.Jugador;
import lombok.Getter;

@Getter
public class JugadorDto {

    private String nombreUsuario;
    private String nombreCompleto;
    private double saldo;
    private int cantidadApuestas;

    public JugadorDto(Jugador jugador) {
        this.nombreUsuario = jugador.getNombreUsuario();
        this.nombreCompleto = jugador.getNombreCompleto();
        this.saldo = jugador.getSaldo();
        this.cantidadApuestas = jugador.getApuestas().size();
    }
}
