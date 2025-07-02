import React, { useState } from 'react';
import PropTypes from 'prop-types';
import {
  Dialog, DialogTitle, DialogContent, DialogActions, Button, Grid, TextField, Checkbox, RadioGroup, Radio,
  FormControlLabel, Switch, Slider, Select, MenuItem, InputLabel, FormControl, Box, Typography, Accordion,
  AccordionSummary, AccordionDetails, Autocomplete, Tabs, Tab, Alert, LinearProgress, CircularProgress, Avatar,
  Badge, Chip, Divider, Tooltip, IconButton, Paper, ToggleButton, ToggleButtonGroup
} from '@mui/material';
import {
  ExpandMore as ExpandMoreIcon, Mail as MailIcon, Delete as DeleteIcon, Save as SaveIcon,
  PhotoCamera as PhotoCameraIcon, AccessAlarm as AccessAlarmIcon,
  FormatBold as FormatBoldIcon, FormatItalic as FormatItalicIcon, FormatUnderlined as FormatUnderlinedIcon,
  Info as InfoIcon
} from '@mui/icons-material';

const ControlsShowcaseDialog = ({ open, onClose }) => {
  const [textValue, setTextValue] = useState('');
  const [numberValue, setNumberValue] = useState(0);
  const [checked, setChecked] = useState(true);
  const [radioValue, setRadioValue] = useState('female');
  const [switchState, setSwitchState] = useState(false);
  const [sliderValue, setSliderValue] = useState(30);
  const [selectValue, setSelectValue] = useState('');
  const [multiSelectValue, setMultiSelectValue] = useState([]);
  const [autocompleteValue, setAutocompleteValue] = useState(null);
  const [tabValue, setTabValue] = useState(0);
  const [toggleButtonValue, setToggleButtonValue] = useState('left');

  const top100Films = [
    { label: 'The Shawshank Redemption', year: 1994 },
    { label: 'The Godfather', year: 1972 },
    { label: 'The Dark Knight', year: 2008 },
  ];

  const handleTabChange = (event, newValue) => {
    setTabValue(newValue);
  };

  const handleToggleButtonChange = (event, newAlignment) => {
    if (newAlignment !== null) {
      setToggleButtonValue(newAlignment);
    }
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="lg" scroll="paper">
      <DialogTitle sx={{ textAlign: 'center', fontWeight: 'bold' }}>MUI Controls Showcase</DialogTitle>
      <DialogContent dividers>
        <Grid container spacing={3} sx={{ p: 2 }}>
          {/* Section: Text Fields */}
          <Grid item xs={12}><Typography variant="h6" gutterBottom>Text Fields</Typography><Divider /></Grid>
          <Grid item xs={12} sm={6} md={4}>
            <TextField label="Outlined" variant="outlined" fullWidth value={textValue} onChange={(e) => setTextValue(e.target.value)} />
          </Grid>
          <Grid item xs={12} sm={6} md={4}>
            <TextField label="Filled" variant="filled" fullWidth />
          </Grid>
          <Grid item xs={12} sm={6} md={4}>
            <TextField label="Standard" variant="standard" fullWidth />
          </Grid>
          <Grid item xs={12} sm={6} md={4}>
            <TextField label="Number Input" type="number" fullWidth value={numberValue} onChange={(e) => setNumberValue(e.target.valueAsNumber)} InputLabelProps={{ shrink: true }} />
          </Grid>
          <Grid item xs={12} sm={6} md={4}>
            <TextField label="Password" type="password" fullWidth autoComplete="current-password" />
          </Grid>
          <Grid item xs={12} sm={6} md={4}>
            <TextField label="Multiline" multiline rows={3} fullWidth />
          </Grid>
          <Grid item xs={12} sm={6} md={4}>
            <TextField label="Error State" error helperText="Incorrect entry." fullWidth />
          </Grid>
          <Grid item xs={12} sm={6} md={4}>
            <TextField label="Disabled" disabled defaultValue="Disabled" fullWidth />
          </Grid>
          <Grid item xs={12} sm={6} md={4}>
            <TextField label="Read Only" InputProps={{ readOnly: true }} defaultValue="Read Only" fullWidth />
          </Grid>

          {/* Section: Buttons */}
          <Grid item xs={12}><Typography variant="h6" gutterBottom sx={{ mt: 2 }}>Buttons</Typography><Divider /></Grid>
          <Grid item xs={12} sm={6} md={3}><Button variant="contained" fullWidth>Contained</Button></Grid>
          <Grid item xs={12} sm={6} md={3}><Button variant="outlined" fullWidth>Outlined</Button></Grid>
          <Grid item xs={12} sm={6} md={3}><Button variant="text" fullWidth>Text</Button></Grid>
          <Grid item xs={12} sm={6} md={3}><Button variant="contained" color="secondary" fullWidth>Secondary</Button></Grid>
          <Grid item xs={12} sm={6} md={3}><Button variant="contained" disabled fullWidth>Disabled</Button></Grid>
          <Grid item xs={12} sm={6} md={3}><Button variant="contained" startIcon={<SaveIcon />} fullWidth>Save</Button></Grid>
          <Grid item xs={12} sm={6} md={3}><Button variant="outlined" endIcon={<DeleteIcon />} color="error" fullWidth>Delete</Button></Grid>
          <Grid item xs={12} sm={6} md={3}>
            <IconButton color="primary" aria-label="add an alarm">
              <AccessAlarmIcon />
            </IconButton>
            <IconButton color="secondary" aria-label="take a photo">
              <PhotoCameraIcon />
            </IconButton>
          </Grid>

          {/* Section: Selection Controls */}
          <Grid item xs={12}><Typography variant="h6" gutterBottom sx={{ mt: 2 }}>Selection Controls</Typography><Divider /></Grid>
          <Grid item xs={12} sm={6} md={4}>
            <FormControlLabel control={<Checkbox checked={checked} onChange={(e) => setChecked(e.target.checked)} />} label="Checkbox" />
            <FormControlLabel control={<Checkbox disabled />} label="Disabled Checkbox" />
          </Grid>
          <Grid item xs={12} sm={6} md={4}>
            <FormControl component="fieldset">
              <RadioGroup row aria-label="gender" name="gender" value={radioValue} onChange={(e) => setRadioValue(e.target.value)}>
                <FormControlLabel value="female" control={<Radio />} label="Female" />
                <FormControlLabel value="male" control={<Radio />} label="Male" />
                <FormControlLabel value="other" control={<Radio />} label="Other" />
                <FormControlLabel value="disabled" disabled control={<Radio />} label="(Disabled)" />
              </RadioGroup>
            </FormControl>
          </Grid>
          <Grid item xs={12} sm={6} md={4}>
            <FormControlLabel control={<Switch checked={switchState} onChange={(e) => setSwitchState(e.target.checked)} />} label="Switch" />
            <FormControlLabel control={<Switch disabled />} label="Disabled Switch" />
          </Grid>

          {/* Section: Slider */}
          <Grid item xs={12}><Typography variant="h6" gutterBottom sx={{ mt: 2 }}>Slider</Typography><Divider /></Grid>
          <Grid item xs={12}>
            <Slider value={sliderValue} onChange={(e, newValue) => setSliderValue(newValue)} aria-labelledby="continuous-slider" valueLabelDisplay="auto" />
          </Grid>
          <Grid item xs={12}>
            <Slider defaultValue={20} step={10} marks min={10} max={100} valueLabelDisplay="on" disabled />
          </Grid>

          {/* Section: Select */}
          <Grid item xs={12}><Typography variant="h6" gutterBottom sx={{ mt: 2 }}>Select</Typography><Divider /></Grid>
          <Grid item xs={12} sm={6}>
            <FormControl fullWidth>
              <InputLabel id="simple-select-label">Age</InputLabel>
              <Select labelId="simple-select-label" id="simple-select" value={selectValue} label="Age" onChange={(e) => setSelectValue(e.target.value)}>
                <MenuItem value={10}>Ten</MenuItem>
                <MenuItem value={20}>Twenty</MenuItem>
                <MenuItem value={30}>Thirty</MenuItem>
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={12} sm={6}>
            <FormControl fullWidth>
              <InputLabel id="multi-select-label">Tags</InputLabel>
              <Select labelId="multi-select-label" multiple value={multiSelectValue} onChange={(e) => setMultiSelectValue(e.target.value)} label="Tags">
                <MenuItem value="tagA">Tag A</MenuItem>
                <MenuItem value="tagB">Tag B</MenuItem>
                <MenuItem value="tagC">Tag C</MenuItem>
              </Select>
            </FormControl>
          </Grid>

          {/* Section: Autocomplete */}
          <Grid item xs={12}><Typography variant="h6" gutterBottom sx={{ mt: 2 }}>Autocomplete</Typography><Divider /></Grid>
          <Grid item xs={12} sm={6}>
            <Autocomplete
              disablePortal
              id="combo-box-demo"
              options={top100Films}
              value={autocompleteValue}
              onChange={(event, newValue) => { setAutocompleteValue(newValue); }}
              renderInput={(params) => <TextField {...params} label="Movie" />}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <Autocomplete multiple options={top100Films.map(option => option.label)} defaultValue={[top100Films[0].label]} renderInput={(params) => (<TextField {...params} label="Multiple Movies" />)} />
          </Grid>

          {/* Section: Toggle Buttons */}
          <Grid item xs={12}><Typography variant="h6" gutterBottom sx={{ mt: 2 }}>Toggle Buttons</Typography><Divider /></Grid>
          <Grid item xs={12}>
            <ToggleButtonGroup value={toggleButtonValue} exclusive onChange={handleToggleButtonChange} aria-label="text alignment">
              <ToggleButton value="left" aria-label="left aligned"><FormatBoldIcon /></ToggleButton>
              <ToggleButton value="center" aria-label="centered"><FormatItalicIcon /></ToggleButton>
              <ToggleButton value="right" aria-label="right aligned"><FormatUnderlinedIcon /></ToggleButton>
              <ToggleButton value="justify" aria-label="justified" disabled><InfoIcon /></ToggleButton>
            </ToggleButtonGroup>
          </Grid>

          {/* Section: Tabs */}
          <Grid item xs={12}><Typography variant="h6" gutterBottom sx={{ mt: 2 }}>Tabs</Typography><Divider /></Grid>
          <Grid item xs={12}>
            <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
              <Tabs value={tabValue} onChange={handleTabChange} aria-label="basic tabs example">
                <Tab label="Item One" />
                <Tab label="Item Two" />
                <Tab label="Item Three" disabled />
              </Tabs>
            </Box>
            {tabValue === 0 && <Box sx={{ p: 3 }}>Content for Item One</Box>}
            {tabValue === 1 && <Box sx={{ p: 3 }}>Content for Item Two</Box>}
          </Grid>

          {/* Section: Accordion */}
          <Grid item xs={12}><Typography variant="h6" gutterBottom sx={{ mt: 2 }}>Accordion</Typography><Divider /></Grid>
          <Grid item xs={12}>
            <Accordion>
              <AccordionSummary expandIcon={<ExpandMoreIcon />} aria-controls="panel1a-content" id="panel1a-header">
                <Typography>Accordion 1</Typography>
              </AccordionSummary>
              <AccordionDetails><Typography>Details for Accordion 1.</Typography></AccordionDetails>
            </Accordion>
            <Accordion defaultExpanded>
              <AccordionSummary expandIcon={<ExpandMoreIcon />} aria-controls="panel2a-content" id="panel2a-header">
                <Typography>Accordion 2 (Expanded by default)</Typography>
              </AccordionSummary>
              <AccordionDetails><Typography>More details for Accordion 2.</Typography></AccordionDetails>
            </Accordion>
            <Accordion disabled>
              <AccordionSummary expandIcon={<ExpandMoreIcon />}><Typography>Disabled Accordion</Typography></AccordionSummary>
            </Accordion>
          </Grid>

          {/* Section: Feedback */}
          <Grid item xs={12}><Typography variant="h6" gutterBottom sx={{ mt: 2 }}>Feedback Elements</Typography><Divider /></Grid>
          <Grid item xs={12}><Alert severity="error">This is an error alert!</Alert></Grid>
          <Grid item xs={12}><Alert severity="warning">This is a warning alert!</Alert></Grid>
          <Grid item xs={12}><Alert severity="info">This is an info alert.</Alert></Grid>
          <Grid item xs={12}><Alert severity="success" onClose={() => {}}>This is a success alert with a close button.</Alert></Grid>
          <Grid item xs={12} sm={6}><Typography>Linear Progress</Typography><LinearProgress /></Grid>
          <Grid item xs={12} sm={6}><Typography>Circular Progress</Typography><CircularProgress /></Grid>

          {/* Section: Data Display */}
          <Grid item xs={12}><Typography variant="h6" gutterBottom sx={{ mt: 2 }}>Data Display</Typography><Divider /></Grid>
          <Grid item xs={12} sm={4} md={2}><Avatar>H</Avatar></Grid>
          <Grid item xs={12} sm={4} md={2}><Avatar sx={{ bgcolor: 'secondary.main' }}>OP</Avatar></Grid>
          <Grid item xs={12} sm={4} md={2}><Badge badgeContent={4} color="primary"><MailIcon /></Badge></Grid>
          <Grid item xs={12} sm={4} md={2}><Chip label="Basic Chip" /></Grid>
          <Grid item xs={12} sm={4} md={2}><Chip label="Deletable Chip" onDelete={() => {}} /></Grid>
          <Grid item xs={12} sm={4} md={2}><Tooltip title="This is a tooltip"><Chip label="Chip with Tooltip" /></Tooltip></Grid>

          {/* Section: Paper */}
          <Grid item xs={12}><Typography variant="h6" gutterBottom sx={{ mt: 2 }}>Paper</Typography><Divider /></Grid>
          <Grid item xs={12} sm={6} md={4}><Paper elevation={0} sx={{p:2, textAlign:'center'}}>Elevation 0</Paper></Grid>
          <Grid item xs={12} sm={6} md={4}><Paper elevation={3} sx={{p:2, textAlign:'center'}}>Elevation 3</Paper></Grid>
          <Grid item xs={12} sm={6} md={4}><Paper elevation={6} sx={{p:2, textAlign:'center'}}>Elevation 6</Paper></Grid>

        </Grid>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Close</Button>
        <Button onClick={onClose} variant="contained">Save Example</Button>
      </DialogActions>
    </Dialog>
  );
};

ControlsShowcaseDialog.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
};

export default ControlsShowcaseDialog;
