package org.britastro.vss.astroimagej.data;

public class ReferenceStar {
    private boolean used;
    private String name;
    private String chartIdentifier;
    private double magnitude;

    public ReferenceStar(String name, double magnitude) {
        this.used = true;
        this.name = name;
        this.chartIdentifier = "";
        this.magnitude = magnitude;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getName() {
        return name;
    }

    public String getChartIdentifier() {
        return chartIdentifier;
    }

    public void setChartIdentifier(String chartIdentifier) {
        this.chartIdentifier = chartIdentifier;
    }

    public double getMagnitude() {
        return magnitude;
    }
}
