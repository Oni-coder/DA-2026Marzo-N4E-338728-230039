package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.excepciones.HipodromoException;
import java.time.LocalTime;

public class EstadoCerrada extends EstadoCarrera {

    public EstadoCerrada(Carrera carrera) {
        super(carrera, Estado.CERRADA);
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
        if (!getCarrera().getParticipaciones().contains(ganador))
            throw new HipodromoException("Debe indicar el caballo ganador de la carrera");
        getCarrera().setGanador(ganador);
        getCarrera().setHoraFinalizacion(LocalTime.now());
        getCarrera().cambiarEstado(new EstadoFinalizada(getCarrera()));
    }

    @Override
    public boolean aceptaApuestas() { return false; }

    @Override
    public void actualizarEstado() {}
}
