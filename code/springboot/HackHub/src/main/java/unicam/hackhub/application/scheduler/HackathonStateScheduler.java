package unicam.hackhub.application.scheduler;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HackathonStateScheduler {

    private static final Logger log = LoggerFactory.getLogger(HackathonStateScheduler.class);
    private final SchedulerHandler schedulerHandler;

    @Scheduled(cron = "0 0 * * * *")
    public void scheduleStateUpdate() {
        schedulerHandler.updateHackathonsStates();
        log.info("Hackathons state update scheduled");
    }
}
