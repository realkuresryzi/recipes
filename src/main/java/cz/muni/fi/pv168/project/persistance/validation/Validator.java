package cz.muni.fi.pv168.project.persistance.validation;

import java.util.List;

@FunctionalInterface
public interface Validator<M> {
    static <M> Validator<M> compose(List<Validator<M>> validators) {
        return model -> validators
                .stream()
                .map(x -> x.validate(model))
                .reduce(new ValidationResult(), (r, e) -> {
                    r.add(e.getValidationErrors());
                    return r;
                });
    }

    ValidationResult validate(M model);

    default Validator<M> and(Validator<M> other) {
        return compose(List.of(this, other));
    }
}
