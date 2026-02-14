package unicam.hackhub.domain.staff.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unicam.hackhub.domain.utils.Role;

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

    private Role role = Role.STAFF;

    public Staff(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
