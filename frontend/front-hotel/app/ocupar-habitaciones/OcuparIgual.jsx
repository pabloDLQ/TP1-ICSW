"use client";

import { botonVolver2 } from "../components/Botones";
import { toHuman } from "../util/fecha";

export function OcuparIgual({ mensaje, onClose, onOcuparIgual }) {
  return (
    <div className="error-overlay">
      <div className="error-contenedor-principal">
        <div className="error-titulo-box">
          <p className="error-titulo-texto">Las siguientes habitaciones están reservadas:</p>

          <div className="error-detalle-box">
            {Array.isArray(mensaje) && mensaje.map((c, idx) => (
              <p key={idx} className="error-detalle-texto">
                - Habitación {c.habitacionId} reservada por {c.nombre} del {toHuman(c.fechaInicio)} al {toHuman(c.fechaFin)}
              </p>
            ))}
          </div>

          <div className="error-detalle-boton">
            {botonVolver2("error-boton-cerrar", onClose)}

            {/* Botón directo para ocupar igual */}
            <button
              className="btn btn-siguiente"
              onClick={onOcuparIgual}
            >
              OCUPAR IGUAL
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}