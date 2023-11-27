package cz.muni.fi.pv168.project.ui.components;

import javax.swing.*;

public class ToolBarComponent {

    private final JToolBar toolBar;


    public ToolBarComponent(JButton add, JButton edit, JButton delete, JLabel totalRecipes) {
        toolBar = new JToolBar();
        toolBar.setFloatable(false);

        toolBar.add(add);
        toolBar.addSeparator();
        toolBar.add(edit);
        toolBar.addSeparator();
        toolBar.add(delete);


        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(totalRecipes);
    }

    public JToolBar getToolBar() {
        return toolBar;
    }

}
