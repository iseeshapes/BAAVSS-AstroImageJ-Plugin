package org.britastro.vss.astroimagej.writer;

import astroj.MeasurementTable;
import org.britastro.vss.astroimagej.data.Header;
import org.britastro.vss.astroimagej.data.Observation;
import org.britastro.vss.astroimagej.data.ReferenceStar;

import java.io.PrintWriter;
import java.util.List;

public class BAAVSSObservationFileWriter {
    private PrintWriter writer;
    private MeasurementTable measurementTable;

    public BAAVSSObservationFileWriter(PrintWriter writer, MeasurementTable measurementTable) {
        this.writer = writer;
        this.measurementTable = measurementTable;
    }

    public void writeHeader (Header header, Observation observation) {
        double sourceRadius = measurementTable.getValue("Source_Radius", 0);
        double skyRadiusMin = measurementTable.getValue("Sky_Rad(min)", 0);
        double skyRadiusMax = measurementTable.getValue("Sky_Rad(max)", 0);

        writer.printf("File Format\tCCD/DSLR v2.01%n");
        writer.printf("Observation Method\tCCD%n");
        writer.printf("Variable\t%s%n", observation.getVariableStarName());
        writer.printf("Chart ID\t%s%n", observation.getChartName());
        writer.printf("Observer code\t%s%n", header.getObserverCode());
        writer.printf("Location\t%s %s H%.0f%n", header.getLatitude(), header.getLongitude(), header.getAltitude());
        writer.printf("Telescope\t%s%n", header.getTelescope());
        writer.printf("Camera\t%s%n", header.getCamera());
        writer.printf("Magnitude type\tInstrumental%n");
        writer.printf("Timing uncertainty\t%.2f%n", header.getTimingUncertainty());
        writer.printf("Phot star rad (arcsec)\t%.2f%n", header.getPixelSize() * sourceRadius);
        writer.printf("Phot inner ann (arcsec)\t%.2f%n", header.getPixelSize() * skyRadiusMin);
        writer.printf("Phot outer ann (arcsec)\t%.2f%n", header.getPixelSize() * skyRadiusMax);
        writer.printf("Photometry software\tBAA VSS PlugIn for AstroImageJ 3.3.1%n");
        writer.printf("Analysis software\tBAA VSS PlugIn for AstroImageJ 3.3.1%n");
        writer.printf("Comment\t%s%n", observation.getComment());
    }

    private void printMagnitudes (int row, String starName, double exposure) {
        writer.printf("%.4f\t", measurementTable.getValue("Source_AMag_" + starName, row));
        writer.printf("%.4f\t", measurementTable.getValue("Source_AMag_Err_" + starName, row));
        double source = measurementTable.getValue("Source-Sky_" + starName, row);
        writer.printf("%.4f\t", -2.5 * Math.log10(source/exposure));

        double signalToNoise = measurementTable.getValue("Source_SNR_" + starName, row);
        writer.printf("%.4f\t", -2.5 * Math.log10(1.0857/signalToNoise));
    }

    public void writeRow (Observation observation, String targetStarName, List<ReferenceStar> referenceStars) {
        writer.print("JulianDate\t");
        writer.print("Filter\t");
        writer.print("VarAbsMag\t");
        writer.print("VarAbsErr\t");
        writer.print("VarMag\t");
        writer.print("VarErr\t");
        writer.print("ExpLen\t");
        writer.print("FileName\t");
        for (ReferenceStar referenceStar : referenceStars) {
            if (referenceStar.isUsed()) {
                writer.print("CmpStar\t");
                writer.print("RefMag\t");
                writer.print("RefErr\t");
                writer.print("CMMag\t");
                writer.print("CmpErr\t");
            }
        }
        writer.printf("%n");

        for (int row=0;row<measurementTable.getCounter();row++) {
            writer.printf("%.4f\t", measurementTable.getValue("JD_UTC", row));
            writer.printf("%s\t", observation.getFilter().name());

            double exposure;
            try {
                exposure = measurementTable.getValue("EXPTIME", row);
                if (Double.isNaN(exposure)) {
                    exposure = observation.getExposure();
                }
            } catch (IllegalArgumentException e) {
                exposure = observation.getExposure();
            }

            printMagnitudes(row, targetStarName, exposure);

            writer.printf("%.2f\t", exposure);

            String filename = measurementTable.getLabel(row);
            if (filename.matches(".*\\s.*")) {
                filename = "\"" + filename + "\"";
            }
            writer.printf("%s\t", filename);

            for (ReferenceStar referenceStar : referenceStars) {
                if (referenceStar.isUsed()) {
                    writer.printf("%s\t", referenceStar.getChartIdentifier());
                    printMagnitudes(row, referenceStar.getName(), exposure);
                }
            }

            writer.printf("%n");
        }
    }
}
