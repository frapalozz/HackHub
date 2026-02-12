package unicam.hackhub.domain.hackathon.model.state;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@Getter
@Setter
public class HackathonStatus {

    @Enumerated(EnumType.STRING)
    private HackathonStateType currentState;

    public HackathonStatus() {
        this.currentState = HackathonStateType.SUBSCRIPTION;
    }

    public enum HackathonStateType {
        SUBSCRIPTION, PROGRESS, EVALUATION, ENDED
    }
}
