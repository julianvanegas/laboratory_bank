"use client";

import SendRoundedIcon from "@mui/icons-material/SendRounded";
import { Alert } from "@mui/material";
import { useRouter, useSearchParams } from "next/navigation";
import { Suspense, useState } from "react";
import TransferForm from "../../../components/TransferForm";
import PageTitle from "../../../components/PageTitle";
import { bankApi, type Transaction } from "../../api";

const emptyTransfer: Omit<Transaction, "id" | "timestamp"> = { senderAccountNumber: "", receiverAccountNumber: "", amount: 0 };

function NewTransactionForm() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [value, setValue] = useState(() => ({ ...emptyTransfer, senderAccountNumber: searchParams.get("from") ?? "" }));
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState("");

  const submit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault(); setBusy(true); setError("");
    try { await bankApi.transfer(value); router.push(searchParams.get("customerId") ? `/customers/${searchParams.get("customerId")}` : "/"); }
    catch (cause) { setError(cause instanceof Error ? cause.message : "No fue posible realizar la transferencia"); }
    finally { setBusy(false); }
  };

  return (
    <main className="page-shell">
      <PageTitle backHref={searchParams.get("from") ? "/customers" : "/"} title="Nueva transacción" description="Realiza una transferencia entre dos cuentas." icon={<SendRoundedIcon />} />
      {error && <Alert severity="error" sx={{ mb: 2, maxWidth: 680 }}>{error}</Alert>}
      <TransferForm value={value} busy={busy} onChange={setValue} onSubmit={submit} />
    </main>
  );
}

export default function NewTransactionPage() {
  return <Suspense fallback={<main className="page-shell" />}><NewTransactionForm /></Suspense>;
}
