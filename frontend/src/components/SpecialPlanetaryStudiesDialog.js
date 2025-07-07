import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { useChartOverlays } from '../context/ChartOverlayContext';
import { drawStudyMarkers } from '../utils/chartUtils';
import {
  Dialog, DialogTitle, DialogContent, DialogActions, Button, Select, MenuItem,
  Radio, RadioGroup, FormControlLabel, Typography, Paper, Box, Grid
} from '@mui/material';

const RETROGRADE_PLANETS = [
  'Mercury', 'Venus', 'Mars', 'Jupiter', 'Saturn', 'Uranus', 'Neptune', 'Pluto'
];
const PLANETS = [
  'Sun', 'Moon', 'Mercury', 'Venus', 'Mars', 'Jupiter', 'Saturn', 'Uranus', 'Neptune', 'Pluto', 'Rahu', 'Ketu'
];
const NAKSHATRA_PLANETS = PLANETS.filter(p => p !== 'Moon');
const COLORS = [
  'Red', 'Green', 'Blue', 'Orange', 'Purple', 'Black', 'Gray', 'Brown', 'Pink', 'Cyan', 'Yellow', 'White'
];
const RASHIS = ['Mesh/Aries', 'Vrishabh/Taurus', 'Mithun/Gemini', 'Kark/Cancer', 'Simha/Leo', 'Kanya/Virgo', 'Tula/Libra', 'Vrischik/Scorpio', 'Dhanu/Sagittarius', 'Makar/Capricorn', 'Kumbh/Aquarius', 'Meen/Pisces'];

