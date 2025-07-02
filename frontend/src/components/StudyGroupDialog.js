import React, { useState } from 'react';
import PropTypes from 'prop-types';
import {
  Dialog, DialogTitle, DialogContent,
  Button, Box, List, ListItem, TextField, Typography
} from '@mui/material';

function StudyGroupDialog({ open, onClose, onSave, studyGroups = [], components = [], onDeleteComponent }) {
  // Local state for selected group/component/description
  const [selectedGroup, setSelectedGroup] = useState(null);
  const [description, setDescription] = useState('');
  const [selectedComponent, setSelectedComponent] = useState(null);

  // Handlers
  const handleGroupSelect = (group) => {
    setSelectedGroup(group);
    setDescription(group?.description || '');
    setSelectedComponent(null);
  };
  const handleComponentSelect = (component) => {
    setSelectedComponent(component);
  };
  const handleDeleteComponent = () => {
    if (onDeleteComponent && selectedComponent) {
      onDeleteComponent(selectedComponent);
      setSelectedComponent(null);
    }
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="md" fullWidth>
      <DialogTitle sx={{ fontWeight: 'bold' }}>Study Group</DialogTitle>
      <DialogContent>
        <Box display="flex" flexDirection="row" gap={2}>
          {/* Left vertical buttons */}
          <Box display="flex" flexDirection="column" justifyContent="space-between" alignItems="center" py={1}>
            <Button variant="contained" color="primary" sx={{ mb: 2, minWidth: 80 }} onClick={onSave}>Save</Button>
            <Button variant="outlined" color="secondary" sx={{ mt: 2, minWidth: 80 }} onClick={onClose}>Close</Button>
          </Box>

          {/* Main content */}
          <Box flex={1} display="flex" flexDirection="column" gap={2}>
            {/* Top: Study Group List */}
            <Box>
              <Typography variant="subtitle1" sx={{ mb: 1 }}>Study Groups</Typography>
              <List sx={{ border: '1px solid #ccc', borderRadius: 1, maxHeight: 100, overflow: 'auto' }}>
                {studyGroups.length === 0 && <ListItem disabled>No study groups</ListItem>}
                {studyGroups.map((group, idx) => (
                  <ListItem
                    button
                    key={idx}
                    selected={selectedGroup === group}
                    onClick={() => handleGroupSelect(group)}
                  >
                    {group.name}
                  </ListItem>
                ))}
              </List>
            </Box>

            {/* Bottom: Description and Components */}
            <Box display="flex" flexDirection="row" gap={2}>
              {/* Description */}
              <Box flex={1}>
                <Typography variant="subtitle1" sx={{ mb: 1 }}>Description</Typography>
                <TextField
                  multiline
                  minRows={4}
                  fullWidth
                  variant="outlined"
                  placeholder="Enter description"
                  value={description}
                  onChange={e => setDescription(e.target.value)}
                />
              </Box>

              {/* Components List */}
              <Box flex={1}>
                <Typography variant="subtitle1" sx={{ mb: 1 }}>Components</Typography>
                <List sx={{ border: '1px solid #ccc', borderRadius: 1, maxHeight: 100, overflow: 'auto' }}>
                  {components.length === 0 && <ListItem disabled>No components</ListItem>}
                  {components.map((comp, idx) => (
                    <ListItem
                      button
                      key={idx}
                      selected={selectedComponent === comp}
                      onClick={() => handleComponentSelect(comp)}
                    >
                      {comp.name || comp}
                    </ListItem>
                  ))}
                </List>
                <Button
                  variant="outlined"
                  color="error"
                  sx={{ mt: 1, minWidth: 80 }}
                  onClick={handleDeleteComponent}
                  disabled={!selectedComponent}
                >
                  Delete
                </Button>
              </Box>
            </Box>
          </Box>
        </Box>
      </DialogContent>
    </Dialog>
  );
}

StudyGroupDialog.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  onSave: PropTypes.func.isRequired,
  studyGroups: PropTypes.array,
  components: PropTypes.array,
  onDeleteComponent: PropTypes.func.isRequired,
};

export default StudyGroupDialog;
