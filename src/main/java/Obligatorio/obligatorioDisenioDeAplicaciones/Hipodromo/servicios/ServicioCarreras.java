package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.servicios;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.excepciones.HipodromoException;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServicioCarreras {

    private Hipodromo hipodromo;
    private List<Modalidad> modalidades = new ArrayList<>();

    public ServicioCarreras() {
        this.hipodromo = new Hipodromo(10.0);
    }

    // === Comision ===

    public void setComision(double porcentaje) { hipodromo.setComision(porcentaje); }
    public double getComision() { return hipodromo.getPorcentajeComision(); }

    // === Caballos ===

    public Caballo agregarCaballo(String nombre) { return hipodromo.agregarCaballo(nombre); }
    public List<Caballo> getCaballos() { return hipodromo.getCaballos(); }

    // === Modalidades ===

    public void agregarModalidad(Modalidad modalidad) { modalidades.add(modalidad); }
    public List<Modalidad> getModalidades() { return modalidades; }

    public Modalidad findModalidad(String nombre) throws HipodromoException {
        for (Modalidad modalidad : modalidades) {
            if (modalidad.getNombre().equalsIgnoreCase(nombre)) return modalidad;
        }
        throw new HipodromoException("Modalidad no encontrada: " + nombre);
    }

    // === Jornadas ===

    public Jornada getOCrearJornada(LocalDate fecha) { return hipodromo.getOCrearJornada(fecha); }
    public List<Jornada> getJornadas() { return hipodromo.getJornadas(); }

    public Jornada getJornadaActualOAnterior() {
        LocalDate hoy = LocalDate.now();
        Jornada resultado = null;
        for (Jornada jornada : hipodromo.getJornadas()) {
            if (!jornada.getFecha().isAfter(hoy)) {
                if (resultado == null || jornada.getFecha().isAfter(resultado.getFecha()))
                    resultado = jornada;
            }
        }
        return resultado;
    }

    public Jornada getJornadaAnterior(LocalDate fecha) {
        Jornada resultado = null;
        for (Jornada jornada : hipodromo.getJornadas()) {
            if (jornada.getFecha().isBefore(fecha)) {
                if (resultado == null || jornada.getFecha().isAfter(resultado.getFecha()))
                    resultado = jornada;
            }
        }
        return resultado;
    }

    public Jornada getJornadaSiguiente(LocalDate fecha) {
        Jornada resultado = null;
        for (Jornada jornada : hipodromo.getJornadas()) {
            if (jornada.getFecha().isAfter(fecha)) {
                if (resultado == null || jornada.getFecha().isBefore(resultado.getFecha()))
                    resultado = jornada;
            }
        }
        return resultado;
    }

    public List<Jornada> getJornadasConCarrerasDisponibles() {
        List<Jornada> resultado = new ArrayList<>();
        for (Jornada jornada : hipodromo.getJornadas()) {
            for (Carrera carrera : jornada.getCarreras()) {
                if (carrera.aceptaApuestas()) { resultado.add(jornada); break; }
            }
        }
        return resultado;
    }

    // === Carreras ===

    public List<Carrera> getCarreras() {
        List<Carrera> todas = new ArrayList<>();
        for (Jornada jornada : hipodromo.getJornadas()) {
            for (Carrera carrera : jornada.getCarreras()) {
                todas.add(carrera);
            }
        }
        return todas;
    }

    public Jornada findJornada(LocalDate fechaJornada) throws HipodromoException {
        for (Jornada jornada : hipodromo.getJornadas()) {
            if (jornada.getFecha().equals(fechaJornada)) return jornada;
        }
        throw new HipodromoException("No existe jornada para la fecha: " + fechaJornada);
    }

    public Carrera findCarrera(LocalDate fechaJornada, int numeroCarrera) throws HipodromoException {
        Jornada jornada = findJornada(fechaJornada);
        for (Carrera carrera : jornada.getCarreras()) {
            if (carrera.getNumero() == numeroCarrera) return carrera;
        }
        throw new HipodromoException("No existe la carrera N° " + numeroCarrera + " en la jornada indicada");
    }

    public Participacion findParticipacion(Carrera carrera, int numeroParticipacion) throws HipodromoException {
        for (Participacion participacion : carrera.getParticipaciones()) {
            if (participacion.getNumero() == numeroParticipacion) return participacion;
        }
        throw new HipodromoException("No existe la participación N° " + numeroParticipacion + " en la carrera");
    }

    // === Operaciones de carrera ===

    public void abrirCarrera(LocalDate fechaJornada, int numeroCarrera) throws HipodromoException {
        findCarrera(fechaJornada, numeroCarrera).abrir();
    }

    public void cerrarCarrera(LocalDate fechaJornada, int numeroCarrera) throws HipodromoException {
        findCarrera(fechaJornada, numeroCarrera).cerrar();
    }

    public void finalizarCarrera(LocalDate fechaJornada, int numeroCarrera, int numeroParticipacionGanadora) throws HipodromoException {
        if (numeroParticipacionGanadora <= 0)
            throw new HipodromoException("Debe indicar el caballo ganador de la carrera");
        Carrera carrera = findCarrera(fechaJornada, numeroCarrera);
        Participacion ganador = findParticipacion(carrera, numeroParticipacionGanadora);
        carrera.finalizar(ganador);
        liquidarApuestas(carrera);
    }

    // === Apuestas ===

    public void realizarApuesta(Jugador jugador, LocalDate fechaJornada, int numeroCarrera,
                                int numeroParticipacion, double monto, String nombreModalidad) throws HipodromoException {
        Carrera carrera = findCarrera(fechaJornada, numeroCarrera);
        if (!carrera.aceptaApuestas())
            throw new HipodromoException("Esta carrera ya no recibe apuestas");
        Participacion participacion = findParticipacion(carrera, numeroParticipacion);
        Modalidad modalidad = findModalidad(nombreModalidad);
        jugador.realizarApuesta(participacion, monto, modalidad);
        carrera.calcularDividendos(hipodromo.getPorcentajeComision());
        carrera.actualizarEstado();
        carrera.avisar(Carrera.Eventos.nuevaApuesta);
    }

    private void liquidarApuestas(Carrera carrera) {
        Participacion ganador = carrera.getGanador();
        double dividendoFinal = ganador.getDividendo();
        for (Apuesta apuesta : ganador.getApuestas()) {
            double pago = apuesta.calcularPago(dividendoFinal);
            apuesta.acreditarPago(pago);
            apuesta.getJugador().acreditarGanancia(pago);
        }
    }

    // === Datos para Tablero Admin ===

    public double getTotalApostadoJornada(Jornada jornada) {
        double total = 0;
        for (Carrera carrera : jornada.getCarreras()) total += carrera.getPozo();
        return total;
    }

    public double getTotalPagadoJornada(Jornada jornada) {
        double total = 0;
        for (Carrera carrera : jornada.getCarreras()) {
            if (carrera.getEstado() == Estado.FINALIZADA) {
                for (Participacion p : carrera.getParticipaciones()) {
                    for (Apuesta a : p.getApuestas()) total += a.getMontoCobrado();
                }
            }
        }
        return total;
    }

    public double getTotalComisionesJornada(Jornada jornada) {
        double total = 0;
        for (Carrera carrera : jornada.getCarreras()) {
            if (carrera.getEstado() == Estado.FINALIZADA)
                total += carrera.getPozo() * (hipodromo.getPorcentajeComision() / 100);
        }
        return total;
    }
}
