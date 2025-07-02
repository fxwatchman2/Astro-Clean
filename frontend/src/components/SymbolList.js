import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import Divider from '@mui/material/Divider'; // Import Divider



function SymbolList({ onSelectSymbol }) {
  const [search, setSearch] = useState("");
  const [selected, setSelected] = useState(null);
  const [symbols, setSymbols] = useState([]);

  const [error, setError] = useState(null);
  useEffect(() => {
    // Fetch symbols from backend on mount
    fetch('http://localhost:8080/api/symbols')
      .then(res => {
        if (!res.ok) throw new Error('Failed to fetch symbols');
        return res.json();
      })
      .then(data => {
        setSymbols(data);
        setError(null);
      })
      .catch(() => {
        setSymbols([]);
        setError('Could not load symbols.');
      });
  }, []);

  const handleClick = (symbol) => {
    setSelected(symbol);
    if (onSelectSymbol) onSelectSymbol(symbol);
  };

  // MUI imports
  // (Place these at the top: import Box from '@mui/material/Box'; import Paper from '@mui/material/Paper'; import TextField from '@mui/material/TextField'; import List from '@mui/material/List'; import ListItem from '@mui/material/ListItem'; import ListItemButton from '@mui/material/ListItemButton'; import ListItemText from '@mui/material/ListItemText'; import Typography from '@mui/material/Typography';)
  return (
    <Box sx={{ width: '100%', height: '100%', display: 'flex', flexDirection: 'column', p: 0, m: 0, bgcolor: '#fff', border: '3px solid #1e293b', boxSizing: 'border-box' /* Removed boxShadow */ }}>
      <TextField
        variant="outlined"
        size="small"
        placeholder="Search Symbol..."
        value={search}
        onChange={e => setSearch(e.target.value)}
        sx={{ m: 1, mb: 0.5, borderRadius: 1, background: '#f8fafc' }}
        inputProps={{ style: { fontSize: 16 } }}
      />
      <Typography sx={{ fontSize: '18px', fontWeight: 700, paddingLeft: '16px', paddingRight: (theme) => theme.spacing(1), py: 1, color: '#0f172a', bgcolor: '#f3f4f6', borderBottom: '1px solid #e5e7eb', textDecoration: 'underline' /* Removed variant and letterSpacing */ }}>
        Symbol
      </Typography>
      {/* This Box is the main scrollable container for the list */}
      <Box sx={{
          direction: 'rtl', // Move scrollbar to the left for WebKit
          flex: 1, 
          overflowY: 'auto', 
          width: '100%', 
          minHeight: 0,
          // Explicit scrollbar styling
          '&::-webkit-scrollbar': {
            width: '8px',
          },
          '&::-webkit-scrollbar-track': {
            backgroundColor: (theme) => theme.palette.background.default, // Or a light grey
            borderRadius: '4px',
          },
          '&::-webkit-scrollbar-thumb': {
            backgroundColor: (theme) => theme.palette.grey[400], // A visible grey
            borderRadius: '4px',
            '&:hover': {
              backgroundColor: (theme) => theme.palette.grey[500],
            }
          },
          // For Firefox
          scrollbarWidth: 'thin',
          scrollbarColor: (theme) => `${theme.palette.grey[400]} ${theme.palette.background.default}`,
        }}>

        {symbols.length === 0 ? (
          <Typography variant="body2" color="text.secondary" sx={{ px: 2, py: 2 }}>
            {error || 'No symbols found.'}
          </Typography>
        ) : (
          // Removed redundant inner div with overflowY: 'auto'
          <List dense disablePadding sx={{ width: '100%', direction: 'ltr' }}> {/* Reset direction for list content */}
            {symbols.filter(symbol => symbol.toLowerCase().includes(search.toLowerCase())).map(symbol => (
              <React.Fragment key={symbol + '-item'}> {/* Added React.Fragment wrapper */}
                <ListItem disablePadding> {/* Removed key={symbol} from ListItem as Fragment now has a key */}
                  <ListItemButton
                    selected={selected === symbol}
                    onClick={() => handleClick(symbol)}
                    onDoubleClick={() => { if (onSelectSymbol) onSelectSymbol(symbol); }}
                    sx={(theme) => ({
                      // borderRadius: 2, // Removing individual item border radius for a more continuous list look with dividers
                      // mb: 0.5, // Removing margin bottom as dividers will provide separation
                      transition: 'background 0.2s, color 0.2s',
                      '&:hover': { 
                        bgcolor: theme.palette.action.hover, 
                      },
                      '&.Mui-selected': {
                        bgcolor: theme.palette.primary.main,
                        color: theme.palette.primary.contrastText, // This should make text color contrast with primary.main
                        '&:hover': {
                          bgcolor: theme.palette.primary.dark,
                        }
                      },
                      paddingLeft: '16px', // Align text to start at 33% of the width
                      paddingRight: (theme) => theme.spacing(1), // Keep some right padding
                      py: '4px', // Reduced vertical padding to pack items tighter
                    })}
                  >
                    <ListItemText 
                        primary={symbol} 
                        primaryTypographyProps={{ 
                            fontWeight: 500, 
                            fontSize: 16 
                            // Removed hardcoded color: '#0f172a' to allow inheritance from ListItemButton's selected state
                        }} 
                    />
                  </ListItemButton>
                </ListItem>
                <Divider component="li" />
              </React.Fragment> /* Closing React.Fragment wrapper */
            ))}
            </List>
          // Removed redundant inner div
        )}
      </Box>
      {/* Removed redundant bottom bar Box, relying on the main container's border */}
    </Box>
  );
}

SymbolList.propTypes = {
  onSelectSymbol: PropTypes.func,
};

export default SymbolList;

