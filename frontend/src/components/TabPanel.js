import React from 'react';
import PropTypes from 'prop-types';
import { Box, Tooltip, IconButton } from '@mui/material';



import TimelineIcon from '@mui/icons-material/Timeline';
import ShowChartIcon from '@mui/icons-material/ShowChart';
import GroupWorkIcon from '@mui/icons-material/GroupWork';
import NightsStayIcon from '@mui/icons-material/NightsStay';
import TravelExploreIcon from '@mui/icons-material/TravelExplore';
import PublicIcon from '@mui/icons-material/Public';
import StarIcon from '@mui/icons-material/Star';
import AutoAwesomeIcon from '@mui/icons-material/AutoAwesome';
import FlareIcon from '@mui/icons-material/Flare';
import FilterVintageIcon from '@mui/icons-material/FilterVintage';
import AdjustIcon from '@mui/icons-material/Adjust';
import ArchitectureIcon from '@mui/icons-material/Architecture';
import HistoryEduIcon from '@mui/icons-material/HistoryEdu';
import FunctionsIcon from '@mui/icons-material/Functions';
import ChangeHistoryIcon from '@mui/icons-material/ChangeHistory';
import DonutLargeIcon from '@mui/icons-material/DonutLarge';
import TrackChangesIcon from '@mui/icons-material/TrackChanges';
import BarChartIcon from '@mui/icons-material/BarChart';

const displayTooltips = true;

const ICONS = [
  { component: <img src={"/images/LongiStudy.png"} style={{ width: 36, height: 36, objectFit: 'contain', display: 'block' }} />, tooltip: 'Study of well known longitudinal aspects or any angular distance (from 0 to 180) between two planets', action: 'open_controls' },
  { component: <img src={"/images/declination.png"} style={{ width: 36, height: 36, objectFit: 'contain', display: 'block' }} />, tooltip: 'Declination Study', action: 'open_declination_study' },
  { component: <TimelineIcon />, tooltip: 'Special Planetary Studies', action: 'open_special_studies' },
  { component: <ShowChartIcon />, tooltip: 'Planet-A Follows Planet-B Study', action: 'open_planet_follow_study' },
  { component: <GroupWorkIcon />, tooltip: 'Planetary Lines Study', action: 'open_planetary_lines_study' },
  { component: <NightsStayIcon />, tooltip: 'Visual Universe', action: 'open_visual_universe' },
  { component: <TravelExploreIcon />, tooltip: 'Explore Planetary Data (Not Implemented)' },
  { component: <PublicIcon />, tooltip: 'World Events (Not Implemented)' },
  { component: <StarIcon />, tooltip: 'Fixed Stars (Not Implemented)' },
  { component: <AutoAwesomeIcon />, tooltip: 'Special Aspects (Not Implemented)' },
  { component: <FlareIcon />, tooltip: 'Solar Flares (Not Implemented)' },
  { component: <FilterVintageIcon />, tooltip: 'Traditional Techniques (Not Implemented)' },
  { component: <AdjustIcon />, tooltip: 'Precision Tools (Not Implemented)' },
  { component: <ArchitectureIcon />, tooltip: 'Geometric Patterns (Not Implemented)' },
  { component: <HistoryEduIcon />, tooltip: 'Historical Ephemeris (Not Implemented)' },
  { component: <FunctionsIcon />, tooltip: 'Custom Formulas (Not Implemented)' },
  { component: <ChangeHistoryIcon />, tooltip: 'Cycle Analysis (Not Implemented)' },
  { component: <DonutLargeIcon />, tooltip: 'Zodiac Wheels (Not Implemented)' },
  { component: <TrackChangesIcon />, tooltip: 'Track Planets (Not Implemented)' },
  // Chart style toggle icon is injected dynamically as the last icon
];

const TabPanel = ({ onOpenControlsDialog, onOpenPlanetFollowStudyDialog, onOpenSpecialStudiesDialog, onOpenDeclinationStudyDialog, onOpenPlanetaryLinesDialog, onOpenVisualUniverseDialog, chartType, onToggleChartType }) => {
  const handleIconClick = (action) => {
    switch (action) {
      case 'open_controls':
      case 'draw_line':
        if (onOpenControlsDialog) onOpenControlsDialog();
        break;
      case 'open_planet_follow_study':
        if (onOpenPlanetFollowStudyDialog) onOpenPlanetFollowStudyDialog();
        break;
      case 'open_special_studies':
        if (onOpenSpecialStudiesDialog) onOpenSpecialStudiesDialog();
        break;
      case 'open_declination_study':
        if (onOpenDeclinationStudyDialog) onOpenDeclinationStudyDialog();
        break;
      case 'open_planetary_lines_study':
        if (onOpenPlanetaryLinesDialog) onOpenPlanetaryLinesDialog();
        break;
      case 'open_visual_universe':
        if (onOpenVisualUniverseDialog) onOpenVisualUniverseDialog();
        break;
      default:
        break;
    }
  };

  return (
    <Box sx={{ display: 'flex', flexDirection: 'row', justifyContent: 'flex-start', alignItems: 'center', p: 0.5 }}>
      {ICONS.map((icon, index) => (
        <Tooltip key={index} title={icon.tooltip} placement="bottom" disableHoverListener={!displayTooltips}>
          <IconButton size="small" onClick={() => handleIconClick(icon.action)} sx={{ p: 0.5, mx: 0.25 }}>
            {icon.component}
          </IconButton>
        </Tooltip>
      ))}
      {/* Chart style toggle icon as last icon */}
      <Tooltip title="Toggles the chart style from bar chart to line chart and then back to bar chart." placement="bottom" disableHoverListener={!displayTooltips}>
        <IconButton
  size="small"
  onClick={() => {
    onToggleChartType();
  }}
  sx={{ p: 0.5, mx: 0.25 }}
>
          {chartType === 'line' ? <BarChartIcon style={{ color: '#1976d2' }} fontSize="small" /> : <ShowChartIcon style={{ color: '#1976d2' }} fontSize="small" />}
        </IconButton>
      </Tooltip>
    </Box>
  );
};

TabPanel.propTypes = {
  onOpenControlsDialog: PropTypes.func.isRequired,
  onOpenPlanetFollowStudyDialog: PropTypes.func.isRequired,
  onOpenSpecialStudiesDialog: PropTypes.func.isRequired,
  onOpenDeclinationStudyDialog: PropTypes.func.isRequired,
  onOpenPlanetaryLinesDialog: PropTypes.func.isRequired,
  onOpenVisualUniverseDialog: PropTypes.func.isRequired,
  chartType: PropTypes.oneOf(['candlestick', 'line']).isRequired,
  onToggleChartType: PropTypes.func.isRequired,
};

export default TabPanel;
