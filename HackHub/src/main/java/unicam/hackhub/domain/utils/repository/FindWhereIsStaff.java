package unicam.hackhub.domain.utils.repository;

import java.util.List;

public interface FindWhereIsStaff<T> {

    List<T> findAllWhereIsStaff(String staffEmail);
}
