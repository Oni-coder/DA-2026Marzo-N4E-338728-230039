package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.Dominio;

import lombok.Getter;

@Getter
public abstract class Usuario {

    private String nombreUsuario;
    private String contrasena;
    private String nombreCompleto;

    public Usuario(String nombreUsuario, String contrasena, String nombreCompleto) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombreCompleto = nombreCompleto;
    }

    public boolean coincideContrasena(String pwd) {
        return this.contrasena != null && this.contrasena.equals(pwd);
    }

    @Override
    public String toString() {
        return nombreCompleto;
    }
}
