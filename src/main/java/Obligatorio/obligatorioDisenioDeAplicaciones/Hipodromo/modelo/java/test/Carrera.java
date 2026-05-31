package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.excepciones.HipodromoException;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.observador.Observable;
import lombok.Getter;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Carrera extends Observable {

    public enum Eventos { nuevaApuesta, cambioEstado }

    private int numero;
    private String nombre;
    private Estado estado;
    private List<Participacion> participaciones = new ArrayList<>();
    private Participacion ganador;
    private LocalTime horaFinalizacion;
    private Jornada jornada;

    public Carrera(int numero, String nombre) {
        this.numero = numero;
        this.nombre = nombre;
        this.estado = Estado.DEFINIDA;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    public void agregarParticipacion(Participacion participacion) {
        participacion.setCarrera(this);
        participaciones.add(participacion);
    }

    public double getPozo() {
        double total = 0;
        for (Participacion p : participaciones) {
            total += p.getTotalApostado();
        }
        return total;
    }

    public int getCantApuestas() {
        int total = 0;
        for (Participacion p : participaciones) {
            total += p.getApuestas().size();
        }
        return total;
    }

    public void calcularDividendos(double comisionPorcentaje) {
        double pozoNeto = getPozo() * (1 - comisionPorcentaje / 100);
        for (Participacion p : participaciones) {
            p.recalcularDividendo(pozoNeto);
        }
    }

    public void actualizarEstado() {
        if (estado == Estado.ABIERTA) {
            boolean todoValidos = true;
            for (Participacion p : participaciones) {
                if (!p.isDividendoValido()) { todoValidos = false; break; }
            }
            if (todoValidos) { estado = Estado.ESTABLE; avisar(Eventos.cambioEstado); }
        } else if (estado == Estado.ESTABLE) {
            boolean hayInvalido = false;
            for (Participacion p : participaciones) {
                if (!p.isDividendoValido()) { hayInvalido = true; break; }
            }
            if (hayInvalido) { estado = Estado.ABIERTA; avisar(Eventos.cambioEstado); }
        }
    }

    public void abrir() throws HipodromoException {
        if (estado != Estado.DEFINIDA)
            throw new HipodromoException("No se puede abrir esta carrera.");
        estado = Estado.ABIERTA;
        avisar(Eventos.cambioEstado);
    }

    public void cerrar() throws HipodromoException {
        if (estado != Estado.ESTABLE)
            throw new HipodromoException("No es posible cerrar esta carrera.");
        estado = Estado.CERRADA;
        avisar(Eventos.cambioEstado);
    }

    public void finalizar(Participacion ganador) throws HipodromoException {
        if (estado != Estado.CERRADA)
            throw new HipodromoException("No se puede finalizar esta carrera.");
        if (!participaciones.contains(ganador))
            throw new HipodromoException("Debe indicar el caballo ganador de la carrera");
        this.ganador = ganador;
        this.horaFinalizacion = LocalTime.now();
        estado = Estado.FINALIZADA;
        avisar(Eventos.cambioEstado);
    }

    public boolean aceptaApuestas() {
        return estado == Estado.ABIERTA || estado == Estado.ESTABLE;
    }
}
