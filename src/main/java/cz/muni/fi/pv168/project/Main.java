package cz.muni.fi.pv168.project;

import cz.muni.fi.pv168.project.ui.MainWindow;
import cz.muni.fi.pv168.project.wiring.DependencyProvider;
import cz.muni.fi.pv168.project.wiring.ProductionDependencyProvider;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The entry point of the application.
 */
public class Main {

    public static void main(String[] args) {
        boolean populate = args.length > 0 && args[0].equals("--populate");
        DependencyProvider dependencyProvider = new ProductionDependencyProvider(populate);
        if (populate) {
            dependencyProvider.getCreatedDatabaseManager().populateSchema();
            dependencyProvider.refresh();
        }
        EventQueue.invokeLater(() -> new MainWindow(dependencyProvider).show());
    }

    private static void initNimbusLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Nimbus layout initialization failed", ex);
        }
    }

}
