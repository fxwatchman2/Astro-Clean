import React from 'react';
import PropTypes from 'prop-types';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import Button from '@mui/material/Button';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';

const SymbolDialog = ({ open, onClose, symbols }) => (
  <Dialog open={open} onClose={onClose}>
    <DialogTitle>Symbol List</DialogTitle>
    <DialogContent>
      <List>
        {symbols.map((sym, idx) => (
          <ListItem key={idx}>{sym}</ListItem>
        ))}
      </List>
    </DialogContent>
    <DialogActions>
      <Button onClick={onClose} variant="contained" color="primary">Close</Button>
    </DialogActions>
  </Dialog>
);

SymbolDialog.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  symbols: PropTypes.array.isRequired,
};

export default SymbolDialog;
