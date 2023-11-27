package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.persistance.repository.Repository;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class IngredientTableModel extends AbstractTableModel {

    private final Repository<Ingredient> ingredients;

    public IngredientTableModel(Repository<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public int getRowCount() {
        return ingredients.getSize();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var ingredient = getEntity(rowIndex);
        return switch (columnIndex) {
            case 0 -> ingredient.getName();
            case 1 -> ingredient.getNutritionValue() + " kcal";
            default -> throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
        };
    }

    @Override
    public String getColumnName(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> "Name";
            case 1 -> "Nutrition value";
            default -> throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
        };
    }

    public void addRow(Ingredient ingredient) {
        int newRowIndex = ingredients.getSize();
        ingredients.create(ingredient);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(int rowIndex, Ingredient ingredient) {
        ingredients.update(ingredient);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void deleteRow(int rowIndex) {
        ingredients.deleteByIndex(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public Ingredient getEntity(int rowIndex) {
        return ingredients.findByIndex(rowIndex).orElseThrow();
    }

    public List<Ingredient> getEntities() {
        return ingredients.findAll();
    }

    public Ingredient getEntityByName(String name) {
        for (Ingredient ingredient : ingredients.findAll()) {
            if (ingredient.getName().equals(name)) {
                return ingredient;
            }
        }
        return null;
    }

    public void refresh() {
        ingredients.refresh();
        fireTableDataChanged();
    }
}
