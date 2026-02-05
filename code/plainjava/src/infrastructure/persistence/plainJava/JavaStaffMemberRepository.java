package plainjava.src.infrastructure.persistence.plainJava;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import plainjava.src.domain.staffMember.model.StaffMember;
import plainjava.src.domain.staffMember.repository.StaffMemberRepository;

public class JavaStaffMemberRepository implements StaffMemberRepository {

    private final Set<StaffMember> staffMembers = new HashSet<>();

    @Override
    public StaffMember findById(String email) {
        return staffMembers.stream()
        .filter(s -> s.getEmail() == email)
        .findFirst()
        .orElse(null);
    }

    @Override
    public List<StaffMember> findAll(List<String> ids) {
        return staffMembers.stream()
                .filter(s -> ids.contains(s.getEmail()))
                .toList();
    }

    @Override
    public StaffMember save(StaffMember member) {
        StaffMember memberPresent = this.findById(member.getEmail());

        if(memberPresent == null) {
            this.staffMembers.add(member);
        } else {
            this.staffMembers.remove(memberPresent);
            this.staffMembers.add(member);
        }

        return member;
    }

    @Override
    public void saveAll(List<StaffMember> entities) {
        entities.forEach(this::save);
    }

}
