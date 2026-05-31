package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.dtos;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test.Apuesta;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test.Carrera;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test.Jornada;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test.Jugador;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test.Modalidad;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TableroJugadorDto {

    private String nombreCompleto;
    private double saldo;
    private double totalApostado;
    private double totalGanado;
    private List<CarreraDisponibleDto> carrerasDisponibles;
    private List<String> modalidades;
    private List<ApuestaHistorialDto> apuestas;

    public TableroJugadorDto(Jugador jugador, List<Jornada> jornadasConDisponibles, List<Modalidad> listaModalidades) {
        this.nombreCompleto = jugador.getNombreCompleto();
        this.saldo = jugador.getSaldo();

        double apostado = 0, ganado = 0;
        for (Apuesta a : jugador.getApuestas()) {
            apostado += a.getMontoNeto();
            ganado += a.getMontoCobrado();
        }
        this.totalApostado = apostado;
        this.totalGanado = ganado;

        this.carrerasDisponibles = new ArrayList<>();
        for (Jornada j : jornadasConDisponibles) {
            for (Carrera c : j.getCarreras()) {
                if (c.aceptaApuestas()) carrerasDisponibles.add(new CarreraDisponibleDto(j, c));
            }
        }

        this.modalidades = new ArrayList<>();
        for (Modalidad m : listaModalidades) this.modalidades.add(m.getNombre());

        // Historial de apuestas más reciente primero
        List<Apuesta> lista = jugador.getApuestas();
        this.apuestas = new ArrayList<>();
        for (int i = lista.size() - 1; i >= 0; i--) {
            apuestas.add(new ApuestaHistorialDto(lista.get(i)));
        }
    }
}
