package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.excepciones.HipodromoException;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.observador.Observable;
import lombok.AccessLevel;
import lombok.Getter;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Carrera extends Observable {

    public enum Eventos { nuevaApuesta, cambioEstado }

    private int numero;
    private String nombre;
    @Getter(AccessLevel.NONE)
    private EstadoCarrera estadoActual;
    private List<Participacion> participaciones = new ArrayList<>();
    private Participacion ganador;
    private LocalTime horaFinalizacion;
    private Jornada jornada;

    public Carrera(int numero, String nombre) {
        this.numero = numero;
        this.nombre = nombre;
        this.estadoActual = new EstadoDefinida(this);
    }

    public Estado getEstado() {
        return estadoActual.getEstado();
    }

    // Llamado por las clases de estado para hacer la transición
    protected void cambiarEstado(EstadoCarrera nuevoEstado) {
        this.estadoActual = nuevoEstado;
        avisar(Eventos.cambioEstado);
    }

    // Llamados por EstadoCerrada al finalizar
    protected void setGanador(Participacion ganador)          { this.ganador = ganador; }
    protected void setHoraFinalizacion(LocalTime hora)        { this.horaFinalizacion = hora; }

    public void setJornada(Jornada jornada)                   { this.jornada = jornada; }

    public void agregarParticipacion(Participacion participacion) {
        participacion.setCarrera(this);
        participaciones.add(participacion);
    }

    public double getPozo() {
        double total = 0;
        for (Participacion p : participaciones) total += p.getTotalApostado();
        return total;
    }

    public int getCantApuestas() {
        int total = 0;
        for (Participacion p : participaciones) total += p.getApuestas().size();
        return total;
    }

    public void calcularDividendos(double comisionPorcentaje) {
        double pozoNeto = getPozo() * (1 - comisionPorcentaje / 100);
        for (Participacion p : participaciones) p.recalcularDividendo(pozoNeto);
    }

    // Delegación al estado actual
    public void abrir()                          throws HipodromoException { estadoActual.abrir(); }
    public void cerrar()                         throws HipodromoException { estadoActual.cerrar(); }
    public void finalizar(Participacion ganador) throws HipodromoException { estadoActual.finalizar(ganador); }
    public boolean aceptaApuestas()                                        { return estadoActual.aceptaApuestas(); }
    public void actualizarEstado()                                         { estadoActual.actualizarEstado(); }
}
