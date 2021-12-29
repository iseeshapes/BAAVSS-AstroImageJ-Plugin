package org.britastro.vss.astroimagej.plugin;

public class BAAVSSPlugInException extends Exception {
    private final String title;

    public BAAVSSPlugInException(String title, String message) {
        super(message);

        this.title = title;
    }

    public BAAVSSPlugInException(String title, String message, Throwable cause) {
        super(message, cause);

        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
