import api from '../http-common';
export const ingresosVueltas  = () => api.get('/api/reportes/vueltas');
export const ingresosPersonas = () => api.get('/api/reportes/personas');
export default { ingresosVueltas, ingresosPersonas };