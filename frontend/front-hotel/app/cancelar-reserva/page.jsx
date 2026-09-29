"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { cabecera } from "../components/Cabecera";
import { MensajeCancelar } from "../components/MensajeCancelar";
import { MensajeError } from "../components/MensajeError";

export default function CancelarReserva() {
    const router = useRouter();

    const [mostrarMensajeCancelar, setMostrarMensajeCancelar] = useState(false);

    const [nombre, setNombre] = useState("");
    const [apellido, setApellido] = useState("");

    const [error, setError] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!apellido || apellido.trim() === "") {
            setError('{"El campo apellido no puede estar vacío"}');
            return;
        }

        try {
            const response = await fetch(
                `/api/reservas?apellido=${encodeURIComponent(apellido)}&nombre=${encodeURIComponent(nombre)}`
            );

            if (!response.ok) {
                const errorText = await response.text();
                setError(`{"${errorText}"}`);
                return;
            }

            const reservas = await response.json();
            if (!reservas || reservas.length === 0) {
                setError('{"No existen reservas para los criterios de búsqueda"}');
                return;
            }
            
            router.push(`/reservas-de?nombre=${encodeURIComponent(nombre)}&apellido=${encodeURIComponent(apellido)}`);

        } catch (err) {
            setError('{"Error de conexión con el servidor"}');
        }
    };

    return (
        <>
            {cabecera()}

            <div className="main-container">
                <div className="contenedor-fechas">
                    <div className="card-fechas">
                        <h1 className="titulo-fechas">Buscar reservas a nombre de:</h1>

                        <form className="form-fechas" onSubmit={handleSubmit}>
                            <div className="fila-inputs">
                                <div className="grupo-fecha">
                                    <label>Nombre</label>
                                    <input
                                        type="text"
                                        placeholder="Ingrese nombre"
                                        value={nombre}
                                        onChange={(e) => setNombre(e.target.value)}
                                    />
                                </div>

                                <div className="grupo-fecha">
                                    <label>Apellido</label>
                                    <input
                                        type="text"
                                        placeholder="Ingrese apellido"
                                        value={apellido}
                                        onChange={(e) => setApellido(e.target.value)}
                                    />
                                </div>
                            </div>

                            <div className="botones-fechas">
                                <button
                                    type="button"
                                    className="btn btn-cancelar"
                                    onClick={() => setMostrarMensajeCancelar(true)}
                                >
                                    Cancelar
                                </button>

                                <button type="submit" className="btn btn-siguiente">
                                    Validar
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            {mostrarMensajeCancelar && (
                <MensajeCancelar onClose={() => setMostrarMensajeCancelar(false)} />
            )}

            {error && (
                <MensajeError
                    mensaje={error}
                    onClose={() => setError(null)}
                />
            )}
        </>
    );
}
