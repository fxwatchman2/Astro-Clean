package com.astrocharts.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class StudyGroup {
    private String name;
    private Date createdAt;
    private Date updatedAt;
    private List<Study> studies;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public List<Study> getStudies() { return studies; }
    public void setStudies(List<Study> studies) { this.studies = studies; }
}
