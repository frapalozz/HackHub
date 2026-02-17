package unicam.hackhub.presentation.dto.request;

import java.util.List;

public record AddMentorsRequest(
        List<String> emailList
) {
}
