package unicam.hackhub.domain.hackathon.model;

import jakarta.persistence.*;
import lombok.*;
import unicam.hackhub.domain.team.model.Team;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Team team;

    @ManyToOne
    private Hackathon hackathon;

    @Column(nullable = false, length = 1000)
    private String description;
}
