"use client";

import { cabecera } from "../components/Cabecera";

export default function ModificarHuesped() {
  return (
    <>
      {cabecera()}
      <div
        className="main-container"
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          height: "80vh",
        }}
      >
        <div
          className="card-form"
          style={{ textAlign: "center", padding: "2rem" }}
        >
          <h1>Caso de Uso 10 Modificar huésped</h1>
        </div>
      </div>
    </>
  );
}
