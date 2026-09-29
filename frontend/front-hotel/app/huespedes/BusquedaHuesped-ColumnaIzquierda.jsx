"use client";

import { botonBuscar } from "../components/Botones";
import { botonCancelarCorto } from "../components/Botones";

export function BusquedaHuespedIzq() { 
  return (
    <div className="busqueda-container">
      {/* Columna izquierda: formulario */}
      <div className="card-form">
        <h2>Datos del huésped</h2>
        <form className="form-busqueda">
          <div className="form-group">
            <label htmlFor="nombre">Nombre</label>
            <input type="text" id="nombre" placeholder="Ingrese nombre" />
          </div>

          <div className="form-group">
            <label htmlFor="apellido">Apellido</label>
            <input type="text" id="apellido" placeholder="Ingrese apellido" />
          </div>

         <div className="form-group">
        <label htmlFor="tipoDocumento">Tipo de Documento</label>
        <select id="tipoDocumento" defaultValue="">
          <option value="" disabled hidden>
            Seleccione tipo de documento
          </option>
          <option value="DNI">DNI</option>
          <option value="LE">LE</option>
          <option value="LC">LC</option>
          <option value="Pasaporte">Pasaporte</option>
          <option value="Otros">Otros</option>
        </select>
      </div>

          <div className="form-group">
            <label htmlFor="numeroDocumento">Número de Documento</label>
            <input type="text" id="numeroDocumento" placeholder="Ingrese número" />
          </div>

          <div className="acciones-form-limitado">
            {botonBuscar({ destino: "/busco" })}
            {botonCancelarCorto({ destino: "/cancelo" })}
          </div>

        </form>
      </div>
    </div>
  );
}