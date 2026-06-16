package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio;

public class ModalidadSuper extends Modalidad {

    public ModalidadSuper() {
        super("Super");
    }

    @Override
    public double calcularCosto(double montoNeto) {
        return montoNeto * 2;
    }

    // Paga x3 si dividendo >= 2, x4 si dividendo < 2
    @Override
    public double calcularPago(double montoNeto, double dividendo, Participacion participacion) {
        double multiplicador = (dividendo >= 2) ? 3 : 4;
        return montoNeto * dividendo * multiplicador;
    }
}
