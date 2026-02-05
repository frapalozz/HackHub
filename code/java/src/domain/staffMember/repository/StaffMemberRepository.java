package code.java.src.domain.staffMember.repository;

import code.java.src.domain.staffMember.model.StaffMember;

public interface StaffMemberRepository {
    
    StaffMember findById(String email);
    void save(StaffMember member);
}
