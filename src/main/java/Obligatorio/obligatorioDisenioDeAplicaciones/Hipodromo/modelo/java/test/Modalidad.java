package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test;

import lombok.Getter;

// Strategy: cada subclase define cómo se cobra y cómo se paga
@Getter
public abstract class Modalidad {

    private String nombre;

    public Modalidad(String nombre) {
        this.nombre = nombre;
    }

    public abstract double calcularCosto(double montoNeto);

    public abstract double calcularPago(double montoNeto, double dividendo, Participacion participacion);

}
