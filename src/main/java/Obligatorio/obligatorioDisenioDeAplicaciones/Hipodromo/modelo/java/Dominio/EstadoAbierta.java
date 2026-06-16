package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.excepciones.HipodromoException;

public class EstadoAbierta extends EstadoCarrera {

    public EstadoAbierta(Carrera carrera) {
        super(carrera, Estado.ABIERTA);
    }

    @Override
    public void abrir() throws HipodromoException {
        throw new HipodromoException("No se puede abrir esta carrera.");
    }

    @Override
    public void cerrar() throws HipodromoException {
        throw new HipodromoException("No es posible cerrar esta carrera.");
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
            if (!p.isDividendoValido()) return;
        }
        getCarrera().cambiarEstado(new EstadoEstable(getCarrera()));
    }
}
