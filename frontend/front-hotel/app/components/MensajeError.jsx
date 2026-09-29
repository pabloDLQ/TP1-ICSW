"use client";

import { botonCerrar2 } from "./Botones";

export function MensajeError({ mensaje, onClose }) {
  // Paso 1: split por {"
  let partes = typeof mensaje === "string" ? mensaje.split('{"') : [mensaje];

  // Paso 2: para cada parte, split por "," y aplanar el resultado
  partes = partes.flatMap(parte => parte.split('","'));

  // Paso 3: eliminar todas las comillas dobles
  partes = partes.map(parte => parte.replace(/"/g, "").trim());

  // Paso 4: eliminar todas las llaves {}
  partes = partes.map(parte => parte.replace(/{|}/g, "").trim());

  return (
    <div className="error-overlay">
      <div className="error-contenedor-principal">
        <div className="error-titulo-box">
          <p className="error-titulo-texto">Se encontraron los siguientes errores:</p>
        

          <div className="error-detalle-box">
            {partes.map((parte, idx) => (
              <p key={idx} className="error-detalle-texto">{parte}</p>
            ))}
          </div>

          <div className="error-detalle-boton">
            {botonCerrar2("error-boton-cerrar", onClose)}
          </div>
        </div>
      </div>
    </div>
  );
}


