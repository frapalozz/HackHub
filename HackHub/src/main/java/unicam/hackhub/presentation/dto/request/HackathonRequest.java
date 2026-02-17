package unicam.hackhub.presentation.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record HackathonRequest(

        @NotBlank(message = "Hackathon name è obbligatorio")
        String name,

        @NotBlank
        String location,

        @NotNull(message = "Subscription deadline è obbligatoria")
        @Future(message = "Subscription deadline deve essere nel futuro")
        LocalDate subscriptionDeadline,

        @NotNull(message = "end date è obbligatoria")
        @Future(message = "end date deve essere nel futuro")
        LocalDate startDate,

        @NotNull(message = "start date è obbligatoria")
        @Future(message = "start date deve essere nel futuro")
        LocalDate endDate,

        @NotNull(message = "Max team size è obbligatorio")
        @Min(value = 1, message = "MaxTeamSize almeno di 1")
        @Max(value = 20, message = "MaxTeamSize non può superare 20")
        int maxTeamSize,

        String requirements,

        @PositiveOrZero(message = "Prize deve essere zero o positivo")
        Double prize,

        @NotBlank(message = "Judge email è obbligatoria")
        @Email(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$", message = "Judge email deve essere valida")
        String judgeEmail,

        List<String> mentorsEmails
        ) {

}
