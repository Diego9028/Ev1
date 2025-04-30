import api from '../http-common';
export const getAll = () => api.get('/api/tarifas');      
export default { getAll };