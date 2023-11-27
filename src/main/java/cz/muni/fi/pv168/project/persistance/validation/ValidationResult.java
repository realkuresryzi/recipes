package cz.muni.fi.pv168.project.persistance.validation;

import cz.muni.fi.pv168.project.ui.MainWindow;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class ValidationResult {
    private final List<String> validationErrors;

    public ValidationResult(Collection<String> validationErrors) {
        this.validationErrors = new ArrayList<>(validationErrors);
    }

    public ValidationResult() {
        this(List.of());
    }

    public void add(String message) {
        if (message != null && !message.isEmpty()) {
            validationErrors.add(message);
        }
    }

    public void add(Collection<String> messages) {
        messages.forEach(this::add);
    }

    public List<String> getValidationErrors() {
        return Collections.unmodifiableList(validationErrors);
    }

    public boolean isValid() {
        return validationErrors.isEmpty();
    }

    @Override
    public String toString() {
        if (isValid()) {
            return "Validation passed";
        }
        return "Validation has failed:\n" + String.join("\n", getValidationErrors());
    }

    public void intoException() {
        if (!isValid()) {
            JOptionPane.showMessageDialog(MainWindow.getFrame(), validationErrors.toString()
                    .replace("[", " ")
                    .replace("]", "")
                    .replace(",", ",\n"));
            throw new ValidationException(toString(), validationErrors);
        }
    }
}
