import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import { Dialog, DialogTitle, DialogContent, DialogActions, Button, TextField, MenuItem, Box, Typography, Radio, RadioGroup, FormControlLabel, FormControl, Select } from '@mui/material';

const planets = ['Sun', 'Moon', 'Mercury', 'Venus', 'Mars', 'Jupiter', 'Saturn', 'Uranus', 'Neptune', 'Pluto', 'Rahu', 'Ketu'];
const planetsWithoutSun = planets.filter(p => p !== 'Sun');
const SIGNS = ['Aries', 'Taurus', 'Gemini', 'Cancer', 'Leo', 'Virgo', 'Libra', 'Scorpio', 'Sagittarius', 'Capricorn', 'Aquarius', 'Pisces'];
const DISPLAY_TYPES = [
  { value: 'vLine', label: 'Vertical Line' },
  { value: 'circle', label: 'Circle' },
  { value: 'square', label: 'Square' },
  { value: 'star', label: 'Star' },
  { value: 'downArrow', label: 'Down Arrow' }
];
const COLORS = ['Red', 'Green', 'Blue', 'Yellow', 'Orange', 'Purple', 'Black', 'White', 'Gray', 'Brown', 'Pink', 'Cyan'];
const STUDY_TYPES = {
  LONGITUDINAL_DISTANCE: 'Longitudinal Distance',
  PLANETARY_INGRESS: 'Planetary Ingress',
};

