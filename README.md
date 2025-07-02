# AstroCharts

AstroCharts is a full-stack application with a Java Spring Boot backend and a React frontend. It provides interactive financial charts and symbol data using TradingView Lightweight Charts and Material-UI (MUI).

---

## Current Features & State

### Frontend (React)
- Uses Material-UI (MUI) for a modern, responsive UI.
- Symbol list panel with search and selection.
- Main chart panel displays candlestick (OHLCV) data and a volume histogram using `lightweight-charts`.
- Robust error handling and loading states.
- Automatically sorts bar data by time for correct chart rendering.
- Logs raw and mapped data to browser console for debugging.

### Backend (Spring Boot)
- REST API endpoints:
  - `/api/symbols` and `/api/getSymbolList` — return all available ticker symbols.
  - `/api/getDataForSymbol/{symbol}` — returns full OHLCV data for a symbol.
  - `/api/getDataForTwoSymbols/{symbol1}/{symbol2}` — returns OHLCV data for two symbols.
- Uses a thread-safe cache (`SharedCacheService` and `AllListsManager`) to load and serve data efficiently.
- Diagnostic logging on startup to confirm ticker and data loading (especially for AAPL).

---

## How to Run

### Backend
1. Ensure your data files (e.g., `tickerlist.txt` and per-symbol data) are present and accessible.
2. Build and run the backend:
   ```sh
   cd backend
   mvn clean package -DskipTests
   java -jar target/*.jar
   ```
3. The backend runs on port 8080 by default.

### Frontend
1. Install dependencies and build:
   ```sh
   cd frontend
   npm install
   npm run build
   npm start
   ```
2. The frontend runs on port 3000 by default.

---

## Troubleshooting

- **No symbols or data loaded:**
  - Check backend logs for file path or data loading errors.
  - Ensure your data files exist and are readable.
- **Chart does not display:**
  - Open browser console for logs and mapping errors.
  - Confirm that the backend returns data and that the frontend sorts it by time.
- **AAPL missing or empty:**
  - See backend startup logs for diagnostic messages.

---

## Next Steps (for future work)
- Enable dynamic symbol selection and chart updates.
- Add more chart features and UI enhancements.
- Add automated tests for backend and frontend.
- Optimize performance for large datasets.

---

**All core objectives for this session have been accomplished. The app is ready for further development tomorrow.**

## Structure
- `backend/`: Java Spring Boot REST API
- `frontend/`: React web app with charting

## Getting Started
See `backend/README.md` and `frontend/README.md` for setup instructions.
