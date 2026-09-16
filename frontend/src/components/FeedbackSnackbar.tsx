"use client";

import { Alert, Snackbar } from "@mui/material";

type FeedbackSnackbarProps = {
  error?: string;
  notice?: string;
  onClose: () => void;
};

export default function FeedbackSnackbar({ error = "", notice = "", onClose }: FeedbackSnackbarProps) {
  const message = error || notice;

  return (
    <Snackbar open={Boolean(message)} autoHideDuration={5000} onClose={onClose}>
      <Alert severity={error ? "error" : "success"} onClose={onClose}>{message}</Alert>
    </Snackbar>
  );
}
