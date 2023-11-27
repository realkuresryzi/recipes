package cz.muni.fi.pv168.project.persistance.validation;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Unit;

import java.util.Optional;

public class IngredientValidator implements Validator<Ingredient> {
    @Override
    public ValidationResult validate(Ingredient model) {
        var result = new ValidationResult();

        validateStringLength("Ingredient name", model.getName(), 1, 50)
                .ifPresent(result::add);

        validateUnit(model.getUnit())
                .ifPresent(result::add);

        validateInt("Ingredient amount", model.getNutritionValue(), 1, 1000)
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

    private Optional<String> validateUnit(Unit unit) {
        if (unit == null) {
            return Optional.of("Unit cant be null");
        }

        return Optional.empty();
    }


}
