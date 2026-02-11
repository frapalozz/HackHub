package unicam.hackhub.domain.hackathon.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long submissionId;
    private String url;

    @OneToOne
    private Valuation valuation;

    public Submission(String url) {
        this.url = url;
    }
}
