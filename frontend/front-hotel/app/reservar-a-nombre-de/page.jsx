"use client";

import { useState, Suspense } from "react";
import { useSearchParams } from "next/navigation";
import { cabecera } from "../components/Cabecera";
import { MensajeError } from "../components/MensajeError";
import { MensajeCargaExitosa } from "./cargadaExitosa";

function FormularioReservaUsuarioContent() {
  const search = useSearchParams();
  const dataParam = search.get("data");
  const habitacionesSeleccionadas = dataParam
    ? JSON.parse(decodeURIComponent(dataParam))
    : [];

  const [formData, setFormData] = useState({
    nombre: "",
    apellido: "",
    telefono: "",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleCancelar = () => {
    setFormData({ nombre: "", apellido: "", telefono: "" });
    setError(null);
    setSuccess(false);
  };

  const handleConfirmar = async () => {
    // Validación de campos vacíos
    const camposVacios = [];
    if (!formData.nombre.trim()) camposVacios.push("Nombre");
    if (!formData.apellido.trim()) camposVacios.push("Apellido");
    if (!formData.telefono.trim()) camposVacios.push("Teléfono");

    if (camposVacios.length > 0) {
      setError(`Los siguientes campos están vacíos: ${camposVacios.join(", ")}`);
      return;
    }

    const solicitud = {
      nombre: formData.nombre,
      apellido: formData.apellido,
      telefono: formData.telefono,
      items: habitacionesSeleccionadas.map((h) => ({
        habitacionId: h.idHabitacion,
        fechaInicio: h.fechaInicio,
        fechaFin: h.fechaFin,
      })),
    };

    try {
      setLoading(true);
      setError(null);
      setSuccess(false);

      const response = await fetch("/api/reservas", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(solicitud),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || "Error desconocido al crear la reserva");
      }

      await response.json();
      setSuccess(true); // ✅ mostramos el overlay de éxito
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      {cabecera()}
      <div className="formulario-reserva-wrapper">
        <div className="formulario-reserva-box">
          <h2 className="formulario-reserva-titulo">Reserva a nombre de:</h2>

          <form className="formulario-reserva-form">
            <div className="formulario-reserva-campo">
              <label htmlFor="nombre">Nombre</label>
              <input
                id="nombre"
                name="nombre"
                type="text"
                placeholder="Ingrese un Nombre"
                value={formData.nombre}
                onChange={handleChange}
              />
            </div>

            <div className="formulario-reserva-campo">
              <label htmlFor="apellido">Apellido</label>
              <input
                id="apellido"
                name="apellido"
                type="text"
                placeholder="Ingrese un Apellido"
                value={formData.apellido}
                onChange={handleChange}
              />
            </div>

            <div className="formulario-reserva-campo">
              <label htmlFor="telefono">Teléfono</label>
              <input
                id="telefono"
                name="telefono"
                type="tel"
                placeholder="Ingrese un Teléfono"
                value={formData.telefono}
                onChange={handleChange}
              />
            </div>

            <div className="formulario-reserva-footer">
              <span className="formulario-reserva-obligatorio"></span>
              <div className="formulario-reserva-botones">
                <button
                  type="button"
                  className="btn btn-cancelar"
                  onClick={handleCancelar}
                >
                  Cancelar
                </button>
                <button
                  type="button"
                  className="btn btn-siguiente"
                  onClick={handleConfirmar}
                  disabled={loading}
                >
                  {loading ? "Enviando..." : "Confirmar"}
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>

      {error && (
        <MensajeError
          mensaje={error}
          onClose={() => setError(null)}
        />
      )}

      {success && (
        <MensajeCargaExitosa
          onClose={() => setSuccess(false)}
        />
      )}
    </>
  );
}

export default function FormularioReservaUsuario() {
  return (
    <Suspense fallback={<div>Cargando...</div>}>
      <FormularioReservaUsuarioContent />
    </Suspense>
  );
}