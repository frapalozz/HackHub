package unicam.hackhub.domain.hackathon.model.state;

import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.Valuation;
import unicam.hackhub.domain.team.model.Team;

public class EvaluationState extends AbstractHackathonState {

    public EvaluationState(Hackathon context) {
        super(context);
    }

    @Override
    public boolean active() {
        return true;
    }

    @Override
    public void valuateSubmission(String teamName, int vote, String description) {
        this.hackathon.getSubmission(teamName).setValuation(new Valuation(vote, description));
    }

    @Override
    public void declareWinner(Team team) {
        this.hackathon.setWinner(team);
    }

}
