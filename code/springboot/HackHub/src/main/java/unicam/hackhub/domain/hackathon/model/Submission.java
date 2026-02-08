package unicam.hackhub.domain.hackathon.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long submissionId;
    private File data;

    @OneToOne
    private Valuation valuation;

    public Submission(File data) {
        this.data = data;
    }
}
