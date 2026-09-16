"use client";

import ArrowForwardRoundedIcon from "@mui/icons-material/ArrowForwardRounded";
import SendRoundedIcon from "@mui/icons-material/SendRounded";
import GroupRoundedIcon from "@mui/icons-material/GroupRounded";
import { Box, Card, CardActionArea, CardContent, Grid, Stack, Typography } from "@mui/material";

const shortcuts = [
  { title: "Clientes", description: "Consulta cuentas, saldos y datos de todos los clientes.", href: "/customers", icon: GroupRoundedIcon },
  { title: "Transacciones", description: "Realiza una transferencia entre cuentas.", href: "/transactions/new", icon: SendRoundedIcon },
];

export default function Home() {
  return (
    <main className="page-shell">
      <Stack spacing={1} sx={{ mb: 5 }}>
        <Typography variant="h1" sx={{ fontSize: { xs: "2.25rem", md: "3.4rem" } }}>Panel de control</Typography>
        <Typography color="text.secondary" sx={{ maxWidth: 560 }}>Gestiona clientes y transacciones bancarias desde un solo lugar.</Typography>
      </Stack>
      <Grid container spacing={2.5}>
        {shortcuts.map(({ title, description, href, icon: Icon }) => (
          <Grid key={title} size={{ xs: 12, md: 6 }}>
            <Card sx={{ height: "100%" }}>
              <CardActionArea href={href} sx={{ height: "100%" }}>
                <CardContent sx={{ p: 3.5, minHeight: 230, display: "flex", flexDirection: "column" }}>
                  <Box className="icon-tile"><Icon /></Box>
                  <Typography variant="h5" sx={{ mt: 3, fontWeight: 800 }}>{title}</Typography>
                  <Typography color="text.secondary" sx={{ mt: 1, lineHeight: 1.6 }}>{description}</Typography>
                  <Stack direction="row" spacing={0.5} sx={{ alignItems: "center", mt: "auto", pt: 3, color: "primary.main" }}><Typography variant="button">Abrir</Typography><ArrowForwardRoundedIcon fontSize="small" /></Stack>
                </CardContent>
              </CardActionArea>
            </Card>
          </Grid>
        ))}
      </Grid>
    </main>
  );
}
