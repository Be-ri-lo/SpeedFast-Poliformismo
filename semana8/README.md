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

## Qué se hizo en esta semana

La aplicación de semanas anteriores quedó conectada a MySQL. Cada pantalla de la interfaz llama a un controlador y este a un DAO. Si algo falla (campo vacío, número mal escrito o error de base de datos), el usuario ve un mensaje con `JOptionPane`.

---

## Cómo ejecutar

1. Encender MySQL y correr `script.sql` en Workbench.
2. En IntelliJ: clic derecho en `semana8/pom.xml` → **Add as Maven Project**.
3. Copiar `conexion.properties.ejemplo` a `conexion.properties` y completar `db.clave`.
4. Ejecutar `src/main/java/app/Main.java`.

Ese archivo de clave **no se sube a GitHub**. Usuario y clave se entregan al docente por separado (informe o mensaje de la plataforma).

---

## Paso 1: Base de datos y conexión

Se creó la base `speedfast_db` con las tres tablas de la pauta: **repartidores**, **pedidos** y **entregas**. En pedidos se dejaron tres campos extra del modelo SpeedFast (kilómetros, peso y si es frágil), para no perder lo de semanas anteriores.

La conexión está en `conexion/ConexionDB.java`. Al partir, `app/Main` prueba si MySQL responde. Si no, muestra el error y no abre la ventana.

---

## Paso 2: Clases DAO

La guía nombra un `ClienteDAO`. En este proyecto no hay clientes: el equivalente es `RepartidorDAO`.

Cada DAO tiene los cuatro métodos: crear, listar, actualizar y eliminar. Usan `PreparedStatement` (con `?`) para no concatenar lo que escribe el usuario. La conexión se cierra sola con `try-with-resources`.

---

## Paso 3: Interfaz conectada a los DAO

Los botones de cada ventana llaman al controlador. Después de guardar, la tabla se recarga.

Al **Asignar** una entrega hay dos combos cargados desde MySQL:

- Pedido: se ve `id - dirección`.
- Repartidor: se ve `id - nombre`.

Los filtros también se actualizan cuando se crea, edita o elimina algo.

---

## Paso 4: Validaciones y errores

Antes de guardar se revisan los campos en la ventana (nombre, dirección, kilómetros, peso). Si falta algo, no se llama al DAO.

Si MySQL falla, el DAO traduce el error y la vista lo muestra. Si se intenta borrar un pedido o un repartidor que todavía tiene entregas, el mensaje es: *tiene entregas asociadas*.

---

## Ventanas

| Menú | Qué se puede hacer |
|---|---|
| Gestionar repartidores | Nuevo, Editar, Eliminar |
| Gestionar pedidos | Nuevo, Editar, Eliminar (con filtros) |
| Gestionar entregas | Asignar, Editar, Eliminar |
| Simular entregas | Recorre las entregas asignadas con un hilo por repartidor |

Registrar un pedido **no** crea la entrega. La entrega aparece cuando se pulsa **Asignar**.

A la derecha de entregas hay una **cola del día**: primero los repartidores libres y, si ya tienen carga, el que lleva menos pedidos hoy. El combo de asignar propone al siguiente, pero se puede cambiar a mano.

---

## Interfaz gráfica (Swing)

Las ventanas son `JFrame` armados en código (no se usan archivos `.form`).

| Componente | Dónde se usa |
|---|---|
| `JFrame` / `JPanel` | Menú y cada pantalla de gestión |
| `JTable` | Listados |
| `JTextField` | Nombre, dirección, km, peso, fecha, hora |
| `JComboBox` | Tipo, estado, pedido, repartidor |
| `JButton` | Nuevo, Editar, Eliminar, Filtrar, Asignar, Recargar |

Los resultados (éxito o error) salen con `JOptionPane`.

---

## Organización del código

```
semana8/
├── pom.xml
├── script.sql
├── README.md
└── src/main/java/
    ├── app/Main.java
    ├── conexion/ConexionDB.java
    ├── dao/
    ├── controlador/
    ├── modelo/
    ├── tareas/
    └── vista/
```

Capas: **vista → controlador → DAO**. La interfaz no importa `java.sql`.

---

## Simular entregas

Hay que asignar al menos un pedido. Después, en el menú, **Simular entregas**:

1. Se crea un hilo (`TareaEntrega`) por cada repartidor con pedidos en camino.
2. El hilo espera unos segundos (simula el viaje).
3. Guarda el pedido como entregado.
4. El área de actividad muestra el progreso y las tablas se recargan.

Si no hay nada asignado, sale: *Debe asignar al menos un pedido antes de simular.*

---

**Repositorio GitHub:** https://github.com/Be-ri-lo/SpeedFast-Poliformismo

**Fecha de entrega:** Semana 8 – Octubre 2026

© Duoc UC | Escuela de Informática y Telecomunicaciones
