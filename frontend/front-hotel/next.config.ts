import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  async rewrites() {
    // Usar variable de entorno para la URL del backend
    // En Docker usa 'backend', en desarrollo local usa 'localhost'
    const backendUrl = process.env.NEXT_PUBLIC_BACKEND_URL || 'http://backend:8080';
    
    return [
      {
        source: '/api/:path*',
        destination: `${backendUrl}/:path*`, // Proxy al backend
      },
    ];
  },
};

export default nextConfig;
