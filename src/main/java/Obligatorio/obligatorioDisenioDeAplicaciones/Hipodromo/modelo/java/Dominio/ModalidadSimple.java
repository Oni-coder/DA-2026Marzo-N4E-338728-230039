package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio;

public class ModalidadSimple extends Modalidad {

    public ModalidadSimple() {
        super("Simple");
    }

    @Override
    public double calcularCosto(double montoNeto) {
        return montoNeto;
    }

    @Override
    public double calcularPago(double montoNeto, double dividendo, Participacion participacion) {
        return montoNeto * dividendo;
    }
}
