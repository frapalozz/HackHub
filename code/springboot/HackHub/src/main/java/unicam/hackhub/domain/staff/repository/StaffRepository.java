package unicam.hackhub.domain.staff.repository;

import unicam.hackhub.domain.staff.model.Staff;
import unicam.hackhub.domain.utils.repository.Find;
import unicam.hackhub.domain.utils.repository.Save;

public interface StaffRepository extends
        Find<Staff, String>,
        Save<Staff> {
}
