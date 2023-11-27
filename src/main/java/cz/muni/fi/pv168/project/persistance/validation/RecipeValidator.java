package cz.muni.fi.pv168.project.persistance.validation;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;

import java.util.Map;
import java.util.Optional;

public class RecipeValidator implements Validator<Recipe> {

    @Override
    public ValidationResult validate(Recipe model) {
        var result = new ValidationResult();

        validateStringLength("Recipe name", model.getName(), 1, 50)
                .ifPresent(result::add);

        validateStringLength("Recipe description", model.getDescription(), 1, 150)
                .ifPresent(result::add);

        validateStringLength("Recipe instructions", model.getInstructions(), 1, 1000)
                .ifPresent(result::add);

        validateInt("Recipe portions", model.getPortions(), 1, 100)
                .ifPresent(result::add);

        validateInt("Recipe duration time", model.getDuration(), 10, 500)
                .ifPresent(result::add);
        validateCategory(model.getCategory())
                .ifPresent(result::add);

        validateIngredientList(model.getIngredients())
                .ifPresent(result::add);

        return result;
    }

    private Optional<String> validateStringLength(String name, String value, int min, int max) {

        if (value.length() < min) {
            return Optional.of(name + " is too short");
        } else if (value.length() > max) {
            return Optional.of(name + " is too long");
        }

        return Optional.empty();
    }

    private Optional<String> validateInt(String name, int value, int min, int max) {

        if (value < min) {
            return Optional.of(name + " cant be less than " + min);
        } else if (value > max) {
            return Optional.of(name + " cant be more than " + max);
        }

        return Optional.empty();
    }

    private Optional<String> validateCategory(Category category) {

        if (category == null) {
            return Optional.of("Category cant be null");
        }
        return Optional.empty();
    }

    private Optional<String> validateIngredientList(Map<Ingredient, Integer> ingredients) {

        if (ingredients == null) {
            return Optional.of("Ingredients cant be null");
        }

        if (ingredients.isEmpty()) {
            return Optional.of("Ingredients cant be empty");
        }

        for (Map.Entry<Ingredient, Integer> entry : ingredients.entrySet()) {
            if (entry.getKey() == null) {
                return Optional.of("Ingredient cant be null");
            }
            if (entry.getValue() == null) {
                return Optional.of("Ingredient amount cant be null");
            }

            if (entry.getValue() < 0) {
                return Optional.of("Ingredient amount cant be less than 0");
            }

            if (entry.getValue() > 1000) {
                return Optional.of("Ingredient amount cant be more than 1000");
            }
        }
        return Optional.empty();
    }
}
