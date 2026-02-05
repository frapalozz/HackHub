package plainjava.src.application.hackathon;

import plainjava.src.application.hackathon.request.CreateHackathonRequest;
import plainjava.src.domain.hackathon.model.Hackathon;

public interface CreateHackathonHandler {
    
    Hackathon createHackathon(CreateHackathonRequest request);
}
