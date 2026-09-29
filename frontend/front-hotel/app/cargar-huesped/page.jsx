"use client";

import { useState } from "react";
import { cabecera } from "../components/Cabecera";
import { HuespedYaCargado } from "./HuespedYaCargado";
import { MensajeError } from "../components/MensajeError";
import { MensajeCancelar } from "../components/MensajeCancelar";

export default function FormularioAltaHuesped() {

  const [mostrarMensajeCancelar, setMostrarMensajeCancelar] = useState(false);
  const [mostrarHuespedYaCargado, setMostrarHuespedYaCargado] = useState(false);
  const [huespedDuplicado, setHuespedDuplicado] = useState(null);


  const [error, setError] = useState(null);


  const [nombre, setNombre] = useState("");
  const [apellido, setApellido] = useState("");
  const [tipoDocumento, setTipoDocumento] = useState("");
  const [numeroDocumento, setNumeroDocumento] = useState("");


  const [cuit, setCuit] = useState("");
  const [telefono, setTelefono] = useState("");
  const [fechaNacimiento, setFechaNacimiento] = useState("");
  const [posicionIva, setPosicionIva] = useState("CONSUMIDOR_FINAL");
  const [ocupacion, setOcupacion] = useState("");
  const [email, setEmail] = useState("");
  const [nacionalidad, setNacionalidad] = useState("");


  const [calle, setCalle] = useState("");
  const [numero, setNumero] = useState("");
  const [piso, setPiso] = useState("");
  const [nroDepartamento, setNroDepartamento] = useState("");
  const [codigoPostal, setCodigoPostal] = useState("");
  const [localidad, setLocalidad] = useState("");
  const [provincia, setProvincia] = useState("");
  const [pais, setPais] = useState("");


  const handleSubmit = async (e) => {
    e.preventDefault();


    const errores = [];
    if (!nombre) errores.push("El campo Nombre es obligatorio");
    if (!apellido) errores.push("El campo Apellido es obligatorio");
    if (!tipoDocumento) errores.push("Debe seleccionar un tipo de documento");
    if (!numeroDocumento) errores.push("El campo Número de Documento es obligatorio");
    if (!telefono) errores.push("El campo Teléfono es obligatorio");
    if (!fechaNacimiento) errores.push("La Fecha de Nacimiento es obligatoria");
    if (!posicionIva) errores.push("Debe seleccionar la Posición frente al IVA");
    if (!ocupacion) errores.push("El campo Ocupación es obligatorio");
    if (!nacionalidad) errores.push("El campo Nacionalidad es obligatorio");
    if (!calle) errores.push("El campo Calle es obligatorio");
    if (!numero) errores.push("El campo Número es obligatorio");
    if (!codigoPostal) errores.push("El campo Código Postal es obligatorio");
    if (!localidad) errores.push("El campo Localidad es obligatorio");
    if (!provincia) errores.push("El campo Provincia es obligatorio");
    if (!pais) errores.push("El campo País es obligatorio");

    if (errores.length > 0) {
      setError('{"' + errores.join('","') + '"}');
      return;
    }

    const huesped = {
      nombre,
      apellido,
      tipoDocumento,
      numeroDocumento,
      cuit,
      telefono,
      fechaNacimiento, // "YYYY-MM-DD"
      posicionIva,
      ocupacion,
      email,
      nacionalidad,
      direccion: {
        calle,
        numero,
        piso,
        nroDepartamento,
        codigoPostal,
        localidad,
        provincia,
        pais,
      },
    };

    try {
      const response = await fetch("/api/huespedes", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(huesped),
      });

      if (response.ok) {
        setError(null);
        alert("Huésped agregado correctamente");

        setNombre("");
        setApellido("");
        setTipoDocumento("");
        setNumeroDocumento("");
        setCuit("");
        setTelefono("");
        setFechaNacimiento("");
        setPosicionIva("CONSUMIDOR_FINAL"); 
        setOcupacion("");
        setEmail("");
        setNacionalidad("");
        setCalle("");
        setNumero("");
        setPiso("");
        setNroDepartamento("");
        setCodigoPostal("");
        setLocalidad("");
        setProvincia("");
        setPais("");
      } else {
        let errorMessage;
        try {
          const errorData = await response.text(); 
          errorMessage = errorData;
        } catch {
          errorMessage = "Error desconocido";
        }


        if (errorMessage.includes("El huésped ya existe")) {
          setHuespedDuplicado(huesped);
          setMostrarHuespedYaCargado(true);
        } else {
          setError("Error al agregar huésped: " + errorMessage);
        }
      }

    } catch {
      setError("Error de conexión con el backend");
    }
  };

    return (
    <>
      {cabecera()}
      <div className="main-container">
        <div className="fondo-form-dar-alta">
          <h2>Datos del Huésped</h2>

          <form className="formulario-dar-alta-12columnas" onSubmit={handleSubmit}>
            {/* Nombre y Apellido */}
            <div className="columna-dar-alta medio-ancho">
              <label>Nombre *</label>
              <input
                type="text"
                placeholder="Ingrese nombre"
                value={nombre}
                onChange={(e) => setNombre(e.target.value)}
              />
            </div>
            <div className="columna-dar-alta medio-ancho">
              <label>Apellido *</label>
              <input
                type="text"
                placeholder="Ingrese apellido"
                value={apellido}
                onChange={(e) => setApellido(e.target.value)}
              />
            </div>

            {/* Tipo y Número de Documento */}
            <div className="columna-dar-alta medio-ancho">
              <label>Tipo de Documento *</label>
              <select
                value={tipoDocumento}
                onChange={(e) => setTipoDocumento(e.target.value)}
              >
                <option value="" disabled hidden>
                  Seleccione tipo de documento
                </option>
                <option value="DNI">DNI</option>
                <option value="LE">LE</option>
                <option value="LC">LC</option>
                <option value="PASAPORTE">Pasaporte</option>
                <option value="OTRO">Otros</option>
              </select>
            </div>
            <div className="columna-dar-alta medio-ancho">
              <label>Número de Documento *</label>
              <input
                type="text"
                placeholder="Ingrese número"
                value={numeroDocumento}
                onChange={(e) => setNumeroDocumento(e.target.value)}
              />
            </div>

            {/* CUIT y Teléfono */}
            <div className="columna-dar-alta medio-ancho">
              <label>CUIT</label>
              <input
                type="text"
                placeholder="Ingrese CUIT"
                value={cuit}
                onChange={(e) => setCuit(e.target.value)}
              />
            </div>
            <div className="columna-dar-alta medio-ancho">
              <label>Teléfono *</label>
              <input
                type="text"
                placeholder="Ingrese teléfono"
                value={telefono}
                onChange={(e) => setTelefono(e.target.value)}
              />
            </div>

            {/* Fecha de Nacimiento */}
            <div className="columna-dar-alta medio-ancho">
              <label>Fecha de Nacimiento *</label>
              <div className="input-with-icon">
                <input
                  type="date"
                  value={fechaNacimiento}
                  onChange={(e) => setFechaNacimiento(e.target.value)}
                />
                <span className="calendar-icon">📅</span>
              </div>
            </div>

            {/* Posición frente al IVA y Ocupación */}
            <div className="columna-dar-alta medio-ancho">
              <label>Posición Frente al IVA *</label>
              <select
                value={posicionIva}
                onChange={(e) => setPosicionIva(e.target.value)}
              >
                <option value="" disabled hidden>
                  Seleccione posición
                </option>
                <option value="CONSUMIDOR_FINAL">Consumidor Final</option>
                <option value="RESPONSABLE_INSCRIPTO">Responsable Inscripto</option>
                <option value="EXENTO">Exento</option>
              </select>
            </div>
            <div className="columna-dar-alta medio-ancho">
              <label>Ocupación *</label>
              <input
                type="text"
                placeholder="Ingrese ocupación"
                value={ocupacion}
                onChange={(e) => setOcupacion(e.target.value)}
              />
            </div>

            {/* Email y Nacionalidad */}
            <div className="columna-dar-alta medio-ancho">
              <label>Email</label>
              <input
                type="email"
                placeholder="Ingrese email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>
            <div className="columna-dar-alta medio-ancho">
              <label>Nacionalidad *</label>
              <input
                type="text"
                placeholder="Ingrese nacionalidad"
                value={nacionalidad}
                onChange={(e) => setNacionalidad(e.target.value)}
              />
            </div>

            {/* Domicilio */}
            <div className="columna-dar-alta completo-ancho">
              <label className="domicilio-label">Domicilio</label>
            </div>
                        {/* Calle y Número */}
            <div className="columna-dar-alta completo-ancho street-number-group">
              <div className="inline-input">
                <label>Calle *</label>
                <input
                  type="text"
                  placeholder="Ingrese calle"
                  value={calle}
                  onChange={(e) => setCalle(e.target.value)}
                />
              </div>
              <div className="inline-input number-input">
                <label>Número *</label>
                <input
                  type="text"
                  placeholder="Ingrese número"
                  value={numero}
                  onChange={(e) => setNumero(e.target.value)}
                />
              </div>
            </div>

            {/* Piso y Departamento */}
            <div className="columna-dar-alta completo-ancho dept-floor-group">
              <div className="inline-input floor-input">
                <label>Piso</label>
                <input
                  type="text"
                  placeholder="Ingrese piso"
                  value={piso}
                  onChange={(e) => setPiso(e.target.value)}
                />
              </div>
              <div className="inline-input">
                <label>Departamento</label>
                <input
                  type="text"
                  placeholder="Ingrese departamento"
                  value={nroDepartamento}
                  onChange={(e) => setNroDepartamento(e.target.value)}
                />
              </div>
            </div>

            {/* Código Postal y Localidad */}
            <div className="columna-dar-alta completo-ancho postcode-location-group">
              <div className="inline-input">
                <label>Código Postal *</label>
                <input
                  type="text"
                  placeholder="Ingrese código postal"
                  value={codigoPostal}
                  onChange={(e) => setCodigoPostal(e.target.value)}
                />
              </div>
              <div className="inline-input location-select">
                <label>Localidad *</label>
                <input
                  type="text"
                  placeholder="Ingrese localidad"
                  value={localidad}
                  onChange={(e) => setLocalidad(e.target.value)}
                />
              </div>
            </div>

            {/* Provincia y País */}
            <div className="columna-dar-alta completo-ancho province-country-group">
              <div className="inline-input">
                <label>Provincia *</label>
                <input
                  type="text"
                  placeholder="Ingrese provincia"
                  value={provincia}
                  onChange={(e) => setProvincia(e.target.value)}
                />
              </div>
              <div className="inline-input country-select">
                <label>País *</label>
                <input
                  type="text"
                  placeholder="Ingrese país"
                  value={pais}
                  onChange={(e) => setPais(e.target.value)}
                />
              </div>
            </div>

            {/* Acciones */}
            <div className="form-actions">
              <span className="required-note">(*) Campos obligatorios</span>
              <div className="buttons-container">
                {/* Botón cancelar no envía nada */}
                <button
                  type="button" 
                  className="btn btn-cancelar"
                  onClick={() => setMostrarMensajeCancelar(true)}
                >
                  Cancelar
                </button>
                {/* Botón siguiente envía el formulario */}
                <button type="submit" className="btn btn-siguiente">Siguiente</button>
              </div>
            </div>
          </form>
        </div>
      </div>
      {error && (
        <MensajeError
          mensaje={error}
          onClose={() => setError(null)}
        />
      )}
      {mostrarMensajeCancelar && (
        <MensajeCancelar 
          onClose={() => setMostrarMensajeCancelar(false)} 
        />
      )}
      {mostrarHuespedYaCargado && (
        <HuespedYaCargado
          huesped={huespedDuplicado}
          onClose={() => setMostrarHuespedYaCargado(false)}
          onSuccess={() => {
            alert("Huésped cargado correctamente");
            setMostrarHuespedYaCargado(false);
            location.reload();
          }}
          onError={(msg) => setError(msg)}
        />
      )}
    </>
  );
}
