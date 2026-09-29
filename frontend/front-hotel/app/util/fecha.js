
export const toHuman = (iso) => {
  const [y, m, d] = iso.split("-");
  return `${d}/${m}/${y}`;
};

export const rangeISO = (inicioISO, finISO) => {
  const res = [];
  const start = new Date(inicioISO);
  const end = new Date(finISO);
  for (let d = new Date(start); d <= end; d.setDate(d.getDate() + 1)) {
    res.push(d.toISOString().slice(0, 10)); // YYYY-MM-DD
  }
  return res;
};