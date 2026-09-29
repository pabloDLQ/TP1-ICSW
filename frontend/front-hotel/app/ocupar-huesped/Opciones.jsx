"use client";
import { useState } from "react";

export function Opciones({ estadias, huespedes, onSeguir, router, setError }) {
  const [loading, setLoading] = useState(false);

  const enviarEstadias = async (redirectUrl) => {
    setLoading(true);
    try {
      if (!huespedes || huespedes.length === 0) {
        setError("Debe seleccionar al menos un huésped principal");
        return;
      }

      const payload = {
      huespedPrincipal: {
        tipoDocumento: huespedes[0].tipoDocumento,
        numeroDocumento: huespedes[0].numeroDocumento,
      },
      acompanantes: huespedes.slice(1).map((h) => ({
        tipoDocumento: h.tipoDocumento,
        numeroDocumento: h.numeroDocumento,
      })),
      items: estadias.map((e) => ({
        habitacionId: e.habitacionId,
        fechaInicio:
          typeof e.fechaInicio === "string"
            ? e.fechaInicio
            : e.fechaInicio.toISOString().split("T")[0],
        fechaFin:
          typeof e.fechaFin === "string"
            ? e.fechaFin
            : e.fechaFin.toISOString().split("T")[0],
        ocuparSobreReserva: e.ocuparSobreReserva,
        reservaIdAsociada: e.reservaIdAsociada,
      })),
    };


      console.log("Payload enviado:", payload);

      const res = await fetch("/api/estadias", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      if (!res.ok) {
        const errorText = await res.text();
        console.error("Error response:", errorText);
        throw new Error(`Error HTTP ${res.status}: ${errorText}`);
      }

      const result = await res.json();
      console.log("Response exitoso:", result);

      router.push(redirectUrl);
    } catch (err) {
      setError("Error al almacenar las estadías");
    } finally {
      setLoading(false);
    }
  };


  return (
    <div className="error-overlay">
      <div className="error-contenedor-principal">
        <div className="error-titulo-box">
          <p className="cancelar-titulo-texto">
            Para seguir, por favor elija una opción...
          </p>

          <div className="error-detalle-boton">
            <button
              onClick={onSeguir}
              type="button"
              className="btn-largo btn-siguiente"
              disabled={loading}
            >
              SEGUIR CARGANDO HUÉSPEDES
            </button>
          </div>

          <div className="error-detalle-boton">
            <button
              onClick={() => enviarEstadias("/ocupar-fechas")}
              type="button"
              className="btn-largo btn-siguiente"
              disabled={loading}
            >
              CARGAR OTRA HABITACIÓN
            </button>
          </div>

          <div className="error-detalle-boton">
            <button
              onClick={() => enviarEstadias("/")}
              type="button"
              className="btn-largo btn-cancelar"
              disabled={loading}
            >
              SALIR
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}