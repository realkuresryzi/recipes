package cz.muni.fi.pv168.project.ui.actions;

import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.ui.dialog.IngredientDialog;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.model.RepositoryWrapper;
import cz.muni.fi.pv168.project.ui.model.UnitRepositoryWrapper;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class IngredientEditAction extends AbstractAction {

    private final JTable ingredientTable;
    private final RepositoryWrapper<Unit> unitRepository;

    public IngredientEditAction(JTable ingredientTable, RepositoryWrapper<Unit> unitRepository) {
        super(null, Icons.EDIT_ICON);
        this.ingredientTable = ingredientTable;
        this.unitRepository = unitRepository;
        this.setEnabled(false);
        ingredientTable.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        putValue(SHORT_DESCRIPTION, "Edit selected ingredient");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var model = (IngredientTableModel) ingredientTable.getModel();
        var selectedRow = ingredientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(ingredientTable, "No ingredient selected", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        var ingredient = model.getEntity(selectedRow);
        int modelRow = ingredientTable.convertRowIndexToModel(selectedRow);
        var dialog = new IngredientDialog(ingredient, (UnitRepositoryWrapper) unitRepository);
        dialog.show(ingredientTable, "Edit ingredient")
                .ifPresent(entity -> model.updateRow(modelRow, entity));
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        var count = selectionModel.getSelectedItemsCount();
        changeActionsState(count);
    }

    private void changeActionsState(int selectedItemsCount) {
        this.setEnabled(selectedItemsCount == 1);
    }
}
