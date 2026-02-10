package unicam.hackhub.presentation.dto.request;

import java.util.List;

public record TeamRequest(String user, String teamName, List<String> emails) {
}
