package code.java.src.application.hackathon;

import code.java.src.application.hackathon.request.CreateHackathonRequest;
import code.java.src.domain.hackathon.model.Hackathon;

public interface CreateHackathonHandler {
    
    Hackathon createHackathon(CreateHackathonRequest request);
}
