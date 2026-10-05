![Duoc UC](https://www.duoc.cl/wp-content/uploads/2022/09/logo-0.png)

# Actividad Formativa – Semana 8
## Persistiendo datos con objetos y base de datos

### Proyecto: SpeedFast

---

## Autor del proyecto

| Campo | Detalle |
|---|---|
| **Nombre completo** | Beatriz López Casanova |
| **Asignatura** | Desarrollo Orientado a Objetos II |
| **Carrera** | Analista Programador Computacional |
| **Sede** | Virtual |

---

## Descripción

SpeedFast es una empresa de reparto a domicilio. En esta semana se persisten en MySQL los mismos datos que antes se manejaban en memoria: **repartidores**, **pedidos** y **entregas**.

Desde el menú se puede:

1. Gestionar repartidores (crear, listar, editar y eliminar).
2. Gestionar pedidos (crear, listar con filtros, editar y eliminar).
3. Gestionar entregas (asignar un pedido a un repartidor, editar y eliminar).
4. Simular el viaje de las entregas que ya están asignadas.

La ventana no escribe SQL. Llama al controlador y el controlador llama al DAO.

El detalle de cada paso de la guía está en `semana8/README.md`.

---

## Cómo ejecutar

1. Tener MySQL encendido y ejecutar `semana8/script.sql`.
2. En IntelliJ: clic derecho en `semana8/pom.xml` → **Add as Maven Project**.
3. Copiar `semana8/conexion.properties.ejemplo` a `semana8/conexion.properties` y completar la clave.
4. Ejecutar `semana8/src/main/java/app/Main.java`.

La clave de MySQL **no se sube a GitHub**. Al docente se le indica en el informe o en el mensaje de la plataforma.

---

**Repositorio GitHub:** https://github.com/Be-ri-lo/SpeedFast-Poliformismo

**Fecha de entrega:** Semana 8 – Octubre 2026

© Duoc UC | Escuela de Informática y Telecomunicaciones
