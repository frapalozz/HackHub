package unicam.hackhub.domain.hackathon.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Valuation {

    private int vote;
    private String description;

    public Valuation(int vote, String description) {
        this.vote = vote;
        this.description = description;
    }
}
