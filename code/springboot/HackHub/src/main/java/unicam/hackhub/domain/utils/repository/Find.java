package unicam.hackhub.domain.utils.repository;

import java.util.List;
import java.util.Optional;

public interface Find<T, K> {

    /**
     * Find an entity by is ID
     * @param id id of the entity
     * @return the entity found or null if nothing is found
     */
    Optional<T> findById(K id);

    /**
     * Find all the entities by their IDs
     * @param ids ids to search for
     * @return a list of all the entities founds
     */
    List<T> findAllById(Iterable<K> ids);
}
