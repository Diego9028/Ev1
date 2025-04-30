// src/http-common.js
import axios from 'axios';

// Variables definidas en tu .env --> recuerda reiniciar Vite tras cambiar .env
const backendHost = import.meta.env.VITE_KARTING_BACKEND_HOST;   // p. ej. 127.0.0.1
const backendPort = import.meta.env.VITE_KARTING_BACKEND_PORT;   // p. ej. 8080

// Si necesitas verlas durante desarrollo, descomenta:
// console.log(`Backend: http://${backendHost}:${backendPort}`);

// Instancia Axios que usarán todos los services (Reserva, Tarifa, Reporte, …)
export default axios.create({
  baseURL: `http://${backendHost}:${backendPort}`,  
  headers: {
    'Content-Type': 'application/json',
  },
});
