"use client";

import { createTheme, CssBaseline, ThemeProvider } from "@mui/material";
import { AppRouterCacheProvider } from "@mui/material-nextjs/v15-appRouter";
import type { ReactNode } from "react";

const theme = createTheme({
  palette: {
    primary: { main: "#6d4aff", dark: "#4b2bb8", light: "#e9e3ff" },
    secondary: { main: "#8b7aa8" },
    background: { default: "#ffffff", paper: "#ffffff" },
    text: { primary: "#272334", secondary: "#706b7d" },
  },
  typography: {
    fontFamily: '"Roboto", "Helvetica Neue", sans-serif',
    h1: { fontWeight: 600 },
    h2: { fontWeight: 600 },
    h3: { fontWeight: 600 },
  },
  shape: { borderRadius: 10 },
  components: {
    MuiCard: { styleOverrides: { root: { border: "1px solid #eceaf2", boxShadow: "0 8px 28px rgba(54, 42, 90, 0.06)" } } },
    MuiButton: { defaultProps: { disableElevation: true } },
  },
});

export default function Providers({ children }: { children: ReactNode }) {
  return <AppRouterCacheProvider><ThemeProvider theme={theme}><CssBaseline />{children}</ThemeProvider></AppRouterCacheProvider>;
}
