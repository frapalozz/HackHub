package unicam.hackhub.domain.hackathon.model;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class Submission {
    
    private File data;
    private Valuation valuation;

    public Submission(File data) {
        this.data = data;
    }
}
