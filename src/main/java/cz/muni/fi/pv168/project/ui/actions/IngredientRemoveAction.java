package cz.muni.fi.pv168.project.ui.actions;

import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;

public class IngredientRemoveAction extends AbstractAction {

    private final JTable ingredientTable;

    public IngredientRemoveAction(JTable ingredientTable) {
        super(null, Icons.DELETE_ICON);
        this.ingredientTable = ingredientTable;
        this.setEnabled(false);
        ingredientTable.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        putValue(SHORT_DESCRIPTION, "Remove selected ingredient");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var model = (IngredientTableModel) ingredientTable.getModel();
        var selectedRow = ingredientTable.getSelectedRow();
        var rowCount = ingredientTable.getSelectedRowCount();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(ingredientTable, "No ingredient selected", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int result = JOptionPane.showConfirmDialog(ingredientTable, "Do you really want to delete " + rowCount + " ingredients", "Ingredient Deletion Dialog",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            Arrays.stream(ingredientTable.getSelectedRows())
                    .map(ingredientTable::convertRowIndexToModel)
                    .boxed()
                    .sorted(Comparator.reverseOrder())
                    .forEach(model::deleteRow);
        }
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        var count = selectionModel.getSelectedItemsCount();
        changeActionsState(count);
    }

    private void changeActionsState(int selectedItemsCount) {
        this.setEnabled(selectedItemsCount >= 1);
    }
}
