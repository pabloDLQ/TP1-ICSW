"use client";

import { cabecera } from "../components/Cabecera";
import { useSearchParams } from "next/navigation";
import { useEffect, useState, Suspense } from "react";
import { MensajeCancelar } from "../components/MensajeCancelar";
import { MensajeError } from "../components/MensajeError";
import { ReservaCancelada } from "./reserva-cancelada";
import { toHuman } from "../util/fecha";

function ReservasDeContent() {
    const searchParams = useSearchParams();

    const nombre = searchParams.get("nombre") || "";
    const apellido = searchParams.get("apellido") || "";

    const [reservas, setReservas] = useState([]);
    const [reservasSeleccionadas, setReservasSeleccionadas] = useState([]);
    const [error, setError] = useState(null);
    const [mostrarMensajeCancelar, setMostrarMensajeCancelar] = useState(false);
    const [mostrarReservaCancelada, setMostrarReservaCancelada] = useState(false);

    useEffect(() => {
        const fetchReservas = async () => {
            try {
                const response = await fetch(
                    `/api/reservas?apellido=${encodeURIComponent(apellido)}&nombre=${encodeURIComponent(nombre)}`
                );

                if (!response.ok) {
                    const errorText = await response.text();
                    setError(errorText);
                    return;
                }

                const data = await response.json();
                setReservas(data);
            } catch {
                setError("Error de conexión con el servidor");
            }
        };

        fetchReservas();
    }, [nombre, apellido]);

    const getIdUnico = (r) =>
        `${r.apellido}-${r.nombre}-${r.idHabitacion}-${r.fechaInicio}`;

    const estaSeleccionada = (reserva) => {
        const id = getIdUnico(reserva);
        return reservasSeleccionadas.some((r) => r.idUnico === id);
    };

    const toggleSeleccion = (reserva) => {
        const id = getIdUnico(reserva);

        const yaEsta = reservasSeleccionadas.some((r) => r.idUnico === id);

        if (yaEsta) {
            setReservasSeleccionadas((prev) =>
                prev.filter((r) => r.idUnico !== id)
            );
        } else {
            setReservasSeleccionadas((prev) => [
                ...prev,
                { ...reserva, idUnico: id }
            ]);
        }
    };

    const enviarCancelacion = async () => {
        if (reservasSeleccionadas.length === 0) {
            setError("Debe seleccionar al menos una reserva");
            return;
        }

        const payload = reservasSeleccionadas.map(r => ({
            nombre: r.nombre,
            apellido: r.apellido,
            idHabitacion: r.idHabitacion,
            fechaInicio: r.fechaInicio,
            fechaFin: r.fechaFin
        }));

        try {
            const response = await fetch("/api/reservas/cancelar", {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(payload)
            });

            if (!response.ok) {
                const errorText = await response.text();
                setError(errorText);
                return;
            }

            const nuevas = reservas.filter(res =>
                !reservasSeleccionadas.some(sel =>
                    sel.idHabitacion === res.idHabitacion &&
                    sel.fechaInicio === res.fechaInicio
                )
            );

            setReservas(nuevas);
            setReservasSeleccionadas([]);
            setMostrarReservaCancelada(true);

        } catch {
            setError("Error de conexión con el servidor");
        }
    };


    return (
        <>
            {cabecera()}

            <div className="main-container">
                <div className="fondo-cancelar-reserva">
                    <h2>Reservas de {nombre} {apellido}</h2>

                    <div className="tabla-reservas-contenedor">
                        <table className="tabla-reservas">
                            <thead>
                                <tr>
                                    <th>Apellido</th>
                                    <th>Nombres</th>
                                    <th>Tipo de habitación</th>
                                    <th>Número de habitación</th>
                                    <th>Fecha inicial</th>
                                    <th>Fecha final</th>
                                </tr>
                            </thead>

                            <tbody>
                                {reservas.map((r, idx) => (
                                    <tr
                                        key={idx}
                                        onClick={() => toggleSeleccion(r)}
                                        className={estaSeleccionada(r) ? "fila-reserva-seleccionada" : ""}
                                        style={{ cursor: "pointer" }}
                                    >
                                        <td>{r.apellido}</td>
                                        <td>{r.nombre}</td>
                                        <td>{r.tipoHabitacion}</td>
                                        <td>{r.idHabitacion}</td>
                                        <td>{toHuman(r.fechaInicio)}</td>
                                        <td>{toHuman(r.fechaFin)}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>

                    <div style={{ marginBottom: "15px", display: "flex", gap: "10px", paddingLeft: "40px" }}>
                        <button
                            className="btn btn-cancelar"
                            onClick={() => setMostrarMensajeCancelar(true)}
                        >
                            CANCELAR
                        </button>

                        <button
                            className="btn btn-siguiente"
                            onClick={enviarCancelacion}
                        >
                            ACEPTAR
                        </button>
                    </div>
                </div>
            </div>
            {error && (
                <MensajeError
                    mensaje={error}
                    onClose={() => setError(null)}
                />
            )}
            {mostrarMensajeCancelar && (
                <MensajeCancelar onClose={() => setMostrarMensajeCancelar(false)} />
            )}
            {mostrarReservaCancelada && <ReservaCancelada />}
        </>
    );
}

export default function ReservasDe() {
  return (
    <Suspense fallback={<div>Cargando...</div>}>
      <ReservasDeContent />
    </Suspense>
  );
}