package cz.muni.fi.pv168.project.persistance.mapper;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.persistance.entity.CategoryEntity;
import cz.muni.fi.pv168.project.persistance.validation.Validator;

public class CategoryMapper implements EntityMapper<CategoryEntity, Category> {

    private final Validator<Category> categoryValidator;

    public CategoryMapper(Validator<Category> validator) {
        this.categoryValidator = validator;
    }

    @Override
    public Category mapToModel(CategoryEntity entity) {
        return new Category(
                entity.id(),
                entity.name(),
                entity.description(),
                entity.color()
        );
    }

    @Override
    public CategoryEntity mapToEntity(Category source) {
        categoryValidator.validate(source).intoException();

        return new CategoryEntity(
                source.getId(),
                source.getName(),
                source.getDescription(),
                source.getColor()
        );
    }
}
