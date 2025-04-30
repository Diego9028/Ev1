// Reportes.jsx – componente para mostrar ingresos por vueltas o por número de personas
// Basado en el estilo y "look & feel" sencillo de RackSemanal.jsx.

import { useState } from "react";
import axios from "../http-common";
import dayjs from "dayjs";
import isSameOrBefore from "dayjs/plugin/isSameOrBefore";
import "dayjs/locale/es";
dayjs.extend(isSameOrBefore);

dayjs.locale("es");

export default function Reportes() {
  // Estados para filtros
  const [inicioMes, setInicioMes] = useState(dayjs().startOf("month").format("YYYY-MM"));
  const [finMes, setFinMes] = useState(dayjs().format("YYYY-MM"));
  const [tipo, setTipo] = useState("vueltas"); // "vueltas" | "personas"

  // Datos estructurados: { criterio: { ["MMM YYYY"]: valor, total: valor } }
  const [datos, setDatos] = useState({});
  const [meses, setMeses] = useState([]); // Array de meses (label)
  const [cargando, setCargando] = useState(false);

  /* Utilidades */
  const generarListaMeses = (from, to) => {
    const lista = [];
    let cursor = from.clone();
    while (cursor.isSameOrBefore(to, "month")) {
      lista.push(cursor.clone());
      cursor = cursor.add(1, "month");
    }
    return lista;
  };

  const fetchDatos = async () => {
    setCargando(true);
    const ini = dayjs(inicioMes + "-01");
    const fin = dayjs(finMes + "-01");

    const listaMeses = generarListaMeses(ini, fin);
    setMeses(listaMeses.map(m => m.format("MMM YYYY")));

    const endpointBase = tipo === "vueltas" ? "/api/reportes/ingresos-vueltas" : "/api/reportes/ingresos-personas";

    // Traemos datos mes a mes y los organizamos
    const resultadosTemp = {};

    for (const mes of listaMeses) {
      const inicioISO = mes.startOf("month").format("YYYY-MM-DD") + "T00:00:00";
      const finISO = mes.endOf("month").format("YYYY-MM-DD") + "T23:59:59";

      try {
        const { data } = await axios.get(endpointBase, { params: { inicio: inicioISO, fin: finISO } });
        data.forEach(r => {
          if (!resultadosTemp[r.criterio]) {
            resultadosTemp[r.criterio] = { total: 0 };
          }
          resultadosTemp[r.criterio][mes.format("MMM YYYY")] = r.totalIngresos;
          resultadosTemp[r.criterio].total += r.totalIngresos;
        });
      } catch (err) {
        console.error(err);
      }
    }
    setDatos(resultadosTemp);
    setCargando(false);
  };

  /* Render */
  return (
    <div style={{ padding: "2rem" }}>
      <h2>Reportes de ingresos</h2>

      {/* Filtros */}
      <div style={{ display: "flex", gap: "1rem", marginBottom: "1rem" }}>
        <div>
          <label>Inicio&nbsp;</label>
          <input type="month" value={inicioMes} onChange={e => setInicioMes(e.target.value)} />
        </div>
        <div>
          <label>Fin&nbsp;</label>
          <input type="month" value={finMes} onChange={e => setFinMes(e.target.value)} />
        </div>
        <div>
          <label>Tipo&nbsp;</label>
          <select value={tipo} onChange={e => setTipo(e.target.value)}>
            <option value="vueltas">Ingresos por vueltas</option>
            <option value="personas">Ingresos por número de personas</option>
          </select>
        </div>
        <button onClick={fetchDatos} disabled={cargando}>
          {cargando ? "Cargando…" : "Generar"}
        </button>
      </div>

      {/* Tabla de resultados */}
      {Object.keys(datos).length > 0 && (
        <div style={{ overflowX: "auto" }}>
          <table style={{ borderCollapse: "collapse", width: "100%" }}>
            <thead>
              <tr style={{ background: "#dee2e6" }}>
                <th style={{ padding: "0.5rem", border: "1px solid #ccc" }}>{tipo === "vueltas" ? "Número de vueltas o tiempo máximo permitido" : "Número de personas"}</th>
                {meses.map(m => (
                  <th key={m} style={{ padding: "0.5rem", border: "1px solid #ccc" }}>{m}</th>
                ))}
                <th style={{ padding: "0.5rem", border: "1px solid #ccc" }}>TOTAL</th>
              </tr>
            </thead>
            <tbody>
              {Object.entries(datos).map(([criterio, valores]) => (
                <tr key={criterio}>
                  <td style={{ padding: "0.5rem", border: "1px solid #ccc", fontWeight: "bold" }}>{criterio}</td>
                  {meses.map(m => (
                    <td key={m} style={{ padding: "0.5rem", border: "1px solid #ccc", textAlign: "right" }}>
                      {valores[m]?.toLocaleString("es-CL") || "-"}
                    </td>
                  ))}
                  <td style={{ padding: "0.5rem", border: "1px solid #ccc", fontWeight: "bold", textAlign: "right" }}>
                    {valores.total.toLocaleString("es-CL")}
                  </td>
                </tr>
              ))}
              {/* Fila total general */}
              <tr style={{ background: "#dee2e6", fontWeight: "bold" }}>
                <td style={{ padding: "0.5rem", border: "1px solid #ccc" }}>TOTAL</td>
                {meses.map(m => {
                  const sumaMes = Object.values(datos).reduce((acc, v) => acc + (v[m] || 0), 0);
                  return (
                    <td key={m} style={{ padding: "0.5rem", border: "1px solid #ccc", textAlign: "right" }}>
                      {sumaMes.toLocaleString("es-CL")}
                    </td>
                  );
                })}
                <td style={{ padding: "0.5rem", border: "1px solid #ccc", textAlign: "right" }}>
                  {Object.values(datos).reduce((acc, v) => acc + v.total, 0).toLocaleString("es-CL")}
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
