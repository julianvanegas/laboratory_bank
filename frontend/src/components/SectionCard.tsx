import type { ReactNode } from "react";
import { Card, CardContent } from "@mui/material";

type SectionCardProps = { children: ReactNode };

export default function SectionCard({ children }: SectionCardProps) {
  return <Card><CardContent sx={{ p: { xs: 2.5, md: 3.5 } }}>{children}</CardContent></Card>;
}
