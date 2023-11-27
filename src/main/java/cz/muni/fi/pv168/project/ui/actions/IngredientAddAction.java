package cz.muni.fi.pv168.project.ui.actions;

import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.ui.dialog.IngredientDialog;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.model.RepositoryWrapper;
import cz.muni.fi.pv168.project.ui.model.UnitRepositoryWrapper;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class IngredientAddAction extends AbstractAction {

    private final JTable ingredientTable;
    private final RepositoryWrapper<Unit> unitRepository;

    public IngredientAddAction(JTable ingredientTable, RepositoryWrapper unitRepository) {
        super(null, Icons.ADD_ICON);
        this.ingredientTable = ingredientTable;
        this.unitRepository = unitRepository;
        putValue(SHORT_DESCRIPTION, "Add new ingredient");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl A"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var model = (IngredientTableModel) ingredientTable.getModel();
        var dialog = new IngredientDialog((UnitRepositoryWrapper) unitRepository);
        dialog.show(ingredientTable, "Add ingredient")
                .ifPresent(model::addRow);

    }
}
