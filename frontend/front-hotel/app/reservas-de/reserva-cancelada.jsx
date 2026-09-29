"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";

export function ReservaCancelada() {
  const router = useRouter();

  useEffect(() => {
    const handleKey = () => {
      router.push("/home");
    };

    window.addEventListener("keydown", handleKey);

    return () => {
      window.removeEventListener("keydown", handleKey);
    };
  }, [router]);

  return (
    <div className="error-overlay">
      <div className="cancelar-contenedor-principal">
        <div className="error-titulo-box">
          <p className="presione-cualquier-tecla-texto">
            Reserva/s cancelada/s<br />
            PRESIONE UNA TECLA PARA CONTINUAR...
          </p>
        </div>
      </div>
    </div>
  );
}