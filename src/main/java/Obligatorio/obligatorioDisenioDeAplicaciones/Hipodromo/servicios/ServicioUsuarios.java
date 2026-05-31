package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.servicios;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.excepciones.HipodromoException;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test.Administrador;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test.Jugador;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServicioUsuarios {

    private List<Administrador> administradores = new ArrayList<>();
    private List<Jugador> jugadores = new ArrayList<>();
    private Set<String> adminsLogueados = new HashSet<>();

    public void agregarAdministrador(Administrador admin) {
        administradores.add(admin);
    }

    public void agregarJugador(Jugador jugador) {
        jugadores.add(jugador);
    }

    public Administrador loginAdministrador(String nombreUsuario, String contrasena) throws HipodromoException {
        for (Administrador admin : administradores) {
            if (admin.getNombreUsuario().equals(nombreUsuario) && admin.coincideContrasena(contrasena)) {
                if (adminsLogueados.contains(nombreUsuario))
                    throw new HipodromoException("Acceso denegado: este administrador ya tiene una sesión activa");
                adminsLogueados.add(nombreUsuario);
                return admin;
            }
        }
        throw new HipodromoException("Acceso denegado: Usuario y/o contraseña incorrectos");
    }

    public void logoutAdministrador(String nombreUsuario) {
        adminsLogueados.remove(nombreUsuario);
    }

    public Jugador loginJugador(String nombreUsuario, String contrasena) throws HipodromoException {
        for (Jugador jugador : jugadores) {
            if (jugador.getNombreUsuario().equals(nombreUsuario) && jugador.coincideContrasena(contrasena)) {
                return jugador;
            }
        }
        throw new HipodromoException("Acceso denegado: Usuario y/o contraseña incorrectos");
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public List<Administrador> getAdministradores() {
        return administradores;
    }
}
