package cz.muni.fi.pv168.project.persistance.repository;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.persistance.dao.CategoryDao;
import cz.muni.fi.pv168.project.persistance.entity.CategoryEntity;
import cz.muni.fi.pv168.project.persistance.mapper.EntityMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CategoryRepository implements Repository<Category> {

    private final CategoryDao dao;
    private final EntityMapper<CategoryEntity, Category> mapper;

    private List<Category> categories = new ArrayList<>();

    public CategoryRepository(CategoryDao categoryDao, EntityMapper<CategoryEntity, Category> categoryMapper) {
        this.dao = categoryDao;
        this.mapper = categoryMapper;
        this.refresh();
    }

    @Override
    public int getSize() {
        return categories.size();
    }

    @Override
    public Optional<Category> findById(long id) {
        return categories.stream().filter(c -> c.getId() == id).findFirst();
    }

    @Override
    public Optional<Category> findByIndex(int index) {
        if (index < getSize())
            return Optional.of(categories.get(index));
        return Optional.empty();
    }

    @Override
    public List<Category> findAll() {
        return Collections.unmodifiableList(categories);
    }

    @Override
    public void refresh() {
        categories = fetchAllEntities();
    }

    private List<Category> fetchAllEntities() {
        return dao.findAll().stream()
                .map(mapper::mapToModel)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void create(Category newEntity) {
        Stream.of(newEntity)
                .map(mapper::mapToEntity)
                .map(dao::create)
                .map(mapper::mapToModel)
                .forEach(e -> categories.add(e));
    }

    @Override
    public void update(Category entity) {
        int index = categories.indexOf(entity);
        Stream.of(entity)
                .map(mapper::mapToEntity)
                .map(dao::update)
                .map(mapper::mapToModel)
                .forEach(e -> categories.set(index, e));
    }

    @Override
    public void deleteByIndex(int index) {
        dao.deleteById(categories.get(index).getId());
        categories.remove(index);
    }
}
