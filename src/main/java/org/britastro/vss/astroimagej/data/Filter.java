package org.britastro.vss.astroimagej.data;

public enum Filter {
    N ("No Filter"),
    U ("Johnson U"),
    B ("Johnson B"),
    V ("Johnson V"),
    R ("Cousins R"),
    I ("Cousins I"),
    SU ("Sloan U"),
    SG ("Sloan G"),
    SR ("Sloan R"),
    SI ("Sloan I"),
    SZ ("Sloan Z"),
    C ("Clear"),
    CR ("Clear (unfiltered) R-band comp star mag"),
    CV ("Clear (unfiltered) V-band comp star mag"),
    TB ("Blue Filter (tricolour)"),
    TG ("Green Filter (tricolour)"),
    TR ("Red Filter (tricolour)"),
    VG ("Corrected Green Channel to Johnson V"),
    BesB ("Bessell-B"),
    BesV ("Bessell-V"),
    IRB ("Infrared Blocking"),
    J ("NIR 1.2 micron"),
    H ("NIR 1.6 micron"),
    K ("NIR 2.2 micron"),
    TY ("Yellow Filter"),
    Z ("Sloan Z?");

    private String displayName;

    Filter (String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
