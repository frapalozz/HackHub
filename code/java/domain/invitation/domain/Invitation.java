package code.java.domain.invitation.domain;

import java.time.LocalDate;

import code.java.domain.team.model.Team;
import code.java.domain.user.model.User;

public class Invitation {
    
    private LocalDate date;
    private Team team;
    private User receiver;
}
