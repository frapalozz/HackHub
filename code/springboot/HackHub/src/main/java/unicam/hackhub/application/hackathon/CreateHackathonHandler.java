package unicam.hackhub.application.hackathon;

import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.application.hackathon.request.CreateHackathonRequest;

public interface CreateHackathonHandler {
    
    Hackathon createHackathon(CreateHackathonRequest request);
}
