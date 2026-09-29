"use client";

import { Suspense } from "react";
import { cabecera } from "../components/Cabecera";
import { useSearchParams } from "next/navigation";


function GenerarFacturaContent() {
    const searchParams = useSearchParams();
    const dataParam = searchParams.get("data");

    if (!dataParam) {
        return (
        <>
            {cabecera()}
            <div className="main-container">
            <p>No se recibió información de la factura.</p>
            </div>
        </>
        );
    }

    let factura;
    try {
        factura = JSON.parse(dataParam);
    } catch (e) {
        return (
        <>
            {cabecera()}
            <div className="main-container">
            <p>Error al leer los datos de la factura.</p>
            </div>
        </>
        );
    }


    const productos = [
        ...(factura.valorEstadia ? [{
            nombre: "Estadía",
            precio: factura.valorEstadia,
            cantidad: 1
        }] : []),

        ...factura.consumosIndividuales.map(c => ({
            nombre: c.descripcion,
            precio: c.precio,
            cantidad: 1
        }))
    ];

    const tipoFactura = factura.tipo;
    const nombreCliente = factura.responsablePagoNombre;
    const cuil = factura.responsablePagoCuit;
    const total = factura.totalFacturado;

    return (
        <>
            {cabecera()}
            <div className="main-container">
                <div className="main-container-factura">
                    <h1>Factura</h1>
                    <div className="fondo-factura">
                        <div className="factura">

                            <div className="factura-encabezado">
                                <div className="factura-logo">
                                    <img src="/logo_hotel.png" alt="Logo Hotel Premier" className="factura-logo-img" />
                                </div>

                                <div className="factura-info" style={{ fontStyle: "italic" }}>
                                    <p style={{ fontSize: "30px", fontWeight: "bold" }}>HOTEL PREMIER</p>
                                    <p style={{ fontSize: "18px" }}>Sucursal Santa Fe, Argentina</p>
                                    <p style={{ fontSize: "18px" }}>Marcial Candioti 4673</p>
                                    <p style={{ fontSize: "18px" }}>Comuníquese con nosotros:</p>

                                    <p style={{ fontSize: "18px" }}>
                                        <img src="/email.png" alt="Mail" style={{ width: "18px", verticalAlign: "middle", marginRight: "6px" }} />
                                        PremierSantaFe@gmail.com
                                    </p>

                                    <p style={{ fontSize: "18px" }}>
                                        <img src="/telefono.png" alt="Teléfono" style={{ width: "18px", verticalAlign: "middle", marginRight: "6px" }} />
                                        +54 0342 354-7944
                                    </p>
                                </div>
                            </div>

                            <p style={{marginTop:"20px", marginLeft:"27px", fontSize:"22px", fontWeight:"bold"}}>Objetos facturados:</p>
                            <div className="factura-costos-totales">
                                <table style={{ width: "100%", borderCollapse: "collapse", marginBottom: "20px" }}>
                                    <thead>
                                        <tr style={{ borderBottom: "1px solid #000" }}>
                                            <th style={{ textAlign: "left", padding: "6px" }}>Producto</th>
                                            <th style={{ textAlign: "left", padding: "6px" }}>Costo</th>
                                            <th style={{ textAlign: "left", padding: "6px" }}>Cantidad</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        {productos.map((p, idx) => (
                                            <tr key={idx}>
                                                <td style={{ padding: "6px" }}>{p.nombre}</td>
                                                <td style={{ padding: "6px" }}>${p.precio.toLocaleString("es-AR")}</td>
                                                <td style={{ padding: "6px" }}>x{p.cantidad}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>

                                <div style={{ textAlign: "left", fontWeight: "bold", fontSize: "20px", marginBottom: "20px",marginLeft:"5px" }}>
                                    Total a pagar: ${total.toLocaleString("es-AR")}
                                </div>
                            </div>


                            <p style={{marginTop:"20px", marginLeft:"27px", fontSize:"22px", fontWeight:"bold"}}>Datos de pago:</p>
                            <div className="factura-datos-pago">
                                <div className="factura-datos-texto">
                                    <p>Factura tipo {tipoFactura}</p>
                                    <p>A nombre de: {nombreCliente}</p>
                                    <p>CUIL: {cuil}</p>
                                </div>
                                <div className="factura-qr-box">
                                <p style={{ fontWeight: "bold" }}>ARCA</p>
                                <img src="/QR.PNG" alt="QR ARCA" style={{ width: "100px", height: "100px" }} />
                                </div>
                            </div>

                        </div>

                        <div className="factura-acciones">
                            <div className="factura-boton" onClick={() => alert("Función de impresión aún no implementada")}>
                                <img src="/imprimir.png" alt="Imprimir" className="factura-boton-img" />
                            </div>

                            <div className="factura-boton" onClick={() => alert("Función de compartido aún no implementada")}>
                                <img src="/compartir.png" alt="Compartir" className="factura-boton-img" />
                            </div>

                            <div className="factura-boton" onClick={() => alert("Función de descarga aún no implementada")}>
                                <img src="/descargar.png" alt="Descargar" className="factura-boton-img" />
                            </div>
                        </div>
                        
                    </div>
                </div>
            </div>
        </>
    );
}

export default function GenerarFactura() {
  return (
    <Suspense fallback={<div>Cargando...</div>}>
      <GenerarFacturaContent />
    </Suspense>
  );
}