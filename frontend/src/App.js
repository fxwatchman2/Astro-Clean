import React, { useRef, useState, useEffect, useCallback } from 'react';
import TabPanel from './components/TabPanel';
import EChartComponent from './components/EChartComponent';
import SymbolList from './components/SymbolList';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import { ThemeProvider } from '@mui/material/styles';
import { Button, IconButton, Tooltip, Snackbar } from '@mui/material';
import GroupIcon from '@mui/icons-material/Group';
import CurrentStudyGroupDialog from './components/CurrentStudyGroupDialog';
import { coolWhiteTheme } from './themes';
import LongitudeStudyDlg from './components/LongitudeStudyDlg';
import PlanetFollowStudyDialog from './components/PlanetFollowStudyDialog';
import SpecialStudiesDialog from './components/SpecialStudiesDialog';
import DeclinationStudyDialog from './components/DeclinationStudyDialog';
import PlanetaryLinesDialog from './components/PlanetaryLinesDialog';
import { useChartOverlays } from './context/ChartOverlayContext';
import VisualUniverseDialog from './components/VisualUniverseDialog';

// Snap dates to previous trading day if needed
function snapDatesToTradingDays(dates, tradingDays) {
  // tradingDays must be sorted ascending
  const sortedTradingDays = [...tradingDays].sort();
  return dates.map(orig => {
    // Find the closest trading day <= orig
    const origTime = new Date(orig).getTime();
    let snapped = false;
    let snappedDate = sortedTradingDays[0];
    for (let i = 0; i < sortedTradingDays.length; ++i) {
      const t = new Date(sortedTradingDays[i]).getTime();
      if (t > origTime) break;
      snappedDate = sortedTradingDays[i];
    }
    if (snappedDate !== orig) snapped = true;
    return { snappedDate, originalDate: orig, snapped };
  });
}

// Robustly convert a date string to milliseconds
function dateToMillis(dateStr) {
  // Handles ISO and most common date formats
  return new Date(dateStr).getTime();
}

// Convert a date string to yyyy-MM-dd
function toYMD(s) {
  return new Date(s).toISOString().slice(0, 10);
}

// Global styles


function StatusBar() {
  return (
    <div style={{
      width: '100%',
      height: 32,
      background: '#f3f4f6',
      borderTop: '1px solid #e5e7eb',
      color: '#334155',
      fontSize: 14,
      display: 'flex',
      alignItems: 'center',
      paddingLeft: 16,
      boxSizing: 'border-box',
    }}>
      Status: Ready
    </div>
  );
}

