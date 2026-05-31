package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.dtos;

import lombok.Getter;

@Getter
public class ModalidadDto {

    private String nombre;

    public ModalidadDto(String nombre) {
        this.nombre = nombre;
    }
}
