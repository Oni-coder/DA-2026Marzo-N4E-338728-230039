package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.dtos;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test.Participacion;
import lombok.Getter;

@Getter
public class ParticipacionDisponibleDto {

    private int numero;
    private String nombreCaballo;
    private String dividendo;        // "S/E" si inválido, número formateado si válido
    private Double dividendoNumerico; // null si inválido, para cálculos en JS
    private double totalApostado;    // necesario para cálculo de Triple en JS

    public ParticipacionDisponibleDto(Participacion p) {
        this.numero = p.getNumero();
        this.nombreCaballo = p.getCaballo().getNombre();
        this.dividendoNumerico = p.getDividendo();
        this.dividendo = p.isDividendoValido() ? String.format("%.2f", p.getDividendo()) : "S/E";
        this.totalApostado = p.getTotalApostado();
    }
}
