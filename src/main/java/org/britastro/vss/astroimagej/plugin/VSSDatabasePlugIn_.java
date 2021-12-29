package org.britastro.vss.astroimagej.plugin;

import ij.IJ;
import ij.plugin.PlugIn;
import org.britastro.vss.astroimagej.gui.ObservationController;
import org.britastro.vss.astroimagej.gui.ObservationForm;

import javax.swing.*;

public class VSSDatabasePlugIn_ implements PlugIn {
    private static ObservationController observationController = null;
    private static ObservationForm form = null;

    @Override
    public void run(String s) {
        if (observationController == null || observationController.isComplete ()) {
            form = null;
            try {
                observationController = new ObservationController();
                form = new ObservationForm(observationController);
            } catch (BAAVSSPlugInException e) {
                JOptionPane.showMessageDialog(IJ.getInstance(), e.getTitle() +
                                "\n\nThis plugin is designed to be used as the last stage in the data reduction process.  It " +
                                "\nrelies on the data in Measurement Table to produce BAA VSS photometric formatted data.\n\n" +
                                e.getMessage(),
                        "BAA VSS CCD Observation File Generator: Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (form != null) {
            form.show();
        }
    }
}
