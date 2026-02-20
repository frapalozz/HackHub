package unicam.hackhub.application.scheduler;

public interface SchedulerHandler {

    /**
     * Updates the states of all hackathons according to the current system time.
     * This method is typically invoked by a scheduler (e.g., cron job) to
     * transition hackathons between states such as SUBSCRIPTION, PROGRESS, EVALUATION,
     * or other custom states.
     */
    void updateHackathonsStates();
}
