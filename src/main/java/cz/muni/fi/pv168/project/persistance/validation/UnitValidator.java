package cz.muni.fi.pv168.project.persistance.validation;

import cz.muni.fi.pv168.project.model.Unit;

import java.util.Optional;

public class UnitValidator implements Validator<Unit> {
    @Override
    public ValidationResult validate(Unit model) {
        ValidationResult result = new ValidationResult();

        validateStringLength("Unit name", model.getName(), 1, 20)
                .ifPresent(result::add);

        validateStringLength("Unit abbreviation", model.getAbbreviation(), 1, 5)
                .ifPresent(result::add);

        validateConversionRate(model.getConversionFactor())
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

    private Optional<String> validateBaseUnit(Unit unit) {
        if (unit.getBaseUnit() == null) {
            return Optional.of("Base unit cannot be null");
        }

        return Optional.empty();
    }

    private Optional<String> validateConversionRate(double conversionRate) {
        if (conversionRate <= 0) {
            return Optional.of("Conversion rate cannot be negative or zero");
        }

        return Optional.empty();
    }
}
