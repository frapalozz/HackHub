package unicam.hackhub.presentation.dto.request;

import jakarta.validation.constraints.*;
import unicam.hackhub.domain.utils.Period;

import java.time.LocalDate;
import java.util.List;

public record HackathonRequest(

        @NotBlank(message = "Hackathon name è obbligatorio")
        String name,

        @NotNull(message = "Subscription deadline è obbligatoria")
        @Future(message = "Subscription deadline deve essere nel futuro")
        LocalDate subscriptionDeadline,

        @NotNull(message = "Hackathon period is required")
        Period hackathonPeriod,

        @NotNull(message = "Max team size è obbligatorio")
        @Min(value = 1, message = "MaxTeamSize almeno di 1")
        @Max(value = 20, message = "MaxTeamSize non può superare 20")
        int maxTeamSize,

        String requirements,

        @PositiveOrZero(message = "Prize deve essere zero o positivo")
        Double prize,

        @NotBlank(message = "Organizer email è obbligatoria")
        @Email(message = "Organizer email deve essere valida")
        String organizerEmail,

        @NotBlank(message = "Judge email è obbligatoria")
        @Email(message = "Judge email deve essere valida")
        String judgeEmail,

        List<String> mentorsEmails
        ) {

}
