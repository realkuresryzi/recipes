package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.BaseUnit;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.persistance.validation.UnitValidator;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class AddUnitDialog extends EntityDialog<Unit> {


    private final JTextField nameField = new JTextField();
    private final JTextField abbreviationField = new JTextField();
    private final JSpinner conversionRateField = new JSpinner(new SpinnerNumberModel(1000, 1, 100000, 10));
    private final JComboBox<String> baseUnitsField;
    private final Unit unit;
    private List<BaseUnit> baseUnits;

    public AddUnitDialog(List<BaseUnit> baseUnits) {
        super(new UnitValidator());
        this.baseUnits = baseUnits;
        List<String> baseUnitNames = new ArrayList<>();
        for (BaseUnit baseUnit : baseUnits) {
            baseUnitNames.add(baseUnit.getAbbreviation());
        }
        baseUnitsField = new JComboBox<>(baseUnitNames.toArray(new String[0]));
        this.unit = new Unit("", "", 1, null);
        setValues();
        addFields();
    }

    private void setValues() {
        nameField.setText(unit.getName());
        abbreviationField.setText(unit.getAbbreviation());
        conversionRateField.setValue(unit.getConversionFactor());
    }

    private void addFields() {
        addWithConstraints("Name:", nameField, "gapleft 100, w 100");
        addWithConstraints("Abbreviation:", abbreviationField, "gapleft 100, w 100");
        addWithConstraints("Conversion rate:", conversionRateField, "gapleft 100, w 100");
        addWithConstraints("Base unit:", baseUnitsField, "gapleft 100, w 100");
    }

    @Override
    Unit getEntity() {
        unit.setName(nameField.getText());
        unit.setAbbreviation(abbreviationField.getText());
        Long conversionFactor = Long.valueOf(conversionRateField.getValue().toString());
        unit.setConversionFactor(conversionFactor.intValue());
        for (BaseUnit baseUnit : baseUnits) {
            if (baseUnit.getAbbreviation().equals(baseUnitsField.getSelectedItem())) {
                unit.setBaseUnit(baseUnit);
                return unit;
            }
        }
        unit.setConversionFactor(1);
        unit.setBaseUnit(new BaseUnit(nameField.getText(), abbreviationField.getText()));
        return unit;
    }

    @Override
    void resetEntity() {
        // do nothing
    }
}
