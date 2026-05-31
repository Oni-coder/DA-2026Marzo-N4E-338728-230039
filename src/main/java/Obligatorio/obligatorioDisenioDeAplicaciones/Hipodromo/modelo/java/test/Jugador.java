package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test;

import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Jugador extends Usuario {

    private double saldo;
    private List<Apuesta> apuestas = new ArrayList<>();

    public Jugador(String nombreUsuario, String contrasena, String nombreCompleto, double saldo) {
        super(nombreUsuario, contrasena, nombreCompleto);
        this.saldo = saldo;
    }

    public void realizarApuesta(Participacion participacion, double montoNeto, Modalidad modalidad) {
        Apuesta apuesta = new Apuesta(montoNeto, modalidad, this, participacion);
        descontarSaldo(apuesta.getCostoJugador());
        participacion.agregarApuesta(apuesta);
        apuestas.add(apuesta);
    }

    public void acreditarGanancia(double monto) {
        this.saldo += monto;
    }

    public void descontarSaldo(double monto) {
        this.saldo -= monto;
    }

}
