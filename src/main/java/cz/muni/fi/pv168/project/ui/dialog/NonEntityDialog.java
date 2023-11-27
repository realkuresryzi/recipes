package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.persistance.validation.Validator;

abstract public class NonEntityDialog extends EntityDialog<String> {
    NonEntityDialog(Validator<String> validator) {
        super(validator);
    }

    String getEntity() {
        return "";
    }
}
