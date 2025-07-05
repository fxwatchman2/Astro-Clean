import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import { Dialog, DialogTitle, DialogContent, DialogActions, Button, TextField, MenuItem, Box, Typography, Radio, RadioGroup, FormControlLabel, FormControl, Select } from '@mui/material';


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
import { compareYMD } from './DateUtils';



export default function LongitudeStudyDlg({ open, onClose, onRun, barData, onAddToCSG }) {
  // Common state
  const [color, setColor] = useState(COLORS[0]);
  const [displayType, setDisplayType] = useState(DISPLAY_TYPES[0].value);
  const [frameOfReference, setFrameOfReference] = useState('Geocentric');
  const [planet1, setPlanet1] = useState('Sun');
  const [planet2, setPlanet2] = useState('Moon');
  const [distance, setDistance] = useState('120');

  const [showLimitModal, setShowLimitModal] = useState(false);
  const [limitMessage, setLimitMessage] = useState("");



  // --- Helper for disabling logic ---
  const isRunDisabled = (() => {
    const d = parseInt(distance, 10);
    if (((planet1 === "Sun" && planet2 === "Mercury") || (planet1 === "Mercury" && planet2 === "Sun")) && d > 28) {
      return true;
    }
    if (((planet1 === "Sun" && planet2 === "Venus") || (planet1 === "Venus" && planet2 === "Sun")) && d > 48) {
      return true;
    }
    return false;
  })();

  useEffect(() => {
    if (isRunDisabled) {
      if ((planet1 === "Sun" && planet2 === "Mercury") || (planet1 === "Mercury" && planet2 === "Sun")) {
        setLimitMessage("For Sun and Mercury, the maximum allowed distance is 28 degrees.");
        setShowLimitModal(true);
      } else if ((planet1 === "Sun" && planet2 === "Venus") || (planet1 === "Venus" && planet2 === "Sun")) {
        setLimitMessage("For Sun and Venus, the maximum allowed distance is 48 degrees.");
        setShowLimitModal(true);
      }
    } else {
      setLimitMessage("");
      setShowLimitModal(false);
    }
  }, [isRunDisabled, planet1, planet2, distance]);

  const planetsAreIdentical = planet1 === planet2;

  const buildStudyObject = () => {
    if (planetsAreIdentical) {
      alert("Planets cannot be identical for this study type.");
      return null;
    }

    return {
      type: 'longitudinalDistance',
      planet1,
      planet2,
      angle: parseInt(distance, 10), // Use 'angle' for consistency
      displayType,
      color,
      frameOfReference: frameOfReference || '', // Always include as string
    };
  };

  useEffect(() => {
    if (frameOfReference === 'Heliocentric') {
      if (planet1 === 'Sun') setPlanet1(planetsWithoutSun[0] || '');
      if (planet2 === 'Sun') setPlanet2(planetsWithoutSun[0] || '');
    }
  }, [frameOfReference, planet1, planet2]);




  const handleRun = () => {
    const study = buildStudyObject();
    // Ensure correct date ordering: fromDate = oldest, toDate = newest (robust fix)
    if (study && barData && Array.isArray(barData.dates) && barData.dates.length > 0) {
      let minDate = barData.dates[0];
      let maxDate = barData.dates[0];
      for (const date of barData.dates) {
        if (typeof date === 'string') {
          if (compareYMD(date, minDate) < 0) minDate = date;
          if (compareYMD(date, maxDate) > 0) maxDate = date;
        }
      }
      // Final check: ensure minDate is not after maxDate
      if (compareYMD(minDate, maxDate) > 0) {
        [minDate, maxDate] = [maxDate, minDate];
      }
      study.fromDate = minDate;
      study.toDate = maxDate;
    }
    if (study) {
      // Remove tuple if present
      if ('tuple' in study) delete study.tuple;
      console.log('[LongitudeStudyDlg] Outgoing single study payload:', study);
      if (typeof onRun === 'function') onRun(study);

      onClose();
    }
  };

  const handleDistanceChange = (event) => {
    let value = event.target.value;
    if (value === '') {
      setDistance('');
      return;
    }
    let numValue = parseInt(value, 10);
    if (isNaN(numValue)) numValue = 0;
    if (numValue > 180) numValue = 180;
    if (numValue < 0) numValue = 0;
    setDistance(numValue.toString());
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
      <FormControl fullWidth>
        <Typography sx={{ fontWeight: 500, fontSize: '0.9rem', mb: 0.5 }}>Distance (in degrees)</Typography>
        <TextField size="small" value={distance} onChange={handleDistanceChange} />
      </FormControl>
    </>
  );

  return (
    <>
      <Dialog open={open} onClose={onClose} PaperProps={{ style: { width: '560px', borderRadius: '12px' } }}>
        <DialogTitle sx={{ textAlign: 'center', fontWeight: 'bold', fontSize: '1.5rem', borderBottom: '1px solid #eee', pb: 1.5 }}>
          Longitude Study
        </DialogTitle>
        <DialogContent sx={{ p: 3 }}>
          <Box display="flex" flexDirection="column" gap={2.5} width="100%">
            {renderLongitudinalDistanceFields()}

          {/* Common Fields */}
          <Box display="flex" flexDirection="row" justifyContent="space-between" alignItems="center" width="100%" gap={2}>
            <Box display="flex" flexDirection="column" width="50%">
              <Typography sx={{ fontWeight: 500, fontSize: '0.9rem', mb: 0.5 }}>Display type</Typography>
              <Select value={displayType || DISPLAY_TYPES[0].value} onChange={(e) => setDisplayType(e.target.value)} size="small">
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
      <DialogActions sx={{ p: '16px 24px', borderTop: '1px solid #eee', justifyContent: 'space-between' }}>
  <Button onClick={onClose} color="secondary" variant="outlined">CLOSE</Button>
        <Box>
          <Button
            onClick={() => {
              if (isRunDisabled) setShowLimitModal(true);
              else {
                const study = buildStudyObject();
                if (study) {
                  // Remove tuple if present
                  if ('tuple' in study) delete study.tuple;
                  console.log('[LongitudeStudyDlg] Outgoing CSG study payload:', study);
                  if (typeof onAddToCSG === 'function') {
                    onAddToCSG(study);
                  }
                }
              }
            }}
            variant="contained"
            sx={{
              mr: 1,
              backgroundColor: '#bbdefb',
              color: '#1e88e5',
              '&:hover': {
                backgroundColor: '#90caf9',
              },
            }}
            disabled={isRunDisabled || planetsAreIdentical}
          >
            Add To CSG
          </Button>
          <Button
            onClick={() => {
              if (isRunDisabled) setShowLimitModal(true);
              else handleRun();
            }}
            variant="contained"
            disabled={isRunDisabled || planetsAreIdentical}
          >
            Run
          </Button>
        </Box>
        {/* Modal for limit violations */}
        <Dialog open={showLimitModal} onClose={() => setShowLimitModal(false)}>
          <DialogTitle>Invalid Distance for Selected Pair</DialogTitle>
          <DialogContent>
            <Typography>{limitMessage}</Typography>
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setShowLimitModal(false)} color="primary">OK</Button>
          </DialogActions>
        </Dialog>
      </DialogActions>
    </Dialog>

    </>
  );
}

LongitudeStudyDlg.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  onRun: PropTypes.func.isRequired,
  barData: PropTypes.shape({
    dates: PropTypes.arrayOf(PropTypes.string)
  }),
  onAddToCSG: PropTypes.func, // Optional, but needed for CSG integration
};
