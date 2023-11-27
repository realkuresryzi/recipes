package cz.muni.fi.pv168.project.ui.dialog;

import javax.swing.*;

public class AboutDialog extends NonEntityDialog {

    private final JTextPane aboutPane = new JTextPane();

    public AboutDialog() {
        super(null);
        setValues();
        addFields();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    }

    private void setValues() {
        aboutPane.setText("This is what we are all about.");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    }

    private void addFields() {
        add("", aboutPane);
    }

    @Override
    void resetEntity() {
        // do nothing
    }
}
