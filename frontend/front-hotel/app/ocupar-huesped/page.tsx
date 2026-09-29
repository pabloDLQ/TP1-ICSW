"use client";

import React, { useEffect, useMemo, useState, Suspense } from "react";
import "../globals.css";
import { cabecera } from "../components/Cabecera";
import { useRouter } from "next/navigation";
import { Opciones } from "./Opciones";
import { MensajeError } from "../components/MensajeError";
import { useSearchParams } from "next/navigation";
import { MensajeCancelar } from "../components/MensajeCancelar";

type TipoDocumento = "DNI" | "LE" | "LC" | "PASAPORTE" | "OTRO";

interface HuespedDto {
  apellido: string;
  nombre: string;
  tipoDocumento: TipoDocumento;
  numeroDocumento: string;
}

const PAGE_SIZE = 5;

function OcuparHuespedContent() {
  const router = useRouter();

  // --- ESTADOS ---
  const [formData, setFormData] = useState({
    nombre: "",
    apellido: "",
    tipoDocumento: "" as "" | TipoDocumento,
    numeroDocumento: "",
  });

  const [resultados, setResultados] = useState<HuespedDto[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string>("");
  const [pagina, setPagina] = useState<number>(1);
  const [selectedHuespedes, setSelectedHuespedes] = useState<HuespedDto[]>([]);
  const [mostrarOpciones, setMostrarOpciones] = useState(false);
  const [huespedesConfirmados, setHuespedesConfirmados] = useState<HuespedDto[]>([]);
  const [estadiasParaCargar, setEstadiasParaCargar] = useState<any[]>([]);
  const [mostrarCancelar, setMostrarCancelar] = useState(false);

  // lo que llega del query string
  interface EstadiaFrontend {
    idHabitacion: number;
    tipo: string;
    fechaInicio: string;
    fechaFin: string;
  }

  // lo que espera el backend
  interface EstadiaSolicitudItemDto {
    habitacionId: number;
    fechaInicio: string;
    fechaFin: string;
    ocuparSobreReserva: boolean;
    reservaIdAsociada: number | null;
  }


  const searchParams = useSearchParams();
  useEffect(() => {
    const data = searchParams.get("data");
    if (data) {
      const parsed: EstadiaFrontend[] = JSON.parse(data);

      // Transformar al formato correcto
      const items: EstadiaSolicitudItemDto[] = parsed.map((e: EstadiaFrontend) => ({
        habitacionId: e.idHabitacion,   // renombrar
        fechaInicio: e.fechaInicio,     // ya está en YYYY-MM-DD
        fechaFin: e.fechaFin,
        ocuparSobreReserva: true,       // permitir ocupar sobre reservas por defecto
        reservaIdAsociada: null         // opcional, se busca automáticamente
      }));

      setEstadiasParaCargar(items);
    }
  }, [searchParams]);



  // --- HANDLERS ---
  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleBuscar = async () => {
    setLoading(true);
    setError("");
    setPagina(1);
    setSelectedHuespedes([]);

    try {
      const params = new URLSearchParams();
      if (formData.apellido.trim()) params.append("apellido", formData.apellido.trim());
      if (formData.nombre.trim()) params.append("nombre", formData.nombre.trim());
      if (formData.tipoDocumento) params.append("tipoDocumento", formData.tipoDocumento);
      if (formData.numeroDocumento.trim())
        params.append("numeroDocumento", formData.numeroDocumento.trim());

      const qs = params.toString();
      const url = qs ? `/api/huespedes?${qs}` : `/api/huespedes`;

      const res = await fetch(url, { method: "GET" });
      if (!res.ok) throw new Error(`Error HTTP ${res.status}`);

      const data: HuespedDto[] = await res.json();
      setResultados(Array.isArray(data) ? data : []);
    } catch (err) {
      setError("Error al buscar huéspedes. Intente nuevamente.");
      setResultados([]);
    } finally {
      setLoading(false);
    }
  };

  const handleCancelar = () => {
    setFormData({
      nombre: "",
      apellido: "",
      tipoDocumento: "",
      numeroDocumento: "",
    });
    setResultados([]);
    setPagina(1);
    setError("");
    setSelectedHuespedes([]);
  };

  // --- PAGINACIÓN ---
  const totalPaginas = useMemo(
    () => Math.max(1, Math.ceil(resultados.length / PAGE_SIZE)),
    [resultados.length]
  );

  const paginaActual = useMemo(
    () => Math.min(Math.max(1, pagina), totalPaginas),
    [pagina, totalPaginas]
  );

  const sliceResultados = useMemo(() => {
    const start = (paginaActual - 1) * PAGE_SIZE;
    return resultados.slice(start, start + PAGE_SIZE);
  }, [resultados, paginaActual]);

  const avanzar = () => setPagina((p) => Math.min(p + 1, totalPaginas));
  const retroceder = () => setPagina((p) => Math.max(p - 1, 1));

  // --- TOGGLE SELECCIÓN ---
  const toggleSeleccion = (h: HuespedDto) => {
  const yaSeleccionado = selectedHuespedes.some(
    (sel) =>
      sel.tipoDocumento === h.tipoDocumento &&
      sel.numeroDocumento === h.numeroDocumento
  );

  if (yaSeleccionado) {
    // quitar de la lista
    setSelectedHuespedes((prev) =>
      prev.filter(
        (sel) =>
          !(
            sel.tipoDocumento === h.tipoDocumento &&
            sel.numeroDocumento === h.numeroDocumento
          )
      )
    );
  } else {
    // agregar a la lista
    setSelectedHuespedes((prev) => [...prev, h]);
  }
};


  // --- BOTÓN SIGUIENTE ---
  const handleSiguiente = () => {
    const totalHuespedes = [...huespedesConfirmados, ...selectedHuespedes];

    if (totalHuespedes.length > 0) {
      setHuespedesConfirmados((prev) => {
        const nuevos = selectedHuespedes.filter(
          (h) =>
            !prev.some(
              (sel) =>
                sel.tipoDocumento === h.tipoDocumento &&
                sel.numeroDocumento === h.numeroDocumento
            )
        );
        return [...prev, ...nuevos];
      });
      setMostrarOpciones(true);
      setSelectedHuespedes([]); // limpiar selección
    } else {
      setError("Tiene que seleccionar al menos un huésped");
    }
  };


  return (
    <>
      {cabecera()}

      <div className="main-container">
        <h2>Asignar huéspedes a la estadía</h2>

        <div className="busqueda-layout">
          {/* --------- Columna Izquierda: Formulario --------- */}
          <div className="card-form">
            <h2>Datos del huésped</h2>

            {/* FORM para soportar Enter */}
            <form
              className="form-busqueda"
              onSubmit={(e) => {
                e.preventDefault();
                handleBuscar();
              }}
            >
              <div className="form-group">
                <label>Nombre</label>
                <input
                  name="nombre"
                  value={formData.nombre}
                  onChange={handleChange}
                  placeholder="Nombre"
                />
              </div>

              <div className="form-group">
                <label>Apellido</label>
                <input
                  name="apellido"
                  value={formData.apellido}
                  onChange={handleChange}
                  placeholder="Apellido"
                />
              </div>

              <div className="form-group">
                <label>Tipo de Documento</label>
                <select
                  name="tipoDocumento"
                  value={formData.tipoDocumento}
                  onChange={handleChange}
                >
                  <option value="">Seleccionar Tipo de Documento</option>
                  <option value="DNI">DNI</option>
                  <option value="LE">LE</option>
                  <option value="LC">LC</option>
                  <option value="PASAPORTE">Pasaporte</option>
                  <option value="OTRO">Otros</option>
                </select>
              </div>

              <div className="form-group">
                <label>Número de Documento</label>
                <input
                  name="numeroDocumento"
                  value={formData.numeroDocumento}
                  onChange={handleChange}
                  placeholder="Número de documento"
                />
              </div>

              {/* Acciones */}
              <div className="acciones-form-limitado">
                <button
                  type="submit"
                  className="btn btn-siguiente"
                  disabled={loading}
                >
                  {loading ? "Buscando..." : "Buscar"}
                </button>

                <button
                  type="button"
                  className="btn btn-cancelar"
                  onClick={() => setMostrarCancelar(true)}
                >
                  Cancelar
                </button>
              </div>

            </form>
          </div>

          {/* --------- Columna Derecha: Resultados --------- */}
          <div className="panel-resultados">
            <h2 className="titulo-panel">Resultados</h2>

            <div className="panel-interno-resultados">
              <div className="contenedor-tabla">
                <table className="tabla-resultados">
                  <thead>
                    <tr>
                      <th>Nombre</th>
                      <th>Apellido</th>
                      <th>Tipo Doc.</th>
                      <th>N° Documento</th>
                    </tr>
                  </thead>

                  <tbody>
                    {sliceResultados.length > 0 ? (
                      sliceResultados.map((h, idx) => {
                        const isSelected = selectedHuespedes.some(
                          (sel) =>
                            sel.tipoDocumento === h.tipoDocumento &&
                            sel.numeroDocumento === h.numeroDocumento
                        );
                        return (
                          <tr
                            key={`${h.tipoDocumento}-${h.numeroDocumento}-${idx}`}
                            onClick={() => toggleSeleccion(h)}
                            className={isSelected ? "fila-seleccionada" : ""}
                            style={{ cursor: "pointer" }}
                          >
                            <td>{h.nombre}</td>
                            <td>{h.apellido}</td>
                            <td>{h.tipoDocumento}</td>
                            <td>{h.numeroDocumento}</td>
                          </tr>
                        );
                      })
                    ) : (
                      <tr className="fila-vacia">
                        <td colSpan={4}>
                          {loading
                            ? "Buscando..."
                            : resultados.length === 0
                            ? "Presione Buscar para listar todos o ingrese filtros para acotar la búsqueda"
                            : "No se encontraron resultados en esta página"}
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>

              {/* Paginación */}
              <div className="paginacion">
                <button onClick={retroceder} disabled={paginaActual === 1}>
                  ◀
                </button>
                <span>
                  Página {paginaActual} de {totalPaginas}
                </span>
                <button onClick={avanzar} disabled={paginaActual === totalPaginas}>
                  ▶
                </button>
              </div>
            </div>

            <div style={{ display: "flex", justifyContent: "flex-end" }}>
              <button
                className="btn btn-siguiente"
                disabled={loading}
                onClick={handleSiguiente}
              >
                ACEPTAR
              </button>
            </div>
          </div>
        </div>
      </div>
      
      {mostrarOpciones && (
        <Opciones
          estadias={estadiasParaCargar}
          huespedes={huespedesConfirmados}
          router={router}
          setError={setError}
          onSeguir={() => {
            // acumular huéspedes seleccionados evitando duplicados
            setHuespedesConfirmados((prev) => {
              const nuevos = selectedHuespedes.filter(
                (h) =>
                  !prev.some(
                    (sel) =>
                      sel.tipoDocumento === h.tipoDocumento &&
                      sel.numeroDocumento === h.numeroDocumento
                  )
              );
              return [...prev, ...nuevos];
            });
            setMostrarOpciones(false); // volver a la búsqueda
            setSelectedHuespedes([]);  // limpiar selección actual
          }}
        />
      )}
      {error && (
        <MensajeError mensaje={error} onClose={() => setError("")} />
      )}
      {mostrarCancelar && (
              <MensajeCancelar 
                onClose={() => setMostrarCancelar(false)} 
              />
      )}
    </>
  );
}

export default function OcuparHuesped() {
  return (
    <Suspense fallback={<div>Cargando...</div>}>
      <OcuparHuespedContent />
    </Suspense>
  );
}
