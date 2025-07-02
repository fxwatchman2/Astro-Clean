package com.astrocharts.csg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/csg")
public class CSGController {
    @Autowired
    private CSGService csgService;

    // Load current CSG
    @GetMapping
    public List<Study> getCSG() {
        return csgService.getStudies();
    }

    // Replace all studies (save CSG)
    @PostMapping
    public void saveCSG(@RequestBody List<Study> studies) {
        csgService.setStudies(studies);
    }

    // Add a study (enforce tuple uniqueness)
    @PostMapping("/add")
    public ResponseEntity<String> addStudy(@RequestBody Study study) {
        boolean added = csgService.addStudy(study);
        if (added) {
            return ResponseEntity.ok("Study added");
        } else {
            return ResponseEntity.badRequest().body("Duplicate study tuple");
        }
    }

    // Delete study by index
    @DeleteMapping("/{index}")
    public void deleteStudy(@PathVariable int index) {
        csgService.removeStudy(index);
    }

    // Clear all studies
    @DeleteMapping("/clear")
    public void clearCSG() {
        csgService.clear();
    }
}
