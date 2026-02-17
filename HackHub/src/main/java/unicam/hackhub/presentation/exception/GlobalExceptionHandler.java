package unicam.hackhub.presentation.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==============================================
    // 1. Gestione errori validazione @RequestBody (@Valid)
    // ==============================================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {

        // Estrae gli errori di campo
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null
                                ? fieldError.getDefaultMessage()
                                : "Validation error"
                ));

        // Crea risposta strutturata
        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation failed for request body")
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    // ==============================================
    // 2. Gestione errori validazione parametri (@PathVariable, @RequestParam)
    // ==============================================
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex) {

        Map<String, String> fieldErrors = new HashMap<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            // Estrae il nome del parametro dal path (es: "getTeam.id" → "id")
            String propertyPath = violation.getPropertyPath().toString();
            String fieldName = extractFieldName(propertyPath);
            fieldErrors.put(fieldName, violation.getMessage());
        }

        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation failed for request parameters")
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    // ==============================================
    // 3. Gestione errori tipo parametro (es: stringa invece di numero)
    // ==============================================
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        String error = String.format(
                "Parameter '%s' should be of type %s",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown"
        );

        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(error)
                .fieldErrors(Map.of(ex.getName(), "Type mismatch"))
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    // ==============================================
    // 4. Gestione eccezioni di dominio personalizzate
    // ==============================================
    /*@ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleTeamNotFound(
            TeamNotFoundException ex) {

        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(HackathonClosedException.class)
    public ResponseEntity<ApiErrorResponse> handleHackathonClosed(
            HackathonClosedException ex) {

        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
     */

    // Per IllegalArgumentException (es: team già esistente, creatore non trovato)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument: {}", ex.getMessage());

        // Analizza il messaggio per determinare il codice HTTP più appropriato
        HttpStatus status = determineHttpStatusFromMessage(ex.getMessage());

        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(status).body(response);
    }

    // Per IllegalStateException (es: utente già in un team)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        log.error("Illegal state: {}", ex.getMessage());

        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value()) // 409 Conflict
                .error("Conflict")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Per eccezioni di risorsa non trovata
    /*
    @ExceptionHandler(ResourceNotFoundException.class) // Crea questa eccezione custom
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage());

        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

     */

    // ==============================================
    // 5. Gestione errori generici (catch-all)
    // ==============================================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        // Log dell'errore completo (solo su server)
        log.error(ex.getMessage());

        // In produzione, non esporre dettagli interni
        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                //.message("An unexpected error occurred")
                .message(ex.getMessage()) // per mostrare l'errore nelle api
                .build();

        return ResponseEntity.internalServerError().body(response);
    }

    // ==============================================
    // Metodi di supporto
    // ==============================================
    private String extractFieldName(String propertyPath) {
        if (propertyPath.contains(".")) {
            return propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
        }
        return propertyPath;
    }

    private HttpStatus determineHttpStatusFromMessage(String message) {
        if (message == null) {
            return HttpStatus.BAD_REQUEST;
        }

        // Analizza il messaggio per determinare il codice HTTP appropriato
        String lowerMessage = message.toLowerCase();

        if (lowerMessage.contains("not found") || lowerMessage.contains("does not exist")) {
            return HttpStatus.NOT_FOUND;
        } else if (lowerMessage.contains("already exists") ||
                lowerMessage.contains("already in use") ||
                lowerMessage.contains("duplicate")) {
            return HttpStatus.CONFLICT;
        } else if (lowerMessage.contains("unauthorized") || lowerMessage.contains("forbidden")) {
            return HttpStatus.FORBIDDEN;
        } else if (lowerMessage.contains("invalid") ||
                lowerMessage.contains("validation") ||
                lowerMessage.contains("required")) {
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.BAD_REQUEST; // Default
    }
}