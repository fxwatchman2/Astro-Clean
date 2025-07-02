import { createTheme } from '@mui/material/styles';

export const coolWhiteTheme = createTheme({
  palette: {
    mode: 'light',
    primary: { main: '#1976d2' },
    background: { default: '#f9fafe', paper: '#fff' },
    text: { primary: '#222', secondary: '#555' },
  },
  typography: {
    fontFamily: '"Nunito", "Roboto", "Helvetica", "Arial", sans-serif',
  },
});

export const darkTheme = createTheme({
  palette: {
    mode: 'dark',
    primary: { main: '#1976d2' },
    background: { default: '#181c23', paper: '#23272f' },
    text: { primary: '#fff', secondary: '#b0b8c1' },
  },
  typography: {
    fontFamily: '"Nunito", "Roboto", "Helvetica", "Arial", sans-serif',
  },
});

export const skyBlueTheme = createTheme({
  palette: {
    mode: 'light',
    primary: { main: '#38bdf8' },
    background: { default: '#e0f7fa', paper: '#b3e5fc' },
    text: { primary: '#155774', secondary: '#4f6e8c' },
  },
  typography: {
    fontFamily: '"Nunito", "Roboto", "Helvetica", "Arial", sans-serif',
  },
});

export const feminineTheme = createTheme({
  palette: {
    mode: 'light',
    primary: { main: '#e573b5' },
    background: { default: '#fff0f6', paper: '#ffe4ec' },
    text: { primary: '#a21caf', secondary: '#d946ef' },
  },
  typography: {
    fontFamily: '"Nunito", "Roboto", "Helvetica", "Arial", sans-serif',
  },
});
