package unicam.hackhub.application.hackathon;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;

@Service
@Primary
public class HackathonHandlerImpl implements HackathonHandler {

    private final HackathonRepository hackathonRepository;

    public HackathonHandlerImpl(HackathonRepository hackathonRepository) {
        this.hackathonRepository = hackathonRepository;
    }

    @Override
    public String declareWinner(Long hackathonId, String teamName) {

        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElse(null);

        if(hackathon == null) {
            throw new IllegalArgumentException("Hackathon not found");
        }

        hackathon.declareWinner(teamName);

        hackathonRepository.save(hackathon);

        return "winner added";
    }
}
