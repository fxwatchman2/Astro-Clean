// StudyGroupTypes.js
// TypeScript-style JSDoc types for StudyGroup and Study, for use in JS codebases

/**
 * @typedef {Object} PlanetIngressSignStudy
 * @property {'planet-ingress-sign'} type
 * @property {string} planet
 * @property {string} sign
 * @property {string} zodiacType
 */

/**
 * @typedef {Object} PlanetInSignStudy
 * @property {'planet-in-sign'} type
 * @property {string} planet
 * @property {string} sign
 * @property {string} zodiacType
 */

/**
 * @typedef {Object} PlanetPlanetAngleStudy
 * @property {'planet-planet-angle'} type
 * @property {string} planet1
 * @property {string} planet2
 * @property {number} angle
 * @property {string} zodiacType
 */

/**
 * @typedef {PlanetIngressSignStudy | PlanetInSignStudy | PlanetPlanetAngleStudy} Study
 */

/**
 * @typedef {Object} StudyGroup
 * @property {string} name
 * @property {string} createdAt // ISO string
 * @property {string} updatedAt // ISO string
 * @property {Study[]} studies
 */

// Example usage:
// /** @type {StudyGroup} */
// const csg = { ... };

export {};
