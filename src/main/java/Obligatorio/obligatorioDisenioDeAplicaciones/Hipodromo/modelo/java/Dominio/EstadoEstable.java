package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.excepciones.HipodromoException;

public class EstadoEstable extends EstadoCarrera {

    public EstadoEstable(Carrera carrera) {
        super(carrera, Estado.ESTABLE);
    }

    @Override
    public void abrir() throws HipodromoException {
        throw new HipodromoException("No se puede abrir esta carrera.");
    }

    @Override
    public void cerrar() throws HipodromoException {
        getCarrera().cambiarEstado(new EstadoCerrada(getCarrera()));
    }

    @Override
    public void finalizar(Participacion ganador) throws HipodromoException {
        throw new HipodromoException("No se puede finalizar esta carrera.");
    }

    @Override
    public boolean aceptaApuestas() { return true; }

    @Override
    public void actualizarEstado() {
        for (Participacion p : getCarrera().getParticipaciones()) {
            if (!p.isDividendoValido()) {
                getCarrera().cambiarEstado(new EstadoAbierta(getCarrera()));
                return;
            }
        }
    }
}
