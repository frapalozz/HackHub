package code.java.domain.staffMember.repository;

import code.java.domain.staffMember.model.StaffMember;

public interface StaffMemberRepository {
    
    StaffMember findById(String email);
}
