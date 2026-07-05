import React from 'react';
import ReactDOM from 'react-dom/client';
import { CssBaseline, ThemeProvider, createTheme } from '@mui/material';
import App from './App';
import './styles.css';

const theme = createTheme({
  palette: {
    primary: { main: '#1b5e57' },
    secondary: { main: '#b35c2e' },
    background: { default: '#f6f7f4' }
  },
  shape: { borderRadius: 8 },
  typography: {
    fontFamily: ['Inter', 'Segoe UI', 'Arial', 'sans-serif'].join(','),
    h1: { fontSize: '1.75rem', fontWeight: 700, letterSpacing: 0 },
    h2: { fontSize: '1.2rem', fontWeight: 700, letterSpacing: 0 },
    button: { textTransform: 'none', fontWeight: 700, letterSpacing: 0 }
  }
});

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <App />
    </ThemeProvider>
  </React.StrictMode>
);