export default function StudyDialog({ open, onClose, onRun }) {
  // --- Custom state for disabling logic and modal ---
  const [showLimitModal, setShowLimitModal] = useState(false);
  const [limitMessage, setLimitMessage] = useState("");

  // --- Helper for disabling logic ---
  let isRunDisabled = false;

  // Common state
  const [studyType, setStudyType] = useState(STUDY_TYPES.LONGITUDINAL_DISTANCE);
  const [color, setColor] = useState(COLORS[0]);
  const [displayType, setDisplayType] = useState(DISPLAY_TYPES[0]);
  const [frameOfReference, setFrameOfReference] = useState('Geocentric');

  // State for Longitudinal Distance
  const [planet1, setPlanet1] = useState('Sun');
  const [planet2, setPlanet2] = useState('Moon');
  const [angle, setAngle] = useState('120');

  // State for Planetary Ingress
  const [ingressPlanet, setIngressPlanet] = useState('Mars');
  const [ingressSign, setIngressSign] = useState(SIGNS[0]);

  const planetsAreIdentical = studyType === STUDY_TYPES.LONGITUDINAL_DISTANCE && planet1 === planet2;

  const buildStudyObject = () => {
    if (planetsAreIdentical) {
      alert("Planets cannot be identical for this study type.");
      return null;
    }

    let study;
    if (studyType === STUDY_TYPES.LONGITUDINAL_DISTANCE) {
      study = {
        type: 'longitudinalDistance',
        planet1,
        planet2,
        angle: parseInt(angle, 10), 
        displayType,
        color,
        frameOfReference,
      };
    } else if (studyType === STUDY_TYPES.PLANETARY_INGRESS) {
      study = {
        type: 'planetaryIngress',
        planet: ingressPlanet,
        sign: ingressSign,
        displayType,
        color,
        frameOfReference,
      };
    }
    return study;
  };

  useEffect(() => {
    if (frameOfReference === 'Heliocentric') {
      if (planet1 === 'Sun') setPlanet1(planetsWithoutSun[0] || '');
      if (planet2 === 'Sun') setPlanet2(planetsWithoutSun[0] || '');
      if (ingressPlanet === 'Sun') setIngressPlanet(planetsWithoutSun[0] || '');
    }
  }, [frameOfReference, planet1, planet2, ingressPlanet]);

  const handleRun = () => {
    const study = buildStudyObject();
    if (study) {
      if (typeof onRun === 'function') onRun(study);
      onClose(); // Close dialog after running
    }
  };

  const handleAngleChange = (event) => {
    let value = event.target.value;
    if (value === '') {
      setAngle('');
      return;
    }
    let numValue = parseInt(value, 10);
    if (isNaN(numValue)) numValue = 0;
    if (numValue > 180) numValue = 180;
    if (numValue < 0) numValue = 0;
    setAngle(numValue.toString());
  };

  const currentPlanetList = frameOfReference === 'Heliocentric' ? planetsWithoutSun : planets;

  const renderLongitudinalDistanceFields = () => (
    <>
      <Box display="flex" flexDirection="row" justifyContent="space-between" width="100%" gap={2}>
        <FormControl fullWidth size="small">
          <Typography sx={{ fontWeight: 500, fontSize: '0.9rem', mb: 0.5 }}>Planet 1</Typography>
          <Select value={planet1} onChange={(e) => setPlanet1(e.target.value)} error={planetsAreIdentical}>
            {currentPlanetList.map((p) => <MenuItem key={p} value={p}>{p}</MenuItem>)}
          </Select>
        </FormControl>
        <FormControl fullWidth size="small">
          <Typography sx={{ fontWeight: 500, fontSize: '0.9rem', mb: 0.5 }}>Planet 2</Typography>
          <Select value={planet2} onChange={(e) => setPlanet2(e.target.value)} error={planetsAreIdentical}>
            {currentPlanetList.map((p) => <MenuItem key={p} value={p}>{p}</MenuItem>)}
          </Select>
        </FormControl>
      </Box>
      <Box display="flex" flexDirection="column" width="50%">
        <Typography sx={{ fontWeight: 500, fontSize: '0.9rem', mb: 0.5 }}>Longitude angle (degrees)</Typography>
        <TextField type="text" value={angle} onChange={handleAngleChange} size="small" fullWidth />
      </Box>
    </>
  );

  const renderPlanetaryIngressFields = () => (
    <Box display="flex" flexDirection="row" justifyContent="space-between" width="100%" gap={2}>
      <FormControl fullWidth size="small">
        <Typography sx={{ fontWeight: 500, fontSize: '0.9rem', mb: 0.5 }}>Planet</Typography>
        <Select value={ingressPlanet} onChange={(e) => setIngressPlanet(e.target.value)}>
          {currentPlanetList.map((p) => <MenuItem key={p} value={p}>{p}</MenuItem>)}
        </Select>
      </FormControl>
      <FormControl fullWidth size="small">
        <Typography sx={{ fontWeight: 500, fontSize: '0.9rem', mb: 0.5 }}>Sign</Typography>
        <Select value={ingressSign} onChange={(e) => setIngressSign(e.target.value)}>
          {SIGNS.map((s) => <MenuItem key={s} value={s}>{s}</MenuItem>)}
        </Select>
      </FormControl>
    </Box>
  );

  return (
    <Dialog open={open} onClose={onClose} PaperProps={{ style: { width: '560px', borderRadius: '12px' } }}>
      <DialogTitle sx={{ textAlign: 'center', fontWeight: 'bold', fontSize: '1.5rem', borderBottom: '1px solid #eee', pb: 1.5 }}>
        Longitude Study
      </DialogTitle>
      <DialogContent sx={{ p: 3 }}>
        <Box display="flex" flexDirection="column" gap={2.5} width="100%">
          <FormControl fullWidth size="small">
            <Typography sx={{ fontWeight: 500, fontSize: '0.9rem', mb: 0.5 }}>Study Type</Typography>
            <Select value={studyType} onChange={(e) => setStudyType(e.target.value)}>
              {Object.values(STUDY_TYPES).map((type) => <MenuItem key={type} value={type}>{type}</MenuItem>)}
            </Select>
          </FormControl>

          {studyType === STUDY_TYPES.LONGITUDINAL_DISTANCE && renderLongitudinalDistanceFields()}
          {studyType === STUDY_TYPES.PLANETARY_INGRESS && renderPlanetaryIngressFields()}

          {/* Common Fields */}
          <Box display="flex" flexDirection="row" justifyContent="space-between" alignItems="center" width="100%" gap={2}>
            <Box display="flex" flexDirection="column" width="50%">
              <Typography sx={{ fontWeight: 500, fontSize: '0.9rem', mb: 0.5 }}>Display type</Typography>
              <Select
                value={displayType || DISPLAY_TYPES[0].value}
                onChange={(e) => setDisplayType(e.target.value)}
                size="small"
              >  
                {DISPLAY_TYPES.map((opt) => (
                  <MenuItem key={opt.value} value={opt.value}>{opt.label}</MenuItem>
                ))}
              </Select>
            </Box>
            <FormControl fullWidth size="small" style={{width: '50%'}}>
              <Typography sx={{ fontWeight: 500, fontSize: '0.9rem', mb: 0.5 }}>Color</Typography>
              <Select value={color} onChange={(e) => setColor(e.target.value)}>
                {COLORS.map((c) => <MenuItem key={c} value={c}>{c}</MenuItem>)}
              </Select>
            </FormControl>
          </Box>
          
          <FormControl component="fieldset">
            <Typography sx={{ fontWeight: 500, fontSize: '0.9rem', mb: 0.5 }}>Frame of Reference</Typography>
            <RadioGroup row value={frameOfReference} onChange={(e) => setFrameOfReference(e.target.value)}>
              <FormControlLabel value="Geocentric" control={<Radio />} label="Geocentric" />
              <FormControlLabel value="Heliocentric" control={<Radio />} label="Heliocentric" />
            </RadioGroup>
          </FormControl>
        </Box>
      </DialogContent>
      <DialogActions sx={{ p: '16px 24px', borderTop: '1px solid #eee', justifyContent: 'flex-end', gap: 2 }}>
        <Button onClick={onClose} sx={{ color: '#555' }}>Cancel</Button>
        <Button
          onClick={handleRun}
          variant="contained"
          sx={{ backgroundColor: '#3f51b5' }}
        >
          Run
        </Button>
        <Button
          onClick={() => {/* Add To CSG logic here if needed */}}
          sx={{ color: '#555' }}
        >
          Add To CSG
        </Button>
      </DialogActions>

    </Dialog>
  );
}

StudyDialog.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  onRun: PropTypes.func.isRequired,
};
