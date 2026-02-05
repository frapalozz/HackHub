package plainjava;

import plainjava.test.application.hackathon.CreateHackathonTest;
import plainjava.test.application.hackathon.SubmissionTest;
import plainjava.test.application.team.CreateTeamTest;
import plainjava.test.application.team.RegisterTeamTest;

public class Main {
    public static void main(String[] args) {
        CreateTeamTest.createTeamTest();
        System.out.println("=====================================================");
        CreateHackathonTest.createHackathonTest();
        System.out.println("=====================================================");
        RegisterTeamTest.registerTeamTest();
        System.out.println("=====================================================");
        SubmissionTest.submissionTest();
        System.out.println("End Test");
    }
}