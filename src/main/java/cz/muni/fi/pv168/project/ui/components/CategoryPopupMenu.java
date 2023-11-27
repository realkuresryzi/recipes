package cz.muni.fi.pv168.project.ui.components;

import cz.muni.fi.pv168.project.ui.actions.CategoryAddAction;
import cz.muni.fi.pv168.project.ui.actions.CategoryEditAction;
import cz.muni.fi.pv168.project.ui.actions.CategoryRemoveAction;

import javax.swing.*;

public class CategoryPopupMenu {

    private final JPopupMenu popupMenu;

    public CategoryPopupMenu(JTable categoryTable) {
        popupMenu = new JPopupMenu();
        popupMenu.add(new CategoryAddAction(categoryTable));
        popupMenu.add(new CategoryEditAction(categoryTable));
        popupMenu.add(new CategoryRemoveAction(categoryTable));
    }

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }
}
