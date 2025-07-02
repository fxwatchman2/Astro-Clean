import React, { createContext, useContext, useState } from 'react';

const ChartOverlayContext = createContext();

export function ChartOverlayProvider({ children }) {
  const [overlays, setOverlays] = useState([]);

  const addOverlay = (overlay) => {
  console.log('[OverlayContext] Adding overlay:', overlay);
  setOverlays((prev) => {
    const updated = [...prev, overlay];
    console.log('[OverlayContext] Overlays after add:', updated);
    return updated;
  });
};
  const clearOverlays = () => {
  console.log('[OverlayContext] Clearing all overlays');
  setOverlays([]);
};
  const removeOverlay = (id) => {
  console.log('[OverlayContext] Removing overlay with id:', id);
  setOverlays((prev) => {
    const updated = prev.filter((o) => o.id !== id);
    console.log('[OverlayContext] Overlays after remove:', updated);
    return updated;
  });
};

  return (
    <ChartOverlayContext.Provider value={{ overlays, addOverlay, clearOverlays, removeOverlay }}>
      {children}
    </ChartOverlayContext.Provider>
  );
}

export function useChartOverlays() {
  return useContext(ChartOverlayContext);
}
