import { Fragment, useCallback, useEffect, useState } from "react";
import axios   from "../http-common";
import dayjs   from "dayjs";
import weekday from "dayjs/plugin/weekday";
import "dayjs/locale/es";

dayjs.extend(weekday);
dayjs.locale("es");

const HOURS = Array.from({ length: 13 }, (_, i) => i + 10);           
const DAYS  = ["Lunes","Martes","Miércoles","Jueves","Viernes","Sábado","Domingo"];

export default function RackSemanal() {
  const [primerDia, setPrimerDia] = useState(() =>
    dayjs().weekday(0).startOf("day")
  );
  
  const [reservas, setReservas] = useState([]);

  const fetchSemana = useCallback(async (lunes) => {
    try {
      const { data } = await axios.get("/api/reservas/semana", {
        params: { primerDia: lunes.format("YYYY-MM-DD") },
      });
      setReservas(data);
    } catch (err) { console.error(err); }
  }, []);

  useEffect(() => { fetchSemana(primerDia); }, [primerDia, fetchSemana]);

  /* ------------- util para saber si la reserva pisa la celda ------------- */
  const reservaEnCelda = (dayIdx, hour) => {
    const cellStart = primerDia.add(dayIdx,"day").hour(hour).minute(0);
    const cellEnd   = cellStart.add(1,"hour");

    return reservas.find((r) => {
      const inicio = dayjs(r.fechaHora);
      const fin    = inicio.add(r.tarifa.duracionTotalMinutos, "minute");
      return fin.isAfter(cellStart) && inicio.isBefore(cellEnd);
    });
  };

  const avanzarSemana    = () => setPrimerDia(primerDia.add(7,"day"));
  const retrocederSemana = () => setPrimerDia(primerDia.subtract(7,"day"));

  const borrarReserva = async (reserva) => {
    if (!reserva) return;
    if (!window.confirm("¿Eliminar esta reserva?")) return;
    try {
      await axios.delete(`/api/reservas/${reserva.id}`);
      alert("Reserva eliminada");
      fetchSemana(primerDia);
    } catch (err) {
      console.error(err);
      alert(err.response?.data?.message || "Error al eliminar");
    }
  };

  /* -------------------- render -------------------- */
  return (
    <div style={{ padding:"2rem" }}>
      <h2>Rack semanal</h2>

      {/* Navegador de semanas */}
      <div style={{display:"flex",justifyContent:"space-between",
                   alignItems:"center",marginBottom:"1rem"}}>
        <button onClick={retrocederSemana}>← Semana anterior</button>
        <h3>
          {primerDia.format("DD/MM")} – {primerDia.add(6,"day").format("DD/MM/YYYY")}
        </h3>
        <button onClick={avanzarSemana}>Semana siguiente →</button>
      </div>

      {/* Grilla */}
      <div style={{display:"grid",
                   gridTemplateColumns:"100px repeat(7, 1fr)",
                   gap:"4px"}}>

        {/* cabecera días */}
        <div></div>
        {DAYS.map((d,i)=>(
          <div key={i} style={{fontWeight:"bold",textAlign:"center"}}>
            {d}<br/>
            <span style={{fontSize:"0.85rem"}}>
              {primerDia.add(i,"day").format("DD/MM")}
            </span>
          </div>
        ))}

        {/* filas por hora */}
        {HOURS.map(hour=>(
          <Fragment key={hour}>
            <div style={{fontWeight:"bold",textAlign:"center"}}>{hour}:00</div>

            {DAYS.map((_,dayIdx)=>{
              const reserva = reservaEnCelda(dayIdx,hour);
              const ocupado = Boolean(reserva);
              return (
                <div
                  key={`${dayIdx}-${hour}`}
                  onClick={()=>borrarReserva(reserva)}
                  style={{
                    height:"60px",
                    backgroundColor: ocupado ? "#4dabf7" : "#e9ecef",
                    cursor: ocupado ? "pointer" : "default",
                    textAlign:"center",padding:"0.25rem",fontSize:"0.75rem"
                  }}
                  onMouseEnter={e=>{
                    if (ocupado) e.currentTarget.style.backgroundColor="#f03e3e";
                  }}
                  onMouseLeave={e=>{
                    if (ocupado) e.currentTarget.style.backgroundColor="#4dabf7";
                  }}
                >
                  {ocupado && (
                    <>
                      <div>{reserva.clientePrincipal.nombre}</div>
                      <div style={{fontSize:"0.65rem"}}>
                        {dayjs(reserva.fechaHora).format("HH:mm")}–
                        {dayjs(reserva.fechaHora)
                          .add(reserva.tarifa.duracionTotalMinutos,"minute")
                          .format("HH:mm")}
                      </div>
                    </>
                  )}
                </div>
              );
            })}
          </Fragment>
        ))}
      </div>
    </div>
  );
}
