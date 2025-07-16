package example.orders.controllerAdvice;

import example.orders.exception.DataHasNotUpdateException;
import example.orders.kafka.producer.KafkaLogProducer;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final KafkaLogProducer logProducer;

    public GlobalExceptionHandler(KafkaLogProducer logProducer) {
        this.logProducer = logProducer;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handlerValidationException(MethodArgumentNotValidException e) {
        logProducer.sendLogError("Order-service", e.getMessage());
        return ResponseEntity.badRequest().body(Objects.requireNonNull(e.getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Error> handlerResourceNotFound(EntityNotFoundException e) {
        logProducer.sendLogError("Order-service", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(e.getMessage()));
    }

    @ExceptionHandler(DataHasNotUpdateException.class)
    public ResponseEntity<Error> handlerDataHasNotUpdate(DataHasNotUpdateException e) {
        logProducer.sendLogError("Order-service", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new Error(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Error> handlerIllegalArgument(IllegalArgumentException e) {
        logProducer.sendLogError("Order-service", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(e.getMessage()));
    }
}
