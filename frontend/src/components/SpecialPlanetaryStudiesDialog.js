import React, { useState } from 'react';
import {
  Dialog, DialogTitle, DialogContent, DialogActions, Button, Select, MenuItem,
  Radio, RadioGroup, FormControlLabel, Typography, Paper, Box, Grid
} from '@mui/material';

const PLANETS = [
  'Sun', 'Moon', 'Mercury', 'Venus', 'Mars', 'Jupiter', 'Saturn', 'Uranus', 'Neptune', 'Pluto', 'Rahu', 'Ketu'
];
const COLORS = [
  'Red', 'Green', 'Blue', 'Yellow', 'Orange', 'Purple', 'Black', 'White', 'Gray', 'Brown', 'Pink', 'Cyan'
];
const RASHIS = ['Mesh/Aries', 'Vrishabh/Taurus', 'Mithun/Gemini', 'Kark/Cancer', 'Simha/Leo', 'Kanya/Virgo', 'Tula/Libra', 'Vrischik/Scorpio', 'Dhanu/Sagittarius', 'Makar/Capricorn', 'Kumbh/Aquarius', 'Meen/Pisces'];

const SpecialPlanetaryStudiesDialog = ({ open, onClose }) => {
  const [highlightType, setHighlightType] = useState('vertical');
  const [color, setColor] = useState(COLORS[0]);
  const [zodiac, setZodiac] = useState('sidereal');
  const [planet1, setPlanet1] = useState('Mars');
  const [rashi, setRashi] = useState(RASHIS[0]);

  return (
    <Dialog open={open} onClose={onClose} maxWidth="md" fullWidth>
      <DialogTitle sx={{ textAlign: 'center', fontWeight: 'bold' }}>
        Special Planetary Studies
      </DialogTitle>
      <DialogContent>
        {/* Highlight and color controls */}
        <Paper variant="outlined" sx={{ p: 2, mb: 2 }}>
          <Grid container alignItems="center" spacing={2}>
            <Grid item>
              <FormControlLabel control={<Radio checked={highlightType === 'vertical'} onChange={() => setHighlightType('vertical')} size="small" />} label={<Typography sx={{ fontSize: 15 }}>Vertical Line</Typography>} />
            </Grid>
            <Grid item>
              <FormControlLabel control={<Radio checked={highlightType === 'circle'} onChange={() => setHighlightType('circle')} size="small" />} label={<Typography sx={{ fontSize: 15 }}>Circle</Typography>} />
            </Grid>
            <Grid item>
              <FormControlLabel control={<Radio checked={highlightType === 'square'} onChange={() => setHighlightType('square')} size="small" />} label={<Typography sx={{ fontSize: 15 }}>Square</Typography>} />
            </Grid>
            <Grid item>
              <FormControlLabel control={<Radio checked={highlightType === 'largeCircle'} onChange={() => setHighlightType('largeCircle')} size="small" />} label={<Typography sx={{ fontSize: 15 }}>Large Circle</Typography>} />
            </Grid>
            <Grid item>
              <FormControlLabel control={<Radio checked={highlightType === 'largeSquare'} onChange={() => setHighlightType('largeSquare')} size="small" />} label={<Typography sx={{ fontSize: 15 }}>Large Square</Typography>} />
            </Grid>
            <Grid item sx={{ ml: 2 }}>
              <Typography component="span" sx={{ mr: 1, fontSize: 15 }}>Select color:</Typography>
              <Select value={color} onChange={e => setColor(e.target.value)} size="small" sx={{ width: 90, mr: 1 }}>
                {COLORS.map(c => <MenuItem key={c} value={c}>{c}</MenuItem>)}
              </Select>
              <Box sx={{ display: 'inline-block', width: 32, height: 32, bgcolor: color.toLowerCase(), border: '1px solid #222', verticalAlign: 'middle', ml: 1 }} />
            </Grid>
          </Grid>
        </Paper>

        {/* Zodiac selection */}
        <Paper variant="outlined" sx={{ p: 2, mb: 2 }}>
          <Grid container alignItems="center" spacing={2}>
            <Grid item>
              <Typography>Zodiac</Typography>
            </Grid>
            <Grid item>
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
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 2 }}>
            <Typography>Planet</Typography>
            <Select value={planet1} onChange={e => setPlanet1(e.target.value)} size="small">{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
            <Typography>in Rashi</Typography>
            <Select value={rashi} onChange={e => setRashi(e.target.value)} size="small">{RASHIS.map(r => <MenuItem key={r} value={r}>{r}</MenuItem>)}</Select>
            <Box sx={{ ml: 'auto', display: 'flex', gap: 1 }}>
              <Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>RUN</Button>
              <Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>ADD TO CSG</Button>
            </Box>
          </Box>

          {/* Row 2: Planet in Nakshatra */}
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 2 }}>
            <Typography>Planet</Typography>
            <Select value={planet1} onChange={e => setPlanet1(e.target.value)} size="small">{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
            <Typography>in Nakshatra</Typography>
            <Select size="small" value={'Ashwini'}><MenuItem value={'Ashwini'}>Ashwini</MenuItem></Select>
            <Box sx={{ ml: 'auto', display: 'flex', gap: 1 }}>
              <Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>RUN</Button>
              <Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>ADD TO CSG</Button>
            </Box>
          </Box>

          {/* Row 3: Planet in Nakshatra in Charan */}
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 2 }}>
            <Typography>Planet</Typography>
            <Select value={planet1} onChange={e => setPlanet1(e.target.value)} size="small">{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
            <Typography>in Nakshatra</Typography>
            <Select size="small" value={'Ashwini'}><MenuItem value={'Ashwini'}>Ashwini</MenuItem></Select>
            <Typography>in Charan</Typography>
            <Select size="small" value={'1'}><MenuItem value={'1'}>1</MenuItem><MenuItem value={'2'}>2</MenuItem><MenuItem value={'3'}>3</MenuItem><MenuItem value={'4'}>4</MenuItem></Select>
            <Box sx={{ ml: 'auto', display: 'flex', gap: 1 }}>
              <Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>RUN</Button>
              <Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>ADD TO CSG</Button>
            </Box>
          </Box>

          {/* Row 4: Planet Midpoint Between Planet And Planet */}
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 2 }}>
            <Typography>Planet</Typography>
            <Select value={planet1} onChange={e => setPlanet1(e.target.value)} size="small">{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
            <Typography>Midpoint Between Planet</Typography>
            <Select size="small" value={'Mars'}>{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
            <Typography>And Planet</Typography>
            <Select size="small" value={'Mars'}>{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
            <Box sx={{ ml: 'auto', display: 'flex', gap: 1 }}>
              <Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>RUN</Button>
              <Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>ADD TO CSG</Button>
            </Box>
          </Box>

          {/* Row 5: Planet Follows [Planet] By Degrees [N] */}
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 2 }}>
            <Typography>Planet</Typography>
            <Select value={planet1} onChange={e => setPlanet1(e.target.value)} size="small">{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
            <Typography>Follows</Typography>
            <Select size="small" value={'Mars'}>{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
            <Typography>By Degrees</Typography>
            <Select size="small" value={'1'}>{[...Array(13).keys()].map(n => <MenuItem key={n+1} value={n+1}>{n+1}</MenuItem>)}</Select>
            <Box sx={{ ml: 'auto', display: 'flex', gap: 1 }}>
              <Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>RUN</Button>
              <Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>ADD TO CSG</Button>
            </Box>
          </Box>

          {/* Row 6: Planet In Each Others' Sign With Planet */}
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 2 }}>
            <Typography>Planet</Typography>
            <Select value={planet1} onChange={e => setPlanet1(e.target.value)} size="small">{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
            <Typography>In Each Others&apos; Sign With Planet</Typography>
            <Select size="small" value={'Mars'}>{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
            <Box sx={{ ml: 'auto', display: 'flex', gap: 1 }}>
              <Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>RUN</Button>
              <Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>ADD TO CSG</Button>
            </Box>
          </Box>

          {/* Row 7: Planet Declination */}
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 2 }}>
            <Typography>Planet</Typography>
            <Select value={planet1} onChange={e => setPlanet1(e.target.value)} size="small">{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
            <Typography>Declination</Typography>
            <Select size="small" value={''}><MenuItem value={''}></MenuItem></Select>
            <Box sx={{ ml: 'auto', display: 'flex', gap: 1 }}>
              <Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>RUN</Button>
              <Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>ADD TO CSG</Button>
            </Box>
          </Box>

          {/* Row 8: Planet Declination */}
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 2 }}>
            <Typography>Planet</Typography>
            <Select value={planet1} onChange={e => setPlanet1(e.target.value)} size="small">{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
            <Typography>Declination</Typography>
            <Select size="small" value={''}><MenuItem value={''}></MenuItem></Select>
            <Box sx={{ ml: 'auto', display: 'flex', gap: 1 }}>
              <Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>RUN</Button>
              <Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>ADD TO CSG</Button>
            </Box>
          </Box>

          {/* Row 9: Planet Declination */}
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 2 }}>
            <Typography>Planet</Typography>
            <Select value={planet1} onChange={e => setPlanet1(e.target.value)} size="small">{PLANETS.map(p => <MenuItem key={p} value={p}>{p}</MenuItem>)}</Select>
            <Typography>Declination</Typography>
            <Select size="small" value={''}><MenuItem value={''}></MenuItem></Select>
            <Box sx={{ ml: 'auto', display: 'flex', gap: 1 }}>
              <Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>RUN</Button>
              <Button variant="contained" sx={{ backgroundColor: '#8e44ad' }}>ADD TO CSG</Button>
            </Box>
          </Box>


        </Paper>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} sx={{ backgroundColor: '#e3f2fd', color: '#1976d2', '&:hover': { backgroundColor: '#bbdefb' } }}>CLOSE</Button>
      </DialogActions>
    </Dialog>
  );
};

import PropTypes from 'prop-types';

SpecialPlanetaryStudiesDialog.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
};

export default SpecialPlanetaryStudiesDialog;
