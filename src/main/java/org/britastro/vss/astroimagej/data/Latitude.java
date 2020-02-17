package org.britastro.vss.astroimagej.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.britastro.vss.astroimagej.plugin.BAAVSSPlugInException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Latitude {
    public static final Pattern latitudePattern = Pattern.compile("(\\d{1,2})\\s(\\d{1,2})\\s(\\d{1,2})([NS])");

    @JsonProperty
    private double angle;

    @JsonCreator
    public Latitude(@JsonProperty("angle") double angle) {
        this.angle = angle;
    }

    @JsonIgnore
    public double getAngle() {
        return angle;
    }

    @Override
    public String toString () {
        char direction = 'N';
        double seconds = Math.toDegrees(angle);
        if (seconds < 0) {
            direction = 'S';
            seconds *= -1;
        }
        double degrees = Math.floor(seconds);
        seconds = (seconds - degrees) * 60.0;
        double hours = Math.floor(seconds);

        return String.format("%.0f %.0f %.0f%c", degrees, hours, seconds, direction);
    }

    @JsonIgnore
    public void setAngle(double angle) {
        this.angle = angle;
    }

    public static Latitude parseLatitude(String latitude) throws BAAVSSPlugInException {
        Matcher matcher = latitudePattern.matcher(latitude.trim());
        if (matcher.matches()) {
            int degrees = Integer.parseInt(matcher.group(1));
            int minutes = Integer.parseInt(matcher.group(2));
            int seconds = Integer.parseInt(matcher.group(3));

            double angle = Math.toRadians(degrees + (double) minutes / 60 + (double) seconds / 3600);
            if ("S".equals(matcher.group(4))) {
                angle *= -1;
            }
            return new Latitude(angle);
        } else {
            throw new BAAVSSPlugInException("Cannot parse " + latitude + " as latitude");
        }
    }
}
