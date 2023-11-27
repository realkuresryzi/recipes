package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.persistance.validation.IngredientValidator;
import cz.muni.fi.pv168.project.ui.model.UnitRepositoryWrapper;

import javax.swing.*;
import java.util.List;

public class IngredientDialog extends EntityDialog<Ingredient> {

    private final UnitRepositoryWrapper unitRepository;
    private final Ingredient ingredient;
    protected ComboBoxModel<String> unitComboBoxModel;
    protected JComboBox<String> unitComboBox;
    private JTextField nameField = new JTextField();
    private JSpinner nutritionValueField = new JSpinner(new SpinnerNumberModel(0, 0, 3500, 1));
    private List<Unit> units;

    protected String originalName;
    protected int originalNutritionValue;
    protected Unit originalUnit;

    public IngredientDialog(Ingredient ingredient, UnitRepositoryWrapper unitRepositoryWrapper) {
        super(new IngredientValidator());
        this.ingredient = ingredient;
        this.unitRepository = unitRepositoryWrapper;
        units = unitRepository.getEntities();
        setValues();
        addFields();

        setOriginalValues();
    }

    public IngredientDialog(UnitRepositoryWrapper unitRepositoryWrapper) {
        this(new Ingredient(), unitRepositoryWrapper);
        setOriginalValues();
    }

    private void setValues() {
        nameField.setText(ingredient.getName());
        nutritionValueField.setValue(ingredient.getNutritionValue());
        unitComboBoxModel = new DefaultComboBoxModel<>(units.stream().map(Unit::getName).toArray(String[]::new));
        unitComboBox = new JComboBox<>(unitComboBoxModel);
        if (ingredient.getUnit() != null) {
            unitComboBox.setSelectedItem(ingredient.getUnit().getName());
        }
    }

    private void setOriginalValues() {
        originalName = ingredient.getName();
        originalNutritionValue = ingredient.getNutritionValue();
        originalUnit = ingredient.getUnit();
    }

    private void addFields() {
        add("Name:", nameField);
        addWithConstraints("Unit:", new JComboBox<>(unitComboBoxModel), "gapleft 200, grow");
        addWithConstraints("Nutrition value: (kcal)", nutritionValueField, "gapleft 200, grow");
    }

    @Override
    void resetEntity() {
        ingredient.setName(originalName);
        ingredient.setNutritionValue(originalNutritionValue);
        ingredient.setUnit(originalUnit);
    }

    @Override
    Ingredient getEntity() {
        ingredient.setName(nameField.getText());
        ingredient.setNutritionValue((int) nutritionValueField.getValue());
        ingredient.setUnit(getUnitByName((String) unitComboBox.getSelectedItem()));
        return ingredient;
    }

    private Unit getUnitByName(String name) {
        return units.stream().filter(unit -> unit.getName() == name).findFirst().get();
    }
}
