-- SpeedFast semana 8
-- Tablas de la pauta: repartidores, pedidos, entregas.
-- Extra (del modelo SpeedFast, no vienen en el CREATE oficial):
--   distancia_km, peso, fragil
-- Ejecutar en MySQL Workbench como root.

CREATE DATABASE IF NOT EXISTS speedfast_db;
USE speedfast_db;

CREATE TABLE IF NOT EXISTS repartidores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    direccion VARCHAR(100) NOT NULL,
    tipo ENUM('COMIDA','ENCOMIENDA','EXPRESS'),
    estado ENUM('PENDIENTE','EN_REPARTO','ENTREGADO'),
    distancia_km DECIMAL(6,2) NOT NULL DEFAULT 1.00,
    peso DECIMAL(8,2) NULL,
    fragil TINYINT(1) NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS entregas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT,
    id_repartidor INT,
    fecha DATE,
    hora TIME,
    FOREIGN KEY (id_pedido) REFERENCES pedidos(id),
    FOREIGN KEY (id_repartidor) REFERENCES repartidores(id)
);

-- Si pedidos ya existía con el script oficial (sin extras), ejecutar una vez:
-- ALTER TABLE pedidos ADD COLUMN distancia_km DECIMAL(6,2) NOT NULL DEFAULT 1.00;
-- ALTER TABLE pedidos ADD COLUMN peso DECIMAL(8,2) NULL;
-- ALTER TABLE pedidos ADD COLUMN fragil TINYINT(1) NOT NULL DEFAULT 0;
