package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.dtos;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test.Apuesta;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test.Carrera;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test.Participacion;
import lombok.Getter;

@Getter
public class CarreraFinalizadaDto {

    private int numero;
    private String nombre;
    private String horaFinalizacion;
    private int cantCaballos;
    private double totalApostado;
    private double totalPagado;
    private String nombreGanador;
    private String dividendoFinalGanador;

    public CarreraFinalizadaDto(Carrera carrera) {
        this.numero = carrera.getNumero();
        this.nombre = carrera.getNombre();
        this.horaFinalizacion = carrera.getHoraFinalizacion() != null
                ? carrera.getHoraFinalizacion().toString().substring(0, 5) : "--:--";
        this.cantCaballos = carrera.getParticipaciones().size();
        this.totalApostado = carrera.getPozo();

        double pagado = 0;
        for (Participacion p : carrera.getParticipaciones()) {
            for (Apuesta a : p.getApuestas()) {
                pagado += a.getMontoCobrado();
            }
        }
        this.totalPagado = pagado;

        Participacion ganador = carrera.getGanador();
        this.nombreGanador = ganador != null ? ganador.getCaballo().getNombre() : "—";
        this.dividendoFinalGanador = (ganador != null && ganador.isDividendoValido())
                ? String.format("%.2f", ganador.getDividendo()) : "S/E";
    }
}
