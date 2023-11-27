package cz.muni.fi.pv168.project.ui.components;

import cz.muni.fi.pv168.project.utils.SearchListener;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class SearchFilterPanel {

    private final JPanel panel;
    private final JTextField searchField;

    public SearchFilterPanel(TableRowSorter sorter) {
        panel = new JPanel();
        var flowLayout = new FlowLayout(FlowLayout.LEFT);
        panel.setLayout(flowLayout);
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 20));

        var searchLabel = new JLabel("Search by Name:");
        var searchListener = new SearchListener(searchField, sorter);
        searchField.getDocument().addDocumentListener(searchListener);

        panel.add(searchLabel);
        panel.add(searchField);
    }

    public JPanel getPanel() {
        return panel;
    }

    public void resetTextField() {
        searchField.setText("");
    }

}
