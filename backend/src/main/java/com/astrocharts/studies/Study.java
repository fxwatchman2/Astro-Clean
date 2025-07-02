package com.astrocharts.studies;

import com.astrocharts.dto.StudyResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = LongitudinalDistanceStudy.class, name = "longitudinalDistance"),
    @JsonSubTypes.Type(value = PlanetaryIngressStudy.class, name = "planetaryIngress"),
    @JsonSubTypes.Type(value = PlanetInSignStudy.class, name = "planetInSign")
})
public abstract class Study {
    private String tupleText;

    private String color;
    private String displayType;
    private String frameOfReference;

    public abstract StudyResult execute(String chartFirstDate, String chartLastDate);

    public String getColor() {
        return color;
    }

    @JsonProperty("color")
    public void setColor(String color) {
        this.color = color;
    }

    public String getDisplayType() {
        return displayType;
    }

    @JsonProperty("displayType")
    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public String getFrameOfReference() {
        return frameOfReference;
    }

    @JsonProperty("frameOfReference")
    public void setFrameOfReference(String frameOfReference) {
        this.frameOfReference = frameOfReference;
    }

    public String getTuple() {
    return getTupleText() != null ? getTupleText() : "";
}

public String getTupleText() {
        return tupleText;
    }

    @JsonProperty("tupleText")
    public void setTupleText(String tupleText) {
        this.tupleText = tupleText;
    }
}
