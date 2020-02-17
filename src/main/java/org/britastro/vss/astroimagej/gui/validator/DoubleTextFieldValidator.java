package org.britastro.vss.astroimagej.gui.validator;

import org.britastro.vss.astroimagej.gui.ObservationController;

import javax.swing.*;

public class DoubleTextFieldValidator extends TextFieldValidator {
    private double minimum;

    public DoubleTextFieldValidator(ObservationController observationController, JTextField textField, double minimum) {
        super(observationController, textField);
        this.minimum = minimum;

        validate();
    }

    @Override
    protected boolean validateText(String text) {
        try {
            return minimum < Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
