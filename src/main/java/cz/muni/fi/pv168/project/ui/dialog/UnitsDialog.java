package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.BaseUnit;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.ui.model.RepositoryWrapper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javax.swing.JOptionPane.*;

//TODO: Text fields should change based on selected unit.
public class UnitsDialog extends EntityDialog<List<Unit>> {

    private final JTextField abbreviationField = new JTextField();
    private final JTextField conversionRateField = new JTextField();
    private final JTextField baseUnitsField = new JTextField();
    private final JButton addUnitButton = new JButton("Add new unit");
    private RepositoryWrapper<Unit> unitRepository;
    private RepositoryWrapper<BaseUnit> baseUnitRepository;
    private List<String> unitNames = new ArrayList<>();
    private JComboBox<String> unitsField;
    private Unit unit;

    public UnitsDialog(RepositoryWrapper<Unit> unitRepository, RepositoryWrapper<BaseUnit> baseUnitRepository) {
        super(null);
        this.unitRepository = unitRepository;
        this.baseUnitRepository = baseUnitRepository;
        List<Unit> units = unitRepository.getEntities();
        unit = units.get(0);

        for (Unit unit : units) {
            unitNames.add(unit.getName());
        }

        unitsField = new JComboBox<String>(unitNames.toArray(new String[0]));
        setConfigValues();
        setValues();
        addFields();
    }

    private void setConfigValues() {
        abbreviationField.setEditable(false);
        conversionRateField.setEditable(false);
        baseUnitsField.setEditable(false);
        addUnitButton.addActionListener(this::addUnit);
        unitsField.addActionListener(this::changeUnit);
    }

    private void setValues() {
        abbreviationField.setText(unit.getAbbreviation());
        conversionRateField.setText(String.valueOf(unit.getConversionFactor()));
        baseUnitsField.setText(unit.getBaseUnit().getAbbreviation());
    }

    private void addFields() {
        addWithConstraints("Unit:", unitsField, "gapleft 100, grow");
        addWithConstraints("Abbreviation:", abbreviationField, "gapleft 100, grow");
        addWithConstraints("Conversion rate:", conversionRateField, "gapleft 100, grow");
        addWithConstraints("Base unit:", baseUnitsField, "gapleft 100, grow");
        addWithConstraints("", addUnitButton, "gapleft 100, grow");
    }

    private void addUnit(ActionEvent e) {
        var dialog = new AddUnitDialog(baseUnitRepository.getEntities());
        Optional<Unit> unit = dialog.show(null, "Add new unit");
        if (unit.isPresent()) {
            /*
            if (unit.get().getName().equals(unit.get().getBaseUnit().getName())) {
                baseUnitRepository.addRow(unit.get().getBaseUnit());
            }

             */
            unitRepository.addRow(unit.get());
            unitsField.addItem(unit.get().getName());
        }
    }

    private void changeUnit(ActionEvent e) {
        unit = unitRepository.getEntityByName((String) unitsField.getSelectedItem());
        setValues();
    }

    public Optional<List<Unit>> show(JComponent parentComponent, String title) {
        JOptionPane.showOptionDialog(parentComponent, panel, title,
                OK_CANCEL_OPTION, PLAIN_MESSAGE, null, null, null);
        return Optional.of(getEntity());
    }

    @Override
    List<Unit> getEntity() {
        return new ArrayList<>();
    }

    @Override
    void resetEntity() {
        // do nothing
    }
}
