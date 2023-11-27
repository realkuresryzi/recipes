package cz.muni.fi.pv168.project.ui.actions;

import cz.muni.fi.pv168.project.ui.dialog.CategoryDialog;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class CategoryEditAction extends AbstractAction {

    private final JTable categoryTable;

    public CategoryEditAction(JTable categoryTable) {
        super(null, Icons.EDIT_ICON);
        this.categoryTable = categoryTable;
        this.setEnabled(false);
        categoryTable.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        putValue(SHORT_DESCRIPTION, "Edit selected category");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var model = (CategoryTableModel) categoryTable.getModel();
        var selectedRow = categoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(categoryTable, "No category selected", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        var category = model.getEntity(selectedRow);
        var dialog = new CategoryDialog(category);
        dialog.show(categoryTable, "Edit category")
                .ifPresent(entity -> model.updateRow(categoryTable.convertRowIndexToModel(selectedRow), entity));

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
