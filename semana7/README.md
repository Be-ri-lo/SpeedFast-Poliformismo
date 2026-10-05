![Duoc UC](https://www.duoc.cl/wp-content/uploads/2022/09/logo-0.png)

# Actividad Formativa – Semana 7
## Conectando aplicaciones Java con bases de datos mediante JDBC

### Proyecto: SpeedFast – Persistencia de pedidos y entregas

---

## Autor del proyecto

| Campo | Detalle |
|---|---|
| **Nombre completo** | Beatriz López Casanova |
| **Asignatura** | Desarrollo Orientado a Objetos II |
| **Carrera** | Analista Programador Computacional |
| **Sede** | Virtual |

---

## Descripción general del sistema

**SpeedFast** es una empresa de reparto a domicilio. Esta semana la interfaz de la semana 6 se conecta a **MySQL** con JDBC: los pedidos, repartidores y entregas se guardan y consultan en `speedfast_db`.

Desde la ventana principal se puede:

1. **Registrar** un pedido (queda en la tabla `pedido`).
2. **Registrar** un repartidor (tabla `repartidor`).
3. **Listar** pedidos y entregas en `JTable`.
4. **Asignar** un repartidor e **iniciar** la entrega (tabla `entrega` + cambio de estado).

La conexión la abre `conexion.ConexionBD` con `DriverManager`. Cada tabla tiene un DAO. **No hay** `DELETE` de filas: cancelar solo cambia el estado.

---

## Qué hay que recordar de esta semana

1. **MySQL** guarda las tablas. **Java** se conecta con **JDBC**.
2. `ConexionBD` solo abre y cierra el canal (`getConexion()`).
3. Cada tabla tiene un **DAO**: `PreparedStatement`, `ResultSet`, `try-catch-finally`.
4. La **GUI** no escribe SQL: llama al controlador y el controlador llama al DAO.
5. Registrar un pedido **no** crea una entrega. La entrega aparece al **asignar** o **iniciar**.

---

## Modelo de base de datos

Base: `speedfast_db` (script en `script.sql`).

| Tabla | Columnas | Para qué |
|---|---|---|
| `repartidor` | id, nombre | Quién entrega |
| `pedido` | id, direccion, tipo, estado, distancia_km, peso, fragil | Qué se entrega |
| `entrega` | id, id_pedido, id_repartidor, fecha, hora | Une pedido + repartidor (FK) |

tipo: `COMIDA` \| `ENCOMIENDA` \| `EXPRESS`  
estado: `PENDIENTE` \| `EN_REPARTO` \| `ENTREGADO`

---

## Estructura de la carpeta `semana7`

```
semana7/
├── pom.xml                 → mysql-connector-j 9.4.0
├── script.sql              → CREATE DATABASE / tablas / FK
├── README.md
├── lib/
│   └── mysql-connector-j-9.4.0.jar
└── src/main/java/
    ├── app/Main.java
    ├── conexion/ConexionBD.java
    ├── dao/
    │   ├── PedidoDAO.java
    │   ├── RepartidorDAO.java
    │   └── EntregaDAO.java
    ├── controlador/
    ├── modelo/
    ├── tareas/
    └── vista/
```

---

## Cómo ejecutar

1. MySQL encendido, base `speedfast_db` creada.
2. IntelliJ: clic derecho en `semana7/pom.xml` → **Add as Maven Project**.
3. Run `semana7/src/main/java/app/Main.java`.
4. Consola: `Conexión a speedfast_db correcta.`

Usuario JDBC: `root` / contraseña: `1234`.

---

## Flujo para estudiar

| En la app | En MySQL |
|---|---|
| Registrar pedido | `INSERT INTO pedido` |
| Registrar repartidor | `INSERT INTO repartidor` |
| Listar pedidos | `SELECT` → JTable |
| Asignar / Iniciar entrega | `INSERT INTO entrega` + `UPDATE pedido.estado` |
| Listar entregas | `SELECT` con JOIN |

---

## Pauta (criterios)

1. Base y tablas con relaciones/FK.
2. Clase de conexión JDBC y manejo de excepciones.
3. Operaciones de los DAO (`guardar`, `listarTodos`).
4. Interfaz (JTable) inserta y consulta.
5. Proyecto en GitHub y comprimido, compilando sin errores.

---

**Repositorio GitHub:** https://github.com/Be-ri-lo/SpeedFast-Poliformismo

**Fecha de entrega:** Semana 7 – Septiembre 2026

© Duoc UC | Escuela de Informática y Telecomunicaciones
