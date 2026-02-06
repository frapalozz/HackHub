package plainjava;

import plainjava.test.application.hackathon.CreateHackathonTest;
import plainjava.test.application.hackathon.SubmissionTest;
import plainjava.test.application.invitation.InviteUserTest;
import plainjava.test.application.team.CreateTeamTest;
import plainjava.test.application.team.RegisterTeamTest;

public class Main {
    public static void main(String[] args) {
        CreateTeamTest.test();
        CreateHackathonTest.test();
        RegisterTeamTest.test();
        SubmissionTest.test();
        InviteUserTest.test();
        System.out.println("End Test");
    }
}