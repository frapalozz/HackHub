package unicam.hackhub.domain.staff.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unicam.hackhub.domain.utils.Role;
import unicam.hackhub.domain.utils.TimeRange;

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
    private TimeRange timeRange;

    public Staff(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
