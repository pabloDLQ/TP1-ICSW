"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";

export function PresioneCualquierTecla({ onClose, estadias }) {
  const router = useRouter();

  useEffect(() => {
    const handleKeyPress = () => {
      if (estadias) {
        router.push(
          `/ocupar-huesped?data=${encodeURIComponent(JSON.stringify(estadias))}`
        );
      }
      if (onClose) onClose();
    };

    window.addEventListener("keydown", handleKeyPress);
    return () => window.removeEventListener("keydown", handleKeyPress);
  }, [estadias, onClose, router]);

  return (
    <div className="error-overlay">
      <div className="cancelar-contenedor-principal">
        <div className="error-titulo-box">
          <p className="presione-cualquier-tecla-texto">
            PRESIONE CUALQUIER TECLA Y CONTINUA...
          </p>
        </div>
      </div>
    </div>
  );
}