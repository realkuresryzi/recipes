package cz.muni.fi.pv168.project.ui.components;

import cz.muni.fi.pv168.project.ui.actions.IngredientAddAction;
import cz.muni.fi.pv168.project.ui.actions.IngredientEditAction;
import cz.muni.fi.pv168.project.ui.actions.IngredientRemoveAction;
import cz.muni.fi.pv168.project.ui.model.RepositoryWrapper;

import javax.swing.*;

public class IngredientPopupMenu {

    private final JPopupMenu popupMenu;

    public IngredientPopupMenu(JTable ingredientTable, RepositoryWrapper unitRepository) {
        popupMenu = new JPopupMenu();
        popupMenu.add(new IngredientAddAction(ingredientTable, unitRepository));
        popupMenu.add(new IngredientEditAction(ingredientTable, unitRepository));
        popupMenu.add(new IngredientRemoveAction(ingredientTable));
    }

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }

}
