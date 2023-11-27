package cz.muni.fi.pv168.project.ui.components;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.persistance.repository.Repository;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class IngredientTableComponent {

    private final JTable ingredientTable;

    public IngredientTableComponent(Repository<Ingredient> ingredients) {
        var model = new IngredientTableModel(ingredients);

        ingredientTable = new JTable(model) {
            public Component prepareRenderer(TableCellRenderer renderer, int Index_row, int Index_col) {
                Component comp = super.prepareRenderer(renderer, Index_row, Index_col);
                comp.setForeground(Color.BLACK);
                comp.setBackground(Color.WHITE);
                if (isRowSelected(Index_row)) {
                    comp.setFont(new Font("Dialog", Font.BOLD, 12));
                }
                return comp;
            }
        };
        ingredientTable.setAutoCreateRowSorter(true);

    }

    public JTable getIngredientTable() {
        return ingredientTable;
    }
}
