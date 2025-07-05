// chartUtils.js
// Shared utility for drawing study markers (lines, shapes, annotations) on the chart.
// Replace console.log with actual charting logic as needed.

/**
 * Draws study markers on the chart for the given dates and options.
 * @param {string[]} dates - Array of date strings (YYYY-MM-DD)
 * @param {object} options - Drawing options
 *   @param {string} options.shapeType - 'vertical', 'circle', etc.
 *   @param {string} options.color
 *   @param {string} options.thickness
 *   @param {string} options.planet
 *   @param {string} options.studyType - Label for annotation
 */
/**
 * Draws study markers on the chart for the given dates and options using the overlay system.
 * @param {string[]} dates - Array of date strings (YYYY-MM-DD)
 * @param {object} options - Drawing options
 *   @param {string} options.shapeType - 'vertical', 'circle', etc.
 *   @param {string} options.color
 *   @param {string} options.thickness
 *   @param {string} options.planet
 *   @param {string} options.studyType - Label for annotation
 * @param {function} addOverlay - Overlay adding function from ChartOverlayContext
 */
export function drawStudyMarkers(dates, options, addOverlay) {
  const { shapeType, color, thickness, planet, studyType } = options;
  dates.forEach(date => {
    // Add as planetaryLines overlay (for future-proofing)
    addOverlay({
      type: 'planetaryLines',
      shapeType,
      planet,
      color,
      thickness,
      studyType,
      points: [{ date, value: null }], // <-- match EChartComponent expectation
    });
    // Also add as vLine overlay for markLine compatibility
    addOverlay({
      type: 'vLine',
      date,
      planet,
      color,
      thickness,
      studyType,
    });
  });
}
