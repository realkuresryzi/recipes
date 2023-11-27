package cz.muni.fi.pv168.project.ui.components;

import cz.muni.fi.pv168.project.ui.dialog.AboutDialog;
import cz.muni.fi.pv168.project.ui.dialog.SettingsDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MenuBarComponent {

    private final JMenuBar menuBar;

    public MenuBarComponent(ActionEvent e) {
        menuBar = new JMenuBar();
        var fileMenu = new JMenu("File");

        var settingsMenuItem = new JMenuItem("Settings");
        settingsMenuItem.addActionListener(this::settings);
        fileMenu.add(settingsMenuItem);

        var unitsMenuItem = new JMenuItem("Units");
        //unitsMenuItem.addActionListener(this::units);
        fileMenu.add(unitsMenuItem);

        var importMenuItem = new JMenuItem("Import");
        //importMenuItem.addActionListener(this::importFile);
        fileMenu.add(importMenuItem);

        var exportMenuItem = new JMenuItem("Export");
        //exportMenuItem.addActionListener(this::exportFile);
        fileMenu.add(exportMenuItem);

        var exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(this::exit);
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);
        var helpMenu = new JMenu("Help");
        var aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(this::about);
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);
    }

    private void exit(ActionEvent e) {
        System.exit(0);
    }

    private void about(ActionEvent e) {
        var dialog = new AboutDialog();
        dialog.show(null, "About");
    }

    private void settings(ActionEvent e) {
        var dialog = new SettingsDialog();
        dialog.show(null, "Settings");
    }

}
