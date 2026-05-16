package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test;

import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Carrera {

    private int numero;
    private String nombre;
    private Estado estado;
    private List<Participacion> participaciones = new ArrayList<>();
    private Participacion ganador;

    public Carrera(int numero, String nombre) {
        this.numero = numero;
        this.nombre = nombre;
        this.estado = Estado.DEFINIDA;
    }

    public void agregarParticipacion(Participacion participacion) {
        participaciones.add(participacion);
    }

    public double getPozo() {
        return participaciones.stream()
                .mapToDouble(Participacion::getTotalApostado)
                .sum();
    }

    public void calcularDividendos(double comisionPorcentaje) {
        double pozoNeto = getPozo() * (1 - comisionPorcentaje / 100);
        participaciones.forEach(p -> p.recalcularDividendo(pozoNeto));
    }

    // Llamar luego de calcularDividendos para actualizar estado Abierta <-> Estable
    public void actualizarEstado() {
        if (estado == Estado.ABIERTA) {
            boolean todoValidos = participaciones.stream().allMatch(Participacion::isDividendoValido);
            if (todoValidos) estado = Estado.ESTABLE;
        } else if (estado == Estado.ESTABLE) {
            boolean hayInvalido = participaciones.stream().anyMatch(p -> !p.isDividendoValido());
            if (hayInvalido) estado = Estado.ABIERTA;
        }
    }

    public void abrir() {
        if (estado != Estado.DEFINIDA) throw new IllegalStateException("Solo una carrera Definida puede abrirse.");
        estado = Estado.ABIERTA;
    }

    public void cerrar() {
        if (estado != Estado.ESTABLE) throw new IllegalStateException("Solo una carrera Estable puede cerrarse.");
        estado = Estado.CERRADA;
    }

    public void finalizar(Participacion ganador) {
        if (estado != Estado.CERRADA) throw new IllegalStateException("Solo una carrera Cerrada puede finalizarse.");
        if (!participaciones.contains(ganador)) throw new IllegalArgumentException("El ganador debe ser un participante de la carrera.");
        this.ganador = ganador;
        estado = Estado.FINALIZADA;
    }

    public boolean aceptaApuestas() {
        return estado == Estado.ABIERTA || estado == Estado.ESTABLE;
    }

}
