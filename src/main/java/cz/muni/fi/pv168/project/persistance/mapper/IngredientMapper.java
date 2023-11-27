package cz.muni.fi.pv168.project.persistance.mapper;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.persistance.entity.IngredientEntity;
import cz.muni.fi.pv168.project.persistance.repository.Repository;
import cz.muni.fi.pv168.project.persistance.validation.Validator;

public class IngredientMapper implements EntityMapper<IngredientEntity, Ingredient> {

    private final Lookup<Unit> unitSupplier;
    private final Validator<Ingredient> ingredientValidator;

    public IngredientMapper(Repository<Unit> unitRepository, Validator<Ingredient> validator) {
        this.unitSupplier = unitRepository::findById;
        this.ingredientValidator = validator;
    }

    public IngredientMapper(Lookup<Unit> unitSupplier, Validator<Ingredient> validator) {
        this.unitSupplier = unitSupplier;
        this.ingredientValidator = validator;
    }

    @Override
    public Ingredient mapToModel(IngredientEntity entity) {
        Unit unit = unitSupplier.get(entity.unitId()).orElseThrow();
        return new Ingredient(
                entity.id(),
                entity.name(),
                unit,
                entity.nutritionValue()
        );
    }

    public IngredientEntity mapToEntity(Ingredient source) {
        ingredientValidator.validate(source).intoException();

        return new IngredientEntity(
                source.getId(),
                source.getName(),
                source.getUnit().getId(),
                source.getNutritionValue()
        );
    }

}
