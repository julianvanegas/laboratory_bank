"use client";

import { Button, Stack, TextField } from "@mui/material";
import ArrowForwardRoundedIcon from "@mui/icons-material/ArrowForwardRounded";
import type { Transaction } from "../app/api";

type TransferFormProps = { value: Omit<Transaction, "id" | "timestamp">; busy?: boolean; onChange: (value: Omit<Transaction, "id" | "timestamp">) => void; onSubmit: (event: React.FormEvent<HTMLFormElement>) => void };

export default function TransferForm({ value, busy = false, onChange, onSubmit }: TransferFormProps) {
  const update = (field: keyof Omit<Transaction, "id" | "timestamp">, fieldValue: string | number) => onChange({ ...value, [field]: fieldValue });

  return (
    <Stack component="form" onSubmit={onSubmit} spacing={2} sx={{ maxWidth: 680 }}>
      <TextField required label="Cuenta de origen" value={value.senderAccountNumber} onChange={(event) => update("senderAccountNumber", event.target.value)} />
      <TextField required label="Cuenta de destino" value={value.receiverAccountNumber} onChange={(event) => update("receiverAccountNumber", event.target.value)} />
      <TextField required type="number" label="Monto" value={value.amount} onChange={(event) => update("amount", Number(event.target.value))} slotProps={{ htmlInput: { min: 0.01, step: 0.01 } }} />
      <Button type="submit" variant="contained" endIcon={<ArrowForwardRoundedIcon />} disabled={busy}>{busy ? "Procesando..." : "Realizar transferencia"}</Button>
    </Stack>
  );
}
