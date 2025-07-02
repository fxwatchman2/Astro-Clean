import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { Dialog, DialogTitle, DialogContent, Button, Box, Typography, TextField, Select, MenuItem, FormControl, FormControlLabel, Checkbox } from '@mui/material';


// --- ChartSVG: Top-level component for chart rendering ---
const ChartSVG = ({ showBackground = true }) => {
    const width = 2916;
    const height = 1026;
    const topLeft = { x: 0, y: 0 };
    const topRight = { x: width, y: 0 };
    const bottomLeft = { x: 0, y: height };
    const bottomRight = { x: width, y: height };
    const topCenter = { x: width / 2, y: 0 };
    const rightCenter = { x: width, y: height / 2 };
    const bottomCenter = { x: width / 2, y: height };
    const leftCenter = { x: 0, y: height / 2 };
    const center = { x: width / 2, y: height / 2 };
    // Define sign positions
    const signPositions = [
      { sign: 'ARIES',       x: width * 0.45, y: height * 0.25 },
      { sign: 'TAURUS',      x: width * 0.20, y: height * 0.10 },
      { sign: 'GEMINI',      x: width * 0.05, y: height * 0.25 },
      { sign: 'CANCER',      x: width * 0.20, y: height * 0.50 },
      { sign: 'LEO',         x: width * 0.05, y: height * 0.75 },
      { sign: 'VIRGO',       x: width * 0.20, y: height * 0.90 },
      { sign: 'LIBRA',       x: width * 0.45, y: height * 0.75 },
      { sign: 'SCORPIO',     x: width * 0.70, y: height * 0.90 },
      { sign: 'SAGITTARIUS', x: width * 0.85 - 1.5 * 72, y: height * 0.75 },
      { sign: 'CAPRICORN',   x: width * 0.65, y: height * 0.50 },
      { sign: 'AQUARIUS',    x: width * 0.85, y: height * 0.25 },
      { sign: 'PISCES',      x: width * 0.70, y: height * 0.10 },
    ];
    const writeText = (x, y, text, isSignName = false) => (
      <text
        key={`text-${x}-${y}-${text}`}
        x={width * x}
        y={height * y}
        textAnchor="start"
        dominantBaseline="hanging"
        style={{
          fontSize: isSignName ? '72px' : '20px',
          fontWeight: isSignName ? 'bold' : 'normal',
          fill: isSignName ? '#c4e3ff' : '#555555',
          fontFamily: 'Arial, sans-serif'
        }}
      >
        {text}
      </text>
    );
    const houseBoundingBoxes = {
      1: { x: 0.35, y: 0.08, w: 0.3, h: 0.18 },
      2: { x: 0.08, y: 0.02, w: 0.18, h: 0.20 },
      3: { x: 0.01, y: 0.22, w: 0.12, h: 0.20 },
      4: { x: 0.03, y: 0.44, w: 0.17, h: 0.20 },
      5: { x: 0.01, y: 0.68, w: 0.12, h: 0.20 },
      6: { x: 0.08, y: 0.80, w: 0.18, h: 0.18 },
      7: { x: 0.35, y: 0.75, w: 0.3, h: 0.18 },
      8: { x: 0.58, y: 0.80, w: 0.18, h: 0.18 },
      9: { x: 0.77, y: 0.68, w: 0.12, h: 0.20 },
      10: { x: 0.70, y: 0.44, w: 0.17, h: 0.20 },
      11: { x: 0.77, y: 0.22, w: 0.12, h: 0.20 },
      12: { x: 0.58, y: 0.02, w: 0.18, h: 0.20 },
    };
    const renderHouseText = () => null;
    const textElements = [
      ...signPositions.map(item => writeText(item.x/width, item.y/height, item.sign, true)),
      ...Object.keys(houseBoundingBoxes).map(houseNum => renderHouseText(houseNum)),
    ];
    const houses = [
      { path: `M ${topCenter.x},${topCenter.y} L ${rightCenter.x},${rightCenter.y} L ${center.x},${center.y} Z`, fill: '#f0f8ff' },
      { path: `M ${topLeft.x},${topLeft.y} L ${topCenter.x},${topCenter.y} L ${center.x},${center.y} Z`, fill: '#e6e6fa' },
      { path: `M ${topLeft.x},${topLeft.y} L ${leftCenter.x},${leftCenter.y} L ${center.x},${center.y} Z`, fill: '#e0ffff' },
      { path: `M ${leftCenter.x},${leftCenter.y} L ${bottomLeft.x},${bottomLeft.y} L ${center.x},${center.y} Z`, fill: '#f0fff0' },
      { path: `M ${bottomLeft.x},${bottomLeft.y} L ${bottomCenter.x},${bottomCenter.y} L ${center.x},${center.y} Z`, fill: '#fff0f5' },
      { path: `M ${bottomCenter.x},${bottomCenter.y} L ${bottomRight.x},${bottomRight.y} L ${center.x},${center.y} Z`, fill: '#f5fffa' },
      { path: `M ${bottomRight.x},${bottomRight.y} L ${rightCenter.x},${rightCenter.y} L ${center.x},${center.y} Z`, fill: '#f0f0ff' },
      { path: `M ${rightCenter.x},${rightCenter.y} L ${topRight.x},${topRight.y} L ${center.x},${center.y} Z`, fill: '#fffaf0' },
      { path: `M ${topRight.x},${topRight.y} L ${topCenter.x},${topCenter.y} L ${center.x},${center.y} Z`, fill: '#f5f5f5' },
      { path: `M ${topCenter.x},${topCenter.y} L ${rightCenter.x},${rightCenter.y} L ${bottomCenter.x},${bottomCenter.y} L ${leftCenter.x},${leftCenter.y} Z`, fill: '#f0ffff' },
      { path: `M ${rightCenter.x},${rightCenter.y} L ${bottomRight.x},${bottomRight.y} L ${center.x},${center.y} Z`, fill: '#fff5ee' },
      { path: `M ${topRight.x},${topRight.y} L ${rightCenter.x},${rightCenter.y} L ${center.x},${center.y} Z`, fill: '#f0ffe0' },
    ];
    return (
      <svg
        viewBox={`0 0 ${width} ${height}`}
        preserveAspectRatio="xMidYMid meet"
        style={{ width: '100%', height: '100%' }}
      >
        {showBackground && houses.map((house, index) => (
          <path 
            key={`house-${index}`} 
            d={house.path} 
            fill={house.fill} 
            stroke="none"
          />
        ))}
        <rect x="0" y="0" width={width} height={height} fill="none" stroke="black" strokeWidth="2" />
        <line x1="0" y1="0" x2="0" y2={height} stroke="black" strokeWidth="4" />
        <line x1={width} y1="0" x2={width} y2={height} stroke="black" strokeWidth="4" />
        <line x1={topLeft.x} y1={topLeft.y} x2={bottomRight.x} y2={bottomRight.y} stroke="black" strokeWidth="2" />
        <line x1={topRight.x} y1={topRight.y} x2={bottomLeft.x} y2={bottomLeft.y} stroke="black" strokeWidth="2" />
        <line x1={topCenter.x} y1={topCenter.y} x2={rightCenter.x} y2={rightCenter.y} stroke="black" strokeWidth="2" />
        <line x1={rightCenter.x} y1={rightCenter.y} x2={bottomCenter.x} y2={bottomCenter.y} stroke="black" strokeWidth="2" />
        <line x1={bottomCenter.x} y1={bottomCenter.y} x2={leftCenter.x} y2={leftCenter.y} stroke="black" strokeWidth="2" />
        <line x1={leftCenter.x} y1={leftCenter.y} x2={topCenter.x} y2={topCenter.y} stroke="black" strokeWidth="2" />
        <g>
          {textElements}
        </g>
      </svg>
    );
};

