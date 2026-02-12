package unicam.hackhub.application.report;

public interface ReportHandler {

    /**
     * Submits a report against a specific team in a hackathon.
     *
     * @param teamName    the name of the team being reported
     * @param hackathonId the unique identifier of the hackathon
     * @param description detailed reason for the report
     * @return a status message indicating success or failure
     */
    String report(String teamName, Long hackathonId, String description);
}
