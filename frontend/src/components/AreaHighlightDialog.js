import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { 
  Dialog, DialogTitle, DialogContent, DialogActions, Button, Select, MenuItem, 
  Typography, Box, TextField, RadioGroup, FormControlLabel, Radio
} from '@mui/material';

// --- Constants ---
const PLANETS = ['Sun', 'Moon', 'Mercury', 'Venus', 'Mars', 'Jupiter', 'Saturn', 'Uranus', 'Neptune', 'Pluto', 'Rahu', 'Ketu'];
const SIGNS = ['Aries', 'Taurus', 'Gemini', 'Cancer', 'Leo', 'Virgo', 'Libra', 'Scorpio', 'Sagittarius', 'Capricorn', 'Aquarius', 'Pisces'];
const ZODIAC_TYPES = ['Sidereal Zodiac', 'Tropical Zodiac'];
const NAKSHATRAS = [
  'Ashwini', 'Bharani', 'Krittika', 'Rohini', 'Mrigashira', 'Ardra', 'Punarvasu', 'Pushya', 'Ashlesha', 
  'Magha', 'Purva Phalguni', 'Uttara Phalguni', 'Hasta', 'Chitra', 'Swati', 'Vishakha', 'Anuradha', 
  'Jyeshtha', 'Mula', 'Purva Ashadha', 'Uttara Ashadha', 'Shravana', 'Dhanishta', 'Shatabhisha', 
  'Purva Bhadrapada', 'Uttara Bhadrapada', 'Revati'
];
const EVENTS = ['Retro', 'Direct', 'Combust', 'Heliacal Rise', 'Heliacal Set', 'Speeding Up', 'Slowing Down', 
  'Changing Sign', 'Changing Nakshatra', 'Changing Charan', 'Zero Decl', 'Peak North Decl', 'Peak South Decl'
];
const COLORS = ['#FF0000', '#00FF00', '#0000FF', '#FFFF00', '#FF00FF', '#00FFFF', '#FFA500', '#800080', '#008000'];

