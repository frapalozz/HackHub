package unicam.hackhub.domain.staffMember.repository;

import unicam.hackhub.domain.staffMember.model.StaffMember;
import unicam.hackhub.domain.utils.repository.Find;
import unicam.hackhub.domain.utils.repository.Save;

public interface StaffMemberRepository extends
        Find<StaffMember, String>,
        Save<StaffMember> {
}
