package cz.muni.fi.pv168.project.ui.components;

import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.persistance.repository.Repository;
import cz.muni.fi.pv168.project.ui.dialog.RecipeDetailDialog;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RecipeTableComponent {

    private final JTable recipeTable;
    private TableModel categoryTableModel;
    private TableModel ingredientTableModel;


    public RecipeTableComponent(Repository<Recipe> recipes, TableModel categoryTableModel, TableModel ingredientTableModel) {
        this.categoryTableModel = categoryTableModel;
        this.ingredientTableModel = ingredientTableModel;

        var model = new RecipeTableModel(recipes);
        this.recipeTable = new JTable(model) {
            public Component prepareRenderer(TableCellRenderer renderer, int Index_row, int Index_col) {
                Component comp = super.prepareRenderer(renderer, Index_row, Index_col);
                var color = model.getRecipe(convertRowIndexToModel(Index_row)).getCategory().getColor();
                var rowColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 50);
                comp.setBackground(rowColor);
                comp.setForeground(Color.BLACK);
                if (isRowSelected(Index_row)) {
                    comp.setFont(new Font("Dialog", Font.BOLD, 12));
                }
                return comp;
            }
        };

        this.recipeTable.setAutoCreateRowSorter(true);
        this.recipeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                tableDoubleClick(e);
            }
        });

    }

    public JTable getRecipeTable() {
        return recipeTable;
    }

    private void tableDoubleClick(MouseEvent mouseEvent) {
        JTable table = (JTable) mouseEvent.getSource();
        Point point = mouseEvent.getPoint();
        int row = table.convertRowIndexToModel(table.rowAtPoint(point));
        if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
            var recipe = ((RecipeTableModel) table.getModel()).getRecipe(row);
            recipeTable.setPreferredSize(new Dimension(800, 600));
            new RecipeDetailDialog(recipe, (CategoryTableModel) categoryTableModel, (IngredientTableModel) ingredientTableModel).show(recipeTable, recipe.getName() + " detail");
        }
    }


}
