package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio;

import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Participacion {

    private int numero;
    private Double dividendo;
    private Caballo caballo;
    private List<Apuesta> apuestas = new ArrayList<>();
    private Carrera carrera;

    public Participacion(int numero, Caballo caballo) {
        this.numero = numero;
        this.caballo = caballo;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public void agregarApuesta(Apuesta apuesta) {
        apuestas.add(apuesta);
    }

    public double getTotalApostado() {
        double total = 0;
        for (Apuesta apuesta : apuestas) {
            total += apuesta.getMontoNeto();
        }
        return total;
    }

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
