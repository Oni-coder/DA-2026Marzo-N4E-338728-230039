package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.excepciones.HipodromoException;

public abstract class EstadoCarrera {

    private Carrera carrera;
    private Estado estado;

    public EstadoCarrera(Carrera carrera, Estado estado) {
        this.carrera = carrera;
        this.estado = estado;
    }

    public Carrera getCarrera() { return carrera; }
    public Estado getEstado()   { return estado; }

    public abstract void abrir()                          throws HipodromoException;
    public abstract void cerrar()                         throws HipodromoException;
    public abstract void finalizar(Participacion ganador) throws HipodromoException;
    public abstract boolean aceptaApuestas();
    public abstract void actualizarEstado();
}
