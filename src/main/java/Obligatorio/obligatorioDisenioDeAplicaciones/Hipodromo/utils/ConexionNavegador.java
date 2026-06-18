package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.utils;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConexionNavegador {

    private List<SseEmitter> conexiones = new ArrayList<>();

    public void conectarSSE() {
        long timeOut = 30 * 60 * 1000; // 30 minutos, igual al timeout por defecto de la sesión
        SseEmitter nuevaConexion = new SseEmitter(timeOut);

        nuevaConexion.onCompletion(() -> removerConexion(nuevaConexion));
        nuevaConexion.onTimeout(() -> removerConexion(nuevaConexion));
        nuevaConexion.onError((ex) -> removerConexion(nuevaConexion));

        synchronized (conexiones) {
            conexiones.add(nuevaConexion);
        }
    }

    private void removerConexion(SseEmitter emisor) {
        synchronized (conexiones) {
            conexiones.remove(emisor);
        }
    }

    public SseEmitter getUltimaConexion() {
        synchronized (conexiones) {
            if (conexiones.isEmpty()) return null;
            return conexiones.get(conexiones.size() - 1);
        }
    }

    public void enviarJSON(Object informacion) {
        try {
            String json = new ObjectMapper().writeValueAsString(informacion);
            enviarMensaje(json);
        } catch (JacksonException e) {
            System.out.println("Error al convertir a JSON: " + e.getMessage());
        }
    }

    public void enviarMensaje(String mensaje) {
        List<SseEmitter> aRemover = new ArrayList<>();
        List<SseEmitter> copiaConexiones;

        synchronized (conexiones) {
            copiaConexiones = new ArrayList<>(conexiones);
        }

        for (SseEmitter emisor : copiaConexiones) {
            try {
                emisor.send(mensaje);
            } catch (Exception e) {
                System.out.println("Error al enviar mensaje a un emisor: " + e.getMessage());
                aRemover.add(emisor);
            }
        }

        if (!aRemover.isEmpty()) {
            synchronized (conexiones) {
                conexiones.removeAll(aRemover);
            }
        }
    }
}
