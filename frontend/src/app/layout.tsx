import type { Metadata } from "next";
import "./globals.css";
import Providers from "./providers";
import AppHeader from "../components/AppHeader";

export const metadata: Metadata = {
  title: "Laboratory Bank | Operaciones",
  description: "Panel de operaciones para Laboratory Bank",
};

export default function RootLayout({ children }: LayoutProps<"/">) {
  return (
    <html lang="es">
      <body><Providers><AppHeader />{children}</Providers></body>
    </html>
  );
}
