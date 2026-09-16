"use client";

import { Button, Stack, TextField } from "@mui/material";
import AddRoundedIcon from "@mui/icons-material/AddRounded";
import type { Customer } from "../app/api";

type CustomerFormProps = { value: Omit<Customer, "id">; busy?: boolean; onChange: (value: Omit<Customer, "id">) => void; onSubmit: (event: React.FormEvent<HTMLFormElement>) => void };

export default function CustomerForm({ value, busy = false, onChange, onSubmit }: CustomerFormProps) {
  const update = (field: keyof Omit<Customer, "id">, fieldValue: string | number) => onChange({ ...value, [field]: fieldValue });

  return (
    <Stack component="form" onSubmit={onSubmit} spacing={2} sx={{ maxWidth: 680 }}>
      <Stack direction={{ xs: "column", sm: "row" }} spacing={2}><TextField required fullWidth label="Nombre" value={value.firstName} onChange={(event) => update("firstName", event.target.value)} /><TextField required fullWidth label="Apellido" value={value.lastName} onChange={(event) => update("lastName", event.target.value)} /></Stack>
      <TextField required label="Número de cuenta" value={value.accountNumber} onChange={(event) => update("accountNumber", event.target.value)} />
      <TextField required type="number" label="Saldo inicial" value={value.balance} onChange={(event) => update("balance", Number(event.target.value))} slotProps={{ htmlInput: { min: 0.01, step: 0.01 } }} />
      <Button type="submit" variant="contained" endIcon={<AddRoundedIcon />} disabled={busy}>{busy ? "Guardando..." : "Crear cliente"}</Button>
    </Stack>
  );
}
