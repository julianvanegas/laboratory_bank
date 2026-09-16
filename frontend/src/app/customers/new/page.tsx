"use client";

import PersonAddAlt1RoundedIcon from "@mui/icons-material/PersonAddAlt1Rounded";
import { Alert } from "@mui/material";
import { useRouter } from "next/navigation";
import { useState } from "react";
import CustomerForm from "../../../components/CustomerForm";
import PageTitle from "../../../components/PageTitle";
import { bankApi, type Customer } from "../../api";

const emptyCustomer: Omit<Customer, "id"> = { firstName: "", lastName: "", accountNumber: "", balance: 0 };

export default function NewCustomerPage() {
  const router = useRouter();
  const [value, setValue] = useState(emptyCustomer);
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState("");

  const submit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault(); setBusy(true); setError("");
    try { const created = await bankApi.createCustomer(value); router.push(`/customers/${created.id}`); }
    catch (cause) { setError(cause instanceof Error ? cause.message : "No fue posible crear el cliente"); }
    finally { setBusy(false); }
  };

  return (
    <main className="page-shell">
      <PageTitle backHref="/customers" title="Crear cliente" description="Registra una nueva cuenta para empezar a operar." icon={<PersonAddAlt1RoundedIcon />} />
      {error && <Alert severity="error" sx={{ mb: 2, maxWidth: 680 }}>{error}</Alert>}
      <CustomerForm value={value} busy={busy} onChange={setValue} onSubmit={submit} />
    </main>
  );
}
