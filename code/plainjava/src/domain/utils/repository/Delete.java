package plainjava.src.domain.utils.repository;

public interface Delete<T> {

    /**
     * Delete an entity
     * @param entity entity to delete
     */
    void delete(T entity);
}
