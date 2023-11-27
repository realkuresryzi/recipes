package cz.muni.fi.pv168.project.ui.components;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.persistance.repository.Repository;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class CategoryTableComponent {

    private final JTable categoryTable;

    public CategoryTableComponent(Repository<Category> categories) {
        var model = new CategoryTableModel(categories);
        categoryTable = new JTable(model) {

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int Index_row, int Index_col) {
                Component comp = super.prepareRenderer(renderer, Index_row, Index_col);

                var color = model.getEntity(convertRowIndexToModel(Index_row)).getColor();
                var rowColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 50);
                comp.setForeground(Color.BLACK);
                comp.setBackground(rowColor);
                if (isRowSelected(Index_row)) {
                    comp.setFont(new Font("Dialog", Font.BOLD, 12));
                }
                return comp;
            }
        };

        categoryTable.setAutoCreateRowSorter(true);
    }

    public JTable getCategoryTable() {
        return categoryTable;
    }
}
