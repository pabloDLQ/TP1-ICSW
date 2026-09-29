
"use client";

import { useEffect, useState } from "react";
import { cabecera } from "../components/Cabecera";
import { MensajeError } from "../components/MensajeError";
import { MensajeCancelar } from "../components/MensajeCancelar";
import { useRouter } from "next/navigation";

export default function BajaHuespedPage() {
  /**
   * Estados del CU11
   */
  const [estado, setEstado] = useState("CONFIRMAR");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [mostrarCancelar, setMostrarCancelar] = useState(false);

  /**
   * Control de vistas
   */
  const [mostrarTabla, setMostrarTabla] = useState(true);

  /**
   * Datos
   */
  const [huespedes, setHuespedes] = useState([]);
  const [huesped, setHuesped] = useState(null);

  const router = useRouter();

  /**
   * Cargar huéspedes para la tabla
   */
  useEffect(() => {
    fetch("/api/huespedes")
      .then(res => res.json())
      .then(data => setHuespedes(data))
      .catch(() => setError("Error al cargar huéspedes"));
  }, []);

  /**
   * Salir del CU al finalizar
   */
  useEffect(() => {
    if (estado === "CONFIRMAR") return;

    const finalizar = () => router.push("/");

    window.addEventListener("keydown", finalizar);
    window.addEventListener("click", finalizar);

    return () => {
      window.removeEventListener("keydown", finalizar);
      window.removeEventListener("click", finalizar);
    };
  }, [estado, router]);

  /**
   * DELETE al backend
   * Compatible con HuespedEliminarDto
   */
  const eliminarHuesped = async () => {
    setLoading(true);
    setError(null);

    try {
      const response = await fetch("/api/huespedes", {
        method: "DELETE",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          tipoDocumento: huesped.tipoDocumento,
          numeroDocumento: huesped.numeroDocumento,
        }),
      });

      if (response.ok) {
        setEstado("ELIMINADO");
      } else if (response.status === 409) {
        setEstado("NO_ELIMINABLE");
      } else {
        let errorMessage;
        try {
          const errorData = await response.json();
          errorMessage = errorData?.message ?? JSON.stringify(errorData);
        } catch {
          errorMessage = await response.text();
        }
        setError(errorMessage);
      }
    } catch {
      setError("Error de conexión con el backend");
    } finally {
      setLoading(false);
    }
  };

  
  const renderContenido = () => {
    switch (estado) {
      case "CONFIRMAR":
        return (
          <>
            <p style={{ textAlign: "center", lineHeight: "1.6" }}>
              Los datos del huésped{" "}
              <strong>
                {huesped.nombre} {huesped.apellido}
              </strong>
              ,<br />
              {huesped.tipoDocumento} {huesped.numeroDocumento} serán eliminados
              del sistema.
            </p>

            <div
              className="botones-habitaciones"
              style={{ justifyContent: "center" }}
            >
              <button
                className="btn btn-cancelar"
                onClick={() => setMostrarCancelar(true)}
                disabled={loading}
              >
                Cancelar
              </button>

              <button
                className="btn btn-siguiente"
                onClick={eliminarHuesped}
                disabled={loading}
              >
                {loading ? "Eliminando..." : "Eliminar"}
              </button>
            </div>
          </>
        );

      case "NO_ELIMINABLE":
        return (
          <p style={{ textAlign: "center", lineHeight: "1.6" }}>
            El huésped no puede ser eliminado pues se ha alojado en el Hotel en
            alguna oportunidad.
            <br />
            <br />
            <strong>PRESIONE CUALQUIER TECLA PARA CONTINUAR...</strong>
          </p>
        );

      case "ELIMINADO":
        return (
          <p style={{ textAlign: "center", lineHeight: "1.6" }}>
            Los datos del huésped{" "}
            <strong>
              {huesped.nombre} {huesped.apellido}
            </strong>
            ,<br />
            {huesped.tipoDocumento} {huesped.numeroDocumento} han sido eliminados
            del sistema.
            <br />
            <br />
            <strong>PRESIONE CUALQUIER TECLA PARA CONTINUAR...</strong>
          </p>
        );

      default:
        return null;
    }
  };

  return (
    <>
      {cabecera()}

      {/* ======= TABLA DE SELECCIÓN ======= */}
      {mostrarTabla && (
        <div className="main-container">
          <div className="panel-interno-resultados">
            <table className="tabla-resultados">
              <thead>
                <tr>
                  <th>Apellido</th>
                  <th>Nombre</th>
                  <th>Documento</th>
                </tr>
              </thead>
              <tbody>
                {huespedes.map((h, i) => (
                  <tr
                    key={i}
                    onClick={() => {
                      setHuesped(h);
                      setMostrarTabla(false);
                    }}
                  >
                    <td>{h.apellido}</td>
                    <td>{h.nombre}</td>
                    <td>
                      {h.tipoDocumento} {h.numeroDocumento}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* ======= CU11 ======= */}
      {!mostrarTabla && (
        <div className="main-container">
          <h2 style={{ textAlign: "center", marginBottom: "40px" }}>
            Dar de Baja Huésped
          </h2>

          <div
            className="fondo-form-dar-alta"
            style={{ maxWidth: "700px", margin: "0 auto" }}
          >
            {renderContenido()}
          </div>
        </div>
      )}

      {error && (
        <MensajeError mensaje={error} onClose={() => setError(null)} />
      )}

      {mostrarCancelar && (
        <MensajeCancelar onClose={() => setMostrarCancelar(false)} />
      )}
    </>
  );
}
