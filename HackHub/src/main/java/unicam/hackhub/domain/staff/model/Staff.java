package unicam.hackhub.domain.staff.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unicam.hackhub.domain.utils.Role;
import unicam.hackhub.domain.utils.TimeRange;

import java.time.LocalTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "staff")
public class Staff {

    @Column(nullable = false)
    private String name;

    @Id
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.STAFF;

    @Embedded
    private TimeRange workingHours = new TimeRange(LocalTime.of(9, 0), LocalTime.of(17, 0));

    public Staff(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Staff(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
