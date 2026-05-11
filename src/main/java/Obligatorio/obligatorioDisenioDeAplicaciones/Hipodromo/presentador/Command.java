package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.presentador;


/**
 * Representa un comando individual que se envía al frontend.
 * Cada comando tiene un identificador (id) y un parámetro (dato asociado).
 * 
 * Antes llamado "Respuesta" - renombrado para reflejar mejor su propósito
 * como parte del patrón Command.
 */
public class Command {
    private String id;
    private Object parametro;

    public Command(String id, Object parametro) {
        this.id = id;
        this.parametro = parametro;
    }

    public Command() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getParametro() {
        return parametro;
    }

    public void setParametro(Object parametro) {
        this.parametro = parametro;
    }
}
