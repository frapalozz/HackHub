package code.java.infrastructure.persistence.plainJava;

import java.util.Set;

import code.java.domain.staffMember.model.StaffMember;
import code.java.domain.staffMember.repository.StaffMemberRepository;

public class JavaStaffMemberRepository implements StaffMemberRepository {

    private Set<StaffMember> staffMembers;

    @Override
    public StaffMember findById(String email) {
        return staffMembers.stream()
        .filter(s -> s.getEmail() == email)
        .findFirst()
        .orElse(null);
    }
    
}
