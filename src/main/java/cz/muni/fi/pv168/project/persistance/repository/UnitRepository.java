package cz.muni.fi.pv168.project.persistance.repository;

import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.persistance.dao.UnitDao;
import cz.muni.fi.pv168.project.persistance.entity.UnitEntity;
import cz.muni.fi.pv168.project.persistance.mapper.EntityMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UnitRepository implements Repository<Unit> {

    private final UnitDao dao;
    private final EntityMapper<UnitEntity, Unit> mapper;

    private List<Unit> units = new ArrayList<>();

    public UnitRepository(UnitDao unitDao, EntityMapper<UnitEntity, Unit> unitMapper) {
        this.dao = unitDao;
        this.mapper = unitMapper;
        this.refresh();
    }

    @Override
    public int getSize() {
        return units.size();
    }

    @Override
    public Optional<Unit> findById(long id) {
        return units.stream().filter(u -> u.getId() == id).findFirst();
    }

    @Override
    public Optional<Unit> findByIndex(int index) {
        if (index < getSize())
            return Optional.of(units.get(index));
        return Optional.empty();
    }

    @Override
    public List<Unit> findAll() {
        return Collections.unmodifiableList(units);
    }

    @Override
    public void refresh() {
        units = fetchAllEntities();

    }

    private List<Unit> fetchAllEntities() {
        return dao.findAll().stream()
                .map(mapper::mapToModel)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void create(Unit newEntity) {
        Stream.of(newEntity)
                .map(mapper::mapToEntity)
                .map(dao::create)
                .map(mapper::mapToModel)
                .forEach(u -> units.add(u));

    }

    @Override
    public void update(Unit entity) {
        int index = units.indexOf(entity);
        Stream.of(entity)
                .map(mapper::mapToEntity)
                .map(dao::update)
                .map(mapper::mapToModel)
                .forEach(u -> units.set(index, u));

    }

    @Override
    public void deleteByIndex(int index) {
        dao.deleteById(units.get(index).getId());
        units.remove(index);

    }
}
