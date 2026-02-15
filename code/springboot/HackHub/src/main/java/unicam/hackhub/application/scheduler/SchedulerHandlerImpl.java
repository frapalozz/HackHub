package unicam.hackhub.application.scheduler;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.state.HackathonStatus;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Primary
@AllArgsConstructor
public class SchedulerHandlerImpl implements SchedulerHandler {

    private final HackathonRepository hackathonRepository;

    @Override
    public void updateHackathonsStates() {
        LocalDate today = LocalDate.now();

        List<Hackathon> hackathons = hackathonRepository.findAll();

        List<Hackathon> toChangeState = new java.util.ArrayList<>(hackathons.stream()
                .filter(h ->
                        h.getStatus().getCurrentState().equals(HackathonStatus.HackathonStateType.SUBSCRIPTION) &&
                                h.getHackathonPeriod().isWithinPeriod(today))
                .toList());

        toChangeState.addAll(hackathons.stream()
                .filter(h ->
                        h.getStatus().getCurrentState().equals(HackathonStatus.HackathonStateType.PROGRESS) &&
                        (h.getHackathonPeriod().endDate().equals(today) || h.getHackathonPeriod().endDate().isBefore(today))
                )
                .toList());

        toChangeState.forEach(Hackathon::toNextState);

        hackathonRepository.saveAll(toChangeState);

    }
}
