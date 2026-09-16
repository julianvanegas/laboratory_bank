"use client";

import VisibilityRoundedIcon from "@mui/icons-material/VisibilityRounded";
import { Avatar, Box, IconButton, Pagination, Stack, Table, TableBody, TableCell, TableHead, TableRow, TextField, Typography } from "@mui/material";
import { useRouter } from "next/navigation";
import { useMemo, useState } from "react";
import type { Customer } from "../app/api";

const money = new Intl.NumberFormat("es-CO", { style: "currency", currency: "COP", maximumFractionDigits: 0 });
const PAGE_SIZE = 100;

type CustomerTableProps = { customers: Customer[] };

export default function CustomerTable({ customers }: CustomerTableProps) {
  const router = useRouter();
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(1);
  const normalizedSearch = search.trim().toLocaleLowerCase();
  const filteredCustomers = useMemo(() => customers.filter((customer) => [customer.firstName, customer.lastName, customer.accountNumber, String(customer.id), String(customer.balance)].some((value) => value.toLocaleLowerCase().includes(normalizedSearch))), [customers, normalizedSearch]);
  const pageCount = Math.max(1, Math.ceil(filteredCustomers.length / PAGE_SIZE));
  const visibleCustomers = filteredCustomers.slice((page - 1) * PAGE_SIZE, page * PAGE_SIZE);

  return (
    <Stack spacing={2}>
      <TextField size="small" label="Buscar cliente" placeholder="Nombre, No. de cuenta o identificador" value={search} onChange={(event) => { setSearch(event.target.value); setPage(1); }} sx={{ maxWidth: 360 }} />
      <Table size="small">
        <TableHead><TableRow><TableCell>Cliente</TableCell><TableCell>Cuenta</TableCell><TableCell align="right">Saldo</TableCell><TableCell align="right">Acciones</TableCell></TableRow></TableHead>
        <TableBody>
          {visibleCustomers.map((customer) => (
            <TableRow key={customer.id} hover onClick={() => router.push(`/customers/${customer.id}`)} onKeyDown={(event) => { if (event.key === "Enter" || event.key === " ") router.push(`/customers/${customer.id}`); }} tabIndex={0} sx={{ cursor: "pointer" }}>
              <TableCell><Stack direction="row" spacing={1.2} sx={{ alignItems: "center" }}><Avatar sx={{ bgcolor: "primary.light", color: "primary.dark", width: 36, height: 36 }}>{customer.firstName?.[0]}{customer.lastName?.[0]}</Avatar><Box><Typography variant="body2" sx={{ fontWeight: 700 }}>{customer.firstName} {customer.lastName}</Typography><Typography variant="caption" color="text.secondary">ID {customer.id}</Typography></Box></Stack></TableCell>
              <TableCell>{customer.accountNumber}</TableCell>
              <TableCell align="right" sx={{ fontWeight: 700 }}>{money.format(customer.balance || 0)}</TableCell>
              <TableCell align="right"><IconButton component="span" aria-label={`Ver ${customer.firstName} ${customer.lastName}`} size="small"><VisibilityRoundedIcon fontSize="small" /></IconButton></TableCell>
            </TableRow>
          ))}
          {visibleCustomers.length === 0 && <TableRow><TableCell colSpan={4}><Typography align="center" color="text.secondary" sx={{ py: 4 }}>{search ? "No se encontraron clientes." : "No hay clientes registrados."}</Typography></TableCell></TableRow>}
        </TableBody>
      </Table>
      {pageCount > 1 && <Pagination count={pageCount} page={Math.min(page, pageCount)} onChange={(_, selectedPage) => setPage(selectedPage)} color="primary" sx={{ alignSelf: "center" }} />}
    </Stack>
  );
}
