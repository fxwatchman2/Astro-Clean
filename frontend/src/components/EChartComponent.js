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
import { useChartOverlays } from '../context/ChartOverlayContext';
const formatDate = (dateStr) => {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  return `${(date.getMonth() + 1).toString().padStart(2, '0')}/${date.getDate().toString().padStart(2, '0')}/${date.getFullYear()}`;
};

const createDateMap = (dates) => {
  const map = new Map();
  if (Array.isArray(dates)) {
    dates.forEach((date, index) => {
      if (date && typeof date === 'string') {
        map.set(date.split('T')[0], index);
      }
    });
  }
  return map;
};

const PAGE_SIZE = 200; // Number of data points to show at once



// Helper Functions
const EChartComponent = forwardRef((props, ref) => {
  const {
    barData: propsBarData,
    drawingInstructions,
    theme,
    loading: propsLoading,
    error: propsError,
    style,
    chartType,
    viewMode
  } = props;

  // --- Planetary Lines Overlay Integration ---
  const { overlays } = useChartOverlays();
  console.log('[EChartComponent] overlays from context:', overlays);
  // Filter overlays for planetary lines type (assume type: 'planetaryLines')
  const planetaryLinesOverlays = overlays?.filter(o => o.type === 'planetaryLines') || [];
  console.log('[EChartComponent] planetaryLinesOverlays:', planetaryLinesOverlays);
  // Filter overlays for longitude-dates shapes: verticalLine, circle, square
  const dateMap = useMemo(() => createDateMap(propsBarData?.dates || []), [propsBarData?.dates]);

  const longitudeShapeOverlays = useMemo(() => overlays?.filter(o => ['verticalLine', 'circle', 'square', 'longitudinal'].includes(o.type)) || [], [overlays]);
  console.log('[EChartComponent] longitudeShapeOverlays:', longitudeShapeOverlays);

  const longitudeShapeMarkLines = useMemo(() => {
    return longitudeShapeOverlays.filter(o => o.type === 'verticalLine' || o.type === 'longitudinal').map(o => {
      // Snap event dates to the closest available trading day.
      const oDateStr = o.date.split('T')[0]; // Ensure YYYY-MM-DD format
      let snappedDate;

      const dateIndex = dateMap.get(oDateStr);
      if (dateIndex !== undefined) {
        // The event date is a trading day.
        snappedDate = propsBarData.dates[dateIndex];
      } else if (Array.isArray(propsBarData?.dates) && propsBarData.dates.length > 0) {
        // If the exact date is not in our dataset, find the closest available trading day.
        const tradingDates = propsBarData.dates.map(d => d.split('T')[0]);
        const eventTime = new Date(oDateStr + 'T12:00:00Z').getTime();
        const nextIndex = tradingDates.findIndex(d => d >= oDateStr);

        if (nextIndex === -1) {
          // All trading days are before the event date, snap to the last one.
          snappedDate = propsBarData.dates[propsBarData.dates.length - 1];
        } else if (nextIndex === 0) {
          // All trading days are after the event date, snap to the first one.
          snappedDate = propsBarData.dates[0];
        } else {
          // The event date is between two trading days. Find the closer one.
          const prevDateStr = tradingDates[nextIndex - 1];
          const nextDateStr = tradingDates[nextIndex];
          const prevTime = new Date(prevDateStr + 'T12:00:00Z').getTime();
          const nextTime = new Date(nextDateStr + 'T12:00:00Z').getTime();
          const diffPrev = eventTime - prevTime;
          const diffNext = nextTime - eventTime;
          if (diffPrev <= diffNext) { // Prefer previous day in case of a tie.
            snappedDate = propsBarData.dates[nextIndex - 1];
          } else {
            snappedDate = propsBarData.dates[nextIndex];
          }
        }
      } else {
        // Fallback if no trading dates are available
        snappedDate = o.date;
      }
      const formattedSnappedDate = formatDate(snappedDate);
      return {
        xAxis: formattedSnappedDate,
        lineStyle: {
          color: o.color || '#1976d2',
          width: o.thickness === 'medium' ? 2 : o.thickness === 'heavy' ? 4 : 1,
          type: o.type === 'dashed' ? 'dashed' : o.type === 'dotted' ? 'dotted' : 'solid',
        },
        name: `${o.planet || ''}-deg${o.degrees || ''}-${o.date || ''}`,
        label: {
          show: true,
          formatter: () => `${o.planetName || o.planet || ''} ${o.degrees || ''}° (Geocentric)\n${snappedDate || ''}`,
          color: '#444',
          fontFamily: 'Roboto, sans-serif',
          fontWeight: 500,
          fontSize: 12,
          align: 'left',
          verticalAlign: 'top',
          position: 'end',
          backgroundColor: 'transparent',
          borderWidth: 0,
          padding: [4, 8, 0, 8],
        },
        tooltip: {
          show: true,
          formatter: () => `${o.planet || ''} @ ${o.degrees || ''}° on ${o.date || ''}`
        }
      };
    });
  }, [longitudeShapeOverlays, dateMap, propsBarData?.dates]);

  // Circles and squares as markPoints
  const _longitudeShapeMarkPoints = useMemo(() => {
    return longitudeShapeOverlays.filter(o => o.type === 'circle' || o.type === 'square').map(o => ({
      coord: [o.date, o.degrees || 0],
      symbol: o.type === 'circle' ? 'circle' : 'rect',
      symbolSize: o.thickness === 'medium' ? 16 : o.thickness === 'heavy' ? 24 : 10,
      itemStyle: {
        color: o.color || '#1976d2',
        borderWidth: 2,
        borderColor: o.color || '#1976d2',
      },
      label: {
        show: true,
        formatter: () => `${o.planet || ''}@${o.degrees || ''}°`,
        color: '#444',
        fontSize: 12,
        fontWeight: 500,
        fontFamily: 'Roboto, sans-serif',
        backgroundColor: 'transparent',
        borderWidth: 0,
      },
      tooltip: {
        show: true,
        formatter: () => `${o.planet || ''} @ ${o.degrees || ''}° on ${o.date || ''}`
      }
    }));
  }, [longitudeShapeOverlays]);

  // Convert vLine overlays to ECharts markLine data
  const vLineMarkLines = useMemo(() => {
    const vLineOverlays = overlays?.filter(o => o.type === 'vLine') || [];
    return vLineOverlays.map(o => ({
      xAxis: o.date,
      lineStyle: {
        color: o.color ? o.color.toLowerCase() : '#1976d2',
        width: o.thickness === 'heavy' ? 2 : o.thickness === 'medium' ? 1 : 0.5,
        type: 'solid'
      },
      name: `${o.planet || ''}-retro-${o.date || ''}`,
      label: {
        show: true,
        formatter: () => `${o.planet || ''} ${o.studyType === 'Stationary' ? 'direct' : 'retro'} on ${o.date}`,
        color: '#444',
        fontFamily: 'Roboto, sans-serif',
        fontWeight: 500,
        fontSize: 12,
        rotate: 90,
        align: 'right',
        verticalAlign: 'middle',
        position: 'insideEndTop',
        backgroundColor: 'transparent',
        borderWidth: 0,
        padding: [0, 0, 0, 32], // top, right, bottom, left: large gap between lower end of label and line
      },
      tooltip: {
        show: true,
        formatter: () => `${o.planet || ''} retro on ${o.date || ''}`
      }
    }));
  }, [overlays]);

  const chartRef = useRef(null);
  const chartContainerRef = useRef(null);
  const contextMenuRef = useRef(null);




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

  // Move markings calculation to a plain variable, not a hook
const getMarkings = (drawingInstructions, currentChartColors, propsBarData) => {
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
    const dataLength = propsBarData?.dates?.length || 0;
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

    const { newCustomDots } = markings;
    
    const initialStart = Math.max(0, dataLength - PAGE_SIZE);
    const startPercent = dataLength > 0 ? (initialStart / dataLength) * 100 : 0;
    const endPercent = 100;

    const renderItem = (params, api) => {
        const dot = newCustomDots[params.dataIndex];
        if (!dot) return;

        const { dateIndex, y, color, type } = dot;
        const coord = api.coord([dateIndex, y]);
        if (!coord) return;

        let size = type.includes('large') ? 16 : 8;
        let symbolShape;

        if (type.includes('circle')) {
            symbolShape = { cx: coord[0], cy: coord[1], r: size / 2 };
        } else if (type.includes('square')) {
            symbolShape = { x: coord[0] - size / 2, y: coord[1] - size / 2, width: size, height: size };
        } else if (type.includes('star')) {
            symbolShape = { cx: coord[0], cy: coord[1], n: 5, r: size };
        } else { // default to circle
            symbolShape = { cx: coord[0], cy: coord[1], r: size / 2 };
        }

        return {
            type: type.includes('square') ? 'rect' : (type.includes('star') ? 'star' : 'circle'),
            shape: symbolShape,
            style: {
                fill: color,
                stroke: '#000',
                lineWidth: 1
            },
            z: 100
        };
    };

    let mainSeries = [];
    if (validatedOhlcData.length) {
      mainSeries.push({
        type: chartType === 'candlestick' ? 'candlestick' : 'line',
        name: propsBarData?.symbol || 'OHLC',
        data: chartType === 'candlestick' ? validatedOhlcData : validatedOhlcData.map(d => d[1]),
        xAxisIndex: 0,
        yAxisIndex: 0,
        itemStyle: {
          color: currentChartColors.candlestickUp,
          color0: currentChartColors.candlestickDown,
          borderColor: currentChartColors.candlestickUp,
          borderColor0: currentChartColors.candlestickDown
        },
        markLine: {
          silent: true,
          symbol: 'none',
          data: [
            ...markings.newMarkLines,
            ...longitudeShapeMarkLines,
            ...vLineMarkLines,
          ],
        },
        markArea: {
          silent: true,
          data: markings.newMarkAreas
        },
        markPoint: {
          data: _longitudeShapeMarkPoints,
        },
      });

      mainSeries.push({
        name: 'Volume',
        type: 'bar',
        xAxisIndex: 1,
        yAxisIndex: 1,
        data: validatedVolumeData,
        itemStyle: {
            color: ({ dataIndex }) => {
                const ohlcItem = validatedOhlcData[dataIndex];
                if (!ohlcItem) return currentChartColors.volumeDown;
                return ohlcItem[1] >= ohlcItem[0] ? currentChartColors.volumeUp : currentChartColors.volumeDown;
            }
        }
      });
    }

    if (newCustomDots.length > 0) {
      mainSeries.push({
        type: 'custom',
        name: 'Custom Dots',
        renderItem: renderItem,
        data: newCustomDots,
        xAxisIndex: 0,
        yAxisIndex: 0,
        z: 100,
      });
    }

    const dataZoomConfig = viewMode === 'ALL'
      ? [
          {
            type: 'inside',
            xAxisIndex: [0, 1],
            start: 0,
            end: 100,
            disabled: true
          }
        ]
      : [
          {
            type: 'inside',
            xAxisIndex: [0, 1],
            start: startPercent,
            end: endPercent,
            minSpan: dataLength > 0 ? (PAGE_SIZE / dataLength) * 100 : undefined,
            maxSpan: dataLength > 0 ? (PAGE_SIZE / dataLength) * 100 : undefined
          }
        ];

    return {
      animation: false,
      theme: theme,
      grid: [
        { left: '4%', right: '30px', top: '5%', height: `${dividerPercent - 5}%` },
        { left: '4%', right: '30px', bottom: '80px', height: `${90 - dividerPercent - 10}%` }
      ],
      xAxis: [
        { type: 'category', data: allFormattedDates, axisLine: { onZero: false }, splitLine: { show: false }, axisLabel: { show: false }, axisTick: { show: false }, boundaryGap: true },
        { type: 'category', data: allFormattedDates, gridIndex: 1, axisLine: { onZero: false }, axisTick: { show: true }, splitLine: { show: false }, axisLabel: { show: true, color: currentChartColors.axisLabelColor }, boundaryGap: true }
      ],
      yAxis: [
        { scale: true, splitArea: { show: true, areaStyle: { color: ['rgba(250,250,250,0.05)', 'rgba(200,200,200,0.05)'] } }, splitLine: { show: true, lineStyle: { color: currentChartColors.gridColor } }, axisLabel: { color: currentChartColors.axisLabelColor, inside: false }, position: 'right' },
        { scale: true, gridIndex: 1, splitNumber: 2, axisLabel: { show: true, color: currentChartColors.axisLabelColor, inside: false }, axisLine: { show: false }, axisTick: { show: false }, splitLine: { show: false }, position: 'right' }
      ],
      dataZoom: dataZoomConfig,
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'cross',
          lineStyle: {
            color: currentChartColors.crosshairColor
          }
        },
        backgroundColor: currentChartColors.tooltipBackground,
        borderColor: currentChartColors.tooltipBorder,
        borderWidth: 1,
        textStyle: {
          color: currentChartColors.tooltipColor
        },
        position: (pos, params, el, elRect, size) => {
          const obj = { top: 10 };
          obj[['left', 'right'][+(pos[0] < size.viewSize[0] / 2)]] = 30;
          return obj;
        },
      },
      axisPointer: {
        link: { xAxisIndex: 'all' },
        label: {
          backgroundColor: '#777'
        }
      },
      series: mainSeries,
      backgroundColor: 'transparent'
    };
  }, [propsBarData, drawingInstructions, theme, chartType, dividerPercent, markings, longitudeShapeMarkLines, vLineMarkLines, _longitudeShapeMarkPoints, currentChartColors, viewMode]);

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



  const handleMouseDown = (e) => {
    e.preventDefault();
    const startY = e.clientY;
    const startPercent = dividerPercent;

    const doDrag = (moveEvent) => {
      const deltaY = moveEvent.clientY - startY;
      if (!chartContainerRef.current) return;
      const chartHeight = chartContainerRef.current.clientHeight;
      const deltaPercent = (deltaY / chartHeight) * 100;
      setDividerPercent(Math.max(10, Math.min(90, startPercent + deltaPercent)));
    };

    const stopDrag = () => {
      document.removeEventListener('mousemove', doDrag);
      document.removeEventListener('mouseup', stopDrag);
    };

    document.addEventListener('mousemove', doDrag);
    document.addEventListener('mouseup', stopDrag);
  };

  const handlePage = (direction) => {
    const chartInstance = chartRef.current?.getEchartsInstance();
    const dataLength = propsBarData?.dates?.length;
    if (!chartInstance || !dataLength) return;

    // Get the current zoom state directly from the chart instance
    const currentZoom = chartInstance.getOption().dataZoom[0];
    const currentStartValue = currentZoom.startValue;

    let newStartValue;

    switch (direction) {
      case 'prev': // Corresponds to '< PREV' button
        newStartValue = Math.max(0, currentStartValue - PAGE_SIZE);
        break;
      case 'next': // Corresponds to 'NEXT >' button
        newStartValue = Math.min(dataLength - PAGE_SIZE, currentStartValue + PAGE_SIZE);
        break;
      case 'oldest':
        newStartValue = 0;
        break;
      case 'latest':
        newStartValue = Math.max(0, dataLength - PAGE_SIZE);
        break;
      default:
        return;
    }

    chartInstance.dispatchAction({
      type: 'dataZoom',
      // Specify the zoom window by data index
      startValue: newStartValue,
      endValue: Math.min(dataLength - 1, newStartValue + PAGE_SIZE - 1),
    });
  };

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
          'contextmenu': handleContextMenu,
        }}
        notMerge={true}
        lazyUpdate={true}
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
  viewMode: PropTypes.string.isRequired,
};

export default EChartComponent;