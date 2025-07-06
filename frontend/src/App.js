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
import { Button, Tooltip, Snackbar } from '@mui/material';
import GroupIcon from '@mui/icons-material/Group';
import CurrentStudyGroupDialog from './components/CurrentStudyGroupDialog';
import { coolWhiteTheme } from './themes';
import LongitudeStudyDlg from './components/LongitudeStudyDlg';
import PlanetFollowStudyDialog from './components/PlanetFollowStudyDialog';
import SpecialStudiesDialog from './components/SpecialStudiesDialog';
import DeclinationStudyDialog from './components/DeclinationStudyDialog';
import PlanetaryLinesDialog from './components/PlanetaryLinesDialog';
import { useChartOverlays } from './context/ChartOverlayContext';
import PlanetGroupDialog from './components/PlanetGroupDialog';
import VisualUniverseDialog from './components/VisualUniverseDialog';
import IconButton from '@mui/material/IconButton';
import StarIcon from '@mui/icons-material/Star';



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
  const eChartRef = useRef();
  const [barData, setBarData] = useState({ ohlc: [], dates: [], volumes: [] });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedSymbol, setSelectedSymbol] = useState('AAPL');
  const [drawingInstructions, setDrawingInstructions] = useState([]);
  const [initialScrollDate, setInitialScrollDate] = useState(null);
  
  // Dialog states
  const [isStudyDialogOpen, setStudyDialogOpen] = useState(false);
  const [isPlanetFollowStudyDialogOpen, setPlanetFollowStudyDialogOpen] = useState(false);
  const [isSpecialStudiesDialogOpen, setSpecialStudiesDialogOpen] = useState(false);
  const [isDeclinationStudyDialogOpen, setDeclinationStudyDialogOpen] = useState(false);
  const [isPlanetaryLinesDialogOpen, setPlanetaryLinesDialogOpen] = useState(false);
  const [isVisualUniverseDialogOpen, setVisualUniverseDialogOpen] = useState(false);
  const [isPlanetGroupDialogOpen, setPlanetGroupDialogOpen] = useState(false);
  const [isCSGDialogOpen, setCSGDialogOpen] = useState(false);
  
  const [chartType, setChartType] = useState('candlestick');
  const { addOverlay, clearOverlays } = useChartOverlays();
  const theme = coolWhiteTheme;

  // CSG State
  const [csg, setCsg] = useState({ name: '', studies: [] });
  const [csgSnackbarOpen, setCsgSnackbarOpen] = useState(false);

  const handleToggleChartType = () => {
    setChartType(prevType => prevType === 'candlestick' ? 'line' : 'candlestick');
  };

  // --- CSG Backend Integration ---
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

  const deleteStudyFromCSG = useCallback(async (idx) => {
    try {
      await fetch(`/api/csg/${idx}`, { method: 'DELETE' });
      await loadCSG();
    } catch (err) {
      alert('Error deleting study from CSG');
    }
  }, [loadCSG]);

  const clearCSG = useCallback(async () => {
    try {
      await fetch('/api/csg/clear', { method: 'DELETE' });
      await loadCSG();
    } catch (err) {
      alert('Error clearing CSG');
    }
  }, [loadCSG]);

  const handleOpenCSGDialog = () => {
    loadCSG();
    setCSGDialogOpen(true);
  };

  const handleRunCSG = useCallback(async () => {
    if (!csg || !csg.studies || csg.studies.length === 0) return;
    try {
      setLoading(true);
      setError(null);
      const csgPayload = { studies: csg.studies, symbol: selectedSymbol };
      console.log('[CSG RUN] Outgoing CSG payload:', JSON.stringify(csgPayload, null, 2));
      const res = await fetch('/api/followstudy/run-csg', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(csgPayload)
      });
      if (!res.ok) throw new Error('Failed to run CSG');
      const responseArray = await res.json();
      const allInstructions = responseArray.flatMap(r => r.drawingInstructions || []);
      setDrawingInstructions(allInstructions);
    } catch (err) {
      setError(err.message || 'Error running CSG');
    } finally {
      setLoading(false);
    }
  }, [csg, selectedSymbol]);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setError(null);
      try {
        const res = await fetch(`/api/getDataForSymbol/${selectedSymbol}`);
        if (!res.ok) {
          throw new Error('Network response was not ok');
        }
        const rawData = await res.json();
        if (!Array.isArray(rawData)) {
          throw new Error('Data is not an array');
        }

        const sortedData = rawData.sort((a, b) => new Date(a.date) - new Date(b.date));

        const formattedData = {
          symbol: selectedSymbol,
          dates: sortedData.map(item => item.date),
          ohlc: sortedData.map(item => [item.open, item.close, item.low, item.high]),
          volumes: sortedData.map(item => item.volume),
        };

        setBarData(formattedData);
        setInitialScrollDate(formattedData.dates.length > 0 ? formattedData.dates[formattedData.dates.length - 1] : null);
      } catch (err) {
        setError(err.message);
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
      if ((study.type === 'longitudinalDistance' || study.type === 'longitudinal') && barData && Array.isArray(barData.dates) && barData.dates.length > 0) {
        const datesArr = barData.dates;
        const startDate = datesArr[0] || '';
        const endDate = datesArr[datesArr.length - 1] || '';

        const params = new URLSearchParams({
          planet1: study.planet1,
          planet2: study.planet2,
          degrees: study.angle,
          fromDate: startDate,
          toDate: endDate,
          shapeType: study.displayType,
          color: study.color,
          thickness: study.thickness || 'medium'
        });

        console.log('[App.js] Sending /api/followstudy/dates GET with params:', params.toString());
        const res = await fetch(`/api/followstudy/dates?${params.toString()}`);

        if (!res.ok) {
          const errorText = await res.text();
          throw new Error(`Failed to fetch longitude dates: ${errorText}`);
        }
        
        const dates = await res.json();
        console.log('[App.js] Received longitude-dates response:', dates);

        if (Array.isArray(dates)) {
          clearOverlays();
          // The response is an array of date strings.
          // We need to map them to overlay objects using the original study parameters.
          dates.forEach(dateStr => {
            const overlay = {
              id: `overlay-${study.planet1}-${study.planet2}-${dateStr}-${Math.random()}`,
              type: 'longitudinal', // This must match the filter in EChartComponent
              date: dateStr,
              planet: study.planet2, // For consistency, though name is more descriptive
              planetName: `${study.planet1}/${study.planet2}`,
              degrees: study.angle,
              color: study.color,
              thickness: study.thickness
            };
            addOverlay(overlay);
          });
        }
      } else {
        const payload = {
          name: "Single Study Run",
          studies: [study],
          symbol: selectedSymbol
        };
        console.log('[SINGLE STUDY RUN] Outgoing study payload:', JSON.stringify(payload, null, 2));

        const res = await fetch('/api/followstudy/aspects', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(payload)
        });

        if (!res.ok) {
            const errorText = await res.text();
            throw new Error(`Failed to run study: ${errorText || res.statusText}`);
        }

        const responseData = await res.json();
        if (responseData && responseData.drawingInstructions) {
            setDrawingInstructions(responseData.drawingInstructions);
        } else {
            console.warn("Study ran but returned no drawing instructions.");
            setDrawingInstructions([]);
        }
      }
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [selectedSymbol, barData, addOverlay, clearOverlays]);

  const handleRunPlanetaryLines = useCallback(async ({ planet, color, type, thickness, factor }) => {
    if (!barData || !barData.dates || !barData.ohlc) {
      alert('Chart data unavailable');
      return;
    }
    let lowest = Infinity, highest = -Infinity;
        barData.ohlc.forEach(([, h, l]) => {
      if (h > highest) highest = h;
      if (l < lowest) lowest = l;
    });
    const payload = {
      planet, color, type, thickness, factor,
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
      if (Array.isArray(data)) {
        data.forEach((overlay, idx) => {
          const overlayObj = {
            id: `planetary-${overlay.planet || planet}-${Date.now()}-${idx}`,
            type: 'planetaryLines',
            planet: overlay.planet,
            points: overlay.points,
            color, lineStyle: type, thickness, factor,
            dates: barData.dates
          };
          addOverlay(overlayObj);
        }); 
      } 
    } catch (err) {
      console.error('[PlanetaryLinesDialog] Error fetching planetary lines:', err);
    }
  }, [barData, addOverlay]);

  return (
    <ThemeProvider theme={theme}>
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh', overflow: 'hidden' }}>
        <AppBar position="static" color="primary" elevation={2} sx={{ zIndex: 1201 }}>
          <Toolbar>
            <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
              {`AstroStockCharts - ${selectedSymbol}`}
            </Typography>
            <IconButton color="primary" aria-label="open planet group dialog" onClick={() => setPlanetGroupDialogOpen(true)}>
              <StarIcon />
            </IconButton>
          </Toolbar>
        </AppBar>

        <Box sx={{ display: 'flex', flexDirection: 'column', flexGrow: 1, overflow: 'hidden' }}>
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
          <Box sx={{ display: 'flex', flexGrow: 1, overflow: 'hidden' }}>
            <Box sx={{ flexGrow: 1, display: 'flex', flexDirection: 'column', overflow: 'hidden', position: 'relative' }}>
              <Box sx={{ position: 'absolute', top: 0, right: 16, zIndex: 10, p: 0.5, display: 'flex', gap: 1, alignItems: 'center' }}>
                <Tooltip title="Open Special Planetary Studies">
                  <IconButton color="primary" onClick={() => setPlanetGroupDialogOpen(true)} size="large" data-testid="planet-group-toolbar-icon">
                    <StarIcon />
                  </IconButton>
                </Tooltip>
                <Button variant="contained" size="small" onClick={() => eChartRef.current?.handlePage('oldest')}>Oldest</Button>
                <Button variant="contained" size="small" onClick={() => eChartRef.current?.handlePage('next')}>Next &gt;</Button>
                <Button variant="contained" size="small" onClick={() => eChartRef.current?.handlePage('prev')}>&lt; Prev</Button>
                <Button variant="contained" size="small" onClick={() => eChartRef.current?.handlePage('latest')}>Latest</Button>
                <Tooltip title="Open Current Study Group">
                  <IconButton color="primary" onClick={handleOpenCSGDialog} size="large" data-testid="csg-toolbar-icon">
                    <GroupIcon />
                  </IconButton>
                </Tooltip>
              </Box>
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
                {!loading && !error && barData && barData.dates.length > 0 && (
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

            <Paper elevation={3} sx={{ width: 120, minWidth: 120, display: 'flex', flexDirection: 'column', p: 1, overflowY: 'auto', borderLeft: '1px solid #ddd' }}>
              <SymbolList selectedSymbol={selectedSymbol} onSelectSymbol={setSelectedSymbol} />
            </Paper>
          </Box>
        </Box>
      </Box>

      <StatusBar />

      <CurrentStudyGroupDialog
        open={isCSGDialogOpen}
        onClose={() => setCSGDialogOpen(false)}
        csg={csg}
        onDeleteStudy={deleteStudyFromCSG}
        onClearStudies={clearCSG}
        onRunStudies={handleRunCSG}
      />
      
      <LongitudeStudyDlg
        open={isStudyDialogOpen}
        onClose={() => setStudyDialogOpen(false)}
        onRun={handleRunSingleStudy}
        onAddToCSG={addStudyToCSG}
      />
      <PlanetFollowStudyDialog
        open={isPlanetFollowStudyDialogOpen}
        onClose={() => setPlanetFollowStudyDialogOpen(false)}
        onRun={handleRunSingleStudy}
      />
      <SpecialStudiesDialog
        open={isSpecialStudiesDialogOpen}
        onClose={() => setSpecialStudiesDialogOpen(false)}
        onRun={handleRunSingleStudy}
      />
      <DeclinationStudyDialog
        open={isDeclinationStudyDialogOpen}
        onClose={() => setDeclinationStudyDialogOpen(false)}
        onRun={handleRunSingleStudy}
      />
      <PlanetaryLinesDialog
        open={isPlanetaryLinesDialogOpen}
        onClose={() => setPlanetaryLinesDialogOpen(false)}
        barData={barData}
        onRun={handleRunPlanetaryLines}
      />
      <PlanetGroupDialog
        open={isPlanetGroupDialogOpen}
        onClose={() => setPlanetGroupDialogOpen(false)}
      />
      <VisualUniverseDialog
        open={isVisualUniverseDialogOpen}
        onClose={() => setVisualUniverseDialogOpen(false)}
      />
      
      <Snackbar
        open={csgSnackbarOpen}
        autoHideDuration={3000}
        onClose={() => setCsgSnackbarOpen(false)}
        message="Study added to Current Study Group"
      />
    </ThemeProvider>
  );
}

export default App;

