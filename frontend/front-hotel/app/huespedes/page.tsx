
"use client";

import React, { useMemo, useState } from "react";
import "../globals.css";
import { cabecera } from "../components/Cabecera"; // Ajustá la ruta si es distinta
import { useRouter } from "next/navigation";
import { MensajeCancelar } from "../components/MensajeCancelar";

type TipoDocumento = "DNI" | "LE" | "LC" | "PASAPORTE" | "OTRO";

interface HuespedDto {
  apellido: string;
  nombre: string;
  tipoDocumento: TipoDocumento;
  numeroDocumento: string;
}

const PAGE_SIZE = 5;

export default function BusquedaHuesped() {
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
  const [selectedHuesped, setSelectedHuesped] = useState<HuespedDto | null>(null);
  const [mostrarMensajeCancelar, setMostrarMensajeCancelar] = useState(false);
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
    setSelectedHuesped(null);

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
/*
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
    setSelectedHuesped(null);
  };*/

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
    const yaSeleccionado =
      selectedHuesped &&
      selectedHuesped.tipoDocumento === h.tipoDocumento &&
      selectedHuesped.numeroDocumento === h.numeroDocumento;

    setSelectedHuesped(yaSeleccionado ? null : h);
  };

  // --- BOTÓN SIGUIENTE ---
  const handleSiguiente = () => {
    if (selectedHuesped) {
      router.push(
        `/modificar-huesped?tipoDocumento=${selectedHuesped.tipoDocumento}&numeroDocumento=${selectedHuesped.numeroDocumento}`
      );
    } else {
      router.push("/cargar-huesped");
    }
  };

  return (
    <>
      {cabecera()}

      <div className="main-container">
        <h2>Buscar Huesped</h2>

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
                  onClick={() => setMostrarMensajeCancelar(true)}
                >
                  Cancelar
                </button>
              </div>

              {error && (
                <p style={{ color: "#ff8e8e", marginTop: 8, textAlign: "center" }}>
                  {error}
                </p>
              )}
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
                        const isSelected =
                          selectedHuesped &&
                          selectedHuesped.tipoDocumento === h.tipoDocumento &&
                          selectedHuesped.numeroDocumento === h.numeroDocumento;

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

            {/* Botón dinámico */}
            <div style={{ display: "flex", justifyContent: "flex-end" }}>
              <button
                className="btn btn-siguiente"
                disabled={loading}
                onClick={handleSiguiente}
              >
                {selectedHuesped ? "SIGUIENTE" : "DAR DE ALTA"}
              </button>
            </div>
          </div>
        </div>
      </div>
      {mostrarMensajeCancelar && (
        <MensajeCancelar 
          onClose={() => setMostrarMensajeCancelar(false)} 
        />
      )}
    </>
  );
}