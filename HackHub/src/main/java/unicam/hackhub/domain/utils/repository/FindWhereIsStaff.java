package unicam.hackhub.domain.utils.repository;

import java.util.List;

public interface FindWhereIsStaff<T> {

    /**
     * Find Entities where is staffEmail associated
     * @param staffEmail staff to search for
     * @return the entities found
     */
    List<T> findAllWhereIsStaff(String staffEmail);
}
