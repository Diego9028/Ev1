import { useState, useEffect } from "react";
import dayjs from "dayjs";
import {
  Box, TextField, Button, MenuItem, Typography
} from "@mui/material";
import { DateTimePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";

import tarifaService from "../services/tarifa.service";
import reservaService from "../services/reserva.service";
import axios from "../http-common";

export default function ReservasNueva() {
  const [tarifas, setTarifas] = useState([]);
  const [reservaId, setReservaId] = useState(null);
  const [form, setForm] = useState({
    email: "",
    tarifaId: "",
    fechaHora: dayjs(),
    cantidadPersonas: 1
  });

  useEffect(() => {
    tarifaService.getAll().then(r => setTarifas(r.data));
  }, []);

  const handleChange = e =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async e => {
    e.preventDefault();

    try {

      const clienteRes = await axios.get("/api/clientes/email", {
        params: { email: form.email }
      });

      const clienteId = clienteRes.data.id;

      const reservaPayload = {
        clientePrincipal: { id: clienteId },
        clientes: [{ id: clienteId }],
        tarifa: { id: form.tarifaId },
        cantidadPersonas: form.cantidadPersonas,
        fechaHora: form.fechaHora.format('YYYY-MM-DDTHH:mm:ss')
      };

      const r = await reservaService.create(reservaPayload);
      setReservaId(r.data.id);
      alert("Reserva creada exitosamente. El comprobante fue enviado a tu correo electrónico.");
    } catch (err) {
      console.error(err);
      alert(err.response?.data?.message || "Error al crear reserva");
    }
  };

  return (
    <Box component="form" onSubmit={handleSubmit} sx={{ p: 3, maxWidth: 480 }}>
      <Typography variant="h5" sx={{ mb: 2 }}>Nueva reserva</Typography>

      <TextField
        label="Correo del cliente"
        name="email"
        type="email"
        required
        fullWidth
        value={form.email}
        onChange={handleChange}
        sx={{ mb: 2 }}
      />

      <TextField
        select fullWidth required name="tarifaId" label="Tarifa"
        value={form.tarifaId}
        onChange={handleChange}
        sx={{ mb: 2 }}
      >
        {tarifas.map(t => (
          <MenuItem key={t.id} value={t.id}>
            {t.numeroVueltas} vueltas – ${t.precio}
          </MenuItem>
        ))}
      </TextField>

      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <DateTimePicker
          label="Fecha y hora"
          value={form.fechaHora}
          onChange={fecha => setForm({ ...form, fechaHora: fecha })}
          sx={{ mb: 2, width: "100%" }}
        />
      </LocalizationProvider>

      <TextField
        type="number" name="cantidadPersonas" label="Personas"
        inputProps={{ min: 1, max: 15 }}
        value={form.cantidadPersonas}
        onChange={handleChange}
        fullWidth required sx={{ mb: 3 }}
      />

      <Button variant="contained" type="submit" fullWidth>
        Reservar
      </Button>
</Box>
  );
}
