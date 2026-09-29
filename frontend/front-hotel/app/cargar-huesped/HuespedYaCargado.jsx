"use client";

export function HuespedYaCargado({ huesped, onClose, onSuccess, onError }) {
    
  const aceptarIgualmente = async () => {
    try {
      const response = await fetch("/api/huespedes", {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(huesped),
      });
      if (response.ok) {
        onSuccess(); // avisás al padre que salió bien
      } else {
        const errorData = await response.text();
        onError("Error al modificar huésped: " + errorData);
      }
    } catch {
      onError("Error de conexión con el backend");
    }
  };
  
   return (
    <div className="error-overlay">
      <div className="error-contenedor-principal">
        <div className="error-titulo-box">
          <p className="error-titulo-textoCU07">
            ¡CUIDADO! El tipo y número de documento ya existen en el sistema
          </p>

          <div className="error-detalle-boton">
            <button
              className="btn btn-cancelar"
              onClick={aceptarIgualmente}
            >
              ACEPTAR IGUALMENTE
            </button>

            <button
              className="btn btn-siguiente"
              onClick={onClose}
            >
              CORREGIR
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}