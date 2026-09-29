
// app/reservar/page.jsx
"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { cabecera } from "../components/Cabecera";
import { botonCancelar } from "../components/Botones";
import { MensajeError } from "../components/MensajeError";

export default function Reservar() {
  const router = useRouter();
  const myLocalDate = new Date().toISOString().split("T")[0];

  const [desde, setDesde] = useState("");
  const [hasta, setHasta] = useState("");
  const [error, setError] = useState(null);

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

    router.push(`/ocupar-habitaciones?inicio=${desde}&fin=${hasta}`);
  };

  return (
    <>
      {cabecera()}

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
              {/* Reutilizamos tu componente para Cancelar */}
              {botonCancelar({ destino: "#" })}

              {/* Botón principal con texto “Validar” */}
              <button type="submit" className="btn btn-siguiente">
                Validar
              </button>
            </div>
          </form>
        </div>
      </div>

      {error && <MensajeError mensaje={error} onClose={() => setError(null)} />}
    </>
  );
}
