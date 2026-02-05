package plainjava.src.domain.staffMember.repository;

import plainjava.src.domain.staffMember.model.StaffMember;

public interface StaffMemberRepository {
    
    StaffMember findById(String email);
    void save(StaffMember member);
}
