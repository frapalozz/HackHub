package plainjava.src.application.hackathon;

import plainjava.src.domain.hackathon.model.Hackathon;
import plainjava.src.domain.hackathon.repository.HackathonRepository;

public class HackathonHandlerImpl implements HackathonHandler {

    private final HackathonRepository hackathonRepository;

    public HackathonHandlerImpl(HackathonRepository hackathonRepository) {
        this.hackathonRepository = hackathonRepository;
    }

    @Override
    public String declareWinner(Long hackathonId, String teamName) {

        Hackathon hackathon = hackathonRepository.findById(hackathonId);

        hackathon.declareWinner(teamName);

        return "winner added";
    }
}
