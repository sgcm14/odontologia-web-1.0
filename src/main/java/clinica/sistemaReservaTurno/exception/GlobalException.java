package clinica.sistemaReservaTurno.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
public class GlobalException {
    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<String> tratamientoResourceNotFoundException(ResourceNotFoundException rnfe) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("mensaje " + rnfe.getMessage());
    }

    @ExceptionHandler({ResourceConflictException.class})
    public  ResponseEntity<String> tratamientoResourceConflictException(ResourceConflictException rce){
        return ResponseEntity.status(HttpStatus.CONFLICT).body("mensaje " + rce.getMessage());
    }

}
