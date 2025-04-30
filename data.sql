CREATE TABLE IF NOT EXISTS kart (
    id bigint NOT NULL,
    codigo character varying(255),
    estado character varying(255),
    modelo character varying(255)
);

CREATE TABLE IF NOT EXISTS tarifa (
    id bigint NOT NULL,
    duracion_total_minutos integer NOT NULL,
    numero_vueltas integer NOT NULL,
    precio integer NOT NULL,
    tiempo_maximo_minutos integer NOT NULL
);

CREATE TABLE IF NOT EXISTS politica_descuento (
    id bigint NOT NULL,
    max_beneficiados integer NOT NULL,
    max_valor integer NOT NULL,
    min_valor integer NOT NULL,
    porcentaje integer NOT NULL,
    tipo character varying(255)
);


-- Inserta los 15 karts iniciales en estado DISPONIBLE
INSERT INTO kart (codigo, estado) VALUES ('K001', 'DISPONIBLE');
INSERT INTO kart (codigo, estado) VALUES ('K002', 'DISPONIBLE');
INSERT INTO kart (codigo, estado) VALUES ('K003', 'DISPONIBLE');
INSERT INTO kart (codigo, estado) VALUES ('K004', 'DISPONIBLE');
INSERT INTO kart (codigo, estado) VALUES ('K005', 'DISPONIBLE');
INSERT INTO kart (codigo, estado) VALUES ('K006', 'DISPONIBLE');
INSERT INTO kart (codigo, estado) VALUES ('K007', 'DISPONIBLE');
INSERT INTO kart (codigo, estado) VALUES ('K008', 'DISPONIBLE');
INSERT INTO kart (codigo, estado) VALUES ('K009', 'DISPONIBLE');
INSERT INTO kart (codigo, estado) VALUES ('K010', 'DISPONIBLE');
INSERT INTO kart (codigo, estado) VALUES ('K011', 'DISPONIBLE');
INSERT INTO kart (codigo, estado) VALUES ('K012', 'DISPONIBLE');
INSERT INTO kart (codigo, estado) VALUES ('K013', 'DISPONIBLE');
INSERT INTO kart (codigo, estado) VALUES ('K014', 'DISPONIBLE');
INSERT INTO kart (codigo, estado) VALUES ('K015', 'DISPONIBLE');

-- Tarifas

INSERT INTO tarifa (id, duracion_total_minutos, numero_vueltas, precio, tiempo_maximo_minutos) VALUES
(1, 30, 10, 15000, 10),
(2, 35, 15, 20000, 15),
(3, 40, 20, 25000, 20);

-- Politicas

INSERT INTO politica_descuento (id, max_beneficiados, max_valor, min_valor, porcentaje, tipo) VALUES
(1, 0, 2, 1, 0,  'GRUPO'),
(2, 0, 5, 3, 10, 'GRUPO'),
(3, 0, 10, 6, 20, 'GRUPO'),
(4, 0, 15, 11, 30, 'GRUPO');




