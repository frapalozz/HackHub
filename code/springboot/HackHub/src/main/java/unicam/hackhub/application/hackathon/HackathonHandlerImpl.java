package unicam.hackhub.application.hackathon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;

@Service
@Primary
public class HackathonHandlerImpl implements HackathonHandler {

    private final HackathonRepository hackathonRepository;

    @Autowired
    public HackathonHandlerImpl(HackathonRepository hackathonRepository) {
        this.hackathonRepository = hackathonRepository;
    }

    @Override
    public String declareWinner(Long hackathonId, String teamName) {

        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElse(null);

        hackathon.declareWinner(teamName);

        return "winner added";
    }
}
