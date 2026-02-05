package plainjava.src.domain.staffMember.model;

public abstract class StaffMember {
    
    private String name;
    private String email;

    public StaffMember(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }
}
