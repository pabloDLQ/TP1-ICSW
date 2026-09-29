"use client";

import { useEffect, useMemo, useState, Suspense } from "react";
import { useSearchParams } from "next/navigation";
import { cabecera } from "../components/Cabecera";
import { LeyendaHabitaciones } from "./LeyendaHabitaciones";
import { rangeISO, toHuman } from "../util/fecha";
import { clasificarHabitaciones } from "../util/habitaciones";
import { MensajeError } from "../components/MensajeError";
import { MensajeCancelar } from "../components/MensajeCancelar";
import { useRouter } from "next/navigation";

function PageMuestraHabitacionesContent() {
  const [mensajeError, setMensajeError] = useState(null);
  const [mostrarCancelar, setMostrarCancelar] = useState(false);
  const router = useRouter();
  const search = useSearchParams();
  const inicio = search.get("inicio"); // YYYY-MM-DD
  const fin = search.get("fin");       // YYYY-MM-DD

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Estado: mapa fecha-habId -> estado (enum del backend o "seleccionada")
  const [estadoCelda, setEstadoCelda] = useState({});
  const [habitaciones, setHabitaciones] = useState([]);

  const fechasISO = useMemo(() => {
    if (!inicio || !fin) return [];
    return rangeISO(inicio, fin);
  }, [inicio, fin]);

  useEffect(() => {
    const cargar = async () => {
      if (!inicio || !fin) return;
      setLoading(true);
      setError(null);
      try {
        const resp = await fetch(
          `/api/habitaciones/disponibilidad?inicio=${inicio}&fin=${fin}`
        );
        if (!resp.ok) {
          const txt = await resp.text();
          throw new Error(txt || "Error consultando disponibilidad");
        }
        const data = await resp.json();
        // data: [{ dia: "YYYY-MM-DD", idHabitacion: number, estadoHabitacion: "DISPONIBLE|RESERVADA|NO_DISPONIBLE|OCUPADA" }, ...]

        const setHab = new Set();
        const mapa = {};
        for (const item of data) {
          const key = `${item.dia}::${item.idHabitacion}`;
          setHab.add(Number(item.idHabitacion));
          mapa[key] = item.estadoHabitacion;
        }
        setHabitaciones(Array.from(setHab).sort((a, b) => a - b));
        setEstadoCelda(mapa);
      } catch (err) {
        setError(err.message || "Error desconocido");
        setHabitaciones([]);
        setEstadoCelda({});
      } finally {
        setLoading(false);
      }
    };
    cargar();
  }, [inicio, fin]);

const toggleSeleccion = (fechaISO, habitacionId) => {
  const key = `${fechaISO}::${habitacionId}`;
  const actual = estadoCelda[key];

  if (actual === "RESERVADA" || actual === "NO_DISPONIBLE" || actual === "OCUPADA") {
    setMensajeError("La habitación no está disponible en la fecha seleccionada.");
    return;
  }

  setEstadoCelda(prev => ({
    ...prev,
    [key]: actual === "seleccionada" ? "DISPONIBLE" : "seleccionada",
  }));
};

  const getClaseCelda = (fechaISO, habitacionId) => {
  const key = `${fechaISO}::${habitacionId}`;
  const estado = estadoCelda[key] || "DISPONIBLE";
    switch (estado) {
      case "DISPONIBLE": return "celda disponible";
      case "RESERVADA": return "celda reservado";
      case "NO_DISPONIBLE": return "celda no-disponible";
      case "OCUPADA": return "celda ocupada";   // 🔎 nuevo estado
      case "seleccionada": return "celda seleccionada";
      default: return "celda disponible";
    }
  };

  const grupos = clasificarHabitaciones(habitaciones);

  const confirmarReserva = () => {
    const seleccionadas = Object.entries(estadoCelda)
      .filter(([_, estado]) => estado === "seleccionada")
      .map(([key]) => {
        const [fechaISO, habId] = key.split("::");
        const id = Number(habId);
        const tipo = grupos.find(g => g.lista.includes(id))?.nombre;
        return { idHabitacion: id, tipo, fecha: fechaISO };
      });

    // 🚨 Validación: si no hay nada seleccionado
    if (seleccionadas.length === 0) {
      setMensajeError("No se seleccionó ningún período de reserva");
      return;
    }

    // Agrupar por habitación
    const agrupadas = [];
    const mapa = new Map();

    for (const sel of seleccionadas) {
      if (!mapa.has(sel.idHabitacion)) {
        mapa.set(sel.idHabitacion, {
          idHabitacion: sel.idHabitacion,
          tipo: sel.tipo,
          fechas: []
        });
      }
      mapa.get(sel.idHabitacion).fechas.push(sel.fecha);
    }

    // Para cada habitación, detectar intervalos consecutivos
    for (const hab of mapa.values()) {
      const fechasOrdenadas = hab.fechas.sort();

      let inicio = fechasOrdenadas[0];
      let anterior = inicio;

      for (let i = 1; i < fechasOrdenadas.length; i++) {
        const actual = fechasOrdenadas[i];
        const diff = (new Date(actual) - new Date(anterior)) / (1000 * 60 * 60 * 24);

        if (diff > 1) {
          agrupadas.push({
            idHabitacion: hab.idHabitacion,
            tipo: hab.tipo,
            fechaInicio: inicio,
            fechaFin: anterior
          });
          inicio = actual;
        }
        anterior = actual;
      }

      agrupadas.push({
        idHabitacion: hab.idHabitacion,
        tipo: hab.tipo,
        fechaInicio: inicio,
        fechaFin: anterior
      });
    }

    router.push(`/confirmar-habitaciones?data=${encodeURIComponent(JSON.stringify(agrupadas))}`);
  };



  return (
    <>
      {cabecera()}
      <div className="main-container">
        <div className="fondo1-habitaciones">
          <h2>
            Disponibilidad entre {inicio ? toHuman(inicio) : "??"} y {fin ? toHuman(fin) : "??"} - Seleccione una o más habitaciones
          </h2>

          {loading && <p>Cargando disponibilidad…</p>}
          {error && <p className="mensaje-error">{error}</p>}

          <div className="fondo2-habitaciones">
            <div className="cuadro-disponibilidad">
              <table className="tabla-disponibilidad">
                <thead>
                  <tr>
                    <th rowSpan="2">Fecha</th>
                    {grupos.map(g => (
                      <th key={g.nombre} colSpan={g.lista.length}>{g.nombre}</th>
                    ))}
                  </tr>
                  <tr>
                    {grupos.flatMap(g => g.lista).map(hab => (
                      <th key={hab}>{hab}</th>
                    ))}
                  </tr>
                </thead>
                <tbody>
                  {fechasISO.map((fechaISO) => (
                    <tr key={fechaISO}>
                      <td>{toHuman(fechaISO)}</td>
                      {grupos.flatMap(g => g.lista).map((hab) => (
                        <td
                          key={`${fechaISO}-${hab}`}
                          className={getClaseCelda(fechaISO, hab)}
                          onClick={() => toggleSeleccion(fechaISO, hab)}
                        />
                      ))}
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          <div className="acciones-habitaciones">
            <div className="botones-habitaciones">
              <button
                className="btn btn-cancelar"
                onClick={() => setMostrarCancelar(true)}
              >
                Cancelar
              </button>

              <button className="btn btn-siguiente" onClick={confirmarReserva}>
                Reservar
              </button>

            </div>
            <div className="leyenda-habitaciones">{LeyendaHabitaciones()}</div>
          </div>
        </div>
      </div>
      {mensajeError && (
        <MensajeError
          mensaje={mensajeError}
          onClose={() => setMensajeError(null)}
        />
      )}
      {mostrarCancelar && (
        <MensajeCancelar onClose={() => setMostrarCancelar(false)} />
      )}
    </>
  );
}

export default function PageMuestraHabitaciones() {
  return (
    <Suspense fallback={<div>Cargando...</div>}>
      <PageMuestraHabitacionesContent />
    </Suspense>
  );
}