package cz.muni.fi.pv168.project.persistance.validation;

import cz.muni.fi.pv168.project.model.BaseUnit;

import java.util.Optional;

public class BaseUnitValidator implements Validator<BaseUnit> {

    @Override
    public ValidationResult validate(BaseUnit model) {
        ValidationResult result = new ValidationResult();

        validateStringLength("Unit name", model.getName(), 1, 20)
                .ifPresent(result::add);

        validateStringLength("Unit abbreviation", model.getAbbreviation(), 1, 5)
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

}
