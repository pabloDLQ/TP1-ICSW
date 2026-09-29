"use client";

import { useSearchParams } from "next/navigation";
import { Suspense } from "react";
import { cabecera } from "../components/Cabecera";
import { botonRechazar, botonPersonalizado } from "../components/Botones";
import { toHuman } from "../util/fecha";

function ConfirmarHabitacionesContent() {
  const search = useSearchParams();
  const dataParam = search.get("data");
  const seleccionadas = dataParam ? JSON.parse(decodeURIComponent(dataParam)) : [];

  const horaIngreso = "12:00hs";
  const horaEgreso = "10:00hs";

  // Día de la semana en español
  const getDiaSemana = (fechaISO) => {
    const dias = ["Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"];
    const [y, m, d] = fechaISO.split("-").map(Number);
    const date = new Date(y, m - 1, d);
    return dias[date.getDay()];
  };

  // Sumar un día a la fecha (para mostrar checkout)
  const sumarUnDia = (fechaISO) => {
    const [y, m, d] = fechaISO.split("-").map(Number);
    const date = new Date(y, m - 1, d);
    date.setDate(date.getDate() + 1);
    const yyyy = date.getFullYear();
    const mm = String(date.getMonth() + 1).padStart(2, "0");
    const dd = String(date.getDate()).padStart(2, "0");
    return `${yyyy}-${mm}-${dd}`;
  };

  return (
    <>
      {cabecera()}
      <div className="main-container">
        <div className="fondo-muestra-habitaciones">
          <h2>Las habitaciones a reservar son:</h2>

          {seleccionadas.map((sel, idx) => {
            const fechaCheckout = sumarUnDia(sel.fechaFin);
            return (
              <div key={idx} className="muestra-habitacion">
                <p className="muestra-habitacion-texto">HABITACIÓN {sel.tipo}</p>
                <p className="muestra-habitacion-texto">
                  - Ingreso: {getDiaSemana(sel.fechaInicio)}, {toHuman(sel.fechaInicio)}, {horaIngreso}
                </p>
                <p className="muestra-habitacion-texto">
                  - Egreso: {getDiaSemana(fechaCheckout)}, {toHuman(fechaCheckout)}, {horaEgreso}
                </p>
              </div>
            );
          })}

          <div className="muestra-habitaciones-botones">
            {botonRechazar({ destino: "/home" })}
            {botonPersonalizado({ texto: "ACEPTAR", destino: `/reservar-a-nombre-de?data=${encodeURIComponent(JSON.stringify(seleccionadas))}` })}
          </div>
        </div>
      </div>
    </>
  );
}

export default function PageConfirmarHabitaciones() {
  return (
    <Suspense fallback={<div>Cargando...</div>}>
      <ConfirmarHabitacionesContent />
    </Suspense>
  );
}