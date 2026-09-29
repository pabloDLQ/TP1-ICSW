"use client";

export function BusquedaHuespedDer() {
  return (
    <div className="panel-resultados">
      <h2 className="titulo-panel">Resultados</h2>

      <div className="panel-interno-resultados">
        <div className="contenedor-tabla">
          <table className="tabla-resultados">
            <thead>
              <tr>
                <th>N°</th>
                <th>Apellido</th>
                <th>Tipo Doc.</th>
                <th>N° Documento</th>
              </tr>
            </thead>
            <tbody>
              <tr className="fila-vacia">
                <td colSpan="4">Ingrese datos para empezar a buscar</td>
              </tr>
            </tbody>
          </table>
        </div>

        <div className="paginacion">
          <button>←</button>
          <span>Página 1</span>
          <button>→</button>
        </div>
      </div>
    </div>
  );
}
