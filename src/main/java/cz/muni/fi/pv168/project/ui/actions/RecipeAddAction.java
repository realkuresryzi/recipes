package cz.muni.fi.pv168.project.ui.actions;

import cz.muni.fi.pv168.project.ui.dialog.RecipeDialog;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class RecipeAddAction extends AbstractAction {

    private final JTable recipeTable;
    private final CategoryTableModel categoryTableModel;
    private final IngredientTableModel ingredientTableModel;

    public RecipeAddAction(JTable recipeTable, CategoryTableModel categoryTableModel, IngredientTableModel ingredientTableModel) {
        super(null, Icons.ADD_ICON);
        this.recipeTable = recipeTable;
        this.categoryTableModel = categoryTableModel;
        this.ingredientTableModel = ingredientTableModel;
        putValue(SHORT_DESCRIPTION, "Add new recipe");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl A"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var model = (RecipeTableModel) recipeTable.getModel();
        var dialog = new RecipeDialog(categoryTableModel, ingredientTableModel);
        dialog.show(recipeTable, "Add recipe")
                .ifPresent(model::addRow);

    }
}
