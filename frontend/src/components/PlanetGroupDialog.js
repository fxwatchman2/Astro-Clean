import React, { useState } from 'react';
import {
  Dialog, DialogTitle, DialogContent, Box, Button, Grid, List, ListItem, ListItemButton, ListItemText, Typography, IconButton, Select, MenuItem, TextField, RadioGroup, FormControlLabel, Radio
} from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import PropTypes from 'prop-types';

const PLANET_LIST = [
  'Sun', 'Moon', 'Mercury', 'Venus', 'Mars', 'Jupiter', 'Saturn',
  'Uranus', 'Neptune', 'Pluto', 'Rahu', 'Ketu', 'Vesta', 'Chiron'
];

const HIGHLIGHT_TYPES = [
  { value: 'vertical', label: 'Vertical Line' },
  { value: 'circle', label: 'Circle' },
  { value: 'square', label: 'Square' },
  { value: 'largeCircle', label: 'Large Circle' },
  { value: 'largeSquare', label: 'Large Square' }
];

const COLOR_OPTIONS = [
  { value: '#d32f2f', label: 'Red' },
  { value: '#1976d2', label: 'Blue' },
  { value: '#388e3c', label: 'Green' },
  { value: '#fbc02d', label: 'Yellow' },
  { value: '#6d4c41', label: 'Brown' }
];

const ZODIAC_SIGNS = [
  'Aries', 'Taurus', 'Gemini', 'Cancer', 'Leo', 'Virgo',
  'Libra', 'Scorpio', 'Sagittarius', 'Capricorn', 'Aquarius', 'Pisces'
];

const RASHI_GROUPS = [
  '1-5-9', '2-6-10', '3-7-11', '4-8-11',
  '1-4-7-10', '2-5-8-11', '3-6-9-12',
  '1-3-5-7-9-11', '2-4-6-8-10-12',
  '1 to 6', '2 to 7', '3 to 8', '4 to 9', '5 to 10', '6 to 11', '7 to 12'
];

const ACTION_ROWS = [
  { label: 'Sidereal Rashi', input: 'sign' },
  { label: 'Tropical Rashi', input: 'sign' },
  { label: 'Sidereal Rashi Group', input: 'group' },
  { label: 'Within Degrees', input: true, degree: true },
  { label: 'All retro together', input: false },
  { label: 'All combust together', input: false },
  { label: 'Same Declination', input: false },
  { label: 'Crossing L Eclipse Degree', input: false },
  { label: 'Crossing S Eclipse Degree', input: false },
  { label: 'Crossing Galactic Center', input: false }
];


