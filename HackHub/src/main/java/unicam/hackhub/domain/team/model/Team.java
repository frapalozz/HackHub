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

    private double balance;

    public Team (String teamName, User user) {
        this.name = teamName;
        this.members = new HashSet<>();
        this.members.add(user);
        this.balance = 0;
    }

    public void addMember(User user) {
        this.members.add(user);
    }

    public void increaseBalance(double amount) {
        this.balance += amount;
    }
}
