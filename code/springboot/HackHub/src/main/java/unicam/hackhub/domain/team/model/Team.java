package unicam.hackhub.domain.team.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unicam.hackhub.domain.user.model.User;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "teams")
public class Team {

    @Id
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private Set<User> members;

    public Team (String teamName, User user) {
        this.name = teamName;
        this.members = new HashSet<>();
        this.members.add(user);
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
