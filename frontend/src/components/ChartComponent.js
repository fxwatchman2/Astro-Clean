import React, { useRef, useEffect, useState } from 'react';
import { createChart, CrosshairMode } from 'lightweight-charts';

import BarChartIcon from '@mui/icons-material/BarChart';
import ShowChartIcon from '@mui/icons-material/ShowChart';
import Tooltip from '@mui/material/Tooltip';
import CurrentStudyGroupDialog from './CurrentStudyGroupDialog';
import useCurrentStudyGroup from './useCurrentStudyGroup';
let oppositionIcon;
try {
  oppositionIcon = require('./icons/pl-opp.svg');
} catch (e) {
  oppositionIcon = null;
}

const ChartComponent = (props) => {
  // State for CSG dialog
  const [csgDialogOpen, setCsgDialogOpen] = useState(false);
  const [csg, setCSG, addStudy, removeStudy] = useCurrentStudyGroup ? useCurrentStudyGroup() : [
    { studies: [] },
    () => {},
    () => {},
    () => {}
  ];
  // console.log('[ChartComponent] Render. props.chartType:', props.chartType);
  // --- State, refs, and hooks ---
  const chartContainerRef = useRef();
  const chartRef = useRef();
  const candleSeriesRef = useRef();
  const volumeSeriesRef = useRef();
  const barsRef = useRef([]); // NEW: Store bar data here
  const [barDetails, setBarDetails] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [testDate, setTestDate] = useState('');
  const [inputDate, setInputDate] = useState('');
  // chartType and setChartType will be managed by parent and passed as props
  const chartKey = props.symbol || 'default';

  // --- Chart setup and data fetching logic ---
  useEffect(() => {
    // console.log('[ChartComponent] useEffect hook started.');
    const container = chartContainerRef.current;
    // console.log('[ChartComponent] container from chartContainerRef.current:', container);
    if (!container) {
      // console.log('[ChartComponent] useEffect hook returning early because container is not available.');
      return;
    }

    let pollCount = 0;
    const pollForBars = () => {
      const candleSeries = candleSeriesRef.current;
      const chart = chartRef.current;
      if (candleSeries && chart) {
        const bars = candleSeries._data || candleSeries._internal?._data || [];
        if (bars && bars.length > 0) {
          return; // Stop polling
        }
      }
      pollCount++;
      if (pollCount < 15) {
        setTimeout(pollForBars, 200);
      } else {
        // console.log('No bars loaded after polling.');
      }
    };
    pollForBars();

    const bgColor = props.chartColors?.background || '#fff';
    const chart = createChart(container, {
      // Chart type logic: remove previous series if switching
      removeSeries: () => {
        if (candleSeriesRef.current) {
          chart.removeSeries(candleSeriesRef.current);
          candleSeriesRef.current = null;
        }
        if (volumeSeriesRef.current) {
          chart.removeSeries(volumeSeriesRef.current);
          volumeSeriesRef.current = null;
        }
        if (lineSeriesRef && lineSeriesRef.current) {
          chart.removeSeries(lineSeriesRef.current);
          lineSeriesRef.current = null;
        }
      },

      width: container.offsetWidth || 600,
      height: container.offsetHeight || 400,
      layout: {
        background: { color: bgColor },
        textColor: props.chartColors?.text || '#222',
        attributionLogo: false,
      },
      grid: {
        vertLines: {
          color: props.chartColors?.grid || '#e0e7ef',
          style: 0,
          visible: true,
        },
        horzLines: {
          color: props.chartColors?.grid || '#e0e7ef',
          style: 0,
          visible: true,
        },
      },
      timeScale: {
        timeVisible: true,
        secondsVisible: false,
        borderVisible: false,
      },
      crosshair: {
        mode: CrosshairMode.Magnet, // Explicitly set mode
        vertLine: {
          labelVisible: true, // Show date label on time axis
        },
        horzLine: {
          labelVisible: true, // Ensure price label remains visible on price axis
        },
      },
    });
    chartRef.current = chart;

    // Subscribe to crosshair move event
    // console.log('[ChartComponent] Attempting to subscribe crosshair move. handleCrosshairMove is:', typeof handleCrosshairMove);
    chart.subscribeCrosshairMove(handleCrosshairMove);
    // console.log('[ChartComponent] Crosshair move subscribed.');

    // Helper: Get bar date as yyyy-mm-dd
    function getBarDate(barTime) {
      if (!barTime) return '';
      if (typeof barTime === 'number') {
        const d = barTime > 1000000000000 ? new Date(barTime) : new Date(barTime * 1000);
        return d.toISOString().slice(0, 10);
      }
      if (typeof barTime === 'string') {
        return barTime.slice(0, 10).replace(/\//g, '-');
      }
      if (typeof barTime === 'object' && barTime.year) {
        return `${barTime.year}-${String(barTime.month).padStart(2, '0')}-${String(barTime.day).padStart(2, '0')}`;
      }
      return '';
    }

    // Function: Find and annotate the bar with the given date
    function findAndAnnotateBarByDate(targetDate) {
      const chart = chartRef.current;
      // Use barsRef.current for annotation logic
      const bars = barsRef.current || [];
      // console.log('[DEBUG] Running findAndAnnotateBarByDate for', targetDate);
      // console.log('[DEBUG] Bars array:', bars);
      let foundBar = null;
      for (let i = 0; i < bars.length; i++) {
        const barDate = getBarDate(bars[i].time);
        // console.log(`[DEBUG] Checking bar ${i}:`, bars[i], 'barDate:', barDate);
        if (barDate === targetDate) {
          foundBar = bars[i];
          break;
        }
      }
      if (foundBar) {
        // console.log('[DEBUG] Found bar for date', targetDate, foundBar);
        // Draw a marker on the candlestick series
        candleSeriesRef.current.setMarkers && candleSeriesRef.current.setMarkers([
          {
            time: foundBar.time,
            position: 'aboveBar',
            color: 'green',
            shape: 'circle',
            text: '',
          },
        ]);
      } else {
        // console.log('[DEBUG] No bar found for date', targetDate);
      }
    }

    // SINGLE CLICK: Draw vertical line annotation for 2024-12-26 only
    chart.subscribeClick(() => {
      // console.log('[DEBUG] Chart click event fired');
      findAndAnnotateBarByDate('2024-12-26');
    });

    // --- SERIES CREATION ---
    candleSeriesRef.current = chart.addCandlestickSeries({
      upColor: props.chartColors?.upCandle || '#26a69a',
      downColor: props.chartColors?.downCandle || '#ef5350',
      borderUpColor: props.chartColors?.upCandle || '#26a69a',
      borderDownColor: props.chartColors?.downCandle || '#ef5350',
      wickUpColor: props.chartColors?.upCandle || '#26a69a',
      wickDownColor: props.chartColors?.downCandle || '#ef5350',
    });
    const volumeSeries = chart.addHistogramSeries({
      priceFormat: { type: 'volume' },
      color: props.chartColors?.volume || '#b0b0b0',
      priceScaleId: '',
      scaleMargins: { top: 0.5, bottom: 0 },
    });
    volumeSeriesRef.current = volumeSeries;

    // --- Resize & crosshair handlers ---
    const handleResize = () => {
      chart.resize(container.offsetWidth || 600, container.offsetHeight || 400);
    };
    window.addEventListener('resize', handleResize);

    // --- Crosshair move handler for bar details popup ---
    const handleCrosshairMove = (param) => {
      // console.log('[ChartComponent] handleCrosshairMove called. Param time:', param.time, 'Series prices:', param.seriesPrices?.get(candleSeriesRef.current)); // Added more specific prefix
      if (!param.time || !param.point) {
        setBarDetails(null);
        return;
      }
      let candleBar = null;
      let volumeBar = null;
      for (const [series, value] of param.seriesData.entries()) {
        if (series === candleSeriesRef.current) candleBar = value;
        if (series === volumeSeriesRef.current) volumeBar = value;
      }
      if (!candleBar && !volumeBar) {
        setBarDetails(null);
        return;
      }
      const barTime = candleBar?.time ?? volumeBar?.time ?? param.time;
      setBarDetails({
        time: barTime,
        open: candleBar?.open,
        high: candleBar?.high,
        low: candleBar?.low,
        close: candleBar?.close,
        volume: volumeBar ? volumeBar.volume ?? volumeBar.value : undefined,
      });
    };

    return () => {
      // console.log('[DEBUG] ChartComponent cleanup for symbol:', props.symbol);
      window.removeEventListener('resize', handleResize);

      // Unsubscribe from crosshair move event
      if (chartRef.current && typeof handleCrosshairMove === 'function') {
        chartRef.current.unsubscribeCrosshairMove(handleCrosshairMove);
      }
      chart.remove();
      if (container) container.innerHTML = '';
    };
  }, [chartKey]);

  // --- Data loading effect ---
  useEffect(() => {
    // DEBUG: Log chartType and barsRef
    // console.log('[ChartComponent] Chart type switching effect running. chartType:', props.chartType);
    // console.log('[ChartComponent] barsRef.current:', barsRef.current);
    if (!chartRef.current || !chartContainerRef.current) return;
    const chart = chartRef.current;
    chart.timeScale().fitContent();

    // Remove all series before switching
    if (candleSeriesRef.current) {
      chart.removeSeries(candleSeriesRef.current);
      candleSeriesRef.current = null;
    }
    if (volumeSeriesRef.current) {
      chart.removeSeries(volumeSeriesRef.current);
      volumeSeriesRef.current = null;
    }
    if (lineSeriesRef && lineSeriesRef.current) {
      chart.removeSeries(lineSeriesRef.current);
      lineSeriesRef.current = null;
    }

    if (props.chartType === 'bar') {
      candleSeriesRef.current = chart.addCandlestickSeries({
        upColor: props.chartColors?.upCandle || '#26a69a',
        downColor: props.chartColors?.downCandle || '#ef5350',
        borderUpColor: props.chartColors?.upCandle || '#26a69a',
        borderDownColor: props.chartColors?.downCandle || '#ef5350',
        wickUpColor: props.chartColors?.upCandle || '#26a69a',
        wickDownColor: props.chartColors?.upCandle || '#ef5350',
      });
      volumeSeriesRef.current = chart.addHistogramSeries({
        priceFormat: { type: 'volume' },
        color: props.chartColors?.volume || '#b0b0b0',
        priceScaleId: '',
        scaleMargins: { top: 0.5, bottom: 0 },
      });
      // Set bar and volume data from barsRef.current
      if (barsRef.current && barsRef.current.length > 0) {
        // console.log('[ChartComponent] Setting candlestick/volume data:', barsRef.current);
        candleSeriesRef.current.setData(
          barsRef.current.map(bar => ({
            time: bar.time,
            open: bar.open,
            high: bar.high,
            low: bar.low,
            close: bar.close,
          }))
        );
        volumeSeriesRef.current.setData(
          barsRef.current.map(bar => ({
            time: bar.time,
            value: bar.volume,
          }))
        );
      } else {
        // console.warn('[ChartComponent] No barsRef data for candlestick/volume series!');
      }
    } else if (props.chartType === 'line') {
      lineSeriesRef.current = chart.addLineSeries({ color: '#1976d2', lineWidth: 2 });
      // Set line data (closing prices) from barsRef.current
      if (barsRef.current && barsRef.current.length > 0) {
        const lineData = barsRef.current.map(bar => ({ time: bar.time, value: bar.close }));
        // console.log('[ChartComponent] Setting line series data:', lineData);
        lineSeriesRef.current.setData(lineData);
      } else {
        // console.warn('[ChartComponent] No barsRef data for line series!');
      }
    }
    // Force chart resize after setting data
    setTimeout(() => {
      try {
        chart.resize(chartContainerRef.current.offsetWidth, chartContainerRef.current.offsetHeight);
        chart.timeScale().fitContent();
        // console.log('[ChartComponent] Forced chart resize and fitContent after series/data update.');
      } catch (e) {
        // console.error('[ChartComponent] Error forcing chart resize:', e);
      }
    }, 100);
  }, [props.chartType]);

  // --- Data loading effect ---
    )
      .then((res) =>
        res.ok ? res.json() : Promise.reject('Failed to fetch data')
      )
      .then((data) => {
        const bars = [];
        data.forEach((bar) => {
          let time = bar.time || bar.date || bar.dateTime || bar.datetime;
          if (typeof time === 'string' && time.includes('T'))
            time = time.split('T')[0];
          if (
            !time ||
            typeof bar.open !== 'number' ||
            typeof bar.high !== 'number' ||
            typeof bar.low !== 'number' ||
            typeof bar.close !== 'number' ||
            typeof bar.volume !== 'number'
          ) {
            return;
          }
          bars.push({
            time,
            open: bar.open,
            high: bar.high,
            low: bar.low,
            close: bar.close,
            volume: bar.volume,
          });
        });
        bars.sort((a, b) => {
          const ta = typeof a.time === 'string' ? new Date(a.time).getTime() : a.time;
          const tb = typeof b.time === 'string' ? new Date(b.time).getTime() : b.time;
          return ta - tb;
        });
        candleSeries.setData(
          bars.map((bar) => ({
            time: bar.time,
            open: bar.open,
            high: bar.high,
            low: bar.low,
            close: bar.close,
          }))
        );
        volumeSeries.setData(
          bars.map((bar) => ({
            time: bar.time,
            value: bar.volume,
          }))
        );
        barsRef.current = bars; // Store bars for annotation logic
        setLoading(false);
      })
      .catch(() => {
        setError('Failed to load chart data');
        setLoading(false);
      });

  // --- Annotation drawing helper ---
  const drawTestAnnotation = (type) => {

  // --- Chart type switching logic ---
  const lineSeriesRef = useRef();

  useEffect(() => {
    if (!chartRef.current || !chartContainerRef.current) return;
    const chart = chartRef.current;
    chart.timeScale().fitContent();

    // Remove all series before switching
    if (candleSeriesRef.current) {
      chart.removeSeries(candleSeriesRef.current);
      candleSeriesRef.current = null;
    }
    if (volumeSeriesRef.current) {
      chart.removeSeries(volumeSeriesRef.current);
      volumeSeriesRef.current = null;
    }
    if (lineSeriesRef.current) {
      chart.removeSeries(lineSeriesRef.current);
      lineSeriesRef.current = null;
    }

    if (props.chartType === 'bar') {
      candleSeriesRef.current = chart.addCandlestickSeries({
        upColor: props.chartColors?.upCandle || '#26a69a',
        downColor: props.chartColors?.downCandle || '#ef5350',
        borderUpColor: props.chartColors?.upCandle || '#26a69a',
        borderDownColor: props.chartColors?.downCandle || '#ef5350',
        wickUpColor: props.chartColors?.upCandle || '#26a69a',
        wickDownColor: props.chartColors?.downCandle || '#ef5350',
      });
      volumeSeriesRef.current = chart.addHistogramSeries({
        priceFormat: { type: 'volume' },
        color: props.chartColors?.volume || '#b0b0b0',
        priceScaleId: '',
        scaleMargins: { top: 0.5, bottom: 0 },
      });
      // Set bar and volume data from barsRef.current
      if (barsRef.current && barsRef.current.length > 0) {
        candleSeriesRef.current.setData(
          barsRef.current.map(bar => ({
            time: bar.time,
            open: bar.open,
            high: bar.high,
            low: bar.low,
            close: bar.close,
          }))
        );
        volumeSeriesRef.current.setData(
          barsRef.current.map(bar => ({
            time: bar.time,
            value: bar.volume,
          }))
        );
      }
    } else if (props.chartType === 'line') {
      lineSeriesRef.current = chart.addLineSeries({ color: '#1976d2', lineWidth: 2 });
      // Set line data (closing prices) from barsRef.current
      if (barsRef.current && barsRef.current.length > 0) {
        const lineData = barsRef.current.map(bar => ({ time: bar.time, value: bar.close }));
        lineSeriesRef.current.setData(lineData);
      }
    }
  }, [props.chartType]);

// ... (rest of the code remains the same)

return (
  <>
    <div
      style={{
        width: '100%',
        height: '100%',
        position: 'relative',
      }}
    >
    {/* Icon Toolbar Row */}
    <div
      style={{
        display: 'flex',
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
        gap: 32,
        marginTop: 8,
        marginBottom: 8,
        minHeight: 0,
        position: 'relative',
        zIndex: 2,
      }}
    >
      {/* First icon (left) */}
      <button style={{ background: 'none', border: 'none', cursor: 'pointer' }} title="Reload">
        <BarChartIcon style={{ fontSize: 32, color: '#888' }} />
      </button>
      {/* Middle icon (opposition) - open CSG dialog */}
      <button style={{ background: 'none', border: 'none', cursor: 'pointer' }} title="Manage Study Group" onClick={() => { console.log('Middle icon clicked'); setCsgDialogOpen(true); }}>
        {oppositionIcon ? (
          <img src={oppositionIcon} alt="Opposition" style={{ width: 32, height: 32 }} />
        ) : (
          <span style={{ fontSize: 28, color: '#1976d2', fontWeight: 'bold' }}>⚖️</span>
        )}
      </button>
      {/* Last icon (right) */}
      <button style={{ background: 'none', border: 'none', cursor: 'pointer' }} title="Show Chart">
        <ShowChartIcon style={{ fontSize: 32, color: '#1976d2' }} />
      </button>
    </div>
    {/* Navigation Buttons Row */}
    <div
      style={{
        display: 'flex',
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
        gap: 8,
        marginTop: 0,
        marginBottom: 2,
        minHeight: 0,
        position: 'relative',
        zIndex: 2,
      }}
    >
      {props.onOldest && <button onClick={props.onOldest}>Oldest</button>}
      {props.onPrev && <button onClick={props.onPrev}>Prev</button>}
      {props.onNext && <button onClick={props.onNext}>Next</button>}
      {props.onLatest && <button onClick={props.onLatest}>Latest</button>}
    </div>
    {/* Study Group Dialog */}
    <CurrentStudyGroupDialog
      open={csgDialogOpen}
      onClose={() => setCsgDialogOpen(false)}
      csg={csg}
      onAddStudy={addStudy}
      onDeleteStudy={removeStudy}
      onSaveCSG={() => {}}
    />

    {/* Symbol label below icons/buttons row */}
    <div
      style={{
        fontWeight: 'bold',
        fontSize: 28,
        background: 'linear-gradient(90deg, #f0f4ff 0%, #e8eaf6 100%)',
        color: '#26336c',
        borderRadius: 10,
        boxShadow: '0 2px 12px rgba(100,120,180,0.08)',
        padding: '8px 36px',
        letterSpacing: 2,
        border: '2px solid #d1d9f0',
        fontFamily: 'Montserrat, Segoe UI, Arial, sans-serif',
        minWidth: 120,
        textAlign: 'center',
        margin: '0 auto 8px',
      }}
    >
      {props.symbol}
    </div>

    {/* TEMPORARY ANNOTATION TEST UI below symbol label */}
    <div
      style={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        gap: 12,
        margin: '12px 0',
      }}
    >
      <input
        type="date"
        value={inputDate}
        onChange={(e) => {
          setInputDate(e.target.value);
          setTestDate(e.target.value);
        }}
      />
      <button onClick={() => drawTestAnnotation('vline')}>V Line</button>
      <button onClick={() => drawTestAnnotation('uparrow')}>Up Arrow</button>
      <button onClick={() => drawTestAnnotation('dnarrow')}>Dn Arrow</button>
    </div>

    {/* Bar details below annotation test UI */}
    {barDetails && (
      <div
        style={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          gap: 12,
          margin: '12px 0',
        }}
      >
        <b>O:</b>
        {barDetails.open !== undefined
          ? Number(barDetails.open).toFixed(2)
          : ''}{' '}
        <b>H:</b>
        {barDetails.high !== undefined
          ? Number(barDetails.high).toFixed(2)
          : ''}{' '}
        <b>L:</b>
        {barDetails.low !== undefined
          ? Number(barDetails.low).toFixed(2)
          : ''}{' '}
        <b>C:</b>
        {barDetails.close !== undefined
          ? Number(barDetails.close).toFixed(2)
          : ''}
      </div>
    )}
  </div>
  </>
 );
}
export default ChartComponent;