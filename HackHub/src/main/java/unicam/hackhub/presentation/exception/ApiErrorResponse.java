package unicam.hackhub.presentation.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;  // Opzionale: percorso della richiesta
    private Map<String, String> fieldErrors;  // Per errori di validazione

    // Costruttore per errori senza field specifici
    public ApiErrorResponse(LocalDateTime timestamp, int status,
                            String error, String message) {
        this(timestamp, status, error, message, null, null);
    }

    // Costruttore completo per Lombok
    public ApiErrorResponse(LocalDateTime timestamp, int status, String error,
                            String message, String path, Map<String, String> fieldErrors) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.fieldErrors = fieldErrors;
    }
}