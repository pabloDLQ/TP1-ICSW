"use client";

export function botonSiguiente({ destino }) {
  return (
    <button 
    onClick={() => window.location.href = destino}
    type="submit" className="btn btn-siguiente">SIGUIENTE</button>
  );
}

export function botonCancelar({ destino }) {
  return (
    <button 
    onClick={() => window.location.href = destino}
    type="submit" className="btn btn-cancelar">CANCELAR</button>
  );
}

export function botonRechazar({ destino }) {
  return (
    <button 
    onClick={() => window.location.href = destino}
    type="submit" className="btn btn-cancelar">RECHAZAR</button>
  );
}

export function botonCerrar({ destino }) {
  return (
    <button 
    onClick={() => window.location.href = destino}
    type="submit" className="btn btn-cancelar">CERRAR</button>
  );
}

export function botonCerrar2(className, onClose) {
  return (
    <button
      type="button"
      className="btn btn-cancelar"
      onClick={onClose}   // aquí llamamos al callback
    >
      Cerrar
    </button>
  );
}


export function botonValidar({ destino }) {
  return (
    <button 
    onClick={() => window.location.href = destino}
    type="submit" className="btn btn-siguiente">VALIDAR</button>
  );
}

export function botonConfirmar({ destino }) {
  return (
    <button 
    onClick={() => window.location.href = destino}
    type="submit" className="btn btn-siguiente">CONFIRMAR</button>
  );
}

export function botonBuscar({ destino }) {
  return (
    <button 
    onClick={() => window.location.href = destino}
    type="submit" className="btn btn-siguiente">BUSCAR</button>
  );
}

export function botonCancelarCorto({ destino }) {
  return (
    <button 
    onClick={() => window.location.href = destino}
    type="submit" className="btn btn-cancelarCorto">CANCELAR</button>
  );
}

export function botonVolver2(className, onClose) {
  return (
    <button
      type="button"
      className="btn btn-cancelar"
      onClick={onClose}   // aquí llamamos al callback
    >
      Volver
    </button>
  );
}

export function botonPersonalizado({ texto, destino }) {
  return (
    <button 
    onClick={() => window.location.href = destino}
    type="submit" className="btn btn-siguiente">{texto}</button>
  );
}