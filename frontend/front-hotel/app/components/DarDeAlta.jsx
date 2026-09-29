export function FormularioAltaHuesped() {
  return (
    <div className="main-container">
      <div className="fondo-form-dar-alta">
        <h2>Datos del Huesped</h2>

        <form className="formulario-dar-alta-12columnas">
          {/* Nombre y Apellido */}
          <div className="columna-dar-alta medio-ancho">
            <label>Nombre *</label>
            <input type="text" placeholder="Ingrese nombre" required />
          </div>
          <div className="columna-dar-alta medio-ancho">
            <label>Apellido *</label>
            <input type="text" placeholder="Ingrese apellido" required />
          </div>

          {/* Tipo y Número de Documento */}
          <div className="columna-dar-alta medio-ancho">
            <label>Tipo de Documento *</label>
            <select required defaultValue="">
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
          <div className="columna-dar-alta medio-ancho">
            <label>Número de Documento *</label>
            <input type="text" placeholder="Ingrese número" required />
          </div>

          {/* CUIT y Teléfono */}
          <div className="columna-dar-alta medio-ancho">
            <label>CUIT</label>
            <input type="text" placeholder="Ingrese CUIT" />
          </div>
          <div className="columna-dar-alta medio-ancho">
            <label>Teléfono</label>
            <input type="text" placeholder="Ingrese teléfono" />
          </div>

          {/* Fecha de Nacimiento */}
          <div className="columna-dar-alta medio-ancho">
            <label>Fecha de Nacimiento *</label>
            <div className="input-with-icon">
              <input type="date" required />
              <span className="calendar-icon">📅</span>
            </div>
          </div>

          {/* Posición frente al IVA y Ocupación */}
          <div className="columna-dar-alta medio-ancho">
            <label>Posición Frente al IVA *</label>
            <select required defaultValue="">
              <option value="" disabled hidden>
                Consumidor Final
              </option>
                <option value="Consumidor Final">Consumidor Final</option>
              <option value="Responsable Inscripto">Responsable Inscripto</option>
              <option value="Exento">Exento</option>
            </select>
          </div>
          <div className="columna-dar-alta medio-ancho">
            <label>Ocupación *</label>
            <input type="text" placeholder="Ingrese ocupación" required />
          </div>

          {/* Email y Nacionalidad */}
          <div className="columna-dar-alta medio-ancho">
            <label>Email</label>
            <input type="email" placeholder="Ingrese email" />
          </div>
          <div className="columna-dar-alta medio-ancho">
            <label>Nacionalidad *</label>
            <input type="nacionalidad" placeholder="Ingrese nacionalidad" required />
          </div>

          {/* Domicilio */}
          <div className="columna-dar-alta completo-ancho">
            <label className="domicilio-label">Domicilio</label>
          </div>
        <div>
            
        </div>
          {/* Calle y Número */}
          <div className="columna-dar-alta completo-ancho street-number-group">
            <div className="inline-input">
              <label>Calle *</label>
              <input type="text" placeholder="Ingrese calle" required />
            </div>
            <div className="inline-input number-input">
              <label>Número *</label>
              <input type="text" placeholder="Ingrese número" required />
            </div>
          </div>

          {/* Piso y Departamento */}
          <div className="columna-dar-alta completo-ancho dept-floor-group">
            <div className="inline-input floor-input">
              <label>Piso</label>
              <input type="text" placeholder="Ingrese piso" />
            </div>
            <div className="inline-input">
              <label>Departamento</label>
              <input type="text" placeholder="Ingrese departamento" />
            </div>
          </div>

          {/* Código Postal y Localidad */}
          <div className="columna-dar-alta completo-ancho postcode-location-group">
            <div className="inline-input">
              <label>Código Postal *</label>
              <input type="text" placeholder="Ingrese código postal" required />
            </div>
            <div className="inline-input location-select">
              <label>Localidad *</label>
              <input type="text" placeholder="Ingrese localidad" required />
            </div>
          </div>

          {/* Provincia y País */}
          <div className="columna-dar-alta completo-ancho province-country-group">
            <div className="inline-input">
              <label>Provincia *</label>
                <input type="provincia" placeholder="Ingrese provincia" required />
            </div>
            <div className="inline-input country-select">
              <label>País *</label>
                <input type="pais" placeholder="Ingrese país" required />
            </div>
          </div>
        </form>

        {/* Acciones */}
        <div className="form-actions">
          <span className="required-note">(*) Campos obligatorios</span>
          <div className="buttons-container">
            <button className="btn btn-cancelar">Cancelar</button>
            <button className="btn btn-siguiente">Siguiente</button>
          </div>
        </div>
      </div>
    </div>
  );
}