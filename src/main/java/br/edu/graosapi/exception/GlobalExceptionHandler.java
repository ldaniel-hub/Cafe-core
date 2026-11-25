package br.edu.graosapi.exception;

import br.edu.graosapi.dto.ApiError;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> tratarNaoEncontrado(ResourceNotFoundException exception) {
        return resposta(HttpStatus.NOT_FOUND, exception.getMessage(), Map.of());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> tratarArgumentoInvalido(IllegalArgumentException exception) {
        return resposta(HttpStatus.BAD_REQUEST, exception.getMessage(), Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> tratarValidacao(MethodArgumentNotValidException exception) {
        Map<String, String> campos = exception.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(
                error -> error.getField(),
                error -> error.getDefaultMessage() == null ? "Valor inválido" : error.getDefaultMessage(),
                (first, ignored) -> first));
        return resposta(HttpStatus.BAD_REQUEST, "Um ou mais campos são inválidos", campos);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> tratarViolacao(ConstraintViolationException exception) {
        return resposta(HttpStatus.BAD_REQUEST, exception.getMessage(), Map.of());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> tratarCorpoInvalido(HttpMessageNotReadableException exception) {
        return resposta(HttpStatus.BAD_REQUEST, "O corpo da requisição está inválido ou malformado", Map.of());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> tratarConflitoDeDados(DataIntegrityViolationException exception) {
        return resposta(HttpStatus.CONFLICT, "A operação viola uma restrição de integridade dos dados", Map.of());
    }

    private ResponseEntity<ApiError> resposta(HttpStatus status, String message, Map<String, String> campos) {
        ApiError body = new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), message, campos);
        return ResponseEntity.status(status).body(body);
    }
}
