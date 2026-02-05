package code.java.test.application.hackathon;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import code.java.src.application.hackathon.CreateHackathonHandler;
import code.java.src.application.hackathon.CreateHackathonHandlerImpl;
import code.java.src.application.hackathon.request.CreateHackathonRequest;
import code.java.src.domain.hackathon.model.Hackathon;
import code.java.src.domain.hackathon.repository.HackathonRepository;
import code.java.src.domain.staffMember.model.Judge;
import code.java.src.domain.staffMember.model.Mentor;
import code.java.src.domain.staffMember.model.Organizer;
import code.java.src.domain.staffMember.repository.StaffMemberRepository;
import code.java.src.domain.utils.Period;
import code.java.src.infrastructure.persistence.plainJava.JavaHackathonRepository;
import code.java.src.infrastructure.persistence.plainJava.JavaStaffMemberRepository;

public class CreateHackathonTest {
    
    public static void createHackathonTest() {
        successCreateHackathonTest();
        staffMembersNotFoundTest();
        datesNotInOrderTest();
    }

    private static void successCreateHackathonTest() {
        // ARRANGE
        StaffMemberRepository staffMemberRepo = new JavaStaffMemberRepository();
        HackathonRepository hackathonRepo = new JavaHackathonRepository();
        CreateHackathonHandler handler = new CreateHackathonHandlerImpl(staffMemberRepo, hackathonRepo);
        Organizer organizer = new Organizer("organizer", "organizer@test.test");
        staffMemberRepo.save(organizer);
        Judge judge = new Judge("judge", "judge@test.test");
        staffMemberRepo.save(judge);
        Mentor mentor1 = new Mentor("mentor1", "mentor1@test.test");
        staffMemberRepo.save(mentor1);
        Mentor mentor2 = new Mentor("mentor2", "mentor2@test.test");
        staffMemberRepo.save(mentor2);
        List<Mentor> mentors = new ArrayList<>();
        mentors.add(mentor1);
        mentors.add(mentor2);
        Hackathon hackathon = null;

        // ACT
        try {
            hackathon = handler.createHackathon(new CreateHackathonRequest(
                "test", 
                LocalDate.parse("2026-02-15"), 
                new Period(LocalDate.parse("2026-02-16"), LocalDate.parse("2026-02-28")), 
                4, 
                "", 
                100.0, 
                organizer.getEmail(), 
                judge.getEmail(), 
                mentors.stream().map(e -> e.getEmail()).toList()));
        } catch (Exception e) {
        }

        // ASSERT
        System.out.println(
            (hackathon != null && hackathonRepo.findById(hackathon.getId()) != null) ?
                ":) Success Test: Hackathon created" : ":( Failed Test: Hackathon created"
        );
    }

    private static void staffMembersNotFoundTest() {
        // ARRANGE
        StaffMemberRepository staffMemberRepo = new JavaStaffMemberRepository();
        HackathonRepository hackathonRepo = new JavaHackathonRepository();
        CreateHackathonHandler handler = new CreateHackathonHandlerImpl(staffMemberRepo, hackathonRepo);
        Organizer organizer = new Organizer("organizer", "organizer@test.test");
        staffMemberRepo.save(organizer);
        Judge judge = new Judge("judge", "judge@test.test");
        staffMemberRepo.save(judge);
        Mentor mentor1 = new Mentor("mentor1", "mentor1@test.test");
        staffMemberRepo.save(mentor1);
        Mentor mentor2 = new Mentor("mentor2", "mentor2@test.test");
        staffMemberRepo.save(mentor2);
        List<Mentor> mentors = new ArrayList<>();
        mentors.add(mentor1);
        mentors.add(mentor2);
        Hackathon hackathon = null;

        // ACT
        try {
            hackathon = handler.createHackathon(new CreateHackathonRequest(
                "test", 
                LocalDate.parse("2026-02-15"), 
                new Period(LocalDate.parse("2026-02-16"), LocalDate.parse("2026-02-28")), 
                4, 
                "", 
                100.0, 
                "aasdasd@test.test", 
                judge.getEmail(), 
                mentors.stream().map(e -> e.getEmail()).toList()));
        } catch (Exception e) {
        }

        // ASSERT
        System.out.println(
            hackathon == null ?
                ":) Success Test: Staff Members not found" : ":( Failed Test: Staff Members not found"
        );
    }

    private static void datesNotInOrderTest() {
        // ARRANGE
        StaffMemberRepository staffMemberRepo = new JavaStaffMemberRepository();
        HackathonRepository hackathonRepo = new JavaHackathonRepository();
        CreateHackathonHandler handler = new CreateHackathonHandlerImpl(staffMemberRepo, hackathonRepo);
        Organizer organizer = new Organizer("organizer", "organizer@test.test");
        staffMemberRepo.save(organizer);
        Judge judge = new Judge("judge", "judge@test.test");
        staffMemberRepo.save(judge);
        Mentor mentor1 = new Mentor("mentor1", "mentor1@test.test");
        staffMemberRepo.save(mentor1);
        Mentor mentor2 = new Mentor("mentor2", "mentor2@test.test");
        staffMemberRepo.save(mentor2);
        List<Mentor> mentors = new ArrayList<>();
        mentors.add(mentor1);
        mentors.add(mentor2);
        Hackathon hackathon = null;

        // ACT
        try {
            hackathon = handler.createHackathon(new CreateHackathonRequest(
                "test", 
                LocalDate.parse("2026-02-18"), 
                new Period(LocalDate.parse("2026-02-16"), LocalDate.parse("2026-02-28")), 
                4, 
                "", 
                100.0, 
                organizer.getEmail(), 
                judge.getEmail(), 
                mentors.stream().map(e -> e.getEmail()).toList()));
        } catch (Exception e) {
        }

        // ASSERT
        System.out.println(
            hackathon == null ?
                ":) Success Test: Dates not in order" : ":( Failed Test: Dates not in order"
        );
    }
}
