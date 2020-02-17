package org.britastro.vss.astroimagej.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Header {
    @JsonProperty
    private String observerCode;
    @JsonProperty
    private Latitude latitude;
    @JsonProperty
    private Longitude longitude;
    @JsonProperty
    private double altitude;
    @JsonProperty
    private String telescope;
    @JsonProperty
    private String camera;
    @JsonProperty
    private double timingUncertainty;
    @JsonProperty
    private double pixelSize;

    public Header() {
        observerCode = "";
        latitude = new Latitude(0.0);
        longitude = new Longitude(0.0);
        altitude = 0;
        telescope = "";
        camera = "";
        timingUncertainty = 0.0;
        pixelSize = 0.0;
    }

    @JsonIgnore
    public String getObserverCode() {
        return observerCode;
    }

    @JsonIgnore
    public void setObserverCode(String observerCode) {
        this.observerCode = observerCode;
    }

    @JsonIgnore
    public Latitude getLatitude() {
        return latitude;
    }

    @JsonIgnore
    public void setLatitude(Latitude latitude) {
        this.latitude = latitude;
    }

    @JsonIgnore
    public Longitude getLongitude() {
        return longitude;
    }

    @JsonIgnore
    public void setLongitude(Longitude longitude) {
        this.longitude = longitude;
    }

    @JsonIgnore
    public double getAltitude() {
        return altitude;
    }

    @JsonIgnore
    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    @JsonIgnore
    public String getTelescope() {
        return telescope;
    }

    @JsonIgnore
    public void setTelescope(String telescope) {
        this.telescope = telescope;
    }

    @JsonIgnore
    public String getCamera() {
        return camera;
    }

    @JsonIgnore
    public void setCamera(String camera) {
        this.camera = camera;
    }

    @JsonIgnore
    public double getTimingUncertainty() {
        return timingUncertainty;
    }

    @JsonIgnore
    public void setTimingUncertainty(double timingUncertainty) {
        this.timingUncertainty = timingUncertainty;
    }

    @JsonIgnore
    public double getPixelSize() {
        return pixelSize;
    }

    @JsonIgnore
    public void setPixelSize(double pixelSize) {
        this.pixelSize = pixelSize;
    }
}
