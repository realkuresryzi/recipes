package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.persistance.repository.Repository;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class RecipeTableModel extends AbstractTableModel {

    private final Repository<Recipe> recipes;

    public RecipeTableModel(Repository<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public int getRowCount() {
        return recipes.getSize();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var recipe = getEntity(rowIndex);
        return switch (columnIndex) {
            case 0 -> recipe.getName();
            case 1 -> recipe.getPortions();
            case 2 -> recipe.getDuration();
            case 3 -> recipe.getDescription();
            case 4 -> recipe.getCategory().getName();
            default -> throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
        };
    }

    public Recipe getRecipe(int rowIndex) {
        return getEntity(rowIndex);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> "Name";
            case 1 -> "Portions";
            case 2 -> "Duration (min)";
            case 3 -> "Description";
            case 4 -> "Category";
            default -> throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
        };
    }

    public void addRow(Recipe recipe) {
        int newRowIndex = recipes.getSize();
        recipes.create(recipe);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(int rowIndex, Recipe recipe) {
        recipes.update(recipe);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void deleteRow(int rowIndex) {
        recipes.deleteByIndex(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public Recipe getEntity(int rowIndex) {
        return recipes.findByIndex(rowIndex).orElseThrow();
    }

    public List<Recipe> getEntities() {
        return recipes.findAll();
    }

    public void refresh() {
        recipes.refresh();
        fireTableDataChanged();
    }
}
