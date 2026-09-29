
"use client";
import React, { useMemo, useRef, useState } from "react";
import { useRouter } from "next/navigation";

// Cabecera y modales (mismo patrón que en otros CU)
import { cabecera } from "../components/Cabecera";
import { MensajeError } from "../components/MensajeError";
import { MensajeCancelar } from "../components/MensajeCancelar";

export default function FacturarPage() {
  const router = useRouter();

  // Paso del flujo
  const [step, setStep] = useState("ingreso");

  // Datos del flujo
  const [habitacion, setHabitacion] = useState("");
  const [fechaSalida, setFechaSalida] = useState(""); // ahora fecha de salida
  const [tipoFactura, setTipoFactura] = useState(null);
  const [valorEstadia, setValorEstadia] = useState(0);

  const [responsableId, setResponsableId] = useState(null);
  const [cuitTercero, setCuitTercero] = useState("");

  // Lista de ocupantes (se poblará al conectar API)
  // Estructura esperada: { id, apellido, nombre, tipoDoc, nroDoc, fechaNacimiento }
  const [estadiaId, setEstadiaId] = useState(null);
  const [ocupantes, setOcupantes] = useState([]); // reemplaza el mock vacío
  const [preview, setPreview] = useState(null);

  // Ítems/consumos (se poblarán al conectar API)
  // Estructura esperada: { id, descripcion, precio } para consumos
  const [consumos, setConsumos] = useState([]); // TODO: traer desde API

  // Selecciones del detalle
  const [pagarEstadia, setPagarEstadia] = useState(false);
  const [pagarConsumos, setPagarConsumos] = useState(false);

  // Totales (mock hasta conectar back)
  const totalConsumos = useMemo(
    () => consumos.reduce((acc, c) => acc + (Number(c.precio) || 0), 0),
    [consumos]
  );
  const totalSinIva = useMemo(
    () => (pagarEstadia ? valorEstadia : 0) + (pagarConsumos ? totalConsumos : 0),
    [pagarEstadia, pagarConsumos, valorEstadia, totalConsumos]
  );

  // Mensajes y modales
  const [errorMsg, setErrorMsg] = useState(null); // string para MensajeError
  const [mostrarCancelar, setMostrarCancelar] = useState(false); // modal cancelar
  const [mostrarConfirmacion, setMostrarConfirmacion] = useState(false); // confirmar responsable

  // Foco al primer inválido
  const refHabitacion = useRef(null);
  const refFecha = useRef(null);

  // ===== Helpers de error =====
  const showErrors = (erroresArray) => {
    setErrorMsg(erroresArray.map(e => `• ${e}`).join("\n"));
  };

  // Responsable seleccionado (texto a mostrar en la confirmación)
  const responsableDisplay = useMemo(() => {
    if (responsableId) {
      const r = ocupantes.find(o => o.id === responsableId);
      return r ? `${r.apellido} ${r.nombre}` : "RESPONSABLE SELECCIONADO";
    }
    if (cuitTercero) return `CUIT: ${cuitTercero}`;
    return "RESPONSABLE NO DEFINIDO";
  }, [responsableId, cuitTercero, ocupantes]);

  // ===== Paso 1: Ingreso y validación rápida (front) =====
  const validarIngresoFront = () => {
    const errores = [];
    const hab = (habitacion || "").trim();
    const fecha = (fechaSalida || "").trim();

    if (!hab) errores.push("Número de habitación no ingresado");
    if (hab && !/^[A-Z0-9]{1,6}$/.test(hab)) errores.push("Número de habitación con formato inválido (solo A-Z y 0-9)");
    if (!fecha) errores.push("Fecha de salida no ingresada");
    if (fecha && !/^\d{4}-\d{2}-\d{2}$/.test(fecha)) errores.push("Fecha de salida con formato inválido (YYYY-MM-DD)");

    return errores;
  };

  const onAceptarIngreso = async () => {
    const errores = validarIngresoFront();
    if (errores.length) {
      showErrors(errores);
      const hasHab = errores.some(m => m.toLowerCase().includes("habitación"));
      const hasFecha = errores.some(m => m.toLowerCase().includes("fecha"));
      if (hasHab && refHabitacion.current) refHabitacion.current.focus();
      else if (hasFecha && refFecha.current) refFecha.current.focus();
      return;
    }
    try {
      const resp = await fetch(
        `/api/facturacion/estadia/huespedes?idHabitacion=${habitacion}&fechaFin=${fechaSalida}`
      );
      if (!resp.ok) {
        const text = await resp.text();
        showErrors([
          "No se pudo obtener la estadía.",
          text,
          `Código de estado: ${resp.status}`,
        ]);
        return;
      }
      const data = await resp.json(); // EstadiaHuespedesDto
      // Guardamos datos clave
      setEstadiaId(data.estadiaId);
      setOcupantes(data.huespedes || []);
      // Avanzamos al siguiente paso
      setStep("huespedes");
    } catch (err) {
      console.error(err);
      showErrors(["Error de conexión con el servidor."]);
    }
  };

  // ===== Cancelar global (abre modal) =====
  const onCancelarGlobal = () => setMostrarCancelar(true);

  // ✅ CAMBIO MÍNIMO: separar "Cerrar" vs "Confirmar"
  const cancelarCerrar = () => {
    // Cierra el modal y permanece en el paso actual (NO navega)
    setMostrarCancelar(false);
  };
  const cancelarConfirmar = () => {
    // Confirma cancelación y navega al Home
    setMostrarCancelar(false);
    router.push("/");
  };

  // ===== Huespedes: selección responsable o CUIT (mutua exclusión) =====
  const onSeleccionarResponsable = (id) => {
    if (cuitTercero) return; // si hay CUIT cargado, no permitimos selección
    setResponsableId(id === responsableId ? null : id);
  };

  const onIngresarCuit = (val) => {
    const cleaned = val.replace(/[^0-9-]/g, "");
    setCuitTercero(cleaned);
    if (cleaned.length > 0) setResponsableId(null);
  };

  const puedeContinuarHuespedes = useMemo(() => {
    if (responsableId) return true;

    if (!cuitTercero) return false;

    const soloDigitos = cuitTercero.replace(/-/g, "");
    return /^\d{11}$/.test(soloDigitos);
  }, [responsableId, cuitTercero]);

  const onContinuarHuespedes = () => {
    if (!puedeContinuarHuespedes) {
      showErrors(["Debe seleccionar un responsable de pago o ingresar un CUIT de tercero válido (11 dígitos)."]);
      return;
    }
    // Mostrar confirmación del responsable antes de pasar al detalle
    setMostrarConfirmacion(true);
  };

  // Confirmación del responsable
  const onConfirmarResponsable = async () => {
    setMostrarConfirmacion(false);
    if (!estadiaId) {
      showErrors(["No se encontró la estadía."]);
      return;
    }

    // Construir responsablePago
    const responsablePago = responsableId
      ? { huespedId: responsableId, cuit: null }
      : cuitTercero
      ? { huespedId: null, cuit: cuitTercero }
      : null;

    if (!responsablePago) {
      showErrors(["Debe seleccionar un responsable de pago o ingresar un CUIT válido."]);
      return;
    }

    try {
      const resp = await fetch(`/api/facturacion/estadia/${estadiaId}/preview`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(responsablePago),
      });
      if (!resp.ok) {
        const text = await resp.text();
        showErrors([
          "No se pudo obtener la previsualización de facturación.",
          text,
          `Código de estado: ${resp.status}`,
        ]);
        return;
      }
      const data = await resp.json(); // FacturacionPreviewDto
      setPreview(data);

      setPagarEstadia(true);
      setPagarConsumos(true);

      setConsumos(data.consumosIndividuales || []);
      // valorEstadia ya lo tenés mockeado, ahora lo reemplazamos:
      // (si tu estado se llama valorEstadia)
      // setValorEstadia(data.valorEstadia);

      setConsumos(data.consumosIndividuales || []);
      setValorEstadia(data.valorEstadia);
      setTipoFactura(data.tipoFactura);

      setStep("detalle");
    } catch (err) {
      console.error(err);
      showErrors(["Error de conexión con el servidor."]);
    }
  };

  const onCancelarConfirmacion = () => {
    // Solo cerrar modal y volver a la pantalla de selección (no termina el CU)
    setMostrarConfirmacion(false);
  };

  // ===== Paso Detalle (Registro de Gastos Generales) =====
  const onAceptarDetalle = async () => {
    if (!pagarEstadia && !pagarConsumos) {
      showErrors(["Debe seleccionar al menos un ítem (Estadía o Consumos) para continuar."]);
      return;
    }
    if (!estadiaId) {
      showErrors(["No se encontró la estadía a facturar."]);
      return;
    }

    const responsablePago = responsableId
      ? { huespedId: responsableId, cuit: null }
      : cuitTercero
      ? { huespedId: null, cuit: cuitTercero }
      : null;

    if (!responsablePago) {
      showErrors(["Debe seleccionar un responsable de pago o ingresar un CUIT válido."]);
      return;
    }

    const payload = {
      estadiaId,
      responsablePago,
      facturarEstadia: pagarEstadia,
      facturarConsumos: pagarConsumos,
    };

    try {
      const resp = await fetch(`/api/facturacion`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
      if (!resp.ok) {
        const text = await resp.text();
        showErrors([
          "Error al crear la factura.",
          text,
          `Código de estado: ${resp.status}`,
        ]);
        return;
      }
      const factura = await resp.json(); // FacturaResultadoDto
      router.push(`/generar-factura?data=${encodeURIComponent(JSON.stringify(factura))}`);
    } catch (err) {
      console.error(err);
      showErrors(["No se pudo conectar con el servidor de facturación."]);
    }
  };

  return (
    <>
      {cabecera()}

      <div className="main-container">
        {/* Título general de la pantalla */}
        <h2>Facturación de Habitación</h2>

        {step === "ingreso" && (
          <PasoIngreso
            habitacion={habitacion}
            fechaSalida={fechaSalida}
            setHabitacion={setHabitacion}
            setFechaSalida={setFechaSalida}
            onAceptar={onAceptarIngreso}
            onCancelar={onCancelarGlobal}
            refHabitacion={refHabitacion}
            refFecha={refFecha}
          />
        )}

        {step === "huespedes" && (
          <PasoHuespedes
            ocupantes={ocupantes}
            responsableId={responsableId}
            cuitTercero={cuitTercero}
            onSeleccionarResponsable={onSeleccionarResponsable}
            onIngresarCuit={onIngresarCuit}
            onCancelar={onCancelarGlobal}
            onContinuar={onContinuarHuespedes}
          />
        )}

        {step === "detalle" && (
          <PasoDetalle
            responsableDisplay={responsableDisplay}
            tipoFactura={tipoFactura}
            pagarEstadia={pagarEstadia}
            setPagarEstadia={setPagarEstadia}
            pagarConsumos={pagarConsumos}
            setPagarConsumos={setPagarConsumos}
            valorEstadia={valorEstadia}
            totalConsumos={totalConsumos}
            totalSinIva={totalSinIva}
            consumos={consumos}
            onCancelar={onCancelarGlobal}
            onAceptar={onAceptarDetalle}
          />
        )}

        {/* Mensaje de Error */}
        {errorMsg && (
          <MensajeError mensaje={errorMsg} onClose={() => setErrorMsg(null)} />
        )}

        {/* Modal Cancelar: AHORA con onClose (solo cerrar) y onConfirm (ir a Home) */}
        {mostrarCancelar && (
          <MensajeCancelar
            onClose={cancelarCerrar}       // solo cierra, NO navega
            onConfirm={cancelarConfirmar}  // confirma, navega al Home
          />
        )}

        {/* Modal Confirmar Responsable */}
        {mostrarConfirmacion && (
          <ConfirmarResponsableModal
            texto={`La persona/empresa que realizará el pago es:\n\n${responsableDisplay}`}
            onCancelar={onCancelarConfirmacion}
            onAceptar={onConfirmarResponsable}
          />
        )}
      </div>
    </>
  );
}

