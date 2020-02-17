package org.britastro.vss.astroimagej.gui.validator;

import org.britastro.vss.astroimagej.gui.ObservationController;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTextFieldValidator extends TextFieldValidator {
    private Pattern pattern;

    public RegexTextFieldValidator(ObservationController observationController, JTextField textField, Pattern pattern) {
        super(observationController, textField);
        this.pattern = pattern;

        validate();
    }

    @Override
    protected boolean validateText (String text) {
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
}
