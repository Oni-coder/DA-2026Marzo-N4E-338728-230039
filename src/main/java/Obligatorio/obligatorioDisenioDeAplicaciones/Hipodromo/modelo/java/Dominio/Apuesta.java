package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio;

import lombok.Getter;
import java.time.LocalDate;

@Getter
public class Apuesta {

    private double montoNeto;
    private double costoJugador;
    private Modalidad modalidad;
    private Jugador jugador;
    private Participacion participacion;
    private double montoCobrado = 0;
    private LocalDate fechaApuesta = LocalDate.now();

    public Apuesta(double montoNeto, Modalidad modalidad, Jugador jugador, Participacion participacion) {
        this.montoNeto = montoNeto;
        this.modalidad = modalidad;
        this.jugador = jugador;
        this.participacion = participacion;
        this.costoJugador = modalidad.calcularCosto(montoNeto);
    }

    public double calcularPago(double dividendo) {
        return modalidad.calcularPago(montoNeto, dividendo, participacion);
    }

    public void acreditarPago(double monto) {
        this.montoCobrado = monto;
    }
}
