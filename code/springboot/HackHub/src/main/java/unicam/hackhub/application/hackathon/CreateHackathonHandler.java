package unicam.hackhub.application.hackathon;

import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.application.hackathon.dto.request.CreateHackathonRequest;

public interface CreateHackathonHandler {

    /**
     * Creates a new hackathon based on the provided request data.
     * <p>
     * The method validates the request (e.g., ensures required fields are present,
     * dates are coherent, and referenced staff exist), then constructs and persists
     * a {@code Hackathon} entity. The returned entity includes its generated
     * identifier and any default state applied during creation.
     * </p>
     *
     * @param request a data transfer object containing all necessary information
     *                for the hackathon, such as name, subscription deadline,
     *                hackathon period, maximum team size, requirements, prize,
     *                organizer, judge, and mentors.
     * @return the fully initialized and persisted {@code Hackathon} entity,
     *         complete with its generated ID and current status
     * @throws IllegalArgumentException if the request is invalid or violates
     *                                  any business rule (e.g., past deadline,
     *                                  non‑existent staff, invalid team size)
     */
    Hackathon createHackathon(CreateHackathonRequest request);
}
