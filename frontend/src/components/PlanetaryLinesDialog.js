import React, { useState } from 'react';
import PropTypes from 'prop-types';
import {
  Dialog, DialogTitle, DialogContent, DialogActions, Button, Select, MenuItem,
  FormControl, Box, Typography, Paper, TextField
} from '@mui/material';

const PLANETS = [
  'Sun', 'Moon', 'Mercury', 'Venus', 'Mars', 'Jupiter', 'Saturn', 'Uranus', 'Neptune', 'Pluto', 'Rahu', 'Ketu'
];

const COLORS = [
  'Red', 'Green', 'Blue', 'Yellow', 'Orange', 'Purple', 'Black', 'White', 'Gray', 'Brown', 'Pink', 'Cyan'
];

const StudyRow = ({ children }) => (
  <Paper variant="outlined" sx={{ p: 2, display: 'flex', alignItems: 'center', gap: 2, mb: 2 }}>
    {children}
  </Paper>
);

StudyRow.propTypes = {
    children: PropTypes.node.isRequired,
};


import { useChartOverlays } from '../context/ChartOverlayContext';

const PlanetaryLinesDialog = ({ open, onClose, onRun, barData }) => {
  const { addOverlay } = useChartOverlays();
  const [color, setColor] = useState(COLORS[0]);
  const [planet1, setPlanet1] = useState(PLANETS[0]);
  const [factor, setFactor] = useState('360');
  const [planet2, setPlanet2] = useState(PLANETS[0]);
  const [degrees2, setDegrees2] = useState('360');
  const [type, setType] = useState('solid');
  const [thickness, setThickness] = useState('light');

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="md">
      <DialogTitle sx={{ textAlign: 'center', fontWeight: 'bold' }}>Planetary Lines Study</DialogTitle>
      <DialogContent sx={{ pt: 2 }}>
        {/* Row 1: Planet, Draw checkbox, Factor, RUN/ADD TO CSG */}
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 3 }}>
          <Typography>Planet</Typography>
          <FormControl size="small" sx={{ minWidth: 120 }}>
            <Select value={planet1} onChange={(e) => setPlanet1(e.target.value)}>
              {PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}
            </Select>
          </FormControl>
          <Typography>Treat 360 degrees =</Typography>
          <TextField
            size="small"
            value={factor}
            onChange={(e) => setFactor(e.target.value)}
            sx={{ width: 80 }}
          />
          <Typography>Multiplying Factor</Typography>
          <Box sx={{ ml: 'auto', display: 'flex', gap: 1 }}>
            <Button
              variant="contained"
              sx={{ backgroundColor: '#8e44ad', '&:hover': { backgroundColor: '#732d91' } }}
              onClick={() => {
                if (onRun) {
                  onRun({
                    planet: planet1,
                    color,
                    type,
                    thickness,
                    factor
                  });
                }
                onClose();
              }}
            >RUN</Button>
            <Button variant="contained" sx={{ backgroundColor: '#8e44ad', '&:hover': { backgroundColor: '#732d91' } }}>ADD TO CSG</Button>
          </Box>
        </Box>

        {/* Row 2: Planet, degrees =, value, RUN/ADD TO CSG */}
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 3 }}>
          <Typography>Planet</Typography>
          <FormControl size="small" sx={{ minWidth: 120 }}>
            <Select value={planet2} onChange={(e) => setPlanet2(e.target.value)}>
              {PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}
            </Select>
          </FormControl>
          <Typography>at degrees =</Typography>
          <TextField
            size="small"
            value={degrees2}
            onChange={(e) => setDegrees2(e.target.value)}
            sx={{ width: 80 }}
          />
          <Box sx={{ ml: 'auto', display: 'flex', gap: 1 }}>
            <Button
              variant="contained"
              sx={{ backgroundColor: '#8e44ad', '&:hover': { backgroundColor: '#732d91' } }}
              onClick={() => {
                // --- NEW HANDLER: Longitude Dates Study ---
                // Use barData.dates for correct date range
                const datesArr = barData?.dates || [];
                let oldestBarDate = datesArr[0] || '';
                let newestBarDate = datesArr[datesArr.length - 1] || '';
                // Ensure correct order
                const [startDate, endDate] = oldestBarDate <= newestBarDate
                  ? [oldestBarDate, newestBarDate]
                  : [newestBarDate, oldestBarDate];
                const payload = {
                  planet: PLANETS.indexOf(planet2),
                  planetName: planet2,
                  degrees: parseFloat(degrees2),
                  oldestBarDate: startDate,
                  newestBarDate: endDate,
                  shapeType: type,
                  color,
                  thickness
                };

                console.log('[PlanetaryLinesDialog] Sending longitude-dates POST:', payload);
                fetch('/api/planet-event/longitude-dates', {
                  method: 'POST',
                  headers: { 'Content-Type': 'application/json' },
                  body: JSON.stringify(payload)
                })
                  .then(res => {
                    if (!res.ok) throw new Error('Failed to fetch longitude dates');
                    return res.json();
                  })
                  .then(events => {
                    console.log('[PlanetaryLinesDialog] Received longitude-dates response:', events);
                    // Add overlays for each event using ChartOverlayContext
                    if (Array.isArray(events)) {
                      events.forEach(ev => {
                        // Build overlay object for chart
                        const overlay = {
                          type: ev.shapeType,
                          date: ev.date,
                          planet: ev.planet,
                          planetName: ev.planetName || planet2,
                          degrees: ev.degrees,
                          color: ev.color,
                          thickness: ev.thickness
                        };
                        // Use ChartOverlayContext
                        if (typeof addOverlay === 'function') {
                          addOverlay(overlay);
                        }
                      });
                    }
                    onClose(); // Close dialog only after success
                  })
                  .catch(err => {
                    alert('Error fetching longitude dates: ' + err.message);
                    onClose(); // Also close dialog on error
                  });
              }}
            >RUN</Button>
            <Button variant="contained" sx={{ backgroundColor: '#8e44ad', '&:hover': { backgroundColor: '#732d91' } }}>ADD TO CSG</Button>
          </Box>
        </Box>

        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 3 }}>
          <Typography>Color</Typography>
          <FormControl size="small" sx={{ minWidth: 120 }}>
            <Select value={color} onChange={(e) => setColor(e.target.value)}>
              {COLORS.map(c => <MenuItem key={c} value={c}>{c}</MenuItem>)}
            </Select>
          </FormControl>
          <Typography>Type</Typography>
          <FormControl size="small" sx={{ minWidth: 120 }}>
            <Select value={type} onChange={(e) => setType(e.target.value)}>
              <MenuItem value="solid">Solid</MenuItem>
              <MenuItem value="dashed">Dashed</MenuItem>
            </Select>
          </FormControl>
          <Typography>Thickness</Typography>
          <FormControl size="small" sx={{ minWidth: 120 }}>
            <Select value={thickness} onChange={(e) => setThickness(e.target.value)}>
              <MenuItem value="light">Light</MenuItem>
              <MenuItem value="medium">Medium</MenuItem>
              <MenuItem value="heavy">Heavy</MenuItem>
            </Select>
          </FormControl>
        </Box>

      </DialogContent>
      <DialogActions sx={{ p: '16px 24px' }}>
        <Button onClick={onClose} variant="contained" color="inherit">CLOSE</Button>
      </DialogActions>
    </Dialog>
  );
};

PlanetaryLinesDialog.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  onRun: PropTypes.func.isRequired,
};

export default PlanetaryLinesDialog;
