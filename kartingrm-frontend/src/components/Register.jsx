import { useState } from "react";
import axios from "../http-common";
import { useNavigate } from "react-router-dom";
import {
  Box, TextField, Button, Typography
} from "@mui/material";
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import dayjs from "dayjs";

export default function Register() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    rut: "",
    nombre: "",
    email: "",
    telefono: "",
    fechaNacimiento: dayjs(),
  });

  const [error, setError] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const submitData = {
        ...formData,
        fechaNacimiento: formData.fechaNacimiento.format("YYYY-MM-DD")
      };
      await axios.post("/api/clientes", submitData);
      alert("¡Registro exitoso!");
      navigate("/");
    } catch (err) {
      setError("Hubo un error al registrar el cliente. Por favor, intenta de nuevo.");
      console.error(err);
    }
  };

  return (
    <Box component="form" onSubmit={handleSubmit} sx={{ p: 3, maxWidth: 480, mx: "auto" }}>
      <Typography variant="h5" sx={{ mb: 2, textAlign: "center" }}>Registro de Cliente</Typography>

      <TextField
        label="RUT"
        name="rut"
        required
        fullWidth
        value={formData.rut}
        onChange={handleChange}
        sx={{ mb: 2 }}
      />

      <TextField
        label="Nombre"
        name="nombre"
        required
        fullWidth
        value={formData.nombre}
        onChange={handleChange}
        sx={{ mb: 2 }}
      />

      <TextField
        label="Correo Electrónico"
        name="email"
        type="email"
        required
        fullWidth
        value={formData.email}
        onChange={handleChange}
        sx={{ mb: 2 }}
      />

      <TextField
        label="Teléfono"
        name="telefono"
        required
        fullWidth
        value={formData.telefono}
        onChange={handleChange}
        sx={{ mb: 2 }}
      />

      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <DatePicker
          label="Fecha de nacimiento"
          value={formData.fechaNacimiento}
          onChange={(date) => setFormData(prev => ({ ...prev, fechaNacimiento: date }))}
          slotProps={{ textField: { fullWidth: true, required: true, sx: { mb: 3 } } }}
        />
      </LocalizationProvider>

      {error && <Typography color="error" variant="body2" sx={{ mb: 2 }}>{error}</Typography>}

      <Button variant="contained" type="submit" fullWidth>
        Registrarse
      </Button>
    </Box>
  );
}
