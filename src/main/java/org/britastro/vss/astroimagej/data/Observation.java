package org.britastro.vss.astroimagej.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Observation {
    @JsonProperty
    private String variableStarName;
    @JsonProperty
    private String chartName;
    @JsonProperty
    private Filter filter;
    @JsonProperty
    private double exposure;
    @JsonProperty
    private String comment;

    public Observation() {
        variableStarName = "";
        chartName = "";
        filter = Filter.V;
        exposure = 0.0;
        comment = "";
    }

    @JsonIgnore
    public String getVariableStarName() {
        return variableStarName;
    }

    @JsonIgnore
    public void setVariableStarName(String variableStarName) {
        this.variableStarName = variableStarName;
    }

    @JsonIgnore
    public String getChartName() {
        return chartName;
    }

    @JsonIgnore
    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    @JsonIgnore
    public Filter getFilter() {
        return filter;
    }

    @JsonIgnore
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    @JsonIgnore
    public double getExposure() {
        return exposure;
    }

    @JsonIgnore
    public void setExposure(double exposure) {
        this.exposure = exposure;
    }

    @JsonIgnore
    public String getComment() {
        return comment;
    }

    @JsonIgnore
    public void setComment(String comment) {
        this.comment = comment;
    }
}
