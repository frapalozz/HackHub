package code.java;

import code.java.test.application.hackathon.CreateHackathonTest;
import code.java.test.application.team.CreateTeamTest;

public class Main {

    public static void main(String[] args) {
        CreateTeamTest.createTeamTest();
        System.out.println("=====================================================");
        CreateHackathonTest.createHackathonTest();
        System.out.println("Fine Test");
    }
}