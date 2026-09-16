import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  async rewrites() {
    const backendUrl = process.env.BACKEND_URL;
    if (!backendUrl) {
      throw new Error("BACKEND_URL debe estar definida en el entorno");
    }

    return [{
      source: "/api/backend/:path*",
      destination: `${backendUrl}/api/:path*`,
    }];
  },
};

export default nextConfig;
