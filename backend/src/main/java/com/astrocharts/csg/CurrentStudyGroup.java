package com.astrocharts.csg;

import java.util.ArrayList;
import java.util.List;

public class CurrentStudyGroup {
    private List<Study> studies = new ArrayList<>();

    public List<Study> getStudies() {
        return studies;
    }

    public void setStudies(List<Study> studies) {
        this.studies = studies;
    }

    public void clear() {
        studies.clear();
    }

    public void addStudy(Study study) {
        studies.add(study);
    }

    public void removeStudy(int index) {
        if (index >= 0 && index < studies.size()) {
            studies.remove(index);
        }
    }
}
