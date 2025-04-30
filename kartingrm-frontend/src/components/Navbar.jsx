import { useState } from "react";
import { Link } from "react-router-dom";

import AppBar      from "@mui/material/AppBar";
import Box         from "@mui/material/Box";
import Toolbar     from "@mui/material/Toolbar";
import Typography  from "@mui/material/Typography";
import IconButton  from "@mui/material/IconButton";
import MenuIcon    from "@mui/icons-material/Menu";

import Sidemenu    from "./Sidemenu";       

export default function Navbar() {
  const [open, setOpen] = useState(false);
  const toggleDrawer = (open) => (event) => setOpen(open);

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static" sx={{ bgcolor: 'primary.main' }}>
        <Toolbar>

          {/* Botón hamburguesa */}
          <IconButton
            size="large"
            edge="start"
            color="inherit"
            aria-label="menu"
            sx={{ mr: 2 }}
            onClick={toggleDrawer(true)}
          >
            <MenuIcon />
          </IconButton>

          {/* Título de la app */}
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            KartingRM – Gestión de Reservas
          </Typography>

          {/* Si luego añades auth, aquí iría el botón Login/Logout */}
        </Toolbar>
      </AppBar>

      {/* Side-drawer con los links */}
      <Sidemenu open={open} toggleDrawer={toggleDrawer} />
    </Box>
  );
}
