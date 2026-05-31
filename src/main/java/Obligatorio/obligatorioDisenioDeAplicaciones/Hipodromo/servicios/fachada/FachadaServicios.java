package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.servicios.fachada;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.dtos.TableroAdminDto;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.dtos.TableroJugadorDto;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.excepciones.HipodromoException;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test.*;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.observador.Observable;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.servicios.ServicioCarreras;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.servicios.ServicioUsuarios;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FachadaServicios extends Observable {

    public enum Eventos { cambioEnCarreras }

    private final ServicioUsuarios sUsuarios;
    private final ServicioCarreras sCarreras;

    public FachadaServicios() {
        sUsuarios = new ServicioUsuarios();
        sCarreras = new ServicioCarreras();
    }

    // === Configuracion ===

    public void setComision(double porcentaje) { sCarreras.setComision(porcentaje); }
    public double getComision() { return sCarreras.getComision(); }

    // === Usuarios ===

    public void agregarAdministrador(Administrador admin) { sUsuarios.agregarAdministrador(admin); }
    public void agregarJugador(Jugador jugador) { sUsuarios.agregarJugador(jugador); }

    public Administrador loginAdministrador(String nombreUsuario, String contrasena) throws HipodromoException {
        return sUsuarios.loginAdministrador(nombreUsuario, contrasena);
    }

    public void logoutAdministrador(String nombreUsuario) {
        sUsuarios.logoutAdministrador(nombreUsuario);
    }

    public Jugador loginJugador(String nombreUsuario, String contrasena) throws HipodromoException {
        return sUsuarios.loginJugador(nombreUsuario, contrasena);
    }

    public List<Jugador> getJugadores() { return sUsuarios.getJugadores(); }

    // === Caballos ===

    public Caballo agregarCaballo(String nombre) { return sCarreras.agregarCaballo(nombre); }
    public List<Caballo> getCaballos() { return sCarreras.getCaballos(); }

    // === Modalidades ===

    public void agregarModalidad(Modalidad modalidad) { sCarreras.agregarModalidad(modalidad); }
    public List<Modalidad> getModalidades() { return sCarreras.getModalidades(); }

    // === Jornadas ===

    public Jornada getOCrearJornada(LocalDate fecha) { return sCarreras.getOCrearJornada(fecha); }
    public List<Jornada> getJornadas() { return sCarreras.getJornadas(); }

    public LocalDate getFechaJornadaActualOAnterior() {
        Jornada j = sCarreras.getJornadaActualOAnterior();
        return j != null ? j.getFecha() : null;
    }

    public LocalDate getFechaJornadaAnterior(LocalDate fecha) {
        Jornada j = sCarreras.getJornadaAnterior(fecha);
        return j != null ? j.getFecha() : null;
    }

    public LocalDate getFechaJornadaSiguiente(LocalDate fecha) {
        Jornada j = sCarreras.getJornadaSiguiente(fecha);
        return j != null ? j.getFecha() : null;
    }

    // === Tableros ===

    public TableroAdminDto getTableroAdmin(LocalDate fecha) throws HipodromoException {
        Jornada jornada = sCarreras.findJornada(fecha);
        boolean hayAnterior = sCarreras.getJornadaAnterior(fecha) != null;
        boolean haySiguiente = sCarreras.getJornadaSiguiente(fecha) != null;
        return new TableroAdminDto(jornada, hayAnterior, haySiguiente, sCarreras.getComision());
    }

    public TableroJugadorDto getTableroJugador(Jugador jugador) {
        List<Jornada> disponibles = sCarreras.getJornadasConCarrerasDisponibles();
        return new TableroJugadorDto(jugador, disponibles, sCarreras.getModalidades());
    }

    // === Operaciones de Carrera (Admin) ===

    public void abrirCarrera(LocalDate fechaJornada, int numeroCarrera) throws HipodromoException {
        sCarreras.abrirCarrera(fechaJornada, numeroCarrera);
        avisar(Eventos.cambioEnCarreras);
    }

    public void cerrarCarrera(LocalDate fechaJornada, int numeroCarrera) throws HipodromoException {
        sCarreras.cerrarCarrera(fechaJornada, numeroCarrera);
        avisar(Eventos.cambioEnCarreras);
    }

    public void finalizarCarrera(LocalDate fechaJornada, int numeroCarrera, int numeroParticipacionGanadora) throws HipodromoException {
        sCarreras.finalizarCarrera(fechaJornada, numeroCarrera, numeroParticipacionGanadora);
        avisar(Eventos.cambioEnCarreras);
    }

    // === Apuestas (Jugador) — valida contraseña y monto antes de delegar ===

    public void realizarApuesta(Jugador jugador, LocalDate fechaJornada, int numeroCarrera,
                                int numeroParticipacion, double monto, String nombreModalidad,
                                String contrasena) throws HipodromoException {
        if (!jugador.coincideContrasena(contrasena))
            throw new HipodromoException("Contraseña incorrecta");
        if (monto < 1)
            throw new HipodromoException("Monto inválido");
        sCarreras.realizarApuesta(jugador, fechaJornada, numeroCarrera, numeroParticipacion, monto, nombreModalidad);
        avisar(Eventos.cambioEnCarreras);
    }

    // === Helpers para precarga ===

    public Carrera findCarrera(LocalDate fechaJornada, int numeroCarrera) throws HipodromoException {
        return sCarreras.findCarrera(fechaJornada, numeroCarrera);
    }

    public Participacion findParticipacion(Carrera carrera, int numeroParticipacion) throws HipodromoException {
        return sCarreras.findParticipacion(carrera, numeroParticipacion);
    }

    public Modalidad findModalidad(String nombre) throws HipodromoException {
        return sCarreras.findModalidad(nombre);
    }
}
