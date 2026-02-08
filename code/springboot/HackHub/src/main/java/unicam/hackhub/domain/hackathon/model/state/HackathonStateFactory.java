package unicam.hackhub.domain.hackathon.model.state;

import unicam.hackhub.domain.hackathon.model.Hackathon;

public class HackathonStateFactory {

    public static HackathonState createState(
            HackathonStatus.HackathonStateType stateType,
            Hackathon hackathon
    ) {
        return switch (stateType) {
            case SUBSCRIPTION -> new SubscriptionState(hackathon);
            case PROGRESS -> new ProgressState(hackathon);
            case EVALUATION -> new EvaluationState(hackathon);
            case ENDED -> new EndedState(hackathon);
        };
    }
}
