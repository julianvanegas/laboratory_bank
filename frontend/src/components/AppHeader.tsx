import AccountBalanceRoundedIcon from "@mui/icons-material/AccountBalanceRounded";
import { AppBar, Box, Button, Stack, Toolbar, Typography } from "@mui/material";

const navigation = [
  { label: "Inicio", href: "/" },
  { label: "Clientes", href: "/customers" },
  { label: "Transacciones", href: "/transactions/new" },
];

export default function AppHeader() {
  return (
    <AppBar position="sticky" color="inherit" elevation={0} className="app-header">
      <Toolbar sx={{ width: "100%", maxWidth: 1180, mx: "auto", px: { xs: 2, sm: 3 } }}>
        <Button href="/" color="inherit" sx={{ p: 0, minWidth: 0, textTransform: "none" }}>
          <Stack direction="row" spacing={1.2} sx={{ alignItems: "center" }}>
            <Box className="brand-mark"><AccountBalanceRoundedIcon /></Box>
            <Typography variant="h6" sx={{ fontWeight: 800 }}>Banco</Typography>
          </Stack>
        </Button>
        <Box sx={{ flexGrow: 1 }} />
        <Stack direction="row" spacing={1}>
          {navigation.map((item) => (
            <Button key={item.href} href={item.href} color="inherit">{item.label}</Button>
          ))}
        </Stack>
      </Toolbar>
    </AppBar>
  );
}