const VisualUniverseDialog = ({ open, onClose }) => {
  // State for bottom status text and pause/continue toggle
  const [statusText] = useState('');
  const [isPaused, setIsPaused] = useState(false);

  // State for background visibility
  const [showBackground, setShowBackground] = useState(true);

  const [rate, setRate] = useState(1);
  const [unit, setUnit] = useState(2);



  return (
    <Dialog
      open={open}
      onClose={onClose}
      PaperProps={{
        sx: {
          height: '95vh',
          width: 'calc((95vh - 175px) * 2916 / 1026)',
          maxWidth: '95vw',
        },
      }}
    >
      <>

        <Box sx={{ width: 'calc((95vh - 175px) * 2916 / 1026)', maxWidth: '95vw', mx: 'auto', display: 'flex', alignItems: 'center', mt: 1, mb: 1, px: 0, pr: 3 }}>
          <DialogTitle sx={{ m: 0, p: 0, flex: 'none', minWidth: 0, fontSize: 28, lineHeight: 1.2 }}>Visual Universe</DialogTitle>
          <Box sx={{ flexGrow: 1 }} />
          <TextField
            label="Current Date"
            value={(() => {
              const now = new Date();
              const mm = String(now.getMonth() + 1).padStart(2, '0');
              const dd = String(now.getDate()).padStart(2, '0');
              const yyyy = now.getFullYear();
              return `${mm}-${dd}-${yyyy}`;
            })()}
            variant="outlined"
            size="small"
            InputProps={{ readOnly: true }}
            sx={{ width: 150, background: '#fff', ml: 2, mr: 2 }}
          />
          <FormControlLabel
            control={<Checkbox checked={showBackground} onChange={() => setShowBackground(!showBackground)} />}
            label="Show Background"
            sx={{ ml: 2 }}
          />
        </Box>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', p: 0, overflow: 'hidden' }}>
          <Box sx={{ flexGrow: 1, minHeight: 0, display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
            <ChartSVG showBackground={showBackground} />
          </Box>
          {/* Bottom margin: Read-only text field and Pause/Continue toggle */}
          <Box sx={{ width: 'calc((95vh - 175px) * 2916 / 1026 - 24px)', maxWidth: '95vw', mx: 'auto', p: 2, borderTop: '1px solid rgba(0, 0, 0, 0.12)', display: 'flex', alignItems: 'center', flexWrap: 'nowrap' }}>
            <Button onClick={onClose} sx={{ backgroundColor: 'grey.500', '&:hover': { backgroundColor: 'grey.700' }, color: 'white', minWidth: 80, mr: 2 }}>CLOSE</Button>
            <TextField
              label="Status Message"
              value={statusText}
              variant="outlined"
              size="small"
              InputProps={{ readOnly: true }}
              sx={{ flex: 1, minWidth: 300, background: '#f8f8f8', mr: 0 }}
            />
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5, ml: 'auto', mr: 2 }}>
              <Button variant="contained" onClick={() => setIsPaused(prev => !prev)} sx={{ minWidth: 100, ml: 2 }}>
                {isPaused ? 'Continue' : 'Pause'}
              </Button>
              <Typography variant="body2" sx={{ fontWeight: 'bold', ml: 2 }}>From Date:</Typography>
              <TextField variant="outlined" size="small" placeholder="MM-DD-YYYY" sx={{ width: 110 }} />
              <Typography variant="body2" sx={{ fontWeight: 'bold', ml: 2 }}>To Date:</Typography>
              <TextField variant="outlined" size="small" placeholder="MM-DD-YYYY" sx={{ width: 110 }} />
              <Typography variant="body2" sx={{ fontWeight: 'bold', ml: 2 }}>Rate of change (s):</Typography>
              <FormControl size="small">
                <Select value={rate} onChange={(e) => setRate(e.target.value)} sx={{ width: 60 }}>
                  <MenuItem value={1}>1</MenuItem>
                  <MenuItem value={2}>2</MenuItem>
                  <MenuItem value={5}>5</MenuItem>
                  <MenuItem value={10}>10</MenuItem>
                </Select>
              </FormControl>
              <Typography variant="body2" sx={{ fontWeight: 'bold', ml: 2 }}>Change Unit:</Typography>
              <FormControl size="small">
                <Select value={unit} onChange={(e) => setUnit(e.target.value)} sx={{ width: 120 }}>
                  <MenuItem value={2}>2 hours</MenuItem>
                  <MenuItem value={8}>8 hours</MenuItem>
                  <MenuItem value={16}>16 hours</MenuItem>
                  <MenuItem value={24}>24 hours</MenuItem>
                  <MenuItem value={168}>168 hours</MenuItem>
                </Select>
              </FormControl>
            </Box>
            <Button variant="contained" sx={{ minWidth: 80 }}>START</Button>
          </Box>
        </DialogContent>
      </>
    </Dialog>
  );
}

ChartSVG.propTypes = {
  showBackground: PropTypes.bool.isRequired
};
ChartSVG.defaultProps = {
  showBackground: true
};

VisualUniverseDialog.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
};

export default VisualUniverseDialog;
