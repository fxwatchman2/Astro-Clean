import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import './DiaryTabs.css';

const DiaryTabs = ({ tabs, value = 0, onChange }) => {
  const [selected, setSelected] = useState(value);
  const [infoBox, setInfoBox] = useState({ visible: false, text: '', top: 0, left: 0 });

  // Sync with parent component's value
  useEffect(() => {
    setSelected(value);
  }, [value]);

  useEffect(() => {
    const handleMouseMove = () => {
      if (infoBox.visible) {
        setInfoBox(prev => ({ ...prev, visible: false }));
      }
    };

    if (infoBox.visible) {
      window.addEventListener('mousemove', handleMouseMove);
      return () => {
        window.removeEventListener('mousemove', handleMouseMove);
      };
    }
  }, [infoBox.visible]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', height: '100%', overflow: 'hidden' }}>
      <div className="diary-tabs">
        {tabs.map((tab, idx) => (
          <div
            key={tab.id || idx}
            className={`diary-tab${selected === idx ? ' selected' : ''}`}
            onClick={(event) => {
              setSelected(idx);
              if (onChange) {
                onChange(event, idx);
              }
              if (tab.tooltip) {
                const rect = event.currentTarget.getBoundingClientRect();
                setInfoBox({
                  visible: true,
                  text: tab.tooltip,
                  top: rect.bottom + window.scrollY + 5,
                  left: rect.left + window.scrollX,
                });
              } else {
                setInfoBox(prev => ({ ...prev, visible: false }));
              }
            }}
            style={{ userSelect: 'none' }}
          >
            <span>{tab.label}</span>
          </div>
        ))}
      </div>
      <div className="diary-tab-content" style={{ flex: 1, minHeight: 0, overflow: 'hidden' }}>
        {tabs[selected]?.content}
      </div>
      {infoBox.visible && (
        <div style={{
          position: 'absolute',
          top: `${infoBox.top}px`,
          left: `${infoBox.left}px`,
          background: 'white',
          border: '1px solid #ddd',
          padding: '12px',
          borderRadius: '6px',
          boxShadow: '0 4px 12px rgba(0,0,0,0.15)',
          zIndex: 10001,
          maxWidth: '350px',
          fontSize: '14px',
          lineHeight: '1.6',
          color: '#333',
        }}>
          {infoBox.text}
        </div>
      )}
    </div>
  );
};

DiaryTabs.propTypes = {
  tabs: PropTypes.arrayOf(PropTypes.shape({
    id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    label: PropTypes.node.isRequired,
    content: PropTypes.node,
    tooltip: PropTypes.string,
  })).isRequired,
  value: PropTypes.number,
  onChange: PropTypes.func,
};

export default DiaryTabs;
