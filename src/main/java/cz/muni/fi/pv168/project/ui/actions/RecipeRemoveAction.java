package cz.muni.fi.pv168.project.ui.actions;

import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;

public class RecipeRemoveAction extends AbstractAction {

    private final JTable recipeTable;

    public RecipeRemoveAction(JTable recipeTable) {
        super(null, Icons.DELETE_ICON);
        this.recipeTable = recipeTable;
        this.setEnabled(false);
        recipeTable.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        putValue(SHORT_DESCRIPTION, "Remove selected recipe");
        putValue(MNEMONIC_KEY, KeyEvent.VK_R);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl R"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var model = (RecipeTableModel) recipeTable.getModel();
        var selectedRow = recipeTable.getSelectedRow();
        var rowCount = recipeTable.getSelectedRowCount();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(recipeTable, "No recipe selected", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(recipeTable, "Do you really want to delete " + rowCount + " recipes", "Recipe Deletion Dialog",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            Arrays.stream(recipeTable.getSelectedRows())
                    .map(recipeTable::convertRowIndexToModel)
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
