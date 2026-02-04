package code.java.domain.user.model;

import code.java.domain.team.model.Team;

public class User {
    
    private String name;
    private String email;
    private Team team;

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public boolean hasTeam() {
        return this.team != null;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object other) {
        if(other == null || !(other instanceof User)) return false;

        User o = (User) other;

        return name.equals(o.getName()) && email.equals(o.getEmail());
    }
}
