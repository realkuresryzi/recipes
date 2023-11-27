package cz.muni.fi.pv168.project.ui.actions;

import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;

public class CategoryRemoveAction extends AbstractAction {

    private final JTable categoryTable;

    public CategoryRemoveAction(JTable categoryTable) {
        super(null, Icons.DELETE_ICON);
        this.categoryTable = categoryTable;
        this.setEnabled(false);
        categoryTable.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        putValue(SHORT_DESCRIPTION, "Remove selected category");
        putValue(MNEMONIC_KEY, KeyEvent.VK_R);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl R"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var model = (CategoryTableModel) categoryTable.getModel();
        var selectedRow = categoryTable.getSelectedRow();
        var rowCount = categoryTable.getSelectedRowCount();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(categoryTable, "No category selected", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(categoryTable, "Do you really want to delete " + rowCount + " categories", "Category Deletion Dialog",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            Arrays.stream(categoryTable.getSelectedRows())
                    .map(categoryTable::convertRowIndexToModel)
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
