package cz.muni.fi.pv168.project.ui.dialog;


import javax.swing.*;

public class SettingsDialog extends NonEntityDialog {
    //TODO: Add new settings options
    private final String[] languages = new String[]{"ENG", "CZ", "SK"};
    //TODO: Add logic for languages, if we even want it.
    private final JComboBox<String> languagesField = new JComboBox<>(languages);

    public SettingsDialog() {
        super(null);
        addFields();
    }


    private void addFields() {
        addWithConstraints("Language:", languagesField, "gapleft 200, grow");
    }

    @Override
    void resetEntity() {
        // do nothing
    }
}
