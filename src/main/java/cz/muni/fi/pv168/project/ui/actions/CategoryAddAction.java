package cz.muni.fi.pv168.project.ui.actions;

import cz.muni.fi.pv168.project.ui.dialog.CategoryDialog;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class CategoryAddAction extends AbstractAction {

    private final JTable categoryTable;

    public CategoryAddAction(JTable categoryTable) {
        super(null, Icons.ADD_ICON);
        this.categoryTable = categoryTable;
        putValue(SHORT_DESCRIPTION, "Add new category");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl A"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var model = (CategoryTableModel) categoryTable.getModel();
        var dialog = new CategoryDialog();
        dialog.show(categoryTable, "Add category")
                .ifPresent(model::addRow);
    }
}
