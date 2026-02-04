package code.java.domain.team.model;

import java.util.Set;

import code.java.domain.user.model.User;

public class Team {
    
    private String name;
    private Set<User> members;

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object other) {
        if(other == null || !(other instanceof Team)) return false;

        Team o = (Team) other;

        return name.equals(o.getName());
    }
}
