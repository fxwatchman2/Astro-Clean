// DateUtils.js
// Utility for robust yyyy-MM-dd date comparison

/**
 * Parse a yyyy-MM-dd string to a Date object in UTC (avoids timezone issues).
 * @param {string} str - Date string in yyyy-MM-dd format
 * @returns {Date}
 */
export function parseYMD(str) {
  const [y, m, d] = str.split('-').map(Number);
  return new Date(Date.UTC(y, m - 1, d));
}

/**
 * Returns -1 if a < b, 1 if a > b, 0 if equal
 * @param {string} a - yyyy-MM-dd
 * @param {string} b - yyyy-MM-dd
 */
export function compareYMD(a, b) {
  const da = parseYMD(a);
  const db = parseYMD(b);
  if (da < db) return -1;
  if (da > db) return 1;
  return 0;
}

/**
 * Ensures the older date is first in [fromDate, toDate]
 * @param {string} fromDate
 * @param {string} toDate
 * @returns {[string, string]} [older, newer]
 */
export function orderDates(fromDate, toDate) {
  return compareYMD(fromDate, toDate) <= 0 ? [fromDate, toDate] : [toDate, fromDate];
}
