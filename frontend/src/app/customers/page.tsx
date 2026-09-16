"use client";

import AddRoundedIcon from "@mui/icons-material/AddRounded";
import GroupRoundedIcon from "@mui/icons-material/GroupRounded";
import RefreshRoundedIcon from "@mui/icons-material/RefreshRounded";
import { Button, Stack } from "@mui/material";
import { useEffect, useState } from "react";
import CustomerTable from "../../components/CustomerTable";
import FeedbackSnackbar from "../../components/FeedbackSnackbar";
import LoadingState from "../../components/LoadingState";
import PageTitle from "../../components/PageTitle";
import { bankApi, type Customer } from "../api";

export default function CustomersPage() {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadCustomers = async () => {
    setLoading(true);
    try { setCustomers(await bankApi.getCustomers()); }
    catch (cause) { setError(cause instanceof Error ? cause.message : "No fue posible cargar los clientes"); }
    finally { setLoading(false); }
  };

  useEffect(() => {
    let active = true;
    bankApi.getCustomers()
      .then((list) => { if (active) setCustomers(list); })
      .catch((cause) => { if (active) setError(cause instanceof Error ? cause.message : "No fue posible cargar los clientes"); })
      .finally(() => { if (active) setLoading(false); });
    return () => { active = false; };
  }, []);

  return (
    <main className="page-shell">
      <PageTitle
        title="Clientes"
        description="Consulta la información de todos los clientes y sus cuentas."
        icon={<GroupRoundedIcon />}
        action={<Stack direction={{ xs: "column", sm: "row" }} spacing={1}><Button variant="outlined" startIcon={<RefreshRoundedIcon />} onClick={() => void loadCustomers()}>Actualizar</Button><Button href="/customers/new" variant="contained" startIcon={<AddRoundedIcon />}>Crear cliente</Button></Stack>}
      />
      {loading ? <LoadingState /> : <CustomerTable customers={customers} />}
      <FeedbackSnackbar error={error} onClose={() => setError("")} />
    </main>
  );
}
