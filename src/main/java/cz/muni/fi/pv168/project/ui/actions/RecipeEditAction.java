package cz.muni.fi.pv168.project.ui.actions;

import cz.muni.fi.pv168.project.ui.dialog.RecipeDialog;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class RecipeEditAction extends AbstractAction {

    private final JTable recipeTable;
    private final CategoryTableModel categoryTableModel;
    private final IngredientTableModel ingredientTableModel;

    public RecipeEditAction(JTable recipeTable, CategoryTableModel categoryTableModel, IngredientTableModel ingredientTableModel) {
        super(null, Icons.EDIT_ICON);
        this.recipeTable = recipeTable;
        this.categoryTableModel = categoryTableModel;
        this.ingredientTableModel = ingredientTableModel;
        this.setEnabled(false);
        recipeTable.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        putValue(SHORT_DESCRIPTION, "Edit selected recipe");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var model = (RecipeTableModel) recipeTable.getModel();
        var selectedRow = recipeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(recipeTable, "No recipe selected", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        var recipe = model.getEntity(selectedRow);
        int modelRow = recipeTable.convertRowIndexToModel(selectedRow);
        var dialog = new RecipeDialog(recipe, categoryTableModel, ingredientTableModel);
        dialog.show(recipeTable, "Edit recipe")
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
