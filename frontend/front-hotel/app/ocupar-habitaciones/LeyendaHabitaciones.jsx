export function LeyendaHabitaciones() {
  return (
    <>
        <div className="leyenda-background leyenda-estado" role="list" aria-label="Leyenda de estados de disponibilidad">
            <div className="leyenda-item leyenda-disponible" role="listitem" aria-label="Disponible">
                <span className="leyenda-cuadro" aria-hidden="true"></span>
                <span className="leyenda-label">Disponible</span>
            </div>
            <div className="leyenda-item leyenda-reservado" role="listitem" aria-label="Reservado">
                <span className="leyenda-cuadro" aria-hidden="true"></span>
                <span className="leyenda-label">Reservado</span>
            </div>
            <div className="leyenda-item leyenda-ocupado" role="listitem" aria-label="Ocupado">
                <span className="leyenda-cuadro" aria-hidden="true"></span>
                <span className="leyenda-label">Ocupado</span>
            </div>
            <div className="leyenda-item leyenda-no-disponible" role="listitem" aria-label="No disponible">
                <span className="leyenda-cuadro" aria-hidden="true"></span>
                <span className="leyenda-label">No disponible</span>
            </div>
        </div>
    </>
    );
}