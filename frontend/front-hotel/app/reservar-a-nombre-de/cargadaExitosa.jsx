"use client";

import { botonPersonalizado } from "../components/Botones";

export function MensajeCargaExitosa({ onClose }) {

  return (
    <div className="error-overlay">
      <div className="cancelar-contenedor-principal">
        <div className="error-titulo-box">
          <p className="cancelar-titulo-texto">¡Cargada de reserva exitosa!</p>
          <div className="error-detalle-boton">
            {botonPersonalizado({ texto: "Volver al inicio", destino: "/home" })}
          </div>
        </div>
      </div>
    </div>
  );
}


