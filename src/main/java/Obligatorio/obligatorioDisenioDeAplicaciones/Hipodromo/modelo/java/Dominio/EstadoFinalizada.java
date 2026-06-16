package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.excepciones.HipodromoException;

public class EstadoFinalizada extends EstadoCarrera {

    public EstadoFinalizada(Carrera carrera) {
        super(carrera, Estado.FINALIZADA);
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
    public boolean aceptaApuestas() { return false; }

    @Override
    public void actualizarEstado() {}
}
