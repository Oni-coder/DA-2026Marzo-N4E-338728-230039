package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.dtos;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.Participacion;
import lombok.Getter;

@Getter
public class ParticipacionDto {

    private int numero;
    private String nombreCaballo;
    private Double dividendo;
    private boolean dividendoValido;
    private double totalApostado;
    private int cantidadApuestas;

    public ParticipacionDto(Participacion participacion) {
        this.numero = participacion.getNumero();
        this.nombreCaballo = participacion.getCaballo().getNombre();
        this.dividendo = participacion.getDividendo();
        this.dividendoValido = participacion.isDividendoValido();
        this.totalApostado = participacion.getTotalApostado();
        this.cantidadApuestas = participacion.getApuestas().size();
    }
}
