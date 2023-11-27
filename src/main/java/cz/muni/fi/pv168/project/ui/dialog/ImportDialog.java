package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.ui.components.JFilePicker;
import cz.muni.fi.pv168.project.utils.DataFileObject;
import cz.muni.fi.pv168.project.utils.importers.formats.JSONImporter;
import cz.muni.fi.pv168.project.utils.importers.formats.XMLImporter;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class
ImportDialog extends EntityDialog<String> {

    private final JFilePicker filePicker = new JFilePicker("Choose import file:", "Browse...");

    public ImportDialog() {
        super(null);
        setValues();
        addFields();
    }

    private void setValues() {
        filePicker.setMode(JFilePicker.MODE_OPEN);
        filePicker.addFileTypeFilter(".json", "JavaScript Object Notation");
        filePicker.addFileTypeFilter(".xml", "Extensible Markup Language");
        JFileChooser fileChooser = filePicker.getFileChooser();
        fileChooser.setCurrentDirectory(new File("C:/"));
    }

    private void addFields() {
        add("", filePicker);
    }

    @Override
    String getEntity() {
        String filePath = filePicker.getSelectedFilePath();
        if (Objects.equals(filePath, "")) {
            return "";
        }
        Object[] options = {"Yes", "Cancel"};
        var confirmationResult = JOptionPane.showOptionDialog(
                null,
                "Doing this will overwrite all your data. Do you still wish to proceed?",
                "Import",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);
        if (confirmationResult != 0) {
            return "";
        }
        return filePath;
    }

    @Override
    void resetEntity() {
        // do nothing
    }
}
