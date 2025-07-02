import React, { useState } from 'react';
import PropTypes from 'prop-types';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import Button from '@mui/material/Button';

import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';

/** Dialog for managing the Current Study Group (CSG) */

function CurrentStudyGroupDialog({
  open = false,
  onClose = () => {},
  csg = { name: '', studies: [] },
  onDeleteStudy = () => {},
  onDeleteAll = () => {},
  onSaveCSG = () => {},
  onRunCSG = () => {}
} = {}) {
  const [selectedIdx, setSelectedIdx] = useState(null);
  const [runError, setRunError] = useState(null);

  const handleRunCSG = async () => {
    setRunError(null);
    try {
      await Promise.resolve(onRunCSG());
      onClose(); // Close dialog after successful run
    } catch (err) {
      setRunError(err?.message || 'Failed to run CSG');
    }
  };


  return (
    <Dialog open={open} onClose={onClose} maxWidth="md" fullWidth>
      <Box border="1px solid #222" borderRadius={0.5}>
        <DialogTitle sx={{ fontWeight: 'bold', fontSize: 22, borderBottom: '2px solid #222', background: '#fff' }}>
          CSG (Current Study Group) Manager
        </DialogTitle>
        <DialogContent sx={{ background: '#fff', p: 3 }}>
          <Typography variant="h6" align="center" sx={{ fontWeight: 'bold', mb: 3, fontSize: 22 }}>
            Studies in Group
          </Typography>
          {/* Studies List Section - scrollable */}
          <Box sx={{ minWidth: 420, mb: 3 }}>
            {csg?.studies?.length === 0 ? (
              <Box sx={{ p: 2, fontStyle: 'italic', color: '#888', textAlign: 'center' }}>
                No studies in group
              </Box>
            ) : (
              <Box sx={{ maxHeight: 280, overflowY: 'auto', borderRadius: 2, border: '1px solid #eee', bgcolor: '#fafbfc', p: 0 }}>
                {csg?.studies?.map((study, idx) => (
                  <Box
                    key={idx}
                    sx={{
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'space-between',
                      borderBottom: idx === csg.studies.length - 1 ? 'none' : '1px solid #eee',
                      px: 2,
                      py: 1.5,
                      bgcolor: selectedIdx === idx ? '#e3f2fd' : 'inherit',
                      boxShadow: selectedIdx === idx ? 2 : 0,
                      cursor: 'pointer',
                      transition: 'background 0.2s, box-shadow 0.2s',
                    }}
                    onClick={() => setSelectedIdx(idx)}
                  >
                    <Box sx={{ flex: 1 }}>
                      <Typography sx={{ fontWeight: 'bold', fontSize: 17 }}>
                        {`${(study.type || '').toLowerCase()}-${study.planet1 || ''}-${study.planet2 || ''}-${study.angle || ''}-${(study.frameOfReference || '').toLowerCase()}`}
                      </Typography>
                      {study.components && (
                        <Typography sx={{ fontSize: 14, color: '#666', mt: 0.5 }}>
                          Components: {study.components}
                        </Typography>
                      )}
                    </Box>
                    <Button
                      variant="outlined"
                      color="error"
                      size="small"
                      sx={{ ml: 2, minWidth: 36, px: 1, py: 0.5 }}
                      onClick={e => { e.stopPropagation(); onDeleteStudy(idx); }}
                    >
                      Delete
                    </Button>
                  </Box>
                ))}
              </Box>
            )}
          </Box>
        </DialogContent>
        <DialogActions sx={{ justifyContent: 'flex-end', px: 3, pb: 2, background: '#fff', mt: 2, gap: 2 }}>
          <Button
            variant="outlined"
            color="primary"
            onClick={onDeleteAll}
            sx={{ minWidth: 110 }}
          >
            Delete All
          </Button>
          <Button
            variant="outlined"
            color="primary"
            onClick={onSaveCSG}
            sx={{ minWidth: 90 }}
          >
            Save
          </Button>
          <Button
            variant="contained"
            color="primary"
            onClick={handleRunCSG}
            disabled={!csg?.studies?.length}
            sx={{ minWidth: 110 }}
          >
            RUN CSG
          </Button>
          {runError && (
            <Typography color="error" sx={{ ml: 2, fontSize: 15 }}>
              Error: {runError}
            </Typography>
          )}
          <Button
            variant="outlined"
            color="secondary"
            onClick={onClose}
            sx={{ minWidth: 90 }}
          >
            Close
          </Button>
        </DialogActions>
      </Box>
    </Dialog>
  );
}

CurrentStudyGroupDialog.propTypes = {
  onDeleteAll: PropTypes.func,
  onSaveCSG: PropTypes.func,
  onRunCSG: PropTypes.func,
  open: PropTypes.bool,
  onClose: PropTypes.func,
  csg: PropTypes.shape({
    name: PropTypes.string,
    studies: PropTypes.arrayOf(PropTypes.object)
  }),
  onDeleteStudy: PropTypes.func,
};

export default CurrentStudyGroupDialog;
