"use client";

export function cabecera(){
    return (
        <div>
            <header className="header">
            <img className="logo" src="./logo_hotel.png" alt="Logo del Hotel Premier" />
            <nav className="navegacion-cabecera">
                <a href="/home">Home</a>
                <a href="/huespedes">Huéspedes</a>
                <a href="/reservar-fechas">Reservar</a>
            </nav>
            <div className="user-menu">
                <div className="avatar-usuario">U</div>
                <div className="icono-menu">☰</div>
            </div>
        </header>
    </div>
    );
}