/* ===== Subcomponentes ===== */

function PasoIngreso({
  habitacion,
  fechaSalida,
  setHabitacion,
  setFechaSalida,
  onAceptar,
  onCancelar,
  refHabitacion,
  refFecha,
}) {
  const [capsLockOn, setCapsLockOn] = useState(false);
  const onKeyCheck = (e) => setCapsLockOn(e.getModifierState && e.getModifierState("CapsLock"));

  return (
    <div className="fondo-form-ingreso-fechas">
      {/* Título del formulario (actualizado) */}
      <h2 style={{ textAlign: "center" }}>
        Ingrese el número de habitación y la fecha de salida
      </h2>

      <div className="formulario-buscar-fechas">
        <div className="columna-buscar-fechas">
          <div className="fila-fecha">
            <label className="label-fecha" htmlFor="numHabitacion">Nro. de Habitación:</label>
            <input
              id="numHabitacion"
              ref={refHabitacion}
              value={habitacion}
              placeholder="AB123"
              onKeyUp={onKeyCheck}
              onChange={(e) => setHabitacion(e.target.value.toUpperCase())}
            />
          </div>
        </div>

        <div className="columna-buscar-fechas">
          <div className="fila-fecha">
            <label className="label-fecha" htmlFor="fechaSalida">Fecha de salida:</label>
            {/* Calendario con clases existentes */}
            <div className="input-with-icon" style={{ width: "100%" }}>
              <input
                id="fechaSalida"
                ref={refFecha}
                type="date"
                value={fechaSalida}
                onKeyUp={onKeyCheck}
                onChange={(e) => setFechaSalida(e.target.value)}
              />
              <span className="calendar-icon">📅</span>
            </div>
          </div>
        </div>
      </div>

      {/* Indicador Bloq Mayús (opcional) */}
      <div style={{ display: "flex", justifyContent: "center", marginTop: 12 }}>
        {capsLockOn && (<span className="kbd-indicator">Bloq Mayús activado</span>)}
      </div>

      <div className="botones-fechas" style={{ marginTop: 24 }}>
        <button className="btn btn-cancelar" onClick={onCancelar}>Cancelar</button>
        <button className="btn btn-siguiente" onClick={onAceptar}>Aceptar</button>
      </div>
    </div>
  );
}

