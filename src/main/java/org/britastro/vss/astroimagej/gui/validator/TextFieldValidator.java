package org.britastro.vss.astroimagej.gui.validator;

import org.britastro.vss.astroimagej.gui.Colours;
import org.britastro.vss.astroimagej.gui.ObservationController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public abstract class TextFieldValidator implements Validator, DocumentListener {
    private ObservationController observationController;
    private JTextField textField;
    private boolean valid;

    public TextFieldValidator(ObservationController observationController, JTextField textField) {
        this.observationController = observationController;
        this.textField = textField;

        textField.getDocument().addDocumentListener(this);
    }

    protected abstract boolean validateText (String text);

    protected void validate () {
        valid = validateText(textField.getText().trim());
        if (valid) {
            textField.setBackground(Colours.success);
        } else {
            textField.setBackground(Colours.fail);
        }
        observationController.validate();
    }

    public boolean isValid() {
        return valid;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        validate();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        validate();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        validate();
    }
}
