package cz.muni.fi.pv168.project.persistance.dao;

import java.util.Collection;
import java.util.Optional;

public interface DataAccessObject<E> {
    /**
     *
     */
    E create(E entity);

    /**
     *
     */
    Collection<E> findAll();

    /**
     *
     */
    Optional<E> findById(long id);

    /**
     *
     */
    E update(E entity);

    /**
     *
     */
    void deleteById(long entityId);

    /**
     *
     *
     */
    int deleteAll();
}
