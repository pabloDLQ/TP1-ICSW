
"use client";

import { botonPersonalizado } from "../components/Botones";

export default function Botonera() {
  return (
    <div
      style={{
        display: "grid",
        gridTemplateColumns: "1fr 1fr",
        gap: "40px",
        padding: "10px",
      }}
    >
      <div style={{ textAlign: "center" }}>
        {botonPersonalizado({ texto: "Buscar un huésped", destino: "/huespedes" })}
        <div style={{ marginTop: "6px" }}>(casos de uso 02 y 09)</div>
      </div>

      <div style={{ textAlign: "center" }}>
        {botonPersonalizado({ texto: "Reservar habitación", destino: "/reservar-fechas" })}
        <div style={{ marginTop: "6px" }}>(casos de uso 04 y 05)</div>
      </div>

      <div style={{ textAlign: "center" }}>
        {botonPersonalizado({ texto: "Cancelar reserva", destino: "/cancelar-reserva" })}
        <div style={{ marginTop: "6px" }}>(caso de uso 06)</div>
      </div>

      <div style={{ textAlign: "center" }}>
        {botonPersonalizado({ texto: "Dar de alta huésped", destino: "/cargar-huesped" })}
        <div style={{ marginTop: "6px" }}>(caso de uso 09)</div>
      </div>

      <div style={{ textAlign: "center" }}>
        {botonPersonalizado({ texto: "Ocupar habitación", destino: "/ocupar-fechas" })}
        <div style={{ marginTop: "6px" }}>(casos de uso 05 y 15)</div>
      </div>

      <div style={{ textAlign: "center" }}>
        {botonPersonalizado({ texto: "Dar de baja huésped", destino: "/baja-huesped" })}
        <div style={{ marginTop: "6px" }}>(caso de uso 11)</div>
      </div>

      {/* Agregado: botón para CU07 - Facturar (manteniendo formato original) */}
      <div style={{ textAlign: "center" }}>
        {botonPersonalizado({ texto: "Facturar habitación", destino: "/facturar" })}
        <div style={{ marginTop: "6px" }}>(caso de uso 07)</div>
      </div>
    </div>
  );
}
