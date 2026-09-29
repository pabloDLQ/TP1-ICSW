
// app/reservar/page.jsx
"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { cabecera } from "../components/Cabecera";
import { MensajeError } from "../components/MensajeError";
import { MensajeCancelar } from "../components/MensajeCancelar";

export default function Reservar() {
  const router = useRouter();


  const [desde, setDesde] = useState("");
  const [hasta, setHasta] = useState("");
  const [error, setError] = useState(null);
  const [mostrarMensajeCancelar, setMostrarMensajeCancelar] = useState(false);

  const today = new Date();
  const myLocalDate = [
    today.getFullYear(),
    String(today.getMonth() + 1).padStart(2, "0"),
    String(today.getDate()).padStart(2, "0"),
  ].join("-");

  const hoy = new Date(myLocalDate);

  const onSubmit = (e) => {
    e.preventDefault();
    setError(null);

    if (!desde || !hasta) {
      setError("Debe ingresar ambas fechas (desde y hasta).");
      return;
    }

    if (desde > hasta) {
      setError("La fecha 'desde' no puede ser mayor que la fecha 'hasta'.");
      return;
    }

    // Validación: fechas anteriores a la actual
    const hoy = new Date(myLocalDate);
    const desdeDate = new Date(desde);
    const hastaDate = new Date(hasta);

    if (desdeDate < hoy) {
      setError(`La fecha ${desdeDate.toLocaleDateString("es-AR")} es anterior a la fecha actual.`);
      return;
    }
    if (hastaDate < hoy) {
      setError(`La fecha ${hastaDate.toLocaleDateString("es-AR")} es anterior a la fecha actual.`);
      return;
    }

    // Navega a la grilla con las fechas
    router.push(`/reservar-habitaciones?inicio=${desde}&fin=${hasta}`);
  };

  return (
    <>
      {cabecera()}

      {/* Contenedor centrado según referencia visual */}
      <div className="contenedor-fechas">
        <div className="card-fechas">
          <h2 className="titulo-fechas">Ingresar fechas de disponibilidad de habitaciones</h2>

          <form className="form-fechas" onSubmit={onSubmit} noValidate>
            <div className="fila-inputs">
              <div className="grupo-fecha">
                <label htmlFor="desde-fecha">Desde fecha:</label>
                <input
                  type="date"
                  id="desde-fecha"
                  min={myLocalDate}
                  value={desde}
                  onChange={(e) => setDesde(e.target.value)}
                />
              </div>

              <div className="grupo-fecha">
                <label htmlFor="hasta-fecha">Hasta fecha:</label>
                <input
                  type="date"
                  id="hasta-fecha"
                  min={myLocalDate}
                  value={hasta}
                  onChange={(e) => setHasta(e.target.value)}
                />
              </div>
            </div>

            <div className="botones-fechas">
              {/* Mantiene misma funcionalidad: abrir mensaje de cancelar */}
              <button
                type="button"
                className="btn btn-cancelar"
                onClick={() => setMostrarMensajeCancelar(true)}
              >
                Cancelar
              </button>

              {/* Mantiene submit; el texto visual es "Validar" como la referencia */}
              <button type="submit" className="btn btn-siguiente">
                Validar
              </button>
            </div>
          </form>
        </div>
      </div>


      {error && <MensajeError mensaje={error} onClose={() => setError(null)} />}
      {mostrarMensajeCancelar && (
        <MensajeCancelar onClose={() => setMostrarMensajeCancelar(false)} />
      )}
    </>
  );
}
