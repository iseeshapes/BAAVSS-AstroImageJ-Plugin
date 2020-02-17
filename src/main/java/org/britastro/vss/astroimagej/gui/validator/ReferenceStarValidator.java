package org.britastro.vss.astroimagej.gui.validator;

import org.britastro.vss.astroimagej.data.ReferenceStar;
import org.britastro.vss.astroimagej.gui.Colours;
import org.britastro.vss.astroimagej.gui.ObservationController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReferenceStarValidator implements Validator, DocumentListener, ActionListener {
    private static final Pattern identifierPattern = Pattern.compile(".{1,255}");

    private ObservationController observationController;
    private JCheckBox checkBox;
    private JTextField textField;
    private String referenceStarName;
    private String previousValue;
    private boolean valid;

    public ReferenceStarValidator(ObservationController observationController, JCheckBox checkBox, JTextField textField,
                                  ReferenceStar referenceStar) {
        this.observationController = observationController;
        this.checkBox = checkBox;
        this.textField = textField;
        this.referenceStarName = referenceStar.getName();
        this.previousValue = referenceStar.getChartIdentifier();

        textField.setText(previousValue);

        checkBox.addActionListener(this);
        checkBox.setSelected(referenceStar.isUsed());
        actionPerformed(null);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        validate ();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        validate();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        validate();
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    private void validate () {
        if (checkBox.isSelected()) {
            try {
                Matcher matcher = identifierPattern.matcher(textField.getText());
                if (matcher.matches()) {
                    textField.setBackground(Colours.success);
                    valid = true;
                } else {
                    textField.setBackground(Colours.fail);
                    valid = false;
                }
            } catch (NumberFormatException e) {
                textField.setBackground(Colours.fail);
                valid = false;
            }
        } else {
            valid = true;
        }
        observationController.validate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (checkBox.isSelected()) {
            textField.setText(previousValue);
            textField.setEnabled(true);
            textField.getDocument().addDocumentListener(this);
            validate();
        } else {
            textField.getDocument().removeDocumentListener(this);
            previousValue = textField.getText();
            textField.setText("");
            textField.setEnabled(false);
            textField.setBackground(Colours.disabled);
            valid = true;
        }
    }

    public String getReferenceStarName() {
        return referenceStarName;
    }

    public boolean isUsed () {
        return checkBox.isSelected();
    }

    public String getChartIdentifier() {
        if (checkBox.isSelected() && valid) {
            return textField.getText();
        } else {
            return "";
        }
    }
}