// --- Main Component ---
const AreaHighlightDialog = ({ open, onClose }) => {
  const [highlightType, setHighlightType] = useState('range'); // 'range' or 'events'

  // --- State for each row ---
  // Row 1
  const [r1Planet, setR1Planet] = useState(PLANETS[0]);
  const [r1FromDegree, setR1FromDegree] = useState('10');
  const [r1ToDegree, setR1ToDegree] = useState('20');
  const [r1Zodiac, setR1Zodiac] = useState(ZODIAC_TYPES[0]);

  // Row 2
  const [r2Planet, setR2Planet] = useState(PLANETS[0]);
  const [r2Sign, setR2Sign] = useState(SIGNS[0]);
  const [r2Zodiac, setR2Zodiac] = useState(ZODIAC_TYPES[0]);

  // Row 3
  const [r3Planet, setR3Planet] = useState(PLANETS[0]);
  const [r3Nakshatra, setR3Nakshatra] = useState(NAKSHATRAS[0]);

  // Row 4
  const [r4StartPlanet1, setR4StartPlanet1] = useState(PLANETS[0]);
  const [r4StartPlanet2, setR4StartPlanet2] = useState(PLANETS[1]);
  const [r4StartDegree, setR4StartDegree] = useState('90');
  const [r4EndPlanet1, setR4EndPlanet1] = useState(PLANETS[0]);
  const [r4EndPlanet2, setR4EndPlanet2] = useState(PLANETS[1]);
  const [r4EndDegree, setR4EndDegree] = useState('180');

  // Row 5
  const [r5StartPlanet, setR5StartPlanet] = useState(PLANETS[0]);
  const [r5StartEvent, setR5StartEvent] = useState(EVENTS[0]);
  const [r5EndPlanet, setR5EndPlanet] = useState(PLANETS[0]);
  const [r5EndEvent, setR5EndEvent] = useState(EVENTS[1]);
  const [color, setColor] = useState(COLORS[0]);

  const actionButtons = ({ disabled = false } = {}) => (
    <>
      <Button variant="contained" size="small" disabled={disabled}>RUN</Button>
      <Button variant="contained" size="small" disabled={disabled}>ADD TO CSG</Button>
    </>
  );

  const isRow5Disabled =
    (r5StartPlanet === 'Sun' && ['Retro', 'Direct', 'Combust'].includes(r5StartEvent)) ||
    (r5EndPlanet === 'Sun' && ['Retro', 'Direct', 'Combust'].includes(r5EndEvent)) ||
    (['Moon', 'Rahu', 'Ketu'].includes(r5StartPlanet) && ['Retro', 'Direct'].includes(r5StartEvent)) ||
    (['Moon', 'Rahu', 'Ketu'].includes(r5EndPlanet) && ['Retro', 'Direct'].includes(r5EndEvent));

  return (
    <Dialog open={open} onClose={onClose} maxWidth="lg">
      <DialogTitle sx={{ textAlign: 'center' }}>Area Highlight Study</DialogTitle>
      <DialogContent sx={{ "& .MuiSelect-select": { minWidth: 120 } }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
          <RadioGroup row value={highlightType} onChange={(e) => setHighlightType(e.target.value)}>
            <FormControlLabel value="range" control={<Radio />} label="Highlight Range" />
            <FormControlLabel value="events" control={<Radio />} label="Draw line from start event to end event" />
          </RadioGroup>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
            <Typography>Color:</Typography>
            <Select
              value={color}
              onChange={(e) => setColor(e.target.value)}
              size="small"
              sx={{ minWidth: 80, '& .MuiSelect-select': { display: 'flex', alignItems: 'center' } }}
              renderValue={(selected) => (
                <Box sx={{ width: 20, height: 20, backgroundColor: selected, border: '1px solid #ccc', borderRadius: '50%' }} />
              )}
            >
              {COLORS.map((c) => (
                <MenuItem key={c} value={c}>
                  <Box sx={{ width: 20, height: 20, backgroundColor: c, border: '1px solid #ccc', borderRadius: '50%' }} />
                </MenuItem>
              ))}
            </Select>
          </Box>
        </Box>

        <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
          {/* Row 1 */}
          <Box sx={{ p: 2, border: '1px solid #ddd', borderRadius: 1, display: 'flex', alignItems: 'center', gap: 2 }}>
            <Box sx={{ flexGrow: 1, display: 'flex', alignItems: 'center', gap: 1.5 }}>
              <Typography>Planet</Typography>
              <Select value={r1Planet} onChange={(e) => setR1Planet(e.target.value)} size="small">{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
              <Typography>from</Typography>
              <TextField value={r1FromDegree} onChange={(e) => setR1FromDegree(e.target.value)} size="small" sx={{ width: 80 }} type="number" />
              <Typography>to</Typography>
              <TextField value={r1ToDegree} onChange={(e) => setR1ToDegree(e.target.value)} size="small" sx={{ width: 80 }} type="number" />
              <Typography>in</Typography>
              <Select value={r1Zodiac} onChange={(e) => setR1Zodiac(e.target.value)} size="small">{ZODIAC_TYPES.map(z => <MenuItem key={z} value={z}>{z}</MenuItem>)}</Select>
            </Box>
            <Box sx={{ display: 'flex', gap: 1, flexShrink: 0 }}>{actionButtons()}</Box>
          </Box>

          {/* Row 2 */}
          <Box sx={{ p: 2, border: '1px solid #ddd', borderRadius: 1, display: 'flex', alignItems: 'center', gap: 2 }}>
            <Box sx={{ flexGrow: 1, display: 'flex', alignItems: 'center', gap: 1.5 }}>
              <Typography>Planet</Typography>
              <Select value={r2Planet} onChange={(e) => setR2Planet(e.target.value)} size="small">{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
              <Typography>in</Typography>
              <Select value={r2Sign} onChange={(e) => setR2Sign(e.target.value)} size="small">{SIGNS.map(s => <MenuItem key={s} value={s}>{s}</MenuItem>)}</Select>
              <Typography>in</Typography>
              <Select value={r2Zodiac} onChange={(e) => setR2Zodiac(e.target.value)} size="small">{ZODIAC_TYPES.map(z => <MenuItem key={z} value={z}>{z}</MenuItem>)}</Select>
            </Box>
            <Box sx={{ display: 'flex', gap: 1, flexShrink: 0 }}>{actionButtons()}</Box>
          </Box>

          {/* Row 3 */}
          <Box sx={{ p: 2, border: '1px solid #ddd', borderRadius: 1, display: 'flex', alignItems: 'center', gap: 2 }}>
            <Box sx={{ flexGrow: 1, display: 'flex', alignItems: 'center', gap: 1.5 }}>
              <Typography>Planet</Typography>
              <Select value={r3Planet} onChange={(e) => setR3Planet(e.target.value)} size="small">{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
              <Typography>in</Typography>
              <Select value={r3Nakshatra} onChange={(e) => setR3Nakshatra(e.target.value)} size="small">{NAKSHATRAS.map(n => <MenuItem key={n} value={n}>{n}</MenuItem>)}</Select>
            </Box>
            <Box sx={{ display: 'flex', gap: 1, flexShrink: 0 }}>{actionButtons()}</Box>
          </Box>

          {/* Row 4 */}
          <Box sx={{ p: 2, border: '1px solid #ddd', borderRadius: 1, display: 'flex', alignItems: 'center', gap: 2 }}>
            <Box sx={{ flexGrow: 1, display: 'flex', flexDirection: 'column', gap: 2 }}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
                <Typography>Start when</Typography>
                <Select value={r4StartPlanet1} onChange={(e) => setR4StartPlanet1(e.target.value)} size="small">{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
                <Typography>and</Typography>
                <Select value={r4StartPlanet2} onChange={(e) => setR4StartPlanet2(e.target.value)} size="small">{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
                <Typography>are</Typography>
                <TextField value={r4StartDegree} onChange={(e) => setR4StartDegree(e.target.value)} size="small" sx={{ width: 80 }} type="number" />
                <Typography>degrees apart</Typography>
              </Box>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
                <Typography>End when</Typography>
                <Select value={r4EndPlanet1} onChange={(e) => setR4EndPlanet1(e.target.value)} size="small">{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
                <Typography>and</Typography>
                <Select value={r4EndPlanet2} onChange={(e) => setR4EndPlanet2(e.target.value)} size="small">{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
                <Typography>are</Typography>
                <TextField value={r4EndDegree} onChange={(e) => setR4EndDegree(e.target.value)} size="small" sx={{ width: 80 }} type="number" />
                <Typography>degrees apart</Typography>
              </Box>
            </Box>
            <Box sx={{ display: 'flex', gap: 1, flexShrink: 0 }}>{actionButtons()}</Box>
          </Box>

          {/* Row 5 */}
          <Box sx={{ p: 2, border: '1px solid #ddd', borderRadius: 1, display: 'flex', alignItems: 'center', gap: 2 }}>
            <Box sx={{ flexGrow: 1, display: 'flex', flexDirection: 'column', gap: 2 }}>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 1.5 }}>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
                  <Typography>Start when</Typography>
                  <Select value={r5StartPlanet} onChange={(e) => setR5StartPlanet(e.target.value)} size="small">{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
                  <Typography>is</Typography>
                  <Select value={r5StartEvent} onChange={(e) => setR5StartEvent(e.target.value)} size="small">{EVENTS.map(e => <MenuItem key={e} value={e}>{e}</MenuItem>)}</Select>
                </Box>
              </Box>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 1.5 }}>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
                  <Typography>End when</Typography>
                  <Select value={r5EndPlanet} onChange={(e) => setR5EndPlanet(e.target.value)} size="small">{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
                  <Typography>is</Typography>
                  <Select value={r5EndEvent} onChange={(e) => setR5EndEvent(e.target.value)} size="small">{EVENTS.map(e => <MenuItem key={e} value={e}>{e}</MenuItem>)}</Select>
                </Box>
              </Box>
            </Box>
            <Box sx={{ display: 'flex', gap: 1, flexShrink: 0 }}>{actionButtons({ disabled: isRow5Disabled })}</Box>
          </Box>
        </Box>
      </DialogContent>
      <DialogActions sx={{ justifyContent: 'flex-start' }}>
        <Button onClick={onClose} variant="contained" color="inherit">CLOSE</Button>
      </DialogActions>
    </Dialog>
  );
};

AreaHighlightDialog.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
};

export default AreaHighlightDialog;
