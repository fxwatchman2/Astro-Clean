import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import { ChartOverlayProvider } from './context/ChartOverlayContext';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <ChartOverlayProvider>
    <App />
  </ChartOverlayProvider>
);
