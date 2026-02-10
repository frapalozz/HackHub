package unicam.hackhub.domain.utils.repository;

import java.util.List;

public interface Save<T> {

    /**
     * Save an entity
     * @param entity entity to save
     * @return the saved entity
     */
    <E extends T> E save(E entity);

    /**
     * Save all entities
     * @param entities entities to save
     */
    <E extends T> List<E> saveAll(Iterable<E> entities);
}
