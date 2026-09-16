"use client";

import SendRoundedIcon from "@mui/icons-material/SendRounded";
import ArrowBackRoundedIcon from "@mui/icons-material/ArrowBackRounded";
import { Alert, Avatar, Box, Button, Stack, Typography } from "@mui/material";
import { useParams } from "next/navigation";
import { useEffect, useState } from "react";
import FeedbackSnackbar from "../../../components/FeedbackSnackbar";
import LoadingState from "../../../components/LoadingState";
import TransactionTable from "../../../components/TransactionTable";
import { bankApi, type Customer, type Transaction } from "../../api";

const money = new Intl.NumberFormat("es-CO", { style: "currency", currency: "COP", maximumFractionDigits: 0 });

export default function CustomerDetailPage() {
  const params = useParams<{ id: string }>();
  const [customer, setCustomer] = useState<Customer | null>(null);
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const load = async () => {
      setLoading(true);
      try {
        const detail = await bankApi.getCustomer(Number(params.id));
        setCustomer(detail);
        setTransactions(await bankApi.getTransactions(detail.accountNumber));
      } catch (cause) { setError(cause instanceof Error ? cause.message : "No fue posible cargar el cliente"); }
      finally { setLoading(false); }
    };
    void load();
  }, [params.id]);

  if (loading) return <main className="page-shell"><LoadingState /></main>;
  if (!customer) return <main className="page-shell"><Alert severity="error">No se encontró el cliente.</Alert></main>;

  return (
    <main className="page-shell">
      <Button href="/customers" startIcon={<ArrowBackRoundedIcon />} sx={{ mb: 3, px: 0 }}>Volver</Button>
      <Stack direction={{ xs: "column", md: "row" }} spacing={3} sx={{ justifyContent: "space-between", alignItems: { md: "flex-start" }, mb: 6 }}>
        <Stack direction="row" spacing={2} sx={{ alignItems: "center" }}>
          <Avatar sx={{ width: 76, height: 76, bgcolor: "primary.light", color: "primary.dark", fontSize: "1.6rem" }}>{customer.firstName[0]}{customer.lastName[0]}</Avatar>
          <Box>
            <Typography variant="h1" sx={{ fontSize: { xs: "2rem", md: "2.8rem" } }}>{customer.firstName} {customer.lastName}</Typography>
            <Typography color="text.secondary">No. de cuenta: {customer.accountNumber}</Typography>
            <Typography color="text.secondary">Identificador: {customer.id}</Typography>
          </Box>
        </Stack>
        <Stack spacing={1} sx={{ alignItems: { md: "flex-end" } }}>
          <Typography variant="body2" color="text.secondary">Saldo de la cuenta</Typography>
          <Typography variant="h5" color="primary.dark" sx={{ fontWeight: 800 }}>{money.format(customer.balance || 0)}</Typography>
          <Button href={`/transactions/new?from=${encodeURIComponent(customer.accountNumber)}&customerId=${customer.id}`} variant="contained" startIcon={<SendRoundedIcon/>}>Transferir</Button>
        </Stack>
      </Stack>
      <Typography variant="h5" sx={{ mb: 2, fontWeight: 800 }}>Movimientos</Typography>
      <TransactionTable transactions={transactions} accountNumber={customer.accountNumber} />
      <FeedbackSnackbar error={error} onClose={() => setError("")} />
    </main>
  );
}
