package unicam.hackhub.domain.support.model;

import jakarta.persistence.*;
import lombok.*;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.staff.model.Staff;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.utils.TimeRange;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupportRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Team team;

    @ManyToOne(optional = false)
    private Hackathon hackathon;

    @ManyToOne(optional = false)
    private Staff mentor;

    @Embedded
    private TimeRange timeRange;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private RequestState state;

    private String callLink;

    public void accept(String linkCall) {
        this.callLink = linkCall;
        this.state = RequestState.SCHEDULED;
    }

    public void decline() {
        this.state = RequestState.DECLINED;
    }
}