function PlanetGroupDialog({ open, onClose, onRunAction }) {
  // Highlight type and color (UI only)
  const [highlightType, setHighlightType] = useState(HIGHLIGHT_TYPES[0].value);
  const [color, setColor] = useState(COLOR_OPTIONS[0].value);

  // Planet selection state
  const [available, setAvailable] = useState(PLANET_LIST);
  const [selected, setSelected] = useState([]);
  const [highlightedLeft, setHighlightedLeft] = useState(null);
  const [highlightedRight, setHighlightedRight] = useState(null);

  // Inputs for the 4 input rows
  const [inputs, setInputs] = useState({ 0: '', 1: '', 2: '', 3: '' });
  const [degreeError, setDegreeError] = useState(false);

  // Select/Deselect logic
  const handleSelect = () => {
    if (highlightedLeft && available.includes(highlightedLeft) && !selected.includes(highlightedLeft)) {
      setSelected([...selected, highlightedLeft]);
      setAvailable(available.filter(p => p !== highlightedLeft));
      setHighlightedLeft(null);
    }
  };
  const handleDeselect = () => {
    if (highlightedRight && selected.includes(highlightedRight)) {
      setAvailable([...available, highlightedRight]);
      setSelected(selected.filter(p => p !== highlightedRight));
      setHighlightedRight(null);
    }
  };

  // Input change
  const handleInputChange = (idx, value) => {
    if (idx === 3) {
      // Degree row: only allow 0-359
      if (/^\d{0,3}$/.test(value) && (value === '' || (parseInt(value, 10) >= 0 && parseInt(value, 10) <= 359))) {
        setInputs({ ...inputs, [idx]: value });
        setDegreeError(false);
      } else {
        setInputs({ ...inputs, [idx]: value });
        setDegreeError(true);
      }
    } else {
      setInputs({ ...inputs, [idx]: value });
    }
  };

  // RUN handler
  const handleRun = (idx) => {
    if (idx === 3 && (degreeError || inputs[3] === '' || isNaN(Number(inputs[3])) || Number(inputs[3]) < 0 || Number(inputs[3]) > 359)) {
      setDegreeError(true);
      return;
    }
    if (onRunAction) {
      onRunAction(idx, {
        selectedPlanets: selected,
        value: inputs[idx] || null
      });
    }
    if (onClose) onClose();
  };

  return (
    <>
      <Dialog
        open={open}
        onClose={onClose}
        maxWidth={false}
        fullWidth={false}
        PaperProps={{ sx: { minWidth: 1100, maxWidth: 1200 } }}
      >
        <DialogTitle sx={{ textAlign: 'center', fontWeight: 'bold' }}>
          Planet Group Study
          <IconButton
            aria-label="close"
            onClick={onClose}
            sx={{ position: 'absolute', right: 8, top: 8, color: (theme) => theme.palette.grey[500] }}
          >
            <CloseIcon />
          </IconButton>
        </DialogTitle>
        <DialogContent>
          {/* Top highlight type and color selector */}
          <Box sx={{ border: '1px solid #e0e0e0', borderRadius: 2, p: 2, mb: 2 }}>
            <Grid container alignItems="center" spacing={2}>
              <Grid item>
                <RadioGroup row value={highlightType} onChange={e => setHighlightType(e.target.value)}>
                  {HIGHLIGHT_TYPES.map(ht => (
                    <FormControlLabel key={ht.value} value={ht.value} control={<Radio size="small" />} label={<Typography sx={{ fontSize: 15 }}>{ht.label}</Typography>} />
                  ))}
                </RadioGroup>
              </Grid>
              <Grid item sx={{ ml: 2 }}>
                <Typography component="span" sx={{ mr: 1, fontSize: 15 }}>Select color:</Typography>
                <Select value={color} onChange={e => setColor(e.target.value)} size="small" sx={{ width: 90, mr: 1 }}>
                  {COLOR_OPTIONS.map(opt => <MenuItem key={opt.value} value={opt.value}>{opt.label}</MenuItem>)}
                </Select>
                <Box sx={{ display: 'inline-block', width: 32, height: 32, bgcolor: color, border: '1px solid #222', verticalAlign: 'middle', ml: 1 }} />
              </Grid>
            </Grid>
          </Box>
          {/* Main layout: Planets, Selection, Actions (two columns side by side), Close button at bottom left */}
          <Grid container spacing={2} alignItems="flex-start" wrap="nowrap">
            {/* Planets List */}
            <Grid item sx={{ minWidth: 140 }}>
              <Typography variant="subtitle1" sx={{ mb: 1, fontSize: 16 }}>Planets</Typography>
              <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'stretch', height: 320, width: 120 }}>
                <Box sx={{ flex: 1, minHeight: 0, overflowY: 'auto', border: '1px solid #ccc', borderRadius: 2, bgcolor: '#fafafa', display: 'flex', flexDirection: 'column' }}>
                  <List sx={{ flex: 1, minHeight: 0 }}>
                    {available.map((planet) => (
                      <ListItem key={planet} disablePadding sx={{ minHeight: 28, p: 0 }}>
                        <ListItemButton
                          selected={highlightedLeft === planet}
                          onClick={() => setHighlightedLeft(planet)}
                          sx={{ minHeight: 28, p: 0, pl: 1 }}
                        >
                          <ListItemText primary={planet} primaryTypographyProps={{ fontSize: 15 }} />
                        </ListItemButton>
                      </ListItem>
                    ))}
                  </List>
                </Box>
              </Box>
            </Grid>
            {/* Select/Deselect Buttons - centered between lists */}
            <Grid item sx={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', height: 220, minWidth: 100 }}>
              <Button variant="contained" color="primary" sx={{ mb: 2, width: 90 }} onClick={handleSelect} disabled={!highlightedLeft}>Select</Button>
              <Button variant="contained" color="secondary" sx={{ width: 90 }} onClick={handleDeselect} disabled={!highlightedRight}>Deselect</Button>
            </Grid>
            {/* Selected Planets List, same size as Planets list */}
            <Grid item sx={{ minWidth: 140 }}>
              <Typography variant="subtitle1" sx={{ mb: 1, fontSize: 16 }}>Selection</Typography>
              <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'stretch', height: 320, width: 120 }}>
                <Box sx={{ flex: 1, minHeight: 0, overflowY: 'auto', border: '1px solid #ccc', borderRadius: 2, bgcolor: '#f5f5f5', display: 'flex', flexDirection: 'column' }}>
                  <List sx={{ flex: 1, minHeight: 0 }}>
                    {selected.map((planet) => (
                      <ListItem key={planet} disablePadding sx={{ minHeight: 28, p: 0 }}>
                        <ListItemButton
                          selected={highlightedRight === planet}
                          onClick={() => setHighlightedRight(planet)}
                          sx={{ minHeight: 28, p: 0, pl: 1 }}
                        >
                          <ListItemText primary={planet} primaryTypographyProps={{ fontSize: 15 }} />
                        </ListItemButton>
                      </ListItem>
                    ))}
                  </List>
                </Box>
              </Box>
            </Grid>
            {/* Actions with RUN buttons (two columns side by side) */}
            <Grid item sx={{ minWidth: 600, flex: 1 }}>
              <Box sx={{ display: 'flex', flexDirection: 'row', gap: 3, width: '100%' }}>
                {/* Left column: Selected Planets in... */}
                <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1, flex: 1 }}>
                  {[0,1,2,3,4].map(rowIdx => (
                    <Box key={rowIdx} sx={{ display: 'grid', gridTemplateColumns: '150px 120px 80px', alignItems: 'center', minHeight: 48 }}>
                      <Typography sx={{ fontSize: 15, fontFamily: 'inherit', fontWeight: 400, textAlign: 'right', pr: 0 }}>{ACTION_ROWS[rowIdx].label}</Typography>
                      {ACTION_ROWS[rowIdx].input === 'sign' ? (
                        <Select
                          size="small"
                          value={inputs[rowIdx] || ''}
                          onChange={e => handleInputChange(rowIdx, e.target.value)}
                          displayEmpty
                          sx={{ width: 110, mx: 1 }}
                        >
                          <MenuItem value="" disabled>Select...</MenuItem>
                          {ZODIAC_SIGNS.map(sign => (
                            <MenuItem key={sign} value={sign}>{sign}</MenuItem>
                          ))}
                        </Select>
                      ) : ACTION_ROWS[rowIdx].input === 'group' ? (
                        <Select
                          size="small"
                          value={inputs[rowIdx] || ''}
                          onChange={e => handleInputChange(rowIdx, e.target.value)}
                          displayEmpty
                          sx={{ width: 110, mx: 1 }}
                        >
                          <MenuItem value="" disabled>Select group</MenuItem>
                          {RASHI_GROUPS.map(group => (
                            <MenuItem key={group} value={group}>{group}</MenuItem>
                          ))}
                        </Select>
                      ) : ACTION_ROWS[rowIdx].input ? (
                        <TextField
                          size="small"
                          value={inputs[rowIdx] || ''}
                          onChange={e => handleInputChange(rowIdx, e.target.value)}
                          error={rowIdx === 3 && degreeError}
                          helperText={rowIdx === 3 && degreeError ? '0-359 only' : ''}
                          inputProps={rowIdx === 3 ? { inputMode: 'numeric', pattern: '[0-9]*', min: 0, max: 359, maxLength: 3 } : {}}
                          sx={{ mx: 1, width: 110 }}
                        />
                      ) : (<Box sx={{ width: 110, mx: 1 }} />)}
                      <Button variant="contained" color="primary" sx={{ justifySelf: 'end', minWidth: 56 }} onClick={() => handleRun(rowIdx)}>RUN</Button>
                    </Box>
                  ))}
                </Box>
                {/* Right column: All combust together, etc. */}
                <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1, flex: 1 }}>
                  {[5,6,7,8,9].map(idx => (
                    <Box key={idx} sx={{ display: 'grid', gridTemplateColumns: '180px 80px', alignItems: 'center', minHeight: 48 }}>
                      <Typography sx={{ fontSize: 15, fontFamily: 'inherit', fontWeight: 400, textAlign: 'right', pr: 0 }}>{ACTION_ROWS[idx].label}</Typography>
                      <Button variant="contained" color="primary" sx={{ justifySelf: 'end', minWidth: 56 }} onClick={() => handleRun(idx)}>RUN</Button>
                    </Box>
                  ))}
                </Box>
              </Box>
            </Grid>

          </Grid>
        </DialogContent>
        {/* CLOSE button in its own row below all content */}
        <Box sx={{ pl: 0, pr: 0, py: 2, borderTop: '1px solid #eee', bgcolor: '#fff', width: '100%', display: 'flex', justifyContent: 'flex-start' }}>
          <Button onClick={onClose} color="secondary" variant="outlined" sx={{ minWidth: 90, ml: 3 }}>
            CLOSE
          </Button>
        </Box>
      </Dialog>
    </>
  );
}

PlanetGroupDialog.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  onRunAction: PropTypes.func
};

export default PlanetGroupDialog;