const SpecialPlanetaryStudiesDialog = ({ open, onClose }) => {
  const [highlightType, setHighlightType] = useState('vertical');

  const [zodiac, setZodiac] = useState('sidereal');
  const [planet1, setPlanet1] = useState('Mars');
  const [fromDate] = useState('2024-01-01');
  const [toDate] = useState('2025-12-31');

  // State for the "Midpoint Between" study
  const [midpointPlanet, setMidpointPlanet] = useState(PLANETS[0]);
  const [betweenPlanet1, setBetweenPlanet1] = useState(PLANETS[1]);
  const [betweenPlanet2, setBetweenPlanet2] = useState(PLANETS[2]);

  // State for colors
  const [stationaryColor, setStationaryColor] = useState(COLORS[0]);
  const [retrogradeColor, setRetrogradeColor] = useState(COLORS[1]);
  const [midpointColor, setMidpointColor] = useState(COLORS[2]);
  const [declinationColor, setDeclinationColor] = useState(COLORS[3]);
  const [rashiColor, setRashiColor] = useState(COLORS[4]);
  const [nakshatraColor, setNakshatraColor] = useState(COLORS[5]);
  const [charanColor, setCharanColor] = useState(COLORS[6]);

  const { addOverlay } = useChartOverlays();

  // Validation for the "Midpoint Between" study
  const areMidpointPlanetsDistinct = new Set([midpointPlanet, betweenPlanet1, betweenPlanet2]).size === 3;

  const formatDateForApi = (dateStr) => {
    const date = new Date(dateStr);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  };

  // Handler for the first RUN button (planet retro dates)
  const handleRunRetroDates = async (studyColor) => {
    try {
      const params = new URLSearchParams({
        planet: planet1,
        studyType: 'Retrograde',
        fromDate: formatDateForApi(fromDate),
        toDate: formatDateForApi(toDate),
        shapeType: highlightType,
        color: studyColor,
        thickness: 'Medium',
      });
      const response = await fetch(`/api/planet-event/retro-dates?${params.toString()}`);
      if (!response.ok) throw new Error('Failed to fetch retro dates');
      const responseData = await response.json();
      drawStudyMarkers(responseData.dates, {
        shapeType: highlightType,
        color: studyColor,
        thickness: 'Medium',
        planet: planet1,
        studyType: 'Retrograde',
      }, addOverlay);
    } catch (err) {
      alert('Error: ' + err.message);
    }
  };

  // Handler for the "Planet is Stationary" RUN button
  const handleRunStationaryDates = async (studyColor) => {
    try {
      const params = new URLSearchParams({
        planet: planet1,
        studyType: 'Stationary',
        fromDate: formatDateForApi(fromDate),
        toDate: formatDateForApi(toDate),
        shapeType: highlightType,
        color: studyColor,
        thickness: 'Medium',
      });
      const response = await fetch(`/api/planet-event/direct-dates?${params.toString()}`);
      console.log('[Dialog] Stationary dates response status:', response.status);
      if (!response.ok) throw new Error('Failed to fetch stationary dates');
      const responseData = await response.json();
      drawStudyMarkers(responseData.dates, {
        shapeType: highlightType,
        color: studyColor,
        thickness: 'Medium',
        planet: planet1,
        studyType: 'Stationary',
      }, addOverlay);
    } catch (err) {
      alert('Error: ' + err.message);
    }
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="md" fullWidth>
      <DialogTitle sx={{ textAlign: 'center', fontWeight: 'bold' }}>
        Special Planetary Studies
      </DialogTitle>
      <DialogContent>
        {/* Highlight and color controls */}
        <Paper variant="outlined" sx={{ p: 2, mb: 2 }}>
          <Grid container alignItems="center" spacing={2}>
            <Grid>
              <FormControlLabel control={<Radio checked={highlightType === 'vertical'} onChange={() => setHighlightType('vertical')} size="small" />} label={<Typography sx={{ fontSize: 15 }}>Vertical Line</Typography>} />
            </Grid>
            <Grid>
              <FormControlLabel control={<Radio checked={highlightType === 'circle'} onChange={() => setHighlightType('circle')} size="small" />} label={<Typography sx={{ fontSize: 15 }}>Circle</Typography>} />
            </Grid>
            <Grid>
              <FormControlLabel control={<Radio checked={highlightType === 'square'} onChange={() => setHighlightType('square')} size="small" />} label={<Typography sx={{ fontSize: 15 }}>Square</Typography>} />
            </Grid>
            <Grid>
              <FormControlLabel control={<Radio checked={highlightType === 'largeCircle'} onChange={() => setHighlightType('largeCircle')} size="small" />} label={<Typography sx={{ fontSize: 15 }}>Large Circle</Typography>} />
            </Grid>

          </Grid>
        </Paper>

        {/* Zodiac selection */}
        <Paper variant="outlined" sx={{ p: 2, mb: 2 }}>
          <Grid container alignItems="center" spacing={2}>
            <Grid>
              <Typography>Zodiac</Typography>
            </Grid>
            <Grid>
              <RadioGroup row value={zodiac} onChange={e => setZodiac(e.target.value)}>
                <FormControlLabel value="sidereal" control={<Radio size="small" />} label={<Typography sx={{ fontSize: 15 }}>Sidereal</Typography>} />
                <FormControlLabel value="tropical" control={<Radio size="small" />} label={<Typography sx={{ fontSize: 15 }}>Tropical</Typography>} />
              </RadioGroup>
            </Grid>
          </Grid>
        </Paper>

        {/* Main study rows */}
        <Paper variant="outlined" sx={{ p: 2 }}>
          {/* Example row: Planet in Rashi */}
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 2 }}>
            <Typography>Planet</Typography>
            <Select value={planet1} onChange={e => setPlanet1(e.target.value)} size="small" sx={{minWidth: 90}}>{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
            <Typography>in Sign</Typography>
            <Select size="small" value={'Mesh/Aries'} sx={{minWidth: 90}}>{RASHIS.map(r => <MenuItem key={r} value={r}>{r}</MenuItem>)}</Select>
            <Select value={rashiColor} onChange={e => setRashiColor(e.target.value)} size="small" sx={{minWidth: 90}}>{COLORS.map(c => <MenuItem key={c} value={c}>{c}</MenuItem>)}</Select>
            <Box sx={{ ml: 'auto', display: 'flex', gap: 1 }}><Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>RUN</Button><Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>ADD TO CSG</Button></Box>
          </Box>

          {/* Row 2: Planet in Nakshatra */}
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 2 }}>
            <Typography>Planet</Typography>
            <Select value={planet1} onChange={e => setPlanet1(e.target.value)} size="small" sx={{minWidth: 90}}>{NAKSHATRA_PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
            <Typography>in Nakshatra</Typography>
            <Select size="small" value={'Ashwini'} sx={{minWidth: 90}}><MenuItem value={'Ashwini'}>Ashwini</MenuItem></Select>
            <Select value={nakshatraColor} onChange={e => setNakshatraColor(e.target.value)} size="small" sx={{minWidth: 90}}>{COLORS.map(c => <MenuItem key={c} value={c}>{c}</MenuItem>)}</Select>
            <Box sx={{ ml: 'auto', display: 'flex', gap: 1 }}><Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>RUN</Button><Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>ADD TO CSG</Button></Box>
          </Box>

          {/* Row 3: Planet in Nakshatra in Charan */}
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 2 }}>
            <Typography>Planet</Typography>
            <Select value={planet1} onChange={e => setPlanet1(e.target.value)} size="small" sx={{minWidth: 90}}>{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
            <Typography>in Nakshatra</Typography>
            <Select size="small" value={'Ashwini'} sx={{minWidth: 90}}><MenuItem value={'Ashwini'}>Ashwini</MenuItem></Select>
            <Typography>in Charan</Typography>
            <Select size="small" value={'1'} sx={{minWidth: 60}}><MenuItem value={'1'}>1</MenuItem><MenuItem value={'2'}>2</MenuItem><MenuItem value={'3'}>3</MenuItem><MenuItem value={'4'}>4</MenuItem></Select>
            <Select value={charanColor} onChange={e => setCharanColor(e.target.value)} size="small" sx={{minWidth: 90}}>{COLORS.map(c => <MenuItem key={c} value={c}>{c}</MenuItem>)}</Select>
            <Box sx={{ ml: 'auto', display: 'flex', gap: 1 }}><Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>RUN</Button><Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>ADD TO CSG</Button></Box>
          </Box>

          {/* Row 4: Planet Midpoint Between Planet And Planet */}
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 2 }}>
            <Typography>Planet</Typography>
            <Select value={midpointPlanet} onChange={e => setMidpointPlanet(e.target.value)} size="small" sx={{minWidth: 90}}>{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
            <Typography>Midpoint Between</Typography>
            <Select value={betweenPlanet1} onChange={e => setBetweenPlanet1(e.target.value)} size="small" sx={{minWidth: 90}}>{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
            <Typography>And</Typography>
            <Select value={betweenPlanet2} onChange={e => setBetweenPlanet2(e.target.value)} size="small" sx={{minWidth: 90}}>{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
            <Select value={midpointColor} onChange={e => setMidpointColor(e.target.value)} size="small" sx={{minWidth: 90}}>{COLORS.map(c => <MenuItem key={c} value={c}>{c}</MenuItem>)}</Select>
            <Box sx={{ ml: 'auto', display: 'flex', gap: 1 }}><Button variant="contained" sx={{ backgroundColor: '#8e44ad' }} disabled={!areMidpointPlanetsDistinct}>RUN</Button><Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>ADD TO CSG</Button></Box>
          </Box>

          {/* Row: Planet is Stationary */}
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 2 }}>
            <Typography>Planet</Typography>
            <Select value={planet1} onChange={e => setPlanet1(e.target.value)} size="small" sx={{minWidth: 90}}>{RETROGRADE_PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
            <Typography>is Stationary</Typography>
            <Select value={stationaryColor} onChange={e => setStationaryColor(e.target.value)} size="small" sx={{minWidth: 90}}>{COLORS.map(c => <MenuItem key={c} value={c}>{c}</MenuItem>)}</Select>
            <Box sx={{ ml: 'auto', display: 'flex', gap: 1 }}><Button onClick={() => handleRunStationaryDates(stationaryColor)} variant="contained" sx={{ backgroundColor: '#8e44ad' }}>RUN</Button><Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>ADD TO CSG</Button></Box>
          </Box>

          {/* Row: Planet is Retrograde */}
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 2 }}>
            <Typography>Planet</Typography>
            <Select value={planet1} onChange={e => setPlanet1(e.target.value)} size="small" sx={{minWidth: 90}}>{RETROGRADE_PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
            <Typography>is Retrograde</Typography>
            <Select value={retrogradeColor} onChange={e => setRetrogradeColor(e.target.value)} size="small" sx={{minWidth: 90}}>{COLORS.map(c => <MenuItem key={c} value={c}>{c}</MenuItem>)}</Select>
            <Box sx={{ ml: 'auto', display: 'flex', gap: 1 }}><Button onClick={() => handleRunRetroDates(retrogradeColor)} variant="contained" sx={{ backgroundColor: '#8e44ad' }}>RUN</Button><Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>ADD TO CSG</Button></Box>
          </Box>

          {/* Row: Planet Close To Declination */}
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 2 }}>
            <Typography>Planet</Typography>
            <Select value={planet1} onChange={e => setPlanet1(e.target.value)} size="small" sx={{minWidth: 90}}>{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
            <Typography>Close To Declination</Typography>
            <Select size="small" value={'Max North'} sx={{minWidth: 90}}><MenuItem value={'Max North'}>Max North</MenuItem><MenuItem value={'Zero'}>Zero</MenuItem><MenuItem value={'Max South'}>Max South</MenuItem></Select>
            <Select value={declinationColor} onChange={e => setDeclinationColor(e.target.value)} size="small" sx={{minWidth: 90}}>{COLORS.map(c => <MenuItem key={c} value={c}>{c}</MenuItem>)}</Select>
            <Box sx={{ ml: 'auto', display: 'flex', gap: 1 }}><Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>RUN</Button><Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>ADD TO CSG</Button></Box>
          </Box>
        </Paper>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} variant="contained" color="inherit">CLOSE</Button>
      </DialogActions>
    </Dialog>
  );
};

SpecialPlanetaryStudiesDialog.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
};

export default SpecialPlanetaryStudiesDialog;