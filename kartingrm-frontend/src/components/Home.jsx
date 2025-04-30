const Home = () => {
  return (
    <div style={{ padding: "1rem" }}>
      <h1>KartingRM – Sistema de Gestión de Reservas de Karting</h1>

      <p>
        KartingRM es una aplicación web que permite a los clientes reservar
        karts, visualizar la ocupación semanal de la pista y generar reportes de
        ingresos. El sistema automatiza las reglas de negocio de tarifas,
        descuentos y disponibilidad de los 15 karts del circuito.
      </p>

      <p>
        El proyecto está construido con{" "}
        <a href="https://spring.io/projects/spring-boot" target="_blank" rel="noreferrer">
          Spring Boot
        </a>{" "}
        para el backend,{" "}
        <a href="https://react.dev/" target="_blank" rel="noreferrer">
          React&nbsp;(Material-UI)
        </a>{" "}
        para el frontend y{" "}
        <a href="https://www.postgresql.org/" target="_blank" rel="noreferrer">
          PostgreSQL
        </a>{" "}
        como base de datos. Todo se despliega en contenedores Docker orquestados
        con Docker Compose.
      </p>

      <p>
        Usa el menú para crear una <strong>Nueva reserva</strong>, revisar el{" "}
        <strong>Rack semanal</strong> o consultar los <strong>Reportes</strong>{" "}
        de ingresos.
      </p>
    </div>
  );
};

export default Home;
