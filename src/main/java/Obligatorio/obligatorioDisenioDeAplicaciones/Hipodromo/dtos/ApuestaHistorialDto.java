package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.dtos;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Apuesta;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Carrera;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Estado;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Participacion;
import lombok.Getter;

@Getter
public class ApuestaHistorialDto {

    private String fecha;
    private int numeroCarrera;
    private String nombreCarrera;
    private int numeroCaballo;
    private String nombreCaballo;
    private double montoApostado;
    private String tipoApuesta;
    private double montoCobrado;
    private String dividendoFinal;
    private String estatus;

    public ApuestaHistorialDto(Apuesta apuesta) {
        Participacion p = apuesta.getParticipacion();
        Carrera c = p.getCarrera();

        this.fecha = c.getJornada().getFecha().toString();
        this.numeroCarrera = c.getNumero();
        this.nombreCarrera = c.getNombre();
        this.numeroCaballo = p.getNumero();
        this.nombreCaballo = p.getCaballo().getNombre();
        this.montoApostado = apuesta.getMontoNeto();
        this.tipoApuesta = apuesta.getModalidad().getNombre();
        this.montoCobrado = apuesta.getMontoCobrado();
        this.dividendoFinal = p.isDividendoValido()
                ? String.format("%.2f", p.getDividendo()) : "S/E";
        this.estatus = c.getEstado() == Estado.FINALIZADA ? "Finalizada" : "Por correr";
    }
}
