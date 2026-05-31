package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.dtos;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test.Apuesta;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test.Carrera;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test.Estado;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test.Jornada;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test.Participacion;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TableroAdminDto {

    private String fechaJornada;
    private boolean hayJornadaAnterior;
    private boolean hayJornadaSiguiente;
    private double totalApostado;
    private double totalPagado;
    private double totalComisiones;
    private double balance;
    private int cantCarreras;
    private int cantFinalizadas;
    private int cantPorCorrer;
    private List<CarreraFinalizadaDto> carrerasFinalizadas;
    private List<CarreraProximaDto> carrerasProximas;

    public TableroAdminDto(Jornada jornada, boolean hayAnterior, boolean haySiguiente, double comision) {
        this.fechaJornada = jornada.getFecha().toString();
        this.hayJornadaAnterior = hayAnterior;
        this.hayJornadaSiguiente = haySiguiente;

        List<Carrera> finalizadasList = new ArrayList<>();
        List<Carrera> proximasList = new ArrayList<>();
        double apostado = 0, pagado = 0, comisiones = 0;

        for (Carrera carrera : jornada.getCarreras()) {
            apostado += carrera.getPozo();
            if (carrera.getEstado() == Estado.FINALIZADA) {
                comisiones += carrera.getPozo() * (comision / 100);
                for (Participacion p : carrera.getParticipaciones()) {
                    for (Apuesta a : p.getApuestas()) pagado += a.getMontoCobrado();
                }
                finalizadasList.add(carrera);
            } else {
                proximasList.add(carrera);
            }
        }

        // Ordenar finalizadas desc por numero (bubble sort)
        for (int i = 0; i < finalizadasList.size() - 1; i++) {
            for (int j = 0; j < finalizadasList.size() - 1 - i; j++) {
                if (finalizadasList.get(j).getNumero() < finalizadasList.get(j + 1).getNumero()) {
                    Carrera tmp = finalizadasList.get(j);
                    finalizadasList.set(j, finalizadasList.get(j + 1));
                    finalizadasList.set(j + 1, tmp);
                }
            }
        }

        this.carrerasFinalizadas = new ArrayList<>();
        for (Carrera c : finalizadasList) carrerasFinalizadas.add(new CarreraFinalizadaDto(c));

        this.carrerasProximas = new ArrayList<>();
        for (Carrera c : proximasList) carrerasProximas.add(new CarreraProximaDto(c));

        this.totalApostado = apostado;
        this.totalPagado = pagado;
        this.totalComisiones = comisiones;
        this.balance = apostado - pagado;
        this.cantCarreras = jornada.getCarreras().size();
        this.cantFinalizadas = finalizadasList.size();
        this.cantPorCorrer = proximasList.size();
    }
}
