package cz.muni.fi.pv168.project.persistance.repository;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.persistance.dao.IngredientDao;
import cz.muni.fi.pv168.project.persistance.entity.IngredientEntity;
import cz.muni.fi.pv168.project.persistance.mapper.EntityMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IngredientRepository implements Repository<Ingredient> {

    private final IngredientDao dao;
    private final EntityMapper<IngredientEntity, Ingredient> mapper;

    private List<Ingredient> ingredients = new ArrayList<>();

    public IngredientRepository(IngredientDao ingredientDao, EntityMapper<IngredientEntity, Ingredient> ingredientMapper) {
        this.dao = ingredientDao;
        this.mapper = ingredientMapper;
        this.refresh();
    }


    @Override
    public int getSize() {
        return ingredients.size();
    }

    @Override
    public Optional<Ingredient> findById(long id) {
        return ingredients.stream().filter(c -> c.getId() == id).findFirst();
    }

    @Override
    public Optional<Ingredient> findByIndex(int index) {
        if (index < getSize() && index >= 0)
            return Optional.of(ingredients.get(index));
        return Optional.empty();
    }

    @Override
    public List<Ingredient> findAll() {
        return Collections.unmodifiableList(ingredients);
    }

    @Override
    public void refresh() {
        ingredients = fetchAllEntities();
    }

    private List<Ingredient> fetchAllEntities() {
        return dao.findAll().stream()
                .map(mapper::mapToModel)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void create(Ingredient newEntity) {
        Stream.of(newEntity)
                .map(mapper::mapToEntity)
                .map(dao::create)
                .map(mapper::mapToModel)
                .forEach(i -> ingredients.add(i));
    }

    @Override
    public void update(Ingredient entity) {
        int index = ingredients.indexOf(entity);
        Stream.of(entity)
                .map(mapper::mapToEntity)
                .map(dao::update)
                .map(mapper::mapToModel)
                .forEach(i -> ingredients.set(index, i));
    }

    @Override
    public void deleteByIndex(int index) {
        dao.deleteById(ingredients.get(index).getId());
        ingredients.remove(index);
    }
}