function App() {
  const { addOverlay } = useChartOverlays();
  console.log('[TEST] App.js mounted');
  const [selectedSymbol, setSelectedSymbol] = useState('AAPL');
  const [csgSnackbarOpen, setCsgSnackbarOpen] = useState(false);
  const [csgDialogOpen, setCsgDialogOpen] = useState(false);

  // --- CSG Backend Integration ---
  const [csg, setCsg] = useState({ name: '', studies: [] });

  // Load CSG from backend
  const loadCSG = useCallback(async () => {
    try {
      const res = await fetch('/api/csg');
      if (!res.ok) throw new Error('Failed to load CSG');
      const studies = await res.json();
      setCsg({ name: '', studies });
    } catch (err) {
      setCsg({ name: '', studies: [] });
    }
  }, []);

  // Add study to CSG in backend
  const addStudyToCSG = useCallback(async (study) => {
    try {
      const res = await fetch('/api/csg/add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(study)
      });
      if (res.ok) {
        setCsgSnackbarOpen(true);
        await loadCSG();
      } else {
        const msg = await res.text();
        alert(msg || 'Duplicate study tuple or error');
      }
    } catch (err) {
      alert('Error adding study to CSG');
    }
  }, [loadCSG]);

  // Delete study from CSG by index
  const deleteStudyFromCSG = useCallback(async (idx) => {
    try {
      await fetch(`/api/csg/${idx}`, { method: 'DELETE' });
      await loadCSG();
    } catch (err) {
      alert('Error deleting study from CSG');
    }
  }, [loadCSG]);

  // Clear all studies
  const clearCSG = useCallback(async () => {
    try {
      await fetch('/api/csg/clear', { method: 'DELETE' });
      await loadCSG();
    } catch (err) {
      alert('Error clearing CSG');
    }
  }, [loadCSG]);

  // Open CSG dialog and load from backend
  const handleOpenCSGDialog = () => {
    loadCSG();
    setCsgDialogOpen(true);
  };

  // Run all studies in CSG and display overlays
  const handleRunCSG = useCallback(async () => {
    if (!csg || !csg.studies || csg.studies.length === 0) return;
    try {
      setLoading(true);
      setError(null);
      // POST to the new backend endpoint for CSG
      const csgPayload = { studies: csg.studies, symbol: selectedSymbol };
      console.log('[CSG RUN] Outgoing CSG payload:', JSON.stringify(csgPayload, null, 2));
      const res = await fetch('/api/followstudy/run-csg', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(csgPayload)
      });
      if (!res.ok) throw new Error('Failed to run CSG');
      const responseArray = await res.json(); // Array of ChartDataResponse
      // Merge all drawingInstructions from all studies
      const allInstructions = responseArray.flatMap(r => r.drawingInstructions || []);
      setDrawingInstructions(allInstructions);
      // Optionally scroll to the latest study date
      setInitialScrollDate(null);
    } catch (err) {
      setError(err.message || 'Error running CSG');
    } finally {
      setLoading(false);
    }
  }, [csg, selectedSymbol]);

  const theme = coolWhiteTheme;
  // Chart style state
  const [chartType, setChartType] = useState('bar');

  const handleToggleChartType = () => {
    setChartType(prev => {
      const nextType = prev === 'bar' ? 'line' : 'bar';
      return nextType;
    });
  };

  const [studyDialogOpen, setStudyDialogOpen] = useState(false);
  const [declinationStudyDialogOpen, setDeclinationStudyDialogOpen] = useState(false);
  const [planetFollowStudyDialogOpen, setPlanetFollowStudyDialogOpen] = useState(false);

  const [specialStudiesDialogOpen, setSpecialStudiesDialogOpen] = useState(false);
  const [planetaryLinesDialogOpen, setPlanetaryLinesDialogOpen] = useState(false);
  const [visualUniverseDialogOpen, setVisualUniverseDialogOpen] = useState(false);
  const [drawingInstructions, setDrawingInstructions] = useState([]);

  const [barData, setBarData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [initialScrollDate, setInitialScrollDate] = useState(null);


  // Keep track of the chart ref
  const eChartRef = useRef(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        setError(null);
        setInitialScrollDate(null); // Reset scroll date on new data load
        
        const response = await fetch(`http://localhost:8080/api/getDataForSymbol/${encodeURIComponent(selectedSymbol || 'AAPL')}`);
        
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const apiData = await response.json();
        const data = [...apiData].reverse(); // Reverse data to be oldest-first
        
        if (!data || !Array.isArray(data)) throw new Error('Invalid data format');
        
        const dates = data.map(b => b.date); // ISO strings like "2011-10-06T00:00:00"
        const ohlc = data.map(b => [b.open, b.close, b.low, b.high]);
        const volumes = data.map(b => b.volume);
        
        const newBarData = { dates, ohlc, volumes };
        setBarData(newBarData);
        // Save true earliest and latest chart dates
        // Use helper for min/max dates
        // No longer set earliest/latest chart date state; logic is stateless now.

      } catch (e) {
        setError(e.toString());
        setBarData(null); // Clear old data on error
      } finally {
        setLoading(false);
      }
    };
    
    fetchData();
  }, [selectedSymbol]);


  const handleRunSingleStudy = useCallback(async (study) => {
    if (!study) return;

    setLoading(true);
    setError(null);

    try {
      // Special handling for longitudinalDistance study
      if (study.type === 'longitudinalDistance' && barData && Array.isArray(barData.dates) && barData.dates.length > 0) {
        // Get oldest and newest dates
        // Use saved earliest and latest chart dates for backend calls
        // Bulletproof: always compute min/max from barData.dates
        const dates = barData.dates;
        let minMillis = Infinity, maxMillis = -Infinity;
        let minDate = null, maxDate = null;
        for (const d of dates) {
          const ms = dateToMillis(d);
          if (ms < minMillis) { minMillis = ms; minDate = d; }
          if (ms > maxMillis) { maxMillis = ms; maxDate = d; }
        }
        // Use only the chart's true start and end dates
        const dateA = new Date(minDate);
        const dateB = new Date(maxDate);
        const fromDateObj = (dateA < dateB) ? dateA : dateB;
        const toDateObj = (dateA > dateB) ? dateA : dateB;
        const fromDate = toYMD(fromDateObj);
        const toDate = toYMD(toDateObj);

        // Prepare params
        const params = new URLSearchParams({
          planet1: study.planet1,
          planet2: study.planet2,
          degrees: study.distance,
          fromDate,
          toDate
        });
        // Call backend
        const response = await fetch(`http://localhost:8080/api/followstudy/dates?${params.toString()}`);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        const result = await response.json();
        // For now: log results, or set state for chart markers as needed
        console.log('[LongitudeStudy] Dates returned:', result);
        // Snap dates to previous trading day if needed
        const snappedDates = snapDatesToTradingDays(result, barData.dates);
        // Draw the user-selected shape for every study date
        // For snapped dates, also draw a triangle marker at the bottom of the bar
        const drawingInstructions = [];
        snappedDates.forEach(({snappedDate, originalDate, snapped}) => {
          // Main shape (as selected by user)
          let shapeType = 'vertical-line';
          if (study.displayType === 'Box') shapeType = 'box';
          else if (study.displayType === 'Dot') shapeType = 'dot';
          // fallback is vertical-line
          drawingInstructions.push({
            style: {
              type: shapeType,
              color: study.color || '#8e44ad',
            },
            params: {
              dateTime: snappedDate,
            },
            meta: {
              tooltip: `${study.planet1} / ${study.planet2} @ ${originalDate}` + (snapped ? ' (snapped)' : ''),
              snapped,
              originalDate
            }
          });
          // If snapped, add a triangle marker
          if (snapped) {
            drawingInstructions.push({
              style: {
                type: 'triangle-marker',
                color: 'orange',
                size: 12,
                position: 'bottom',
              },
              params: {
                dateTime: snappedDate,
              },
              meta: {
                tooltip: `Snapped to previous trading day: ${snappedDate}`,
                snapped,
                originalDate
              }
            });
          }
        });
        setDrawingInstructions(drawingInstructions);
        setLoading(false);
        return;
      }

      // Default: old study logic
      const payload = {
        name: "Single Study Run",
        studies: [study],
        symbol: selectedSymbol
      };
      console.log('[SINGLE STUDY RUN] Outgoing study payload:', JSON.stringify(payload, null, 2));

      const res = await fetch('http://localhost:8080/api/followstudy/aspects', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (!res.ok) {
        const errorText = await res.text();
        throw new Error(`Failed to run study: ${errorText}`);
      }

      const responseData = await res.json();

      // If the study returns new chart data, use it.
      // Otherwise, create a new object reference for the existing barData to force a re-render.
      setBarData(currentBarData => responseData.ohlcvData ? responseData.ohlcvData : { ...currentBarData });

      // Always update drawing instructions. If the response doesn't have them, clear them.
      setDrawingInstructions(responseData.drawingInstructions || []);

    } catch (err) {
      // console.error(`[App.js] Failed to run single study:`, err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [selectedSymbol]);

  return (
    <ThemeProvider theme={theme}>
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh', overflow: 'hidden' }}>
        {/* AppBar Header */}
        <AppBar position="static" color="primary" elevation={2} sx={{ zIndex: 1201 }}>
          <Toolbar>
            <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
              {`AstroStockCharts - ${selectedSymbol}`}
            </Typography>
          </Toolbar>
        </AppBar>

        {/* Main Content Area */}
        <Box sx={{ display: 'flex', flexDirection: 'column', flexGrow: 1, overflow: 'hidden' }}>
          {/* Top Toolbar */}
          <Paper elevation={2} sx={{ borderRadius: 0, borderBottom: '1px solid #ddd', zIndex: 10 }}>
            <TabPanel
              onOpenControlsDialog={() => setStudyDialogOpen(true)}
              onOpenPlanetFollowStudyDialog={() => setPlanetFollowStudyDialogOpen(true)}
              onOpenSpecialStudiesDialog={() => setSpecialStudiesDialogOpen(true)}
              onOpenDeclinationStudyDialog={() => setDeclinationStudyDialogOpen(true)}
              onOpenPlanetaryLinesDialog={() => setPlanetaryLinesDialogOpen(true)}
              onOpenVisualUniverseDialog={() => setVisualUniverseDialogOpen(true)}
              chartType={chartType}
              onToggleChartType={handleToggleChartType}
            />
          </Paper>
          {/* Content below toolbar */}
          <Box sx={{ display: 'flex', flexGrow: 1, overflow: 'hidden' }}>
            {/* Center Panel: Chart Area */}
            <Box sx={{ flexGrow: 1, display: 'flex', flexDirection: 'column', overflow: 'hidden', position: 'relative' }}>
              {/* Main Chart Scrolling Buttons & Toolbar Icons */}
              <Box sx={{ position: 'absolute', top: 0, right: 16, zIndex: 10, p: 0.5, display: 'flex', gap: 1, alignItems: 'center' }}>
                <Button variant="contained" size="small" onClick={() => eChartRef.current?.handlePage('oldest')}>Oldest</Button>
                <Button variant="contained" size="small" onClick={() => eChartRef.current?.handlePage('next')}>Next &gt;</Button>
                <Button variant="contained" size="small" onClick={() => eChartRef.current?.handlePage('prev')}>&lt; Prev</Button>
                <Button variant="contained" size="small" onClick={() => eChartRef.current?.handlePage('latest')}>Latest</Button>
                {/* Toolbar Icon for SSG Dialog */}
                <Tooltip title="Open Current Study Group">
                  <IconButton color="primary" onClick={handleOpenCSGDialog} size="large" data-testid="csg-toolbar-icon">
                    <GroupIcon />
                  </IconButton>
                </Tooltip>
              </Box>
              {/* Chart Component */}
              <Box sx={{ flexGrow: 1, height: '100%', width: '100%', position: 'relative' }}>
                {loading && (
                  <Box sx={{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0, display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 5, backgroundColor: 'rgba(255, 255, 255, 0.5)' }}>
                    <Typography>Loading chart data...</Typography>
                  </Box>
                )}
                {error && (
                  <Box sx={{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0, display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 5, backgroundColor: 'rgba(255, 255, 255, 0.5)' }}>
                    <Typography color="error">Error: {error}</Typography>
                  </Box>
                )}
                {!loading && !error && (
                  <EChartComponent
                    ref={eChartRef}
                    symbol={selectedSymbol}
                    barData={barData}
                    loading={loading}
                    error={error}
                    drawingInstructions={drawingInstructions}
                    initialScrollDate={initialScrollDate}
                    theme={theme.palette.mode}
                    chartType={chartType}
                  />
                )}
              </Box>
            </Box>

            {/* Right Panel: Symbol List */}
            <Paper elevation={3} sx={{ width: 120, minWidth: 120, display: 'flex', flexDirection: 'column', p: 1, overflowY: 'auto', borderLeft: '1px solid #ddd' }}>
              <SymbolList selectedSymbol={selectedSymbol} onSelectSymbol={setSelectedSymbol} />
            </Paper>
          </Box>
        </Box>
      </Box>
      {/* END MAIN LAYOUT BOX */}

    {/* Status Bar */}
    <StatusBar />

    {/* CSG Dialog */}
    <CurrentStudyGroupDialog
      open={csgDialogOpen}
      onClose={() => setCsgDialogOpen(false)}
      csg={csg}
      onDeleteStudy={deleteStudyFromCSG}
      onSaveCSG={clearCSG}
      onRunCSG={handleRunCSG}
    />

    {/* Dialogs */}
    <LongitudeStudyDlg 
      open={studyDialogOpen}
      onClose={() => setStudyDialogOpen(false)}
      onRun={handleRunSingleStudy}
      onAddToCSG={addStudyToCSG}
    />
    {/* Snackbar for CSG addition */}
    <Snackbar
      open={csgSnackbarOpen}
      autoHideDuration={2000}
      onClose={() => setCsgSnackbarOpen(false)}
      message="Study added to Current Study Group!"
      anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
    />
    <PlanetFollowStudyDialog
      open={planetFollowStudyDialogOpen}
      onClose={() => setPlanetFollowStudyDialogOpen(false)}
      onRun={handleRunSingleStudy}
      barData={barData}
    />
    <SpecialStudiesDialog 
      open={specialStudiesDialogOpen} 
      onClose={() => setSpecialStudiesDialogOpen(false)} 
    />
    <DeclinationStudyDialog 
      open={declinationStudyDialogOpen}
      onClose={() => setDeclinationStudyDialogOpen(false)}
      onRun={handleRunSingleStudy}
    />
    <PlanetaryLinesDialog 
      open={planetaryLinesDialogOpen}
      onClose={() => setPlanetaryLinesDialogOpen(false)}
      barData={barData}
      onRun={async ({ planet, color, type, thickness, factor }) => {
        if (!barData || !barData.dates || !barData.ohlc) {
          alert('Chart data unavailable');
          return;
        }
        // Compute lowest/highest prices from ohlc
        let lowest = Infinity, highest = -Infinity;
        barData.ohlc.forEach(([/*o*/, h, l, /*c*/]) => {
          if (h > highest) highest = h;
          if (l < lowest) lowest = l;
        });
        const payload = {
          planet,    // user selection
          color,     // user selection
          type,      // user selection
          thickness, // user selection
          factor,    // user selection
          dates: barData.dates,
          lowest,
          highest
        };
        try {
          const resp = await fetch('/api/followstudy/getPlanetaryLines', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
          });
          if (!resp.ok) throw new Error(`HTTP ${resp.status}`);
          const data = await resp.json();
          // All debug logging commented out for production cleanliness
          // console.log('[PlanetaryLinesDialog] Payload sent to backend:', payload);
          // console.log('[PlanetaryLinesDialog] Backend response:', data);
          if (Array.isArray(data)) {
            data.forEach((overlay, idx) => {
              const overlayObj = {
                id: `planetary-${overlay.planet || planet}-${Date.now()}-${idx}`,
                type: 'planetaryLines',
                planet: overlay.planet,
                points: overlay.points,
                color,           // user selection
                lineStyle: type, // user selection
                thickness,       // user selection
                factor,          // user selection
                dates: barData.dates
              };
              // console.log('[PlanetaryLinesDialog] Adding overlay to context:', overlayObj);
              addOverlay(overlayObj);
            });
          } else {
            // console.warn('[PlanetaryLinesDialog] Unexpected backend response format:', data);
          }
        } catch (e) {
          alert('Failed to get planetary lines: ' + e.message);
        }
      }}
    />
    <VisualUniverseDialog
      open={visualUniverseDialogOpen}
      onClose={() => setVisualUniverseDialogOpen(false)}
    />
  </ThemeProvider>
  );
}


export default App;
