package org.britastro.vss.astroimagej.gui;

import astroj.MeasurementTable;
import com.fasterxml.jackson.databind.ObjectMapper;
import ij.IJ;
import org.britastro.vss.astroimagej.data.Header;
import org.britastro.vss.astroimagej.data.Observation;
import org.britastro.vss.astroimagej.data.ReferenceStar;
import org.britastro.vss.astroimagej.plugin.BAAVSSPlugInException;
import org.britastro.vss.astroimagej.writer.BAAVSSObservationFileWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObservationController {
    private static final Pattern magnitudeHeadingPattern = Pattern.compile("Source_AMag_([TC])(\\d+)");

    private static final String headerFilename = "header.json";
    private static final String observationFilename = "observations.json";

    private Header header;
    private Observation observation;
    private String targetStarName;
    private List<String> targetStarNames;
    private List<ReferenceStar> referenceStars;
    private MeasurementTable measurementTable;
    private boolean complete;

    private List<ObservationControllerListener> listeners = new ArrayList<>();

    public ObservationController () throws BAAVSSPlugInException {
        measurementTable = getMeasurementTable();
        targetStarNames = new ArrayList<>();
        referenceStars = new ArrayList<>();
        populateStarNames();
        readPreviousData();

        complete = false;
    }

    public void addListener (ObservationControllerListener listener) {
        listeners.add(listener);
    }

    private File getDirectory () {
        return new File (System.getProperty("user.home"), ".astroimagej");
    }

    private void readPreviousData () {
        header = null;
        observation = null;
        File directory = getDirectory();

        try {
            if (directory.exists()) {
                ObjectMapper objectMapper = new ObjectMapper();

                File headerFile = new File(directory, headerFilename);
                if (headerFile.exists()) {
                    header = objectMapper.readValue(headerFile, Header.class);
                }

                File observationFile = new File (directory, observationFilename);
                if (observationFile.exists()) {
                    observation = objectMapper.readValue (observationFile, Observation.class);
                }
            }
        } catch (IOException e) {
            IJ.log("Failed to open BAAVSS settings files " + e.getMessage());
        }
        if (header == null) {
            header = new Header();
        }
        if (observation == null) {
            observation = new Observation();
        }
    }

    private void writePreviousData () {
        File directory = getDirectory();

        try {
            if (!directory.exists()) {
                Files.createDirectory(directory.toPath());
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(directory, headerFilename), header);
            mapper.writeValue(new File(directory, observationFilename), observation);
        } catch (IOException e) {
            IJ.log("Failed to save BAAVSS settings files " + e.getMessage());
        }
    }

    private MeasurementTable getMeasurementTable () throws BAAVSSPlugInException {
        String[] tableNames = MeasurementTable.getMeasurementTableNames();

        if (tableNames == null || tableNames.length == 0) {
            throw new BAAVSSPlugInException("No measurement table found.",
                    "If you have closed the table please reopen using the open table button on the main menu.");
        }

        if (tableNames.length > 1) {
            throw new BAAVSSPlugInException("Only one Measurement Table used at any one time.",
                    "Please close all tables other than Measurement Table with the the data to be exported.");
        }

        MeasurementTable table = MeasurementTable.getTable(tableNames[0]);
        if (table.getCounter() == 0) {
            throw new BAAVSSPlugInException("There is no data in the Measurement Table.",
                    "Please process some images or open a valid table.");
        }
        return table;
    }

    private void populateStarNames () throws BAAVSSPlugInException {
        String[] headings = measurementTable.getHeadings();
        for (String heading : headings) {
            Matcher matcher = magnitudeHeadingPattern.matcher(heading);
            if (matcher.matches()) {
                if ("T".equals(matcher.group(1))) {
                    targetStarNames.add("T" + matcher.group(2));
                } else if ("C".equals(matcher.group(1))) {
                    String starName = "C" + matcher.group(2);
                    double magnitude = measurementTable.getValue(heading, 0);
                    referenceStars.add(new ReferenceStar(starName, magnitude));
                }
            }
        }

        if (targetStarNames.isEmpty()) {
            throw new BAAVSSPlugInException("No target stars with magnitudes found.",
                    "Ensure that at least one star is selected as a target star.");
        }
        targetStarName = targetStarNames.get(0);

        if (referenceStars.isEmpty()) {
            throw new BAAVSSPlugInException("No comparision stars with magnitudes found.",
                    "At least one comparison star star with known magnitude must be found in the Measurement Table.");
        }
    }

    public Header getHeader() {
        return header;
    }

    public Observation getObservation() {
        return observation;
    }

    public List<ReferenceStar> getReferenceStars() {
        return referenceStars;
    }

    public List<String> getTargetStarNames() {
        return targetStarNames;
    }

    public String getTargetStarName() {
        return targetStarName;
    }

    public void writeFile (Header header, Observation observation, String targetStarName,
                           List<ReferenceStar> referenceStars, File file) throws BAAVSSPlugInException {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            BAAVSSObservationFileWriter observationFileWriter = new BAAVSSObservationFileWriter(writer, measurementTable);
            observationFileWriter.writeHeader(header, observation);
            observationFileWriter.writeRow(observation, targetStarName, referenceStars);
            writer.flush();

            this.header = header;
            this.observation = observation;
            this.targetStarName = targetStarName;
            this.referenceStars = referenceStars;

            writePreviousData();
            complete();
        } catch (IOException e) {
            throw new BAAVSSPlugInException("Unable to save observation to file", e.getMessage(), e);
        }
    }

    public void validate () {
        for (ObservationControllerListener listener : listeners) {
            listener.validate ();
        }
    }

    public void complete () {
        complete = true;
        for (ObservationControllerListener listener : listeners) {
            listener.complete ();
        }
    }

    public boolean isComplete() {
        return complete;
    }
}
