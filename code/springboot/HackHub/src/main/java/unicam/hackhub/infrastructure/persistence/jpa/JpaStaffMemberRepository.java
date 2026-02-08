package unicam.hackhub.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.hackhub.domain.staffMember.model.StaffMember;
import unicam.hackhub.domain.staffMember.repository.StaffMemberRepository;

@Repository
public interface JpaStaffMemberRepository extends JpaRepository<StaffMember, String>, StaffMemberRepository {
}
