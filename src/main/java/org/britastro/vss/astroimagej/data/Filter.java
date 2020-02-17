package org.britastro.vss.astroimagej.data;

public enum Filter {
    B ("Johnson B"),
    BesB ("Bessell-B"),
    BesV ("Bessell-V"),
    C ("Clear"),
    CR ("Clear (unfiltered) R-band comp star mag"),
    CV ("Clear (unfiltered) V-band comp star mag"),
    H ("NIR 1.6 micron"),
    I ("Cousins I"),
    IRB ("Infrared Blocking"),
    J ("NIR 1.2 micron"),
    K ("NIR 2.2 micron"),
    N ("No Filter"),
    R ("Cousins R"),
    SG ("Sloan G"),
    SI ("Sloan I"),
    SR ("Sloan R"),
    SU ("Sloan U"),
    SZ ("Sloan Z"),
    TB ("Blue Filter (tricolour)"),
    TG ("Green Filter (tricolour)"),
    TR ("Red Filter (tricolour)"),
    TY ("Yellow Filter"),
    U ("Johnson U"),
    V ("Johnson V"),
    VG ("Corrected Green Channel to Johnson V"),
    Z ("Sloan Z");

    private String displayName;

    Filter (String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
