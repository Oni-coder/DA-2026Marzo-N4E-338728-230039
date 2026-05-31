package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test;

import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Participacion {

    private int numero;
    private Double dividendo; // null mientras no haya apuestas suficientes
    private Caballo caballo;
    private List<Apuesta> apuestas = new ArrayList<>();

    public Participacion(int numero, Caballo caballo) {
        this.numero = numero;
        this.caballo = caballo;
    }

    public void agregarApuesta(Apuesta apuesta) {
        apuestas.add(apuesta);
    }

    public double getTotalApostado() {
        return apuestas.stream().mapToDouble(Apuesta::getMontoNeto).sum();
    }

    // pozoNeto = total apostado en la carrera ya descontada la comisión
    public void recalcularDividendo(double pozoNeto) {
        double totalApostado = getTotalApostado();
        if (totalApostado == 0) {
            dividendo = null;
        } else {
            dividendo = pozoNeto / totalApostado;
        }
    }

    public boolean isDividendoValido() {
        return dividendo != null && dividendo > 1;
    }

}
