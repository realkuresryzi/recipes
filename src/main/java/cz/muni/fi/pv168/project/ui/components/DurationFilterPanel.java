package cz.muni.fi.pv168.project.ui.components;

import cz.muni.fi.pv168.project.utils.DurationListener;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class DurationFilterPanel {

    private final JPanel panel;

    public DurationFilterPanel(JTextField fromField, JTextField toField, TableRowSorter sorter) {
        panel = new JPanel();

        var durationLabel = new JLabel("Duration:");
        panel.add(durationLabel);

        var fromFilterComponent = new JPanel();
        fromFilterComponent.setLayout(new BoxLayout(fromFilterComponent, BoxLayout.PAGE_AXIS));
        fromFilterComponent.setAlignmentX(Component.LEFT_ALIGNMENT);


        fromField.setAlignmentX(Component.LEFT_ALIGNMENT);
        fromField.setPreferredSize(new Dimension(50, 20));


        var fromLabel = new JLabel("From");
        fromLabel.setAlignmentX(Component.LEFT_ALIGNMENT);


        var toFilterComponent = new JPanel();
        toFilterComponent.setLayout(new BoxLayout(toFilterComponent, BoxLayout.PAGE_AXIS));
        toFilterComponent.setAlignmentX(Component.LEFT_ALIGNMENT);

        toField.setAlignmentX(Component.LEFT_ALIGNMENT);
        toField.setPreferredSize(new Dimension(50, 20));

        DurationListener durationListener = new DurationListener(fromField, toField, sorter);

        fromField.getDocument().addDocumentListener(durationListener);
        toField.getDocument().addDocumentListener(durationListener);

        var toLabel = new JLabel("To");
        toLabel.setAlignmentX(Component.LEFT_ALIGNMENT);


        fromFilterComponent.add(fromLabel);
        fromFilterComponent.add(fromField);
        toFilterComponent.add(toLabel);
        toFilterComponent.add(toField);


        panel.add(fromFilterComponent);
        panel.add(toFilterComponent);
    }

    public JPanel getPanel() {
        return panel;
    }

}
