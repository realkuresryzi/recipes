package cz.muni.fi.pv168.project.persistance.repository;

import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.persistance.dao.RecipeDao;
import cz.muni.fi.pv168.project.persistance.entity.RecipeEntity;
import cz.muni.fi.pv168.project.persistance.mapper.EntityMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecipeRepository implements Repository<Recipe> {

    private final RecipeDao dao;
    private final EntityMapper<RecipeEntity, Recipe> mapper;

    private List<Recipe> recipes = new ArrayList<>();

    public RecipeRepository(RecipeDao recipeDao, EntityMapper<RecipeEntity, Recipe> recipeMapper) {
        this.dao = recipeDao;
        this.mapper = recipeMapper;
        this.refresh();
    }

    @Override
    public int getSize() {
        return recipes.size();
    }

    @Override
    public Optional<Recipe> findById(long id) {
        return recipes.stream().filter(r -> r.getId() == id).findFirst();
    }

    @Override
    public Optional<Recipe> findByIndex(int index) {
        if (index < getSize())
            return Optional.of(recipes.get(index));
        return Optional.empty();
    }

    @Override
    public List<Recipe> findAll() {
        return Collections.unmodifiableList(recipes);
    }

    @Override
    public void refresh() {
        recipes = fetchAllEntities();
    }

    private List<Recipe> fetchAllEntities() {
        return dao.findAll().stream()
                .map(mapper::mapToModel)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void create(Recipe newEntity) {
        Stream.of(newEntity)
                .map(mapper::mapToEntity)
                .map(dao::create)
                .map(mapper::mapToModel)
                .forEach(r -> recipes.add(r));
    }

    @Override
    public void update(Recipe entity) {
        int index = recipes.indexOf(entity);
        Stream.of(entity)
                .map(mapper::mapToEntity)
                .map(dao::update)
                .map(mapper::mapToModel)
                .forEach(r -> recipes.set(index, r));

    }

    @Override
    public void deleteByIndex(int index) {
        dao.deleteById(recipes.get(index).getId());
        recipes.remove(index);
    }
}