function PasoHuespedes({
  ocupantes,
  responsableId,
  cuitTercero,
  onSeleccionarResponsable,
  onIngresarCuit,
  onCancelar,
  onContinuar,
}) {
  return (
    <div className="panel-resultados">
      <div className="panel-interno-resultados">
        <div className="titulo-panel">Huéspedes</div>
        <div className="contenedor-tabla">
          <table className="tabla-resultados">
            <thead>
              <tr>
                <th>Apellido</th>
                <th>Nombre</th>
                <th>Tipo de documento</th>
                <th>Número de documento</th>
                <th>Fecha de nacimiento</th>
                <th>Resp.</th>
              </tr>
            </thead>
            <tbody>
              {(!ocupantes || ocupantes.length === 0) ? (
                <tr className="fila-vacia">
                  <td colSpan={6}>Sin datos (se cargará al conectar la API)</td>
                </tr>
              ) : (
                ocupantes.map(o => (
                  <tr
                    key={o.id}
                    className={responsableId === o.id ? "fila-seleccionada" : ""}
                    onClick={() => onSeleccionarResponsable(o.id)}
                  >
                    <td>{o.apellido}</td>
                    <td>{o.nombre}</td>
                    <td>{o.tipoDoc}</td>
                    <td>{o.nroDoc}</td>
                    <td>{o.fechaNacimiento}</td>
                    <td>
                      <input
                        type="radio"
                        checked={responsableId === o.id}
                        onChange={() => onSeleccionarResponsable(o.id)}
                        disabled={Boolean(cuitTercero)}
                        aria-label="Seleccionar como responsable"
                      />
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      <div style={{ marginTop: 20 }}>
        <label htmlFor="cuitTercero" style={{ fontWeight: "bold" }}>Pago de Terceros:</label>
        <div className="form-group" style={{ marginTop: 8 }}>
          <label htmlFor="cuitTercero">Ingrese el CUIT :</label>
          <input
            id="cuitTercero"
            placeholder="00 00000000 0"
            value={cuitTercero}
            onChange={(e) => onIngresarCuit(e.target.value)}
            disabled={Boolean(responsableId)}
            inputMode="numeric"
          />
        </div>
      </div>

      <div className="buttons-row" style={{ marginTop: 24 }}>
        <button className="btn btn-cancelar" onClick={onCancelar}>Cancelar</button>
        <button className="btn btn-siguiente" onClick={onContinuar}>Continuar</button>
      </div>
    </div>
  );
}

function PasoDetalle({
  responsableDisplay,
  tipoFactura,
  pagarEstadia,
  setPagarEstadia,
  pagarConsumos,
  setPagarConsumos,
  valorEstadia,
  totalConsumos,
  totalSinIva,
  consumos,
  onCancelar,
  onAceptar,
}) {
  return (
    <div className="card-form">
      <h2 style={{ marginBottom: 20 }}>Registro de Gastos Generales</h2>

      {/* Encabezado: persona física/jurídica */}
      <div className="panel-interno-resultados" style={{ marginBottom: 16 }}>
        <span><b>Persona Física / Jurídica :</b> {responsableDisplay}</span>
      </div>

      {/* Dos columnas: izquierda opciones, derecha consumos */}
      <div className="busqueda-layout" style={{ gridTemplateColumns: "1fr 1fr" }}>
        {/* Izquierda: opciones y resumen */}
        <div className="panel-interno-resultados">
          <div style={{ marginBottom: 12 }}>
            <b>Elija que se pagará a nombre de : {responsableDisplay}</b>
          </div>

          <div style={{ display: "flex", flexDirection: "column", gap: 8 }}>
            <label style={{ display: "inline-flex", alignItems: "center", gap: 8 }}>
              <input
                type="checkbox"
                checked={pagarEstadia}
                onChange={(e) => setPagarEstadia(e.target.checked)}
              />
              <span>
                Valor de la estadía : ${Number(valorEstadia).toLocaleString("es-AR")}
              </span>
            </label>

            <label style={{ display: "inline-flex", alignItems: "center", gap: 8 }}>
              <input
                type="checkbox"
                checked={pagarConsumos}
                onChange={(e) => setPagarConsumos(e.target.checked)}
              />
              <span>
                Consumo Total de la Habitación : ${Number(totalConsumos).toLocaleString("es-AR")}
              </span>
            </label>

            <div style={{ marginTop: 8 }}>
              <b>Total a Pagar (sin IVA) : </b>${Number(totalSinIva).toLocaleString("es-AR")}
            </div>

            <div style={{ marginTop: 4 }}>
              <b>Tipo de Factura : </b>{tipoFactura}
            </div>
          </div>
        </div>

        {/* Derecha: tabla de consumos */}
        <div className="panel-interno-resultados">
          <div className="titulo-panel" style={{ marginBottom: 8 }}>Consumos de la Habitación:</div>
          <div className="contenedor-tabla">
            <table className="tabla-resultados">
              <thead>
                <tr>
                  <th style={{ width: "70%" }}>Descripción</th>
                  <th style={{ width: "30%", textAlign: "right" }}>Precio</th>
                </tr>
              </thead>
              <tbody>
                {(!consumos || consumos.length === 0) ? (
                  <tr className="fila-vacia">
                    <td colSpan={2}>Sin datos (se cargarán al conectar la API)</td>
                  </tr>
                ) : (
                  consumos.map((c, idx) => (
                    <tr key={`${c.descripcion}-${c.precio}-${idx}`}>
                      <td>{c.descripcion}</td>
                      <td style={{ textAlign: "right" }}>
                        ${Number(c.precio).toLocaleString("es-AR")}
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Botones */}
      <div className="buttons-row" style={{ marginTop: 24 }}>
        <button className="btn btn-cancelar" onClick={onCancelar}>Cancelar</button>
        <button className="btn btn-siguiente" onClick={onAceptar}>Aceptar</button>
      </div>
    </div>
  );
}

/* ===== Modal de confirmación de responsable (reutiliza estilos existentes) ===== */
function ConfirmarResponsableModal({ texto, onCancelar, onAceptar }) {
  return (
    <div className="error-overlay" role="dialog" aria-modal="true">
      <div className="error-contenedor-principal" style={{ width: 520 }}>
        <div className="error-titulo-box">
          <p className="error-titulo-texto">La persona/empresa que realizará el pago es:</p>
        </div>
        <div className="error-detalle-box">
          <p className="error-detalle-texto" style={{ textAlign: "center", fontWeight: "bold" }}>
            {texto.split("\n").pop()}
          </p>
          <div className="error-detalle-boton" style={{ gap: 16 }}>
            <button className="btn btn-cancelar" onClick={onCancelar}>Cancelar</button>
            <button className="btn btn-siguiente" onClick={onAceptar}>Aceptar</button>
          </div>
        </div>
      </div>
    </div>
  );
}