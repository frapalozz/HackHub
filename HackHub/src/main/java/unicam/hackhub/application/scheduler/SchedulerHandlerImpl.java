package unicam.hackhub.application.scheduler;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.state.HackathonStatus;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;

import java.time.LocalDate;
import java.util.List;

@Component
@Primary
@AllArgsConstructor
public class SchedulerHandlerImpl implements SchedulerHandler {

    private final HackathonRepository hackathonRepository;
    private static final Logger log = LoggerFactory.getLogger(SchedulerHandlerImpl.class);

    @Override
    @Scheduled(cron = "0 0 * * * *")
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

        log.info("Hackathons state update scheduled");
    }
}
