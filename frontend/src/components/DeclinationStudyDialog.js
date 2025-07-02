import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import { Dialog, DialogTitle, DialogContent, DialogActions, Button, MenuItem, Box, Typography, Radio, RadioGroup, FormControlLabel, FormControl, Select } from '@mui/material';

const planets = ['Sun', 'Moon', 'Mercury', 'Venus', 'Mars', 'Jupiter', 'Saturn', 'Uranus', 'Neptune', 'Pluto', 'Rahu', 'Ketu'];
const planetsWithoutSun = planets.filter(p => p !== 'Sun');
const DISPLAY_TYPES = [
  { value: 'vLine', label: 'Vertical Line' },
  { value: 'circle', label: 'Circle' },
  { value: 'square', label: 'Square' },
  { value: 'star', label: 'Star' },
  { value: 'downArrow', label: 'Down Arrow' }
];
const COLORS = ['Red', 'Green', 'Blue', 'Yellow', 'Orange', 'Purple', 'Black', 'White', 'Gray', 'Brown', 'Pink', 'Cyan'];
const DECLINATION_OPTIONS = ['Parallel', 'Contra Parallel', 'Parallel North', 'Parallel South', 'Zero Decl'];

export default function DeclinationStudyDialog({ open, onClose, onRun }) {
  const [color, setColor] = useState(COLORS[0]);
  const [displayType, setDisplayType] = useState(DISPLAY_TYPES[0]);
  const frameOfReference = 'Geocentric'; // Read-only, always Geocentric
  const [planet1, setPlanet1] = useState('Sun');
  const [planet2, setPlanet2] = useState('Moon');
  const [declination, setDeclination] = useState(DECLINATION_OPTIONS[0]);

  const planetsAreIdentical = planet1 === planet2;

  const buildStudyObject = () => {
    if (planetsAreIdentical) {
      alert("Planets cannot be identical for this study type.");
      return null;
    }

    return {
      type: 'declination',
      planet1,
      planet2,
      angle: declination, // Use 'angle' for tuple uniqueness
      displayType,
      color,
      frameOfReference: frameOfReference || '',
    };
  };

  useEffect(() => {
    if (frameOfReference === 'Heliocentric') {
      if (planet1 === 'Sun') setPlanet1(planetsWithoutSun[0] || '');
      if (planet2 === 'Sun') setPlanet2(planetsWithoutSun[0] || '');
    }
  }, [frameOfReference, planet1, planet2]);

  const currentPlanetList = frameOfReference === 'Heliocentric' ? planetsWithoutSun : planets;

  const renderDeclinationFields = () => (
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
        <FormControl fullWidth size="small">
          <Typography sx={{ fontWeight: 500, fontSize: '0.9rem', mb: 0.5 }}>Declination</Typography>
          <Select value={declination} onChange={(e) => setDeclination(e.target.value)}>
            {DECLINATION_OPTIONS.map((opt) => <MenuItem key={opt} value={opt}>{opt}</MenuItem>)}
          </Select>
        </FormControl>
      </Box>
    </>
  );

  return (
    <Dialog open={open} onClose={onClose} PaperProps={{ style: { width: '560px', borderRadius: '12px' } }}>
      <DialogTitle sx={{ textAlign: 'center', fontWeight: 'bold', fontSize: '1.5rem', borderBottom: '1px solid #eee', pb: 1.5 }}>
        Declination Study
      </DialogTitle>
      <DialogContent sx={{ p: 3 }}>
        <Box display="flex" flexDirection="column" gap={2.5} width="100%">
          {renderDeclinationFields()}
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
            <RadioGroup row value={frameOfReference} onChange={() => {}}>
              <FormControlLabel value="Geocentric" control={<Radio />} label="Geocentric" disabled />
              <FormControlLabel value="Heliocentric" control={<Radio />} label="Heliocentric" disabled />
            </RadioGroup>
          </FormControl>
        </Box>
      </DialogContent>
      <DialogActions sx={{ p: '16px 24px', borderTop: '1px solid #eee', justifyContent: 'space-between' }}>
        <Button onClick={onClose} variant="contained" sx={{ backgroundColor: 'grey.200', color: 'grey.800', '&:hover': { backgroundColor: 'grey.300' } }}>
          Cancel
        </Button>
        <Box>
          <Button onClick={() => console.log('Add to CSG not implemented')} variant="contained" sx={{ mr: 1, backgroundColor: '#bbdefb', color: '#1e88e5', '&:hover': { backgroundColor: '#90caf9' } }}>
            Add To CSG
          </Button>

          <Button onClick={() => onRun(buildStudyObject())} variant="contained" disabled={planetsAreIdentical}>
            Run
          </Button>
        </Box>
      </DialogActions>
    </Dialog>
  );
}

DeclinationStudyDialog.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  onRun: PropTypes.func.isRequired,
};
