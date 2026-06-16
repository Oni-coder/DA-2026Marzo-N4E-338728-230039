package Obligatorio.obligatorioDisenioDeAplicaciones;

import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.excepciones.HipodromoException;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio.*;
import Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.servicios.fachada.FachadaServicios;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class ObligatorioDisenioDeAplicacionesApplication {

    public static void main(String[] args) throws HipodromoException {
        ConfigurableApplicationContext context = SpringApplication.run(ObligatorioDisenioDeAplicacionesApplication.class, args);
        FachadaServicios fachada = context.getBean(FachadaServicios.class);
        precargarDatos(fachada);
    }

    private static void precargarDatos(FachadaServicios fachada) throws HipodromoException {

        // Comision del hipodromo: 10%
        fachada.setComision(10.0);

        // Administradores
        fachada.agregarAdministrador(new Administrador("a1", "a1", "Usuario Administrador"));
        fachada.agregarAdministrador(new Administrador("a2", "a2", "Administrador Dos"));

        // Jugadores principales
        fachada.agregarJugador(new Jugador("j1", "j1", "Usuario Jugador", 2000));
        fachada.agregarJugador(new Jugador("j2", "j2", "Jugador Dos", 5000));

        // Jugadores adicionales para las apuestas de carreras pasadas (j3..j22)
        for (int i = 3; i <= 22; i++) {
            fachada.agregarJugador(new Jugador("j" + i, "j" + i, "Jugador " + i, 10000));
        }

        // Caballos
        Caballo relampago  = fachada.agregarCaballo("Relámpago");
        Caballo tormenta   = fachada.agregarCaballo("Tormenta");
        Caballo vendaval   = fachada.agregarCaballo("Vendaval");
        Caballo estrella   = fachada.agregarCaballo("Estrella");
        Caballo cometa     = fachada.agregarCaballo("Cometa");
        Caballo trueno     = fachada.agregarCaballo("Trueno");
        Caballo rayo       = fachada.agregarCaballo("Rayo");
        Caballo brisa      = fachada.agregarCaballo("Brisa");

        // Modalidades (Strategy)
        fachada.agregarModalidad(new ModalidadSimple());
        fachada.agregarModalidad(new ModalidadTriple());
        fachada.agregarModalidad(new ModalidadSuper());

        LocalDate hoy = LocalDate.now();

        // ===== 3 carreras de HOY en estado DEFINIDA sin apuestas =====
        Jornada jornadaHoy = fachada.getOCrearJornada(hoy);

        Carrera c1hoy = jornadaHoy.agregarCarrera("Gran Premio Nacional");
        c1hoy.agregarParticipacion(new Participacion(1, relampago));
        c1hoy.agregarParticipacion(new Participacion(2, tormenta));
        c1hoy.agregarParticipacion(new Participacion(3, vendaval));

        Carrera c2hoy = jornadaHoy.agregarCarrera("Copa de Verano");
        c2hoy.agregarParticipacion(new Participacion(1, estrella));
        c2hoy.agregarParticipacion(new Participacion(2, cometa));

        Carrera c3hoy = jornadaHoy.agregarCarrera("Premio del Rey");
        c3hoy.agregarParticipacion(new Participacion(1, trueno));
        c3hoy.agregarParticipacion(new Participacion(2, rayo));
        c3hoy.agregarParticipacion(new Participacion(3, brisa));

        // ===== 2 carreras de SEMANA PASADA en estado CERRADA con apuestas =====
        LocalDate semanaPasada = hoy.minusWeeks(1);
        Jornada jornadaPasada = fachada.getOCrearJornada(semanaPasada);

        List<Jugador> jugadores = fachada.getJugadores();
        Modalidad simple = fachada.findModalidad("Simple");
        double comision = fachada.getComision();

        // -- Carrera pasada 1: Relámpago, Tormenta, Estrella --
        Carrera cp1 = jornadaPasada.agregarCarrera("Clásico del Pasado");
        Participacion cp1p1 = new Participacion(1, relampago);
        Participacion cp1p2 = new Participacion(2, tormenta);
        Participacion cp1p3 = new Participacion(3, estrella);
        cp1.agregarParticipacion(cp1p1);
        cp1.agregarParticipacion(cp1p2);
        cp1.agregarParticipacion(cp1p3);
        cp1.abrir();

        // 10 apuestas a cada caballo, jugadores diferentes
        for (int i = 0; i < 10; i++) {
            jugadores.get(i).realizarApuesta(cp1p1, 500, simple);
            cp1.calcularDividendos(comision);
            cp1.actualizarEstado();
        }
        for (int i = 10; i < 20; i++) {
            jugadores.get(i).realizarApuesta(cp1p2, 600, simple);
            cp1.calcularDividendos(comision);
            cp1.actualizarEstado();
        }
        for (int i = 2; i < 12; i++) {
            jugadores.get(i).realizarApuesta(cp1p3, 400, simple);
            cp1.calcularDividendos(comision);
            cp1.actualizarEstado();
        }
        cp1.cerrar();

        // -- Carrera pasada 2: Cometa, Vendaval --
        Carrera cp2 = jornadaPasada.agregarCarrera("Premio Nocturno");
        Participacion cp2p1 = new Participacion(1, cometa);
        Participacion cp2p2 = new Participacion(2, vendaval);
        cp2.agregarParticipacion(cp2p1);
        cp2.agregarParticipacion(cp2p2);
        cp2.abrir();

        for (int i = 0; i < 15; i++) {
            jugadores.get(i).realizarApuesta(cp2p1, 700, simple);
            cp2.calcularDividendos(comision);
            cp2.actualizarEstado();
        }
        for (int i = 5; i < 20; i++) {
            jugadores.get(i).realizarApuesta(cp2p2, 500, simple);
            cp2.calcularDividendos(comision);
            cp2.actualizarEstado();
        }
        cp2.cerrar();

        // ===== 1 carrera de SEMANA SIGUIENTE en estado DEFINIDA =====
        Jornada jornadaFutura = fachada.getOCrearJornada(hoy.plusWeeks(1));
        Carrera cFutura = jornadaFutura.agregarCarrera("Gran Premio del Futuro");
        cFutura.agregarParticipacion(new Participacion(1, trueno));
        cFutura.agregarParticipacion(new Participacion(2, brisa));
        cFutura.agregarParticipacion(new Participacion(3, relampago));
    }
}
