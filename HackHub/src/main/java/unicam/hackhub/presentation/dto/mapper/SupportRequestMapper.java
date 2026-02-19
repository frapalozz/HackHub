package unicam.hackhub.presentation.dto.mapper;

import org.springframework.stereotype.Component;
import unicam.hackhub.domain.support.model.SupportRequest;
import unicam.hackhub.presentation.dto.response.SupportRequestResponse;

@Component
public class SupportRequestMapper {

    public SupportRequestResponse supportRequestToSupportRequestResponse(SupportRequest supportRequest) {
        return new SupportRequestResponse(
                supportRequest.getId(),
                supportRequest.getTeam().getName(),
                supportRequest.getHackathon().getId(),
                supportRequest.getMentor().getEmail(),
                supportRequest.getState().name(),
                supportRequest.getDate(),
                supportRequest.getTimeRange(),
                supportRequest.getCallLink()
        );
    }
}
