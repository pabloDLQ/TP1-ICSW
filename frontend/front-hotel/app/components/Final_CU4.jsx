"use client";

import { botonCancelar } from "./Botones";

export function ResumenSeleccion() {
  const seleccion = [
    {
      tipo: "Habitación individual estándar",
      ingreso: "Domingo, 08/02/2026, 12:00hs",
      egreso: "Sábado, 14/02/2026, 12:00hs",
    },
    {
      tipo: "Habitación doble estándar",
      ingreso: "Domingo, 08/02/2026, 12:00hs",
      egreso: "Sábado, 14/02/2026, 12:00hs",
    },
  ];

  return (
    <div className="resumen-container">
      <div className="resumen-box">
        <h2 className="resumen-titulo">Se seleccionó lo siguiente:</h2>

        <div className="resumen-lista">
          {seleccion.map((item, idx) => (
            <div key={idx} className="resumen-item">
              <div className="resumen-tipo">{item.tipo}</div>
              <div className="resumen-fechas">
                <div className="resumen-linea">Ingreso: {item.ingreso}</div>
                <div className="resumen-linea">Egreso: {item.egreso}</div>
              </div>
            </div>
          ))}
        </div>

        <div className="resumen-botones-row">
          <div className="resumen-boton-izquierda">
            {botonCancelar("/volver")}
          </div>
          <div className="resumen-boton-derecha">
            <button className="btn btn-rechazar">Rechazar</button>
            <button className="btn btn-siguiente">Aceptar</button>
          </div>
        </div>
      </div>
    </div>
  );
}