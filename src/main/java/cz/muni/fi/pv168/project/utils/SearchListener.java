package cz.muni.fi.pv168.project.utils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

public class SearchListener implements DocumentListener {

    private final JTextField searchField;
    private final TableRowSorter sorter;

    public SearchListener(JTextField searchField, TableRowSorter rowSorter) {
        this.searchField = searchField;
        this.sorter = rowSorter;
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

    // filter by name
    private void filter() {
        String text = searchField.getText().trim();
        if (text.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    public void reset() {
        sorter.setRowFilter(null);
    }

}
