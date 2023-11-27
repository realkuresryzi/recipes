package cz.muni.fi.pv168.project.ui.dialog;


import cz.muni.fi.pv168.project.persistance.validation.Validator;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.Optional;

import static javax.swing.JOptionPane.*;

abstract class EntityDialog<E> {

    private final Validator<E> validator;


    protected JPanel panel = new JPanel();

    EntityDialog(Validator<E> validator) {
        this.validator = validator;
        panel.setLayout(new MigLayout("wrap 2"));
    }

    void add(String labelText, JComponent component) {
        var label = new JLabel(labelText);
        panel.add(label);
        panel.add(component, "wmin 250lp, grow");
    }

    void addWithConstraints(String labelText, JComponent component, String constraints) {
        var label = new JLabel(labelText);
        panel.add(label);
        panel.add(component, constraints);
    }

    void add(JComponent leftComponent, JComponent rightComponent) {
        panel.add(leftComponent, "gapleft, wmax 250lp, grow");
        panel.add(rightComponent, "gapleft 10, grow");
    }

    abstract E getEntity();

    abstract void resetEntity();


    public Optional<E> show(JComponent parentComponent, String title) {
        int result = showOptionDialog(parentComponent, panel, title,
                OK_CANCEL_OPTION, PLAIN_MESSAGE, null, null, null);

        while (result == OK_OPTION) {
            var entity = getEntity();

            if (validator != null) {
                var validation = validator.validate(entity);

                try {
                    validation.intoException();
                    return Optional.of(entity);

                } catch (Exception e) {
                    resetEntity();
                    result = showOptionDialog(parentComponent, panel, title,
                            OK_CANCEL_OPTION, PLAIN_MESSAGE, null, null, null);


                }
            } else {
                return Optional.of(entity);
            }




        }
        return Optional.empty();
    }
}
