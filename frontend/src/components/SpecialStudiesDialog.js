import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { 
    Dialog, DialogTitle, DialogContent, DialogActions, Button, Checkbox, FormControlLabel, 
    Radio, RadioGroup, Select, MenuItem, Typography, Box, Paper, Tooltip
} from '@mui/material';

const DISPLAY_TYPES = [
  { value: 'vLine', label: 'Vertical Line' },
  { value: 'circle', label: 'Circle' },
  { value: 'square', label: 'Square' },
  { value: 'star', label: 'Star' },
  { value: 'downArrow', label: 'Down Arrow' }
];

const planetOptions = ['Sun', 'Moon', 'Mercury', 'Venus', 'Mars', 'Jupiter', 'Saturn', 'Uranus', 'Neptune', 'Pluto', 'Rahu', 'Ketu', 'Vesta', 'Chiron'];
const nakshatraOptions = ['Ashwini', 'Bharani', 'Krittika', 'Rohini', 'Mrigashira', 'Ardra', 'Punarvasu', 'Pushya', 'Ashlesha', 'Magha', 'Purva Phalguni', 'Uttara Phalguni', 'Hasta', 'Chitra', 'Swati', 'Vishakha', 'Anuradha', 'Jyeshtha', 'Mula', 'Purva Ashadha', 'Uttara Ashadha', 'Shravana', 'Dhanishta', 'Shatabhisha', 'Purva Bhadrapada', 'Uttara Bhadrapada', 'Revati'];
const charanOptions = ['ALL', '1', '2', '3', '4'];
const signOptions = ['Aries / Mesha (मेष)', 'Taurus / Vrishabha (वृषभ)', 'Gemini / Mithuna (मिथुन)', 'Cancer / Karka (कर्क)', 'Leo / Simha (सिंह)', 'Virgo / Kanya (कन्या)', 'Libra / Tula (तुला)', 'Scorpio / Vrishchika (वृश्चिक)', 'Sagittarius / Dhanu (धनुष)', 'Capricorn / Makara (मकर)', 'Aquarius / Kumbha (कुम्भ)', 'Pisces / Meena (मीन)'];
const declinationOptions = ['Max North', 'Zero', 'Max South'];
const colorOptions = ['Red', 'Green', 'Blue', 'Yellow', 'Orange', 'Purple'];

const StudyRow = ({ controls }) => {
    const isMidpointRow = controls.some(c => c.type === 'label' && c.label === 'at Midpoint');

    const getInitialSelections = () => {
        const selects = controls.filter(c => c.type === 'select');
        const initial = {};

        if (isMidpointRow && selects.length === 3) {
            initial[0] = selects[0].options[0]; // Sun
            initial[1] = selects[1].options[1]; // Moon
            initial[2] = selects[2].options[4]; // Mars
        } else {
            selects.forEach((control, index) => {
                initial[index] = control.options[0];
            });
        }
        return initial;
    };

    const [selections, setSelections] = useState(getInitialSelections());

    const handleSelectionChange = (selectIndex, value) => {
        setSelections(prev => ({ ...prev, [selectIndex]: value }));
    };

    let buttonsDisabled = false;
    if (isMidpointRow) {
        const selectedPlanets = Object.values(selections);
        const uniquePlanets = new Set(selectedPlanets);
        buttonsDisabled = uniquePlanets.size < selectedPlanets.length;
    }

    let selectCounter = -1;

    return (
        <Paper variant="outlined" sx={{ p: 1.5, mb: 1.5, borderRadius: '8px' }}>
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                <Box sx={{ display: 'flex', flexGrow: 1, alignItems: 'center', gap: 1 }}>
                    {controls.map((control, index) => (
                        <React.Fragment key={index}>
                            {control.type === 'label' && <Typography variant="body1" sx={{ whiteSpace: 'nowrap' }}>{control.label}</Typography>}
                            {control.type === 'select' && (() => {
                                selectCounter++;
                                const currentSelectIndex = selectCounter;
                                return (
                                    <Select
                                        value={selections[currentSelectIndex]}
                                        onChange={(e) => handleSelectionChange(currentSelectIndex, e.target.value)}
                                        size="small"
                                        sx={{ minWidth: 120 }}
                                    >
                                        {control.options.map(opt => <MenuItem key={opt} value={opt}>{opt}</MenuItem>)}
                                    </Select>
                                );
                            })()}
                        </React.Fragment>
                    ))}
                </Box>
                <Box sx={{ display: 'flex', gap: 1 }}>
                    <Tooltip title="Execute right away">
                        <span>
                            <Button variant="contained" size="small" sx={{ backgroundColor: '#8e44ad', '&:hover': { backgroundColor: '#732d91' } }} disabled={buttonsDisabled}>RUN</Button>
                        </span>
                    </Tooltip>
                    <Tooltip title="Add to Current Study Group to execute with other studies">
                        <span>
                            <Button variant="contained" size="small" sx={{ backgroundColor: '#8e44ad', '&:hover': { backgroundColor: '#732d91' } }} disabled={buttonsDisabled}>ADD To CSG</Button>
                        </span>
                    </Tooltip>
                </Box>
            </Box>
        </Paper>
    );
};

