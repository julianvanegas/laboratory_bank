import { Box, CircularProgress } from "@mui/material";

export default function LoadingState() {
  return <Box sx={{ display: "grid", placeItems: "center", minHeight: 280 }}><CircularProgress /></Box>;
}
