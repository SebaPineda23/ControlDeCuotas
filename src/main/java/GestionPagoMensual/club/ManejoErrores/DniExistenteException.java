package GestionPagoMensual.club.ManejoErrores;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DniExistenteException extends RuntimeException {

    public DniExistenteException(String message) {
        super(message);
    }
    public static final HttpStatus CLIENTE_YA_REGISTRADO =
            HttpStatus.valueOf( "Ya existe un cliente registrado con el DNI");
}