StudyRow.propTypes = {
    controls: PropTypes.array.isRequired,
};

const SpecialStudiesDialog = ({ open, onClose }) => {
    const [highlight, setHighlight] = useState(true);
    const [displayType, setDisplayType] = useState(DISPLAY_TYPES[0].value);

        const studyDefinitions = [
        [{type: 'label', label: 'Planet'}, {type: 'select', options: planetOptions}, {type: 'label', label: 'in Sign'}, {type: 'select', options: signOptions}],
        [{type: 'label', label: 'Planet'}, {type: 'select', options: planetOptions}, {type: 'label', label: 'in Nakshatra'}, {type: 'select', options: nakshatraOptions}, {type: 'label', label: 'in Charan'}, {type: 'select', options: charanOptions}],
        [{type: 'label', label: 'Planet'}, {type: 'select', options: planetOptions}, {type: 'label', label: 'at Midpoint'}, {type: 'select', options: planetOptions}, {type: 'label', label: 'and'}, {type: 'select', options: planetOptions}],
        [{type: 'label', label: 'Planet'}, {type: 'select', options: planetOptions}, {type: 'label', label: 'is Stationary'}],
        [{type: 'label', label: 'Planet'}, {type: 'select', options: planetOptions}, {type: 'label', label: 'changes Sign'}],
        [{type: 'label', label: 'Planet'}, {type: 'select', options: planetOptions}, {type: 'label', label: 'changes Nakshatra'}],
        [{type: 'label', label: 'Planet'}, {type: 'select', options: planetOptions}, {type: 'label', label: 'Perigee/Apogee'}],
        [{type: 'label', label: 'Planet'}, {type: 'select', options: planetOptions}, {type: 'label', label: 'is Retrograde'}],
        [{type: 'label', label: 'Planet'}, {type: 'select', options: planetOptions}, {type: 'label', label: 'Close To Declination'}, {type: 'select', options: declinationOptions}],
    ];

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="md">
            <DialogTitle sx={{ textAlign: 'center', fontWeight: 'bold', fontSize: '1.5rem' }}>
                Special Planetary Studies
            </DialogTitle>
            <DialogContent>
                <Box sx={{ p: 1 }}>
                    <FormControlLabel 
                        control={<Checkbox checked={highlight} onChange={(e) => setHighlight(e.target.checked)} />} 
                        label="Highlight background"
                    />
                    <RadioGroup
                        row
                        value={displayType || DISPLAY_TYPES[0].value}
                        onChange={e => setDisplayType(e.target.value)}
                        sx={{ mb: 2 }}
                    >
                        {DISPLAY_TYPES.map(opt => (
                            <FormControlLabel key={opt.value} value={opt.value} control={<Radio />} label={opt.label} />
                        ))}
                    </RadioGroup>
                    <Box sx={{ my: 2, display: 'flex', alignItems: 'center', gap: 2 }}>
                        <Typography>Select color:</Typography>
                        <Select defaultValue={colorOptions[0]} size="small">
                            {colorOptions.map(c => <MenuItem key={c} value={c}>{c}</MenuItem>)}
                        </Select>

                    </Box>

                    {studyDefinitions.map((controls, index) => (
                        <StudyRow key={index} controls={controls} />
                    ))}
                </Box>
            </DialogContent>
            <DialogActions sx={{ p: '16px 24px' }}>
                <Button onClick={onClose} variant="contained" sx={{ backgroundColor: 'grey', '&:hover': { backgroundColor: '#5a5a5a' } }}>
                    Close
                </Button>
            </DialogActions>
        </Dialog>
    );
};

SpecialStudiesDialog.propTypes = {
    open: PropTypes.bool.isRequired,
    onClose: PropTypes.func.isRequired,
};

export default SpecialStudiesDialog;
