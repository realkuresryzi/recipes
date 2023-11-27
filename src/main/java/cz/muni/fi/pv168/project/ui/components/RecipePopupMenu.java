package cz.muni.fi.pv168.project.ui.components;

import cz.muni.fi.pv168.project.ui.actions.RecipeAddAction;
import cz.muni.fi.pv168.project.ui.actions.RecipeEditAction;
import cz.muni.fi.pv168.project.ui.actions.RecipeRemoveAction;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;

import javax.swing.*;

public class RecipePopupMenu {
    private final JPopupMenu popupMenu;

    public RecipePopupMenu(JTable recipeTable, CategoryTableModel categoryTableModel, IngredientTableModel ingredientTableModel) {
        popupMenu = new JPopupMenu();
        popupMenu.add(new RecipeAddAction(recipeTable, categoryTableModel, ingredientTableModel));
        popupMenu.add(new RecipeEditAction(recipeTable, categoryTableModel, ingredientTableModel));
        popupMenu.add(new RecipeRemoveAction(recipeTable));
    }

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }
}
