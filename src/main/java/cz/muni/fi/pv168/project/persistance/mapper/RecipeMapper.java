package cz.muni.fi.pv168.project.persistance.mapper;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.persistance.entity.RecipeEntity;
import cz.muni.fi.pv168.project.persistance.repository.Repository;
import cz.muni.fi.pv168.project.persistance.validation.Validator;

import java.util.HashMap;
import java.util.Map;

public class RecipeMapper implements EntityMapper<RecipeEntity, Recipe> {

    private final Lookup<Ingredient> ingredientLookup;

    private final Lookup<Category> categoryLookup;
    private final Validator<Recipe> recipeValidator;
    public RecipeMapper(Repository<Ingredient> ingredientRepository,
                        Repository<Category> categoryRepository,
                        Validator<Recipe> validator) {
        this.ingredientLookup = ingredientRepository::findById;
        this.categoryLookup = categoryRepository::findById;
        this.recipeValidator = validator;
    }

    public RecipeMapper(Lookup<Ingredient> ingredientLookup, Lookup<Category> categoryLookup, Validator<Recipe> recipeValidator) {
        this.ingredientLookup = ingredientLookup;
        this.categoryLookup = categoryLookup;
        this.recipeValidator = recipeValidator;
    }


    @Override
    public Recipe mapToModel(RecipeEntity entity) {

        Map<Long, Integer> ingredientAmounts = entity.ingredientsAmounts();
        Map<Ingredient, Integer> ingredients = new HashMap<>();
        for (var i : ingredientAmounts.entrySet()) {
            var ingredient = ingredientLookup.get(i.getKey()).orElseThrow();
            ingredients.put(ingredient, i.getValue());
        }
        Category category = categoryLookup.get(entity.categoryId()).orElseThrow();

        return new Recipe(
                entity.id(),
                entity.name(),
                entity.description(),
                ingredients,
                entity.instructions(),
                category,
                entity.portions(),
                entity.duration()
        );
    }

    @Override
    public RecipeEntity mapToEntity(Recipe source) {
        recipeValidator.validate(source).intoException();

        Map<Ingredient, Integer> ingredients = source.getIngredients();
        Map<Long, Integer> ingredientsIds = new HashMap<>();
        for (var i : ingredients.entrySet()) {
            ingredientsIds.put(i.getKey().getId(), i.getValue());
        }

        return new RecipeEntity(
                source.getId(),
                source.getName(),
                source.getDescription(),
                source.getInstructions(),
                ingredientsIds,
                source.getCategory().getId(),
                source.getPortions(),
                source.getDuration()
        );
    }

}
