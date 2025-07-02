import React, { useState, useCallback } from 'react';
import PropTypes from 'prop-types';
import {
  Dialog, DialogTitle, DialogContent, DialogActions, Button, Grid, TextField, Select, MenuItem,
  InputLabel, FormControl, Typography, Box, Tooltip
} from '@mui/material';
import { orderDates } from './DateUtils';

const PLANETS = [
  'Sun', 'Moon', 'Mercury', 'Venus', 'Mars', 'Jupiter', 'Saturn', 'Uranus', 'Neptune', 'Pluto', 'Rahu', 'Ketu'
];

const COLORS = [
  'Red', 'Green', 'Blue', 'Yellow', 'Orange', 'Purple', 'Black', 'White', 'Gray', 'Brown', 'Pink', 'Cyan'
];

const PlanetFollowStudyDialog = ({ open, onClose, onRun, barData }) => {

  const [planet1, setPlanet1] = useState(PLANETS[0]);
  const [planet2, setPlanet2] = useState(PLANETS[1]);
  const [distance, setDistance] = useState(120);
  const DISPLAY_TYPES = [
  { value: 'vLine', label: 'Vertical Line' },
  { value: 'circle', label: 'Circle' },
  { value: 'square', label: 'Square' },
  { value: 'star', label: 'Star' },
  { value: 'downArrow', label: 'Down Arrow' }
];
const [displayType, setDisplayType] = useState(DISPLAY_TYPES[0].value);
  const [color, setColor] = useState(COLORS[0]);

  const arePlanetsSame = planet1 === planet2;


const handleRun = useCallback(async () => {
    try {
      // Determine fromDate and toDate (ensure correct order)
      const datesArr = barData?.dates || [];
      let fromDate = '', toDate = '';
      if (datesArr.length > 1) {
        [fromDate, toDate] = orderDates(datesArr[0], datesArr[datesArr.length - 1]);
      } else {
        fromDate = toDate = datesArr[0] || '';
      }

      // Removed unused 'study' variable to fix ESLint no-unused-vars error


      const params = new URLSearchParams({
        planet1: planet1,
        planet2: planet2,
        angle: distance, // Use 'distance' as the value for 'angle' param
        fromDate,
        toDate
      });
      const response = await fetch(`http://localhost:8080/api/followstudy/dates?${params.toString()}`);
      
      if (!response.ok) {
        let errorMsg = `HTTP error ${response.status}`;
        try {
            const errorData = await response.json();
            errorMsg = errorData.message || errorData.error || JSON.stringify(errorData);
        } catch (e) {
            // Ignore if response is not JSON
        }
        console.error('Failed to fetch follow study dates:', errorMsg);
        alert(`Error fetching dates: ${errorMsg}. Please ensure the backend is running and check console for details.`);
        return; 
      }
      
      const dates = await response.json();
      
      if (onRun) {
        onRun({ dates, displayType, color, planet1, planet2, distance }); // Pass all relevant data
      }
      onClose(); 
    } catch (error) {
      console.error('Error in handleRun:', error);
      alert(`An unexpected error occurred: ${error.message}. Check console for details.`);
    }
  }, [planet1, planet2, distance, displayType, color, onRun, onClose, barData]);

  const handleDistanceChange = (event) => {
    let value = parseInt(event.target.value, 10);
    if (isNaN(value)) {
      value = 0;
    } else if (value < 0) {
      value = 0;
    } else if (value > 180) {
      value = 180;
    }
    setDistance(value);
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="md">
      <DialogTitle sx={{ textAlign: 'center', fontWeight: 'bold' }}>Planet-A Follows Planet-B Study</DialogTitle>
      <DialogContent sx={{ pt: 2, pb: 3, px: 3 }}>
        <Box component="form" noValidate autoComplete="off">
          <Grid container direction="column" spacing={3}> {/* Main container for rows */}
            {/* Row 1: Planet1 'Follows' Planet2 'By Degrees' Longitude */}
            <Grid item xs={12}>
              <Grid container spacing={1} alignItems="center"> 
                <Grid item xs={12} sm>
                  <FormControl fullWidth margin="dense">
                    <InputLabel id="planet1-select-label">Planet 1</InputLabel>
                    <Select
                      labelId="planet1-select-label"
                      value={planet1}
                      label="Planet 1"
                      onChange={(e) => setPlanet1(e.target.value)}
                    >
                      {PLANETS.map((p) => (
                        <MenuItem key={p} value={p}>{p}</MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                </Grid>
                <Grid item>
                  <Typography variant="subtitle1" sx={{ px: 0.5 }}>Follows</Typography>
                </Grid>
                <Grid item xs={12} sm>
                  <FormControl fullWidth margin="dense">
                    <InputLabel id="planet2-select-label">Planet 2</InputLabel>
                    <Select
                      labelId="planet2-select-label"
                      value={planet2}
                      label="Planet 2"
                      onChange={(e) => setPlanet2(e.target.value)}
                    >
                      {PLANETS.map((p) => (
                        <MenuItem key={p} value={p}>{p}</MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                </Grid>
                <Grid item>
                  <Typography variant="subtitle1" sx={{ px: 0.5, whiteSpace: 'nowrap' }}>By Degrees</Typography>
                </Grid>
                <Grid item xs={12} sm="auto">
                  <TextField
                    margin="dense"
                    label="Distance"
                    type="number"
                    value={distance}
                    onChange={handleDistanceChange} // Use the existing robust handler
                    inputProps={{
                      min: 0,
                      max: 180,
                      step: "1"
                    }}
                    sx={{ maxWidth: '100px' }} 
                  />
                </Grid>
              </Grid>
            </Grid>

            {/* Row 2: Display and Color */}
            <Grid item xs={12}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, width: '100%' }}>
                <Typography variant="subtitle1">Display:</Typography>
                <FormControl fullWidth size="small">
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
                </FormControl>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, ml: 'auto' }}>
                  <Typography variant="subtitle1" sx={{ whiteSpace: 'nowrap' }}>Select color:</Typography>
                  <FormControl size="small" sx={{ minWidth: 120 }}>
                    <Select value={color} onChange={(e) => setColor(e.target.value)}>
                      {COLORS.map((c) => (
                        <MenuItem key={c} value={c}>{c}</MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                </Box>
              </Box>
            </Grid>



          </Grid> {/* End of main Grid container for rows */}
        </Box>
      </DialogContent>
      <DialogActions sx={{ p: '16px 24px', justifyContent: 'space-between' }}>
        <Button onClick={onClose}>Close</Button>
        <Box sx={{ display: 'flex', gap: 1 }}>
            <Tooltip title="Current Study Group (CSG) is a mechanism to group together multiple studies in one place and be able to save those for future use as well">
              <span>
                <Button variant="contained" sx={{ backgroundColor: '#8e44ad', '&:hover': { backgroundColor: '#732d91' } }} disabled={arePlanetsSame}>Add to CSG</Button>
              </span>
            </Tooltip>
            <Button onClick={handleRun} variant="contained" color="primary" disabled={arePlanetsSame}>Run</Button>
        </Box>
      </DialogActions>
    </Dialog>
  );
};

PlanetFollowStudyDialog.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  onRun: PropTypes.func.isRequired,
  barData: PropTypes.shape({
    dates: PropTypes.arrayOf(PropTypes.string),
    ohlc: PropTypes.array,
    volumes: PropTypes.array
  })
};

export default PlanetFollowStudyDialog;
