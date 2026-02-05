package plainjava.src.domain.staffMember.repository;

import plainjava.src.domain.staffMember.model.StaffMember;
import plainjava.src.domain.utils.repository.Find;
import plainjava.src.domain.utils.repository.Save;

public interface StaffMemberRepository extends
        Find<StaffMember, String>,
        Save<StaffMember> {
}
