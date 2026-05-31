package Obligatorio.obligatorioDisenioDeAplicaciones.Hipodromo.excepciones;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final int ERROR_CODE = 299;

    @ExceptionHandler(HipodromoException.class)
    public ResponseEntity<String> manejarHipodromoException(HipodromoException ex) {
        return ResponseEntity.status(ERROR_CODE).body(ex.getMessage());
    }
}
