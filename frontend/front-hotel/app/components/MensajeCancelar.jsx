"use client";

import { botonCerrar2 } from "./Botones";
import { botonConfirmar } from "./Botones";

export function MensajeCancelar({ onClose }) {

  return (
    <div className="error-overlay">
      <div className="cancelar-contenedor-principal">
        <div className="error-titulo-box">
          <p className="cancelar-titulo-texto">¿Está seguro que desea cancelar?</p>
          <div className="error-detalle-boton">
            {botonCerrar2("error-boton-cerrar", onClose)}
            {botonConfirmar({destino: "/home"})}
          </div>
        </div>
      </div>
    </div>
  );
}


