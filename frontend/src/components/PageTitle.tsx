import ArrowBackRoundedIcon from "@mui/icons-material/ArrowBackRounded";
import { Box, Button, Stack, Typography } from "@mui/material";
import type { ReactNode } from "react";

type PageTitleProps = { eyebrow?: string; title: string; description?: string; backHref?: string; action?: ReactNode; icon?: ReactNode };

export default function PageTitle({ eyebrow, title, description, backHref, action, icon }: PageTitleProps) {
  return (
    <Stack direction={{ xs: "column", sm: "row" }} sx={{ justifyContent: "space-between", alignItems: { sm: "flex-start" }, gap: 2, mb: 4 }}>
      <Box>
        {backHref && <Button href={backHref} startIcon={<ArrowBackRoundedIcon />} sx={{ mb: 1, px: 0 }}>Volver</Button>}
        {eyebrow && <Typography className="eyebrow">{eyebrow}</Typography>}
        <Stack direction="row" spacing={1.25} sx={{ alignItems: "center", mt: 0.5 }}>
          {icon && <Box className="icon-tile" sx={{ flexShrink: 0, "& svg": { fontSize: 26 } }}>{icon}</Box>}
          <Typography variant="h1" sx={{ fontSize: { xs: "2rem", md: "2.8rem" } }}>{title}</Typography>
        </Stack>
        {description && <Typography color="text.secondary" sx={{ mt: 1 }}>{description}</Typography>}
      </Box>
      {action}
    </Stack>
  );
}
