// src/services/reserva.service.js
import api from '../http-common';

// C  R  U  D  helpers
const create     = data => api.post('/api/reservas', data);          // C
const getAll     = ()   => api.get ('/api/reservas');                // R (lista)
const getById    = id  => api.get (`/api/reservas/${id}`);           // R (uno)
const getWeek    = iso => api.get ('/api/reservas/semana', { params:{ inicio: iso }});
const update     = (id, data) => api.patch(`/api/reservas/${id}`, data); // U
const remove     = id  => api.delete(`/api/reservas/${id}`);         // D

export default { create, getAll, getById, getWeek, update, remove };
