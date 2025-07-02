// useCurrentStudyGroup.js
// React hook for managing the Current Study Group (CSG) in memory
// Uses StudyGroup and Study JSDoc types from StudyGroupTypes.js

import { useState } from 'react';

/**
 * @returns {[csg: StudyGroup, setCSG: Function, addStudy: Function, removeStudy: Function, resetCSG: Function]}
 */
export default function useCurrentStudyGroup(initialCSG) {
  const [csg, setCSG] = useState(initialCSG || {
    name: 'Current Study Group',
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString(),
    studies: []
  });

  // Add a study to the CSG
  function addStudy(study) {
    setCSG(prev => ({
      ...prev,
      updatedAt: new Date().toISOString(),
      studies: [...prev.studies, study]
    }));
  }

  // Remove a study by index
  function removeStudy(index) {
    setCSG(prev => ({
      ...prev,
      updatedAt: new Date().toISOString(),
      studies: prev.studies.filter((_, i) => i !== index)
    }));
  }

  // Reset CSG to a new group (e.g. when loading an SSG)
  function resetCSG(newCSG) {
    setCSG({
      ...newCSG,
      updatedAt: new Date().toISOString()
    });
  }

  return [csg, setCSG, addStudy, removeStudy, resetCSG];
}
