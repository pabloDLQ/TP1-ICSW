"use client";

import { cabecera } from "../components/Cabecera";
import Botonera from "./botonera";
import {UsuarioYaCargado} from "../cargar-huesped/HuespedYaCargado";

export default function home() {
  return (
    <>
    {cabecera()}
    <div className="main-container">
      <h2>¡Bienvenido al hotel Premier!</h2>
      <div className="home-container">
        <Botonera />
      </div>
    </div>
    </>
  );
}
