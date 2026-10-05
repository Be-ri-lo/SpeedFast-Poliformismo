-- SpeedFast semana 7: base y tablas
-- Ejecutar en MySQL Workbench conectado como root.

CREATE DATABASE IF NOT EXISTS speedfast_db;
USE speedfast_db;

CREATE TABLE IF NOT EXISTS repartidor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    direccion VARCHAR(150) NOT NULL,
    tipo VARCHAR(30) NOT NULL,      -- COMIDA | ENCOMIENDA | EXPRESS
    estado VARCHAR(20) NOT NULL,    -- PENDIENTE | EN_REPARTO | ENTREGADO
    distancia_km DECIMAL(6,2) NOT NULL,
    peso DECIMAL(8,2) NULL,         -- solo encomienda
    fragil TINYINT(1) NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS entrega (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_repartidor INT NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id),
    FOREIGN KEY (id_repartidor) REFERENCES repartidor(id)
);

-- Relaciones:
-- 1 repartidor → muchas entregas
-- 1 pedido → una o varias entregas (si se asigna más de una vez)

-- Si la tabla pedido ya existía sin estas columnas, ejecutar una vez:
-- ALTER TABLE pedido ADD COLUMN distancia_km DECIMAL(6,2) NOT NULL DEFAULT 1.00;
-- ALTER TABLE pedido ADD COLUMN peso DECIMAL(8,2) NULL;
-- ALTER TABLE pedido ADD COLUMN fragil TINYINT(1) NOT NULL DEFAULT 0;
