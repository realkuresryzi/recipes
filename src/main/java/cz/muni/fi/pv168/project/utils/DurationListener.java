package cz.muni.fi.pv168.project.utils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.List;

public class DurationListener implements DocumentListener {

    private final JTextField fromField;
    private final JTextField toField;
    private final TableRowSorter sorter;

    public DurationListener(JTextField durationField, JTextField toField, TableRowSorter rowSorter) {
        this.fromField = durationField;
        this.toField = toField;
        this.sorter = rowSorter;
    }

    private static Integer parseIntOrDefault(String toParse, int defaultValue) {
        try {
            return Integer.parseInt(toParse);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        filter();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        filter();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        filter();
    }

    // parse from and to field to int and filter by duration in range from-to
    private void filter() {
        try {
            int from = parseIntOrDefault(fromField.getText(), 0);
            int to = parseIntOrDefault(toField.getText(), Integer.MAX_VALUE);

            if (from > to) {
                int tmp = from;
                from = to;
                to = tmp;
            }

            List<RowFilter<Object, Object>> filters = new ArrayList<>();
            filters.add(RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, from, 2));
            filters.add(RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, to, 2));
            sorter.setRowFilter(RowFilter.andFilter(filters));
        } catch (NumberFormatException e) {
            sorter.setRowFilter(null);
        }
    }

    public void reset() {
        sorter.setRowFilter(null);
    }

}
