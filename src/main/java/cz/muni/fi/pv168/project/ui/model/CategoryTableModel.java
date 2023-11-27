package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.persistance.repository.Repository;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class CategoryTableModel extends AbstractTableModel {

    private final Repository<Category> categories;

    public CategoryTableModel(Repository<Category> categories) {
        this.categories = categories;
    }


    @Override
    public int getRowCount() {
        return categories.getSize();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var category = getEntity(rowIndex);
        return switch (columnIndex) {
            case 0 -> category.getName();
            case 1 -> category.getDescription();
            default -> throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
        };
    }

    @Override
    public String getColumnName(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> "Name";
            case 1 -> "Description";
            default -> throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
        };
    }

    public void addRow(Category category) {
        int newRowIndex = categories.getSize();
        categories.create(category);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(int rowIndex, Category category) {
        categories.update(category);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void deleteRow(int rowIndex) {
        categories.deleteByIndex(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public Category getEntity(int rowIndex) {
        return categories.findByIndex(rowIndex).orElseThrow();
    }

    public List<Category> getEntities() {
        return categories.findAll();
    }

    public Category getEntityByName(String name) {
        for (Category category : categories.findAll()) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
    }

    public void refresh() {
        categories.refresh();
        fireTableDataChanged();
    }

}
