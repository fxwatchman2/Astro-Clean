import React, {
  useEffect,
  useRef,
  useState,
  useMemo,
  useCallback,
  forwardRef,
  useImperativeHandle
} from 'react';
import PropTypes from 'prop-types';
import ReactECharts from 'echarts-for-react';

const PAGE_SIZE = 200; // Number of data points to show at once

const detailsPanelStyle = `
  background-color: rgba(25, 27, 31, 0.9);
  border: 1px solid #333;
  border-radius: 8px;
  padding: 12px;
  color: #fff;
  font-family: 'Roboto', sans-serif;
  font-size: 14px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.3);
  width: auto;
  max-width: 250px;
`;

// Helper Functions
const formatDate = (dateStr) => {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  return `${(date.getMonth() + 1).toString().padStart(2, '0')}/${date.getDate().toString().padStart(2, '0')}/${date.getFullYear()}`;
};

const createDateMap = (dates) => {
  const map = new Map();
  dates.forEach((date, index) => {
    if (date && typeof date === 'string') {
      map.set(date.split('T')[0], index);
    }
  });
  return map;
};

import { useChartOverlays } from '../context/ChartOverlayContext';

const EChartComponent = forwardRef((props, ref) => {
  const {
    barData: propsBarData,
    drawingInstructions,
    theme,
    loading: propsLoading,
    error: propsError,
    style,
    chartType
  } = props;

  // --- Planetary Lines Overlay Integration ---
  const { overlays } = useChartOverlays();
  console.log('[EChartComponent] overlays from context:', overlays);
  // Filter overlays for planetary lines type (assume type: 'planetaryLines')
  const planetaryLinesOverlays = overlays?.filter(o => o.type === 'planetaryLines') || [];
  console.log('[EChartComponent] planetaryLinesOverlays:', planetaryLinesOverlays);

  const chartRef = useRef(null);
  const chartContainerRef = useRef(null);
  const contextMenuRef = useRef(null);
  const programmaticUpdateRef = useRef(false); // To distinguish user scroll from code scroll

  const [currentDataWindowStart, setCurrentDataWindowStart] = useState(0);

  // On initial load or when barData changes, focus last page by default
  useEffect(() => {
    if (propsBarData?.dates?.length) {
      setCurrentDataWindowStart(Math.max(0, propsBarData.dates.length - PAGE_SIZE));
    }
    // eslint-disable-next-line
  }, [propsBarData?.dates]);
  const [dividerPercent, setDividerPercent] = useState(70);
  const [contextMenuVisible, setContextMenuVisible] = useState(false);
  const [contextMenuPosition, setContextMenuPosition] = useState({ x: 0, y: 0 });
  const [contextMenuItems] = useState(['Add Alert', 'Draw Trendline']);

  const lightChartColors = {
    candlestickUp: '#22c55e',
    candlestickDown: '#ef4444',
    volumeUp: 'rgba(34, 197, 94, 0.4)',
    volumeDown: 'rgba(239, 68, 68, 0.4)',
    primary: '#c23531',
    secondary: '#2f4554',
    volume: '#61a0a8',
    tooltipBackground: 'rgba(255, 255, 255, 0.9)',
    tooltipBorder: '#ccc',
    tooltipColor: '#333',
    gridColor: '#e0e6f1',
    axisLabelColor: '#6e7079',
    crosshairColor: '#999'
  };

  const darkChartColors = {
    candlestickUp: '#22c55e',
    candlestickDown: '#ef4444',
    volumeUp: 'rgba(34, 197, 94, 0.4)',
    volumeDown: 'rgba(239, 68, 68, 0.4)',
    primary: '#00da3c',
    secondary: '#ff0000',
    volume: '#3A91F3',
    tooltipBackground: 'rgba(25, 27, 31, 0.9)',
    tooltipBorder: '#333',
    tooltipColor: '#fff',
    gridColor: '#2c3440',
    axisLabelColor: '#ccc',
    crosshairColor: '#777'
  };

  const currentChartColors = theme === 'dark' ? darkChartColors : lightChartColors;

  const lastPossibleStart = Math.max(0, (propsBarData?.dates?.length || 0) - PAGE_SIZE);

  // --- Backend-driven indicator overlays state ---
  // const [mainPanelIndicator, setMainPanelIndicator] = useState([]);
  // const [mainPanelColor, setMainPanelColor] = useState('#ff9800');
  // const [mainPanelLineStyle, setMainPanelLineStyle] = useState('solid');
  // const [volumePanelIndicator, setVolumePanelIndicator] = useState([]);
  // const [volumePanelColor, setVolumePanelColor] = useState('#1976d2');
  // const [volumePanelLineStyle, setVolumePanelLineStyle] = useState('dashed');

  // Fetch mock indicator overlays from backend whenever barData date range changes
  useEffect(() => {
    const fetchIndicator = async (panel) => {
      if (!propsBarData?.dates?.length) return;
      const fromDate = propsBarData.dates[0];
      const toDate = propsBarData.dates[propsBarData.dates.length - 1];
      const payload = {
        symbol: propsBarData?.symbol || 'AAPL',
        fromDate,
        toDate,
        indicator: { type: 'mock', panel }
      };
      try {
        const resp = await fetch('/api/followstudy/indicator', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(payload)
        });
        if (!resp.ok) throw new Error('Failed to fetch indicator overlay');
        await resp.json();
        if (panel === 'main') {
          // main panel overlays are disabled
        } else {
          // Removed state updates
        }
      } catch (err) {
        // fallback: clear overlays
        if (panel === 'main') {
          // main panel overlays are disabled
        } else {
          // setVolumePanelIndicator([]);
        }
      }
    };
    fetchIndicator('main');
    fetchIndicator('volume');
    // eslint-disable-next-line
  }, [propsBarData?.dates]);

  const dateMap = useMemo(() => {
    return propsBarData?.dates ? createDateMap(propsBarData.dates) : new Map();
  }, [propsBarData?.dates]);

  // Move markings calculation to a plain variable, not a hook
const getMarkings = (drawingInstructions, dateMap, currentChartColors, propsBarData) => {
  const newMarkLines = [];
  const newMarkAreas = [];
  const newCustomDots = [];
  if (drawingInstructions && Array.isArray(drawingInstructions) && propsBarData?.dates) {
    drawingInstructions.filter(Boolean).forEach((instr) => {
      const type = instr?.style?.type;
      const color = instr?.style?.color;
      const dateTime = instr?.params?.dateTime;

      if ((type === 'vLine') && dateTime) {
        // Robust snapping: find the closest bar on or after the backend date
        const backendDate = dateTime.split('T')[0];
        let dateIndex = dateMap.get(backendDate);
        if (dateIndex === undefined && propsBarData?.dates) {
          // Find the first index >= backendDate
          const barDates = propsBarData.dates;
          dateIndex = barDates.findIndex(d => d >= backendDate);
          // Debug log
          if (dateIndex === -1) {
            // eslint-disable-next-line no-console

          } else {
            // eslint-disable-next-line no-console

          }
        } else if (dateIndex !== undefined) {
          // eslint-disable-next-line no-console

        }
        if (dateIndex !== undefined && dateIndex !== -1) {
          newMarkLines.push([
            { xAxis: dateIndex, yAxis: 'min', lineStyle: { color: color || currentChartColors.primary, width: 2, type: 'solid' }, label: { show: true, formatter: instr.meta?.tooltip || '', position: 'insideEndTop' } },
            { xAxis: dateIndex, yAxis: 'max' }
          ]);
        }
      } else if (type === 'box' || type === 'markArea') {
        const fromDateTime = instr.params?.fromDateTime;
        const toDateTime = instr.params?.toDateTime;
        if (fromDateTime && toDateTime) {
          const startIndex = dateMap.get(fromDateTime.split('T')[0]);
          const endIndex = dateMap.get(toDateTime.split('T')[0]);
          if (startIndex !== undefined && endIndex !== undefined) {
            newMarkAreas.push([
              {
                xAxis: startIndex,
                itemStyle: {
                  color: color || 'rgba(255, 182, 193, 0.3)',
                },
                label: {
                  show: true,
                  formatter: instr.meta?.tooltip || '',
                  position: 'insideTop',
                  color: '#000',
                  fontSize: 10,
                }
              },
              {
                xAxis: endIndex
              }
            ]);
          }
        }
      } else if ((type === 'circle' || type === 'square' || type === 'star' || type === 'downArrow') && dateTime) {
        if (type === 'square') {
          // intentionally left blank
        }
        const backendDate = dateTime.split('T')[0];
        let dateIndex = dateMap.get(backendDate);
        if (dateIndex === undefined && propsBarData?.dates) {
          // Snap to the first bar date >= backendDate
          const barDates = propsBarData.dates;
          dateIndex = barDates.findIndex(d => d >= backendDate);
          if (dateIndex === -1) {
            // eslint-disable-next-line no-console

          } else {
            // eslint-disable-next-line no-console

          }
        } else if (dateIndex !== undefined) {
          // eslint-disable-next-line no-console

        }
        if (dateIndex !== undefined && dateIndex !== -1 && instr.params?.y !== undefined) {
          const dotObj = {
            dateIndex,
            y: instr.params.y,
            color: color || currentChartColors.primary,
            tooltip: instr.meta?.tooltip || '',
            type // 'large-circle', 'large-square', 'star', 'large-arrow'
          };
          if (type === 'square') {
            // intentionally left blank
          }
          newCustomDots.push(dotObj);
        }
      }
    });
  }
  return { newMarkLines, newMarkAreas, newCustomDots };
};

// Remove markings hook and use variable instead
const markings = getMarkings(drawingInstructions, dateMap, currentChartColors, propsBarData);

  const finalChartOptions = useMemo(() => {
    const fullOhlcArray = propsBarData?.ohlc || [];
    const fullVolumeArray = propsBarData?.volumes || [];

    if (!propsBarData?.dates?.length || !fullOhlcArray.length) {
      return { series: [] };
    }

    const allFormattedDates = propsBarData.dates.map(formatDate);

    const validatedOhlcData = fullOhlcArray.map((item) => {
      if (Array.isArray(item) && item.length === 4 && item.every(val => typeof val === 'number' && isFinite(val))) {
        return item;
      }
      return [NaN, NaN, NaN, NaN];
    });

    const validatedVolumeData = fullVolumeArray.map((item) => {
      if (typeof item === 'number' && isFinite(item)) {
        return item;
      }
      return NaN;
    });

    const { newMarkLines, newMarkAreas, newCustomDots } = markings;

    // Custom series for dots/circles
    let customDotSeries = null;
    let customDotData = [];
    if (newCustomDots && newCustomDots.length > 0) {
      

    if (customDotData.length > 0) {
      customDotSeries = {
        type: 'custom',
        name: 'Study Dots',
        data: customDotData,
        renderItem: function(params, api) {
          // Diagnostic: Log all dots and params processed by renderItem
          if (typeof window !== 'undefined') {
            try {
              // intentionally left blank
            } catch (e) {
              // intentionally ignore error for diagnostics
            }
          }
          const dataIndex = api.value(0);
          const yValue = api.value(1);
          const dot = newCustomDots.find(d => d.dateIndex === dataIndex && d.y === yValue);
          if (!dot) return;
          let size = 20;
          let coord = api.coord([dataIndex, yValue]);
          return {
            type: 'rect',
            shape: {
              x: coord[0] - size / 2,
              y: coord[1] - size / 2,
              width: size,
              height: size
            },
            style: {
              fill: dot.color || currentChartColors.primary,
              stroke: '#000',
              lineWidth: 2,
              shadowBlur: 8,
              shadowColor: '#000'
            }
          };
        },
        z: 100
      };
    }
  }

  // Compose series array
  let mainSeries = [];
  // --- Planetary Lines Overlay Series ---
  let planetaryLinesSeries = [];
  // DEBUG: Log overlay data and barData.dates
  console.log('planetaryLinesOverlays', planetaryLinesOverlays, 'barData.dates', propsBarData?.dates?.length);
  if (planetaryLinesOverlays.length > 0 && propsBarData?.dates?.length) {
    // DEBUG: Log overlays at entry
    console.log('Processing planetaryLinesOverlays:', planetaryLinesOverlays);
    planetaryLinesOverlays.forEach((overlay) => {
  // Each overlay: { planet, points: [{date, value}, ...] }
  const { planet, points } = overlay;
  // Normalize chart dates
  const normalizeDate = dateStr => (typeof dateStr === 'string' ? dateStr.substring(0, 10) : '');
  const chartDateArr = Array.isArray(propsBarData.dates) ? propsBarData.dates.map(normalizeDate) : [];
  // Build a map from date to value
  const dateToVal = {};
  if (Array.isArray(points)) {
    points.forEach(pt => {
      const d = normalizeDate(pt.date);
      dateToVal[d] = pt.value;
    });
  }
  // Build aligned values array for polyline
  const alignedValues = chartDateArr.map(d => dateToVal[d] !== undefined ? dateToVal[d] : null);
  // Debug: Print last 10 dates and values
  console.log(`[DEBUG] ${planet} aligned dates (last 10):`, chartDateArr.slice(-10));
  console.log(`[DEBUG] ${planet} aligned values (last 10):`, alignedValues.slice(-10));
  // Render as a single polyline (thick red for debug)
  planetaryLinesSeries.push({
    type: 'line',
    name: `[DEBUG] ${planet} Polyline`,
    data: alignedValues,
    showSymbol: false,
    lineStyle: { color: '#FF0000', width: 6, type: 'solid' },
    emphasis: { focus: 'series' },
    z: 1000,
    tooltip: {
      show: true,
      trigger: 'item',
      formatter: () => `Longitude line for ${planet}`,
    },
    xAxisIndex: 0,
    yAxisIndex: 0,
  });
});
  }
  // DEBUG: Log the constructed planetaryLinesSeries
  console.log('planetaryLinesSeries', planetaryLinesSeries);
  if (chartType === 'bar') {
    // Insert planetary lines overlays before volume
    mainSeries = [
      {
        type: 'candlestick',
        name: 'OHLC',
        data: validatedOhlcData,
        itemStyle: {
          color: currentChartColors.candlestickUp,
          color0: currentChartColors.candlestickDown,
          borderColor: currentChartColors.candlestickUp,
          borderColor0: currentChartColors.candlestickDown
        },
        markLine: {
          silent: true,
          data: newMarkLines,
          animation: false
        },
        markArea: {
          silent: true,
          data: newMarkAreas,
          animation: false
        },
      },
      {
        name: 'Volume',
        type: 'bar',
        xAxisIndex: 1,
        yAxisIndex: 1,
        data: validatedVolumeData,
        itemStyle: {
          color: (params) => {
            const ohlc = validatedOhlcData[params.dataIndex] || [0,0];
            return ohlc[1] >= ohlc[0] ? currentChartColors.volumeUp : currentChartColors.volumeDown;
          }
        }
      },
      {
        name: 'Volume Panel Indicator',
        type: 'line',
        xAxisIndex: 1,
        yAxisIndex: 1,
        showSymbol: false,
        lineStyle: { color: currentChartColors.line, width: 2 },
        emphasis: { focus: 'series' },
        z: 10,
      },
      ...planetaryLinesSeries,
      {
        name: 'Volume',
        type: 'bar',
        xAxisIndex: 1,
        yAxisIndex: 1,
        data: validatedVolumeData,
        itemStyle: {
          color: (params) => {
            const ohlc = validatedOhlcData[params.dataIndex] || [0,0];
            return ohlc[1] >= ohlc[0] ? currentChartColors.volumeUp : currentChartColors.volumeDown;
          }
        }
      },
    ];
  }

    // Add custom dots/circles series if present
    if (customDotSeries) {
      mainSeries.push(customDotSeries);
    }

    return {
      animation: false,
      grid: [
        { left: '4%', right: '1%', height: `${dividerPercent}%` },
        { left: '4%', right: '1%', top: `${dividerPercent}%`, height: `${95 - dividerPercent}%` }
      ],
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'cross'
        },
        position: function (pos, params, dom, rect, size) {
          const obj = { top: 60 };
          obj[['left', 'right'][+(pos[0] < size.viewSize[0] / 2)]] = 5;
          return obj;
        },
        formatter: (params) => {
          const dataIndex = params[0].dataIndex;
          const ohlc = validatedOhlcData[dataIndex];
          const volume = validatedVolumeData[dataIndex];
          const date = allFormattedDates[dataIndex];

          if (!ohlc || ohlc.some(isNaN)) return null;

          return `
            <div style="${detailsPanelStyle}">
              <div><strong>${date}</strong></div>
              <div>Open: ${ohlc[0].toFixed(2)}</div>
              <div>High: ${ohlc[3].toFixed(2)}</div>
              <div>Low: ${ohlc[2].toFixed(2)}</div>
              <div>Close: ${ohlc[1].toFixed(2)}</div>
              <div>Volume: ${volume}</div>
            </div>
          `;
        }
      },
      axisPointer: {
        link: { xAxisIndex: 'all' },
        label: { backgroundColor: '#777' }
      },
      xAxis: [
        { type: 'category', data: allFormattedDates, scale: true, boundaryGap: false, axisLine: { onZero: false }, splitLine: { show: false }, min: 'dataMin', max: 'dataMax', axisPointer: { label: { show: false } }, gridIndex: 0 },
        { type: 'category', data: allFormattedDates, scale: true, boundaryGap: false, axisLine: { onZero: false }, splitLine: { show: false }, min: 'dataMin', max: 'dataMax', axisPointer: { z: 100 }, gridIndex: 1 },
      ],
      yAxis: [
        { scale: true, splitArea: { show: false }, gridIndex: 0 },
        {
          scale: true,
          gridIndex: 1,
          splitNumber: 2,
          axisLabel: { show: false },
          axisLine: { show: false },
          axisTick: { show: false },
          splitLine: { show: false },
          splitArea: { show: false }
        },
      ],
      dataZoom: [
        { type: 'inside', xAxisIndex: [0, 1], startValue: currentDataWindowStart, endValue: currentDataWindowStart + PAGE_SIZE -1, zoomOnMouseWheel: false, moveOnMouseWheel: true, preventDefaultMouseMove: false }
      ],
      series: mainSeries
    };
  }, [propsBarData, drawingInstructions, chartType, dividerPercent, currentChartColors, markings, currentDataWindowStart]);

  const handleClickOutside = useCallback((event) => {
    if (contextMenuRef.current && !contextMenuRef.current.contains(event.target)) {
      setContextMenuVisible(false);
    }
  }, []);

  useEffect(() => {
    if (contextMenuVisible) document.addEventListener('mousedown', handleClickOutside);
    else document.removeEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, [contextMenuVisible, handleClickOutside]);

  const handleChartDataZoom = (event) => {
    if (event.batch) {
        const newStartValue = event.batch[0].startValue;
        if (newStartValue !== undefined && newStartValue !== currentDataWindowStart) {
            if (!programmaticUpdateRef.current) {
                setCurrentDataWindowStart(newStartValue);
            }
        }
    }
  };

  const handleDividerDrag = (e) => {
    const chartRect = chartContainerRef.current.getBoundingClientRect();
    const newDividerPercent = ((e.clientY - chartRect.top) / chartRect.height) * 100;
    if (newDividerPercent > 10 && newDividerPercent < 90) {
      setDividerPercent(newDividerPercent);
    }
  };

  const handleMouseDown = (e) => {
    e.preventDefault();
    const handleMouseMove = (e) => handleDividerDrag(e);
    const handleMouseUp = () => {
      document.removeEventListener('mousemove', handleMouseMove);
      document.removeEventListener('mouseup', handleMouseUp);
    };
    document.addEventListener('mousemove', handleMouseMove);
    document.addEventListener('mouseup', handleMouseUp);
  };

  const handlePage = useCallback((direction) => {
    let newStart;
    if (direction === 'next') newStart = Math.min(currentDataWindowStart + PAGE_SIZE, lastPossibleStart);
    else if (direction === 'prev') newStart = Math.max(0, currentDataWindowStart - PAGE_SIZE);
    else if (direction === 'latest') newStart = lastPossibleStart;
    else if (direction === 'oldest') newStart = 0;
    setCurrentDataWindowStart(newStart);
  }, [currentDataWindowStart, lastPossibleStart]);

  const handleContextMenu = useCallback((params) => {
    params.event.preventDefault();
    const chart = chartRef.current.getEchartsInstance();
    const pointInPixel = [params.event.offsetX, params.event.offsetY];
    const pointInGrid = chart.convertFromPixel('grid', pointInPixel);
    if (pointInGrid) {
      setContextMenuPosition({ x: params.event.clientX, y: params.event.clientY });
      setContextMenuVisible(true);
    }
  }, []);

  const handleMenuItemClick = useCallback(() => {
    setContextMenuVisible(false);
  }, []);

  useImperativeHandle(ref, () => ({
    handlePage,
    getChartInstance: () => chartRef.current?.getEchartsInstance(),
  }));

  if (propsError) return <div style={{ color: 'red', padding: '20px' }}>Error: {propsError}</div>;

  return (
    <div style={{ ...style, display: 'flex', flexDirection: 'column', height: '100%' }}>
      <div ref={chartContainerRef} style={{ flex: 1, minHeight: 0, width: '100%', position: 'relative' }}>
        <div onMouseDown={handleMouseDown} style={{ position: 'absolute', top: `${dividerPercent}%`, left: '4%', right: '30px', height: '12px', cursor: 'row-resize', zIndex: 10, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
          <div style={{ width: 28, height: 7, background: '#d0d5e2', borderRadius: 4, border: '1px solid #b0b0b0' }} />
        </div>
        <ReactECharts
          ref={chartRef}
          option={finalChartOptions}
          style={{ height: '100%', width: '100%' }}
          theme={theme}
          onEvents={{
            'datazoom': handleChartDataZoom,
            'contextmenu': handleContextMenu,
          }}
          notMerge={false}
          lazyUpdate={false}
          showLoading={propsLoading}
          opts={{ renderer: 'svg' }}
        />
        {contextMenuVisible && (
          <div
            ref={contextMenuRef}
            style={{
              position: 'fixed',
              top: contextMenuPosition.y,
              left: contextMenuPosition.x,
              background: currentChartColors.tooltipBackground,
              color: currentChartColors.tooltipColor,
              border: `1px solid ${currentChartColors.tooltipBorder}`,
              borderRadius: '4px',
              padding: '5px 0',
              zIndex: 1000,
              boxShadow: '2px 2px 5px rgba(0,0,0,0.2)',
              minWidth: '150px',
            }}
          >
            {contextMenuItems.map((item, index) => (
              <div
                key={index}
                onClick={handleMenuItemClick}
                style={{ padding: '8px 12px', cursor: 'pointer' }}
                onMouseEnter={(e) => e.currentTarget.style.backgroundColor = currentChartColors.primary ? currentChartColors.primary + '33' : '#f0f0f0'}
                onMouseLeave={(e) => e.currentTarget.style.backgroundColor = 'transparent'}
              >
                {item}
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
});

EChartComponent.displayName = 'EChartComponent';

EChartComponent.propTypes = {
  barData: PropTypes.shape({
    symbol: PropTypes.string,
    dates: PropTypes.arrayOf(PropTypes.string),
    ohlc: PropTypes.arrayOf(PropTypes.arrayOf(PropTypes.number)),
    volumes: PropTypes.arrayOf(PropTypes.number),
  }), // Retaining the more specific shape, can be undefined initially
  drawingInstructions: PropTypes.array,
  theme: PropTypes.string.isRequired,
  loading: PropTypes.bool,
  error: PropTypes.string,
  style: PropTypes.object,
  chartType: PropTypes.string.isRequired,
};

export default EChartComponent;