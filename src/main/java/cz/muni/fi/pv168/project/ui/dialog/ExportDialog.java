package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.ui.components.JFilePicker;

import javax.swing.*;
import java.io.File;

public class ExportDialog extends EntityDialog<String> {
    private final String[] exportTypes = new String[]{"JSON (JavaScript Object Notation)", "XML (Extensible Markup Language)", "PDF (Portable Document Format)"};

    private final JFilePicker filePicker = new JFilePicker("", "Browse...");
    private final JFileChooser fileChooser = new JFileChooser();
    private final JTextField nameField = new JTextField();
    private final ComboBoxModel<String> exportTypesComboBox = new DefaultComboBoxModel<>(exportTypes);


    public ExportDialog() {
        super(null);
        setValues();
        addFields();
    }

    private void setValues() {
        fileChooser.setCurrentDirectory(new File("C:/"));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        filePicker.setMode(JFilePicker.MODE_SAVE);

        JFileChooser fChooser = filePicker.getFileChooser();
        fChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        nameField.setText("");
    }

    private void addFields() {
        add("Choose directory:", filePicker);
        add("File name:", nameField);
        add("Export type:", new JComboBox<>(exportTypesComboBox));
    }

    private String getTypeEndingString(String type) {
        if (type.equals("JSON (JavaScript Object Notation)")) {
            return ".json";
        } else if (type.equals("XML (Extensible Markup Language)")) {
            return ".xml";
        } else {
            return ".pdf";
        }
    }

    @Override
    String getEntity() {
        if (nameField.getText().equals("") || filePicker.getSelectedFilePath().equals("")) {
            return "";
        }
        return filePicker.getSelectedFilePath() + "/" + nameField.getText() + getTypeEndingString((String) exportTypesComboBox.getSelectedItem());
    }

    @Override
    void resetEntity() {
        // do nothing
    }
}
