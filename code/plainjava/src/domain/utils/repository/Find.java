package plainjava.src.domain.utils.repository;

import java.util.List;

public interface Find<T, K> {

    /**
     * Find an entity by is ID
     * @param id id of the entity
     * @return the entity found or null if nothing is found
     */
    T findById(K id);

    /**
     * Find all the entities by their IDs
     * @param ids ids to search for
     * @return a list of all the entities founds
     */
    List<T> findAll(List<K> ids);
}
