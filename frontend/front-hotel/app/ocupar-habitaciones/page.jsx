"use client";

import { useEffect, useMemo, useState, Suspense } from "react";
import { useSearchParams, useRouter } from "next/navigation";
import { cabecera } from "../components/Cabecera";
import { LeyendaHabitaciones } from "./LeyendaHabitaciones";
import { rangeISO, toHuman } from "../util/fecha";
import { clasificarHabitaciones } from "../util/habitaciones";
import { MensajeError } from "../components/MensajeError";
import { MensajeCancelar } from "../components/MensajeCancelar";
import { OcuparIgual } from "./OcuparIgual";
import { PresioneCualquierTecla } from "./PresioneCualquierTecla";

function PageOcuparHabitacionesContent() {
  const [estadiasParaCargar, setEstadiasParaCargar] = useState(null);
  const [estadoBase, setEstadoBase] = useState({});
  const [mensajeError, setMensajeError] = useState(null);
  const [mostrarCancelar, setMostrarCancelar] = useState(false);
  const [conflictosMensaje, setConflictosMensaje] = useState(null);
  const [mostrarPresioneTecla, setMostrarPresioneTecla] = useState(false);
  const router = useRouter();
  const search = useSearchParams();
  const inicio = search.get("inicio");
  const fin = search.get("fin");

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

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

        const setHab = new Set();
        const mapa = {};
        for (const item of data) {
          const key = `${item.dia}::${item.idHabitacion}`;
          setHab.add(Number(item.idHabitacion));
          mapa[key] = item.estadoHabitacion;
        }
        setHabitaciones(Array.from(setHab).sort((a, b) => a - b));
        setEstadoCelda(mapa);
        setEstadoBase(mapa);
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

    if (actual === "NO_DISPONIBLE" || actual === "OCUPADA") {
      setMensajeError("La habitación no puede ser ocupada en la fecha seleccionada.");
      return;
    }

    if (actual === "seleccionada") {
        const original = estadoBase[key] || "DISPONIBLE";
        setEstadoCelda((prev) => ({ ...prev, [key]: original }));
    } else {
        setEstadoCelda((prev) => ({ ...prev, [key]: "seleccionada" }));
    }

  };

  const getClaseCelda = (fechaISO, habitacionId) => {
    const key = `${fechaISO}::${habitacionId}`;
    const estado = estadoCelda[key] || "DISPONIBLE";
    switch (estado) {
      case "DISPONIBLE": return "celda disponible";
      case "RESERVADA": return "celda reservado";
      case "NO_DISPONIBLE": return "celda no-disponible";
      case "OCUPADA": return "celda ocupada"; // ✅ se pinta como ocupada
      case "seleccionada": return "celda seleccionada ocupacion";
      default: return "celda disponible";
    }
  };

  
  const grupos = clasificarHabitaciones(habitaciones);
    const confirmarOcupacion = async () => {
    const seleccionadas = Object.entries(estadoCelda)
      .filter(([_, estado]) => estado === "seleccionada")
      .map(([key]) => {
        const [fechaISO, habId] = key.split("::");
        const id = Number(habId);
        const tipo = grupos.find((g) => g.lista.includes(id))?.nombre;
        return { idHabitacion: id, tipo, fecha: fechaISO };
      });

    // 🚨 Validación: si no hay nada seleccionado
    if (seleccionadas.length === 0) {
      setMensajeError("No se seleccionó ningún período de ocupación");
      return;
    }

    const agrupadas = [];
    const mapa = new Map();

    for (const sel of seleccionadas) {
      if (!mapa.has(sel.idHabitacion)) {
        mapa.set(sel.idHabitacion, {
          idHabitacion: sel.idHabitacion,
          tipo: sel.tipo,
          fechas: [],
        });
      }
      mapa.get(sel.idHabitacion).fechas.push(sel.fecha);
    }


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
            fechaFin: anterior,
          });
          inicio = actual;
        }
        anterior = actual;
      }

      agrupadas.push({
        idHabitacion: hab.idHabitacion,
        tipo: hab.tipo,
        fechaInicio: inicio,
        fechaFin: anterior,
      });
    }

    // Normaliza a "YYYY-MM-DD"
    const toISODate = (d) =>
      typeof d === "string" ? d : new Date(d).toISOString().split("T")[0];

    // Agrupa fechas ISO consecutivas en intervalos [{ fechaInicio, fechaFin }]
    const agruparFechasConsecutivas = (fechasISO) => {
      if (!fechasISO?.length) return [];
      const ordenadas = Array.from(new Set(fechasISO)).sort(); // dedup + orden
      const intervalos = [];
      let inicio = ordenadas[0];
      let anterior = ordenadas[0];

      const addIntervalo = (i, f) => intervalos.push({ fechaInicio: i, fechaFin: f });

      for (let i = 1; i < ordenadas.length; i++) {
        const actual = ordenadas[i];
        // calcular el día siguiente esperado de 'anterior'
        const prev = new Date(anterior + "T00:00:00Z");
        prev.setUTCDate(prev.getUTCDate() + 1);
        const siguienteEsperado = prev.toISOString().split("T")[0];

        if (actual !== siguienteEsperado) {
          addIntervalo(inicio, anterior);
          inicio = actual;
        }
        anterior = actual;
      }
      addIntervalo(inicio, anterior);
      return intervalos;
    };

    try {
  let conflictosAcumulados = [];

  for (const item of agrupadas) {
    const resp = await fetch(
      `/api/habitaciones/conflictos-reservas?habitacionId=${item.idHabitacion}&inicio=${item.fechaInicio}&fin=${item.fechaFin}`
    );
    if (!resp.ok) {
      const txt = await resp.text();
      throw new Error(txt || "Error consultando conflictos");
    }

    // Lista de ConflictoReservaDto
    const conflictos = await resp.json();

    if (Array.isArray(conflictos) && conflictos.length > 0) {
      conflictos.forEach((c) => {
        // c: { reservaId, titular, telefono, diasConflicto: Set<LocalDate> }
        const nombreConf = c.titular || "Reserva existente";
        const dias = Array.isArray(c.diasConflicto)
          ? c.diasConflicto.map(toISODate)
          : []; // si viene como Set, JSON ya lo serializa como array

        if (dias.length === 0) return;

        // Recortar al rango solicitado para esta habitación (defensivo)
        const diasRecortados = dias.filter(
          (d) => d >= item.fechaInicio && d <= item.fechaFin
        );

        const intervalos = agruparFechasConsecutivas(
          diasRecortados.length ? diasRecortados : dias
        );

        for (const itv of intervalos) {
          conflictosAcumulados.push({
            habitacionId: item.idHabitacion,
            nombre: nombreConf,
            fechaInicio: itv.fechaInicio,
            fechaFin: itv.fechaFin,
          });
        }
      });
    }
  }

  if (conflictosAcumulados.length > 0) {
    setEstadiasParaCargar(agrupadas);
    setConflictosMensaje(conflictosAcumulados);
    return;
  }

  // ✅ No hay conflictos → guardamos las estadías y mostramos PresioneCualquierTecla
  setEstadiasParaCargar(agrupadas);
  setMostrarPresioneTecla(true);
} catch (err) {
  setMensajeError(err.message);
}

  };
    return (
    <>
      {cabecera()}
      <div className="main-container">
        <div className="fondo1-habitaciones">
          <h2>
            Disponibilidad entre {inicio ? toHuman(inicio) : "??"} y{" "}
            {fin ? toHuman(fin) : "??"} - Seleccione una o más habitaciones
          </h2>

          {loading && <p>Cargando disponibilidad…</p>}
          {error && <p className="mensaje-error">{error}</p>}

          <div className="fondo2-habitaciones">
            <div className="cuadro-disponibilidad">
              <table className="tabla-disponibilidad">
                <thead>
                  <tr>
                    <th rowSpan="2">Fecha</th>
                    {grupos.map((g) => (
                      <th key={g.nombre} colSpan={g.lista.length}>
                        {g.nombre}
                      </th>
                    ))}
                  </tr>
                  <tr>
                    {grupos.flatMap((g) => g.lista).map((hab) => (
                      <th key={hab}>{hab}</th>
                    ))}
                  </tr>
                </thead>
                <tbody>
                  {fechasISO.map((fechaISO) => (
                    <tr key={fechaISO}>
                      <td>{toHuman(fechaISO)}</td>
                      {grupos.flatMap((g) => g.lista).map((hab) => (
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

              <button className="btn btn-siguiente" onClick={confirmarOcupacion}>
                Ocupar
              </button>
            </div>
            <div className="leyenda-habitaciones">{LeyendaHabitaciones()}</div>
          </div>
        </div>
      </div>

      {mensajeError && (
        <MensajeError mensaje={mensajeError} onClose={() => setMensajeError(null)} />
      )}
      {mostrarCancelar && (
        <MensajeCancelar onClose={() => setMostrarCancelar(false)} />
      )}
    {conflictosMensaje && (
    <OcuparIgual
      mensaje={conflictosMensaje}
      onClose={() => setConflictosMensaje(null)}
      onOcuparIgual={() => {
        setConflictosMensaje(null);
        setMostrarPresioneTecla(true); // activa PresioneCualquierTecla
      }}
    />

    )}
      {mostrarPresioneTecla && (
        <PresioneCualquierTecla
          onClose={() => setMostrarPresioneTecla(false)}
          estadias={estadiasParaCargar}
        />
      )}
    </>
  );
}

export default function PageOcuparHabitaciones() {
  return (
    <Suspense fallback={<div>Cargando...</div>}>
      <PageOcuparHabitacionesContent />
    </Suspense>
  );
}