package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.modelo.java.test;

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

    public boolean login(String nombreUsuario, String contrasena) {
        return this.nombreUsuario.equals(nombreUsuario) && this.contrasena.equals(contrasena);
    }

}
