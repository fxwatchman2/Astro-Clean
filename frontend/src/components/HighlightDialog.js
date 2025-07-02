import React, { useState } from 'react';

const HighlightDialog = ({ isOpen, onClose, onSave }) => {
  if (!isOpen) {
    return null;
  }

  // TODO: Add state for form inputs (startDate, endDate, fillType, fillValue)
  // TODO: Add form elements (date pickers, dropdowns, color input)
  // TODO: Add save and cancel logic

  return (
    <div style={styles.overlay}>
      <div style={styles.dialog}>
        <h2>Add Background Highlight</h2>
        <p>Dialog content will go here...</p>
        <button onClick={onClose}>Cancel</button>
        {/* <button onClick={handleSave}>Save</button> */}
      </div>
    </div>
  );
};

const styles = {
  overlay: {
    position: 'fixed',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    zIndex: 1000,
  },
  dialog: {
    backgroundColor: '#fff',
    padding: '20px',
    borderRadius: '8px',
    boxShadow: '0 2px 10px rgba(0, 0, 0, 0.1)',
    color: '#333', // Ensuring text is visible on white background
  },
};

export default HighlightDialog;
