package unicam.hackhub.presentation.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import unicam.hackhub.application.hackathon.dto.request.CreateHackathonRequest;
import unicam.hackhub.domain.utils.Period;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class HackathonRequest {

        @NotBlank(message = "Hackathon name è obbligatorio")
        private String name;

        @NotNull(message = "Subscription deadline è obbligatoria")
        @Future(message = "Subscription deadline deve essere nel futuro")
        private LocalDate subscriptionDeadline;

        @NotNull(message = "Hackathon period is required")
        private Period hackathonPeriod;

        @NotNull(message = "Max team size è obbligatorio")
        @Min(value = 1, message = "MaxTeamSize almeno di 1")
        @Max(value = 20, message = "MaxTeamSize non può superare 20")
        private int maxTeamSize;

        private String requirements;

        @PositiveOrZero(message = "Prize deve essere zero o positivo")
        private Double prize;

        @NotBlank(message = "Organizer email è obbligatoria")
        @Email(message = "Organizer email deve essere valida")
        private String organizerEmail;

        @NotBlank(message = "Judge email è obbligatoria")
        @Email(message = "Judge email deve essere valida")
        private String judgeEmail;

        private List<String> mentorsEmails;

        public CreateHackathonRequest toCreateHackathonRequest() {
            return new CreateHackathonRequest(
                    this.name,
                    this.subscriptionDeadline,
                    this.hackathonPeriod,
                    this.maxTeamSize,
                    this.requirements,
                    this.prize,
                    this.organizerEmail,
                    this.judgeEmail,
                    this.mentorsEmails
            );
        }
}
