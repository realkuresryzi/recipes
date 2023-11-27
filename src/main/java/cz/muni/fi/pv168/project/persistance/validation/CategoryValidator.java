package cz.muni.fi.pv168.project.persistance.validation;

import cz.muni.fi.pv168.project.model.Category;

import java.awt.*;
import java.util.Optional;

public class CategoryValidator implements Validator<Category> {
    @Override
    public ValidationResult validate(Category model) {
        var result = new ValidationResult();

        validateStringLength("Category name", model.getName(), 1, 50)
                .ifPresent(result::add);

        validateStringLength("Category description", model.getDescription(), 1, 100)
                .ifPresent(result::add);

        validateColor(model.getColor())
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

    private Optional<String> validateColor(Color color) {
        if (color == null) {
            return Optional.of("Color is null");
        }

        return Optional.empty();
    }
}