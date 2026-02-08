package unicam.hackhub.domain.utils.repository;

import java.util.List;

public interface Save<T> {

    /**
     * Save an entity
     * @param entity entity to save
     * @return the saved entity
     */
    T save(T entity);

    /**
     * Save all entities
     * @param entities entities to save
     */
    void saveAll(List<T> entities);
}
