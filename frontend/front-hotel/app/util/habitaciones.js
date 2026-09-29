// util/habitaciones.js
export const gruposPorRango = [
  { nombre: "INDIVIDUAL ESTÁNDAR", inicio: 1, fin: 10 },
  { nombre: "DOBLE ESTÁNDAR", inicio: 11, fin: 28 },
  { nombre: "DOBLE SUPERIOR", inicio: 29, fin: 36 },
  { nombre: "SUPERIOR FAMILY PLAN", inicio: 37, fin: 46 },
  { nombre: "SUITE DOBLE", inicio: 47, fin: 48 }
];

export const clasificarHabitaciones = (ids) => {
  const grupos = gruposPorRango.map(g => ({ ...g, lista: [] }));
  for (const id of ids) {
    const g = grupos.find(gr => id >= gr.inicio && id <= gr.fin);
    if (g) g.lista.push(id);
  }
  return grupos;
};