package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test;

public class ModalidadTriple extends Modalidad {

    public ModalidadTriple() {
        super("Triple");
    }

    @Override
    public double calcularCosto(double montoNeto) {
        return montoNeto * 1.5;
    }

    // Paga x2 si total apostado al caballo < 100.000, x3 si >= 100.000
    @Override
    public double calcularPago(double montoNeto, double dividendo, Participacion participacion) {
        double totalApostadoCaballo = participacion.getTotalApostado();
        double multiplicador = (totalApostadoCaballo < 100_000) ? 2 : 3;
        return montoNeto * dividendo * multiplicador;
    }
}
