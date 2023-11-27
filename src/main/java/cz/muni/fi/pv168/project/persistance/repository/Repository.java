package cz.muni.fi.pv168.project.persistance.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<M> {

    int getSize();

    Optional<M> findById(long id);

    Optional<M> findByIndex(int index);

    List<M> findAll();

    void refresh();

    void create(M newEntity);

    void update(M entity);

    void deleteByIndex(int index);
}
