package cz.muni.fi.pv168.project.persistance.repository;

import cz.muni.fi.pv168.project.model.BaseUnit;
import cz.muni.fi.pv168.project.persistance.dao.BaseUnitDao;
import cz.muni.fi.pv168.project.persistance.entity.BaseUnitEntity;
import cz.muni.fi.pv168.project.persistance.mapper.EntityMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BaseUnitRepository implements Repository<BaseUnit> {

    private final BaseUnitDao dao;
    private final EntityMapper<BaseUnitEntity, BaseUnit> mapper;

    private List<BaseUnit> baseUnits = new ArrayList<>();

    public BaseUnitRepository(BaseUnitDao unitDao, EntityMapper<BaseUnitEntity, BaseUnit> unitMapper) {
        this.dao = unitDao;
        this.mapper = unitMapper;
        this.refresh();
    }

    @Override
    public int getSize() {
        return baseUnits.size();
    }

    @Override
    public Optional<BaseUnit> findById(long id) {
        return baseUnits.stream().filter(u -> u.getId() == id).findFirst();
    }

    @Override
    public Optional<BaseUnit> findByIndex(int index) {
        if (index < getSize())
            return Optional.of(baseUnits.get(index));
        return Optional.empty();
    }

    @Override
    public List<BaseUnit> findAll() {
        return Collections.unmodifiableList(baseUnits);
    }

    @Override
    public void refresh() {
        baseUnits = fetchAllEntities();

    }

    private List<BaseUnit> fetchAllEntities() {
        return dao.findAll().stream()
                .map(mapper::mapToModel)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void create(BaseUnit newEntity) {
        Stream.of(newEntity)
                .map(mapper::mapToEntity)
                .map(dao::create)
                .map(mapper::mapToModel)
                .forEach(u -> baseUnits.add(u));

    }

    @Override
    public void update(BaseUnit entity) {
        int index = baseUnits.indexOf(entity);
        Stream.of(entity)
                .map(mapper::mapToEntity)
                .map(dao::update)
                .map(mapper::mapToModel)
                .forEach(u -> baseUnits.set(index, u));

    }

    @Override
    public void deleteByIndex(int index) {
        dao.deleteById(baseUnits.get(index).getId());
        baseUnits.remove(index);

    }
}
