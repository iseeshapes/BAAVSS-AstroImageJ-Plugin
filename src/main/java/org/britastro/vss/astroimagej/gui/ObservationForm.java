package org.britastro.vss.astroimagej.gui;

import ij.IJ;
import ij.io.SaveDialog;
import org.britastro.vss.astroimagej.data.*;
import org.britastro.vss.astroimagej.gui.validator.DoubleTextFieldValidator;
import org.britastro.vss.astroimagej.gui.validator.ReferenceStarValidator;
import org.britastro.vss.astroimagej.gui.validator.RegexTextFieldValidator;
import org.britastro.vss.astroimagej.gui.validator.Validator;
import org.britastro.vss.astroimagej.plugin.BAAVSSPlugInException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ObservationForm implements ObservationControllerListener {
    private ObservationController observationController;

    private List<Validator> validators;

    private JFrame frame;

    private JTextField observerCode;
    private JTextField latitude;
    private JTextField longitude;
    private JTextField altitude;
    private JTextField telescope;
    private JTextField camera;
    private JTextField pixelSize;
    private JTextField timingUncertainty;

    private JTextField variableStarName;
    private JTextField chartName;
    private JComboBox<Filter> filter;
    private JTextField comment;

    private JTextField exposure;

    private String targetStarName = null;
    private TargetStarObserver targetStarObserver;

    private JButton saveButton;

    private class TargetStarObserver implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButton button = (JRadioButton)e.getSource();
            if (button.isSelected()) {
                targetStarName = button.getText();
            }
            validate();
        }
    }

    public ObservationForm(ObservationController observationController) {
        this.observationController = observationController;
        observationController.addListener(this);

        validators = new ArrayList<>();
        targetStarObserver = new TargetStarObserver();

        frame = new JFrame();
        frame.setTitle("BAA VSS CCD Observation File Generator");

        frame.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        c.gridx = 0;
        c.gridy = createHeaderPanel();
        c.gridwidth = 2;
        frame.add(createTargetStarPanel(), c);
        c.gridy++;
        frame.add(createReferenceStarPanel(), c);
        c.anchor = GridBagConstraints.SOUTH;
        c.weighty = 1.0;
        c.gridy++;
        frame.add(createButtonPanel(), c);

        frame.pack ();

        validate();
    }

    public void show () {
        frame.setVisible(true);
    }

    private void addComponent (String labelText, JComponent component, int index) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.ipadx = 5;
        c.ipady = 5;

        c.gridy = index;
        c.gridx = 0;
        frame.add(new JLabel(labelText), c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.gridx = 1;

        frame.add(component, c);
    }

    private JTextField addTextField (String labelText, String value, int index) {
        JTextField textField = new JTextField(value);
        addComponent(labelText, textField, index);
        return textField;
    }

    private void addStretchPanel (JComponent panel, int index, int width) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = width;
        c.gridy = index;
        panel.add(new JLabel(), c);
    }

    private int createHeaderPanel () {
        Header header = observationController.getHeader();
        Observation observation = observationController.getObservation();

        int index = 0;

        observerCode = addTextField("Observer Code", header.getObserverCode(), index++);
        validators.add(new RegexTextFieldValidator(observationController, observerCode, Pattern.compile("[A-Za-z0-9]{0,5}")));

        latitude = addTextField("Latitude (DD MM SS[NS])", header.getLatitude().toString(), index++);
        validators.add(new RegexTextFieldValidator(observationController, latitude, Latitude.latitudePattern));

        longitude = addTextField("Longitude (DDD MM SS[EW])", header.getLongitude().toString(), index++);
        validators.add(new RegexTextFieldValidator(observationController, longitude, Longitude.longitudePattern));

        altitude = addTextField("Altitude (m)", String.format("%.0f", header.getAltitude()), index++);
        validators.add(new DoubleTextFieldValidator(observationController, altitude, -200.0));

        telescope = addTextField("Telescope", header.getTelescope(), index++);
        validators.add(new RegexTextFieldValidator(observationController, telescope, Pattern.compile(".{0,255}")));

        camera = addTextField("Camera", header.getCamera(), index++);
        validators.add(new RegexTextFieldValidator(observationController, camera, Pattern.compile(".{0,255}")));

        pixelSize = addTextField("Arcsec per Pixel", String.format("%f", header.getPixelSize()), index++);
        validators.add(new DoubleTextFieldValidator(observationController, pixelSize, 0.0));

        timingUncertainty = addTextField("Timing uncertainty (s)",
                String.format("%.4f", header.getTimingUncertainty()), index++);
        validators.add(new DoubleTextFieldValidator(observationController, timingUncertainty, -0.0001));

        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 2;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = index++;
        JSeparator separator = new JSeparator();
        separator.setOrientation(SwingConstants.HORIZONTAL);
        frame.add(separator, c);

        variableStarName = addTextField("Variable Star", observation.getVariableStarName(), index++);
        validators.add(new RegexTextFieldValidator(observationController, variableStarName, Pattern.compile(".{0,255}")));

        chartName = addTextField("Chart Id", observation.getChartName(), index++);
        validators.add(new RegexTextFieldValidator(observationController, chartName, Pattern.compile(".{0,50}")));

        filter = new JComboBox<>(Filter.values());
        filter.setSelectedItem(observation.getFilter());
        filter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                observationController.validate();
            }
        });
        addComponent("Filter", filter, index++);

        exposure = addTextField("Exposure (s)", String.format("%f", observation.getExposure()), index++);
        validators.add(new DoubleTextFieldValidator(observationController, exposure, 0.0));

        comment = addTextField("Comment", observation.getComment(), index++);
        validators.add(new RegexTextFieldValidator(observationController, comment, Pattern.compile(".{0,255}")));

        return index;
    }

    private JPanel createTargetStarPanel() {
        JPanel panel = new JPanel();

        panel.setBorder(BorderFactory.createTitledBorder("Target Star"));
        panel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        ButtonGroup targetStar = new ButtonGroup();
        for (String starName : observationController.getTargetStarNames()) {
            JRadioButton radio = new JRadioButton();
            radio.setText(starName);
            radio.addActionListener(targetStarObserver);
            if (starName.equals(observationController.getTargetStarName())) {
                radio.setSelected(true);
                targetStarName = observationController.getTargetStarName();
            }
            targetStar.add(radio);
            panel.add(radio, c);
            c.gridx++;
        }
        c.weightx = 10.0;
        panel.add(new JLabel(), c);
        addStretchPanel(panel, 1, c.gridx);
        return panel;
    }

    private void addReferenceStar (ReferenceStar referenceStar, JPanel panel, int x, int y) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridx = x;
        c.gridy = y;

        JCheckBox checkBox = new JCheckBox();
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(80, 18));
        validators.add(new ReferenceStarValidator(observationController, checkBox, textField, referenceStar));

        c.weightx = 0.0;
        panel.add(new JLabel(String.format("%s (%.3f)", referenceStar.getName(), referenceStar.getMagnitude())), c);
        c.gridx++;
        panel.add(checkBox, c);
        c.gridx++;
        panel.add(textField, c);
        c.gridx++;
        c.weightx = 1.0;
        panel.add(new JLabel(), c);
    }

    private JPanel createReferenceStarPanel () {
        final int columnSize = 3;
        final int numberOfComponentsPerStar = 4;
        JPanel panel = new JPanel();

        panel.setBorder(BorderFactory.createTitledBorder("Reference stars"));
        panel.setLayout(new GridBagLayout());

        int row = 0;
        int column = 0;
        for (ReferenceStar referenceStar : observationController.getReferenceStars()) {
            addReferenceStar(referenceStar, panel, column * (numberOfComponentsPerStar + 1), row);
            column++;
            if (column == columnSize) {
                column = 0;
                row++;
            }
        }
        int end = columnSize * (numberOfComponentsPerStar + 1) - 1;
        if (row == 0) {
            end = column * (numberOfComponentsPerStar + 1) - 1;
        } else if (row == 1 && column == columnSize - 1) {
            row = 0;
        }
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.VERTICAL;
        c.gridheight = row + 1;
        c.gridy = 0;
        for (int x = numberOfComponentsPerStar;x<end;x+=numberOfComponentsPerStar + 1) {
            c.gridx = x;
            panel.add(new JSeparator(SwingConstants.VERTICAL), c);
        }
        addStretchPanel(panel, row + 1, columnSize * (numberOfComponentsPerStar + 1) - 1);
        return panel;
    }

    private JPanel createButtonPanel () {
        JPanel panel = new JPanel();

        panel.setLayout(new GridBagLayout());

        addStretchPanel(panel, 0, 3);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.gridy = 1;
        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });
        panel.add(saveButton, c);

        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.gridx = 1;
        panel.add(new JLabel("AIJ V" + IJ.getAstroVersion()), c);

        c.anchor = GridBagConstraints.EAST;
        c.weightx = 0.0;
        c.gridx = 2;
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                observationController.complete();
            }
        });
        panel.add(cancelButton, c);

        return panel;
    }

    @Override
    public void validate () {
        boolean valid = true;
        int referenceStarsUsed = 0;
        for (Validator validator : validators) {
            if (!validator.isValid()) {
                valid = false;
            }
            if (validator instanceof ReferenceStarValidator && ((ReferenceStarValidator)validator).isUsed()) {
                referenceStarsUsed++;
            }
        }
        if (referenceStarsUsed == 0) {
            valid = false;
        }
        if (filter != null && filter.getSelectedItem() == null) {
            valid = false;
        }
        if (targetStarName == null) {
            valid = false;
        }
        if (saveButton != null) {
            saveButton.setEnabled(valid);
        }
    }

    @Override
    public void complete() {
        frame.setVisible(false);
    }

    private void updateReferenceStar (List<ReferenceStar> referenceStars, ReferenceStarValidator validator) {
        for (ReferenceStar referenceStar : referenceStars) {
            if (referenceStar.getName().equals(validator.getReferenceStarName())) {
                referenceStar.setUsed(validator.isUsed());
                referenceStar.setChartIdentifier(validator.getChartIdentifier());
                break;
            }
        }
    }

    private void saveFile () {
        try {
            Header header = new Header();
            header.setObserverCode(observerCode.getText().trim());
            header.setLatitude(Latitude.parseLatitude(latitude.getText().trim()));
            header.setLongitude(Longitude.parseLongitude(longitude.getText().trim()));
            header.setAltitude(Double.parseDouble(altitude.getText()));
            header.setTelescope(telescope.getText().trim());
            header.setCamera(camera.getText().trim());
            header.setPixelSize(Double.parseDouble(pixelSize.getText().trim()));
            header.setTimingUncertainty(Double.parseDouble(timingUncertainty.getText().trim()));

            Observation observation = new Observation();
            observation.setVariableStarName(variableStarName.getText().trim());
            observation.setChartName(chartName.getText().trim());
            observation.setFilter((Filter)filter.getSelectedItem());
            observation.setExposure(Double.parseDouble(exposure.getText().trim()));
            observation.setComment(comment.getText().trim());

            List<ReferenceStar> referenceStars = observationController.getReferenceStars();
            for (Validator validator : validators) {
                if (validator instanceof ReferenceStarValidator) {
                    updateReferenceStar(referenceStars, (ReferenceStarValidator)validator);
                }
            }
            SaveDialog saveDialog = new SaveDialog("Save VSS Database File", observation.getVariableStarName(), ".txt");
            File file = new File(saveDialog.getDirectory(), saveDialog.getFileName());
            observationController.writeFile(header, observation, targetStarName, referenceStars, file);
        } catch (BAAVSSPlugInException e) {
            JOptionPane.showMessageDialog(IJ.getInstance(), e.getTitle() + "\n\n" + e.getMessage(),
                    "BAA VSS CCD Observation File Generator: Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
