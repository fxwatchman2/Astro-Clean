package com.astrocharts.csg;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CSGService {
    private final CurrentStudyGroup csg = new CurrentStudyGroup();

    public List<Study> getStudies() {
        return csg.getStudies();
    }

    public void setStudies(List<Study> studies) {
        csg.setStudies(studies);
    }

    public boolean addStudy(Study study) {
        // Enforce tuple uniqueness for longitude study
        if (!csg.getStudies().contains(study)) {
            csg.addStudy(study);
            return true;
        }
        return false;
    }

    public void removeStudy(int index) {
        csg.removeStudy(index);
    }

    public void clear() {
        csg.clear();
    }
}
