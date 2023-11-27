package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.persistance.validation.CategoryValidator;
import cz.muni.fi.pv168.project.utils.ColorMap;

import javax.swing.*;
import java.awt.*;

public class CategoryDialog extends EntityDialog<Category> {

    private final ColorMap colorMap = new ColorMap();
    private final JTextField nameField = new JTextField();
    private final JTextField descriptionField = new JTextField();
    private final ComboBoxModel<String> colorComboBoxModel;
    private final Category category;
    private String originalName;
    private String originalDescription;
    private Color originalColor;


    protected JComboBox<String> colorComboBox;

    public CategoryDialog(Category category) {
        super(new CategoryValidator());
        this.category = category;
        colorComboBoxModel = new DefaultComboBoxModel<>(colorMap.getColorsNames());

        colorComboBox = new JComboBox<>(colorComboBoxModel);
        colorComboBox.setSelectedItem(category.getColor());

        setValues();
        addFields();

        setOriginalValues();

    }

    public CategoryDialog() {
        this(new Category());
        setOriginalValues();
    }

    private void setValues() {
        nameField.setText(category.getName());
        descriptionField.setText(category.getDescription());
        colorComboBox.setSelectedItem(colorMap.getColorString(category.getColor()));
    }

    private void addFields() {
        add("Name:", nameField);
        add("Description:", descriptionField);
        addWithConstraints("Color:", new JComboBox<>(colorComboBoxModel), "gapleft 200, grow");
    }

    private void setOriginalValues() {
        originalName = category.getName();
        originalDescription = category.getDescription();
        originalColor = category.getColor();
    }

    @Override
    void resetEntity() {
        category.setName(originalName);
        category.setDescription(originalDescription);
        category.setColor(originalColor);
    }

    @Override
    Category getEntity() {
        category.setName(nameField.getText());
        category.setDescription(descriptionField.getText());
        var color = colorMap.getColorByName((String) colorComboBox.getSelectedItem());
        category.setColor(color);
        return category;
    }
}
