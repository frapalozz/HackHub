package plainjava.src.domain.team.model;

import java.util.HashSet;
import java.util.Set;

import plainjava.src.domain.user.model.User;

public class Team {
    
    private String name;
    private Set<User> members;

    public Team (String teamName, User user) {
        this.name = teamName;
        this.members = new HashSet<>();
        this.members.add(user);
    }

    public String getName() {
        return this.name;
    }

    public Set<User> getMembers() {
        return this.members;
    }

    public void addMember(User user) {
        this.members.add(user);
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Team o)) return false;

        return name.equals(o.getName());
    }
}
