![Duoc UC](https://www.duoc.cl/wp-content/uploads/2022/09/logo-0.png)

# Actividad Formativa – Semana 4
## Ejecutando tareas en paralelo con hilos en Java

### Proyecto: SpeedFast – Optimización de entregas

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

**SpeedFast** es una empresa de reparto a domicilio que gestiona comida, encomiendas y compras express. La carpeta `src/` conserva la **semana 2**. La carpeta `semana 3/` conserva interfaces y polimorfismo. La entrega actual está en **`semana4`**: cada repartidor corre como un hilo (`Runnable`) y `Main` los lanza en paralelo con `ExecutorService`.

| Tipo de pedido | Fórmula de tiempo |
|---|---|
| `PedidoComida` | 15 min + 2 min por cada km |
| `PedidoEncomienda` | 20 min + 1.5 min por km (redondeado) |
| `PedidoExpress` | 10 min; +5 min si distancia > 5 km |

---

## Estructura del repositorio

```
SpeedFast-Poliformismo/
├── src/main/java/org/speedFast/     → Semana 2
├── semana 3/                        → Semana 3
└── semana4/                         → Semana 4 (entrega)
    ├── pom.xml
    ├── README.md
    └── src/main/java/org/speedFast/
        ├── app/Main.java
        ├── interfaces/
        ├── model/  (Pedido, subclases y Repartidor)
        └── util/EstadoPedido.java
```

Más detalle en `semana4/README.md`.

### Ejecutar (semana 4)

En IntelliJ: `semana4/src/main/java/org/speedFast/app/Main.java` → Run.

```
Cancelando PedidoExpress #107...
Estado actual: CANCELADO

[Repartidor: Camila] Entregando PedidoComida #101...
[Repartidor: Luis] Entregando PedidoExpress #102...
[Repartidor: Camila] Pedido #101 entregado.
[Repartidor: Luis] Pedido #107 cancelado. No se entrega.
...
[Main] Sistema finalizado.
```

---

**Repositorio GitHub:** https://github.com/Be-ri-lo/SpeedFast-Poliformismo

**Fecha de entrega:** Semana 4 – Septiembre 2026

© Duoc UC | Escuela de Informática y Telecomunicaciones
