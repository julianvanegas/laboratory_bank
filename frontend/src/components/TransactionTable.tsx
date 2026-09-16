import ArrowDownwardRoundedIcon from "@mui/icons-material/ArrowDownwardRounded";
import ArrowUpwardRoundedIcon from "@mui/icons-material/ArrowUpwardRounded";
import { Pagination, Stack, Table, TableBody, TableCell, TableHead, TableRow, TextField, Typography } from "@mui/material";
import { useMemo, useState } from "react";
import type { Transaction } from "../app/api";

const money = new Intl.NumberFormat("es-CO", { style: "currency", currency: "COP", maximumFractionDigits: 0 });
const date = new Intl.DateTimeFormat("es-CO", { dateStyle: "medium", timeStyle: "short" });
const PAGE_SIZE = 100;

type TransactionTableProps = { transactions: Transaction[]; accountNumber?: string };

export default function TransactionTable({ transactions, accountNumber }: TransactionTableProps) {
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(1);
  const normalizedSearch = search.trim().toLocaleLowerCase();
  const filteredTransactions = useMemo(() => transactions.filter((item) => [item.senderAccountNumber, item.receiverAccountNumber, String(item.amount), item.timestamp ? date.format(new Date(item.timestamp)) : ""].some((value) => value.toLocaleLowerCase().includes(normalizedSearch))), [transactions, normalizedSearch]);
  const pageCount = Math.max(1, Math.ceil(filteredTransactions.length / PAGE_SIZE));
  const visibleTransactions = filteredTransactions.slice((page - 1) * PAGE_SIZE, page * PAGE_SIZE);

  return (
    <Stack spacing={2}>
      <TextField size="small" label="Buscar movimiento" placeholder="No. de cuenta, fecha o monto" value={search} onChange={(event) => { setSearch(event.target.value); setPage(1); }} sx={{ maxWidth: 360 }} />
      <Table size="small">
        <TableHead><TableRow><TableCell sx={{ width: 32, p: 0 }} aria-label="Tipo de movimiento" /><TableCell>Fecha</TableCell><TableCell>Origen</TableCell><TableCell>Destino</TableCell><TableCell align="right">Monto</TableCell></TableRow></TableHead>
        <TableBody>
        {visibleTransactions.map((item, index) => {
          const isOutgoing = accountNumber === item.senderAccountNumber;
          const isIncoming = accountNumber === item.receiverAccountNumber;
          const movementLabel = isOutgoing ? "Egreso" : isIncoming ? "Ingreso" : "Movimiento";
          const MovementIcon = isOutgoing ? ArrowDownwardRoundedIcon : ArrowUpwardRoundedIcon;

          return <TableRow key={item.id ?? index}><TableCell sx={{ width: 32, p: 0, textAlign: "center" }} aria-label={movementLabel}><MovementIcon titleAccess={movementLabel} fontSize="small" sx={{ color: isOutgoing ? "error.main" : "success.main", display: "block", mx: "auto" }} /></TableCell><TableCell>{item.timestamp ? date.format(new Date(item.timestamp)) : "-"}</TableCell><TableCell>{item.senderAccountNumber}</TableCell><TableCell>{item.receiverAccountNumber}</TableCell><TableCell align="right" sx={{ fontWeight: 700 }}>{money.format(item.amount || 0)}</TableCell></TableRow>;
        })}
        {visibleTransactions.length === 0 && <TableRow><TableCell colSpan={5}><Typography align="center" color="text.secondary" sx={{ py: 4 }}>{search ? "No se encontraron movimientos." : "No hay transacciones para esta cuenta."}</Typography></TableCell></TableRow>}
        </TableBody>
      </Table>
      {pageCount > 1 && <Pagination count={pageCount} page={Math.min(page, pageCount)} onChange={(_, selectedPage) => setPage(selectedPage)} color="primary" sx={{ alignSelf: "center" }} />}
    </Stack>
  );
}
