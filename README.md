![Duoc UC](https://www.duoc.cl/wp-content/uploads/2022/09/logo-0.png)

# SpeedFast – Sistema de entregas

### Actividad actual: Semana 5 – Sincronizando procesos en sistemas concurrentes

---

## Autor del proyecto

| Campo | Detalle |
|---|---|
| **Nombre completo** | Beatriz López Casanova |
| **Asignatura** | Desarrollo Orientado a Objetos II |
| **Carrera** | Analista Programador Computacional |
| **Sede** | Virtual |

---

## Descripción general

**SpeedFast** es una empresa de reparto a domicilio. El repositorio guarda el avance por semana:

| Carpeta | Semana | Tema |
|---|---|---|
| `src/` | Semana 2 | Pedidos y subclases |
| `semana 3/` | Semana 3 | Interfaces y polimorfismo |
| `semana4/` | Semana 4 | Hilos y `ExecutorService` |
| `semana 5/` | **Semana 5 (entrega)** | Sincronización de la zona de carga |
| `semana 5 alternativa/` | Extra | Igual que semana 5, más un `Monitor` |

En la semana 5 varios repartidores acceden a la **misma** zona de carga. Con `synchronized` cada pedido lo retira un solo hilo.

---

## Estructura del repositorio

```
SpeedFast-Poliformismo/
├── src/main/java/org/speedFast/     → Semana 2
├── semana 3/                        → Semana 3
├── semana4/                         → Semana 4
├── semana 5/                        → Semana 5 (entrega)
│   ├── pom.xml
│   ├── README.md
│   └── src/main/java/org/speedFast/
│       ├── app/Main.java
│       ├── model/  (Pedido, ZonaDeCarga, Repartidor)
│       └── util/EstadoPedido.java
└── semana 5 alternativa/            → Extra con Monitor (no se pide en las instrucciones)
```

Más detalle en `semana 5/README.md`.

### Ejecutar (semana 5)

En IntelliJ: `semana 5/src/main/java/org/speedFast/app/Main.java` → Run.

```
[Zona de carga inicializada]
Pedido #1 agregado. Destino: Santiago Centro
...
[Repartidor - Juan] Retirando pedido #1...
[Repartidor - Juan] Estado: EN_REPARTO
[Repartidor - Juan] Entregando pedido #1...
[Repartidor - Juan] Estado: ENTREGADO
...
Todos los pedidos han sido entregados correctamente.
```

---

**Repositorio GitHub:** https://github.com/Be-ri-lo/SpeedFast-Poliformismo

**Fecha de entrega:** Semana 5 – Septiembre 2026

© Duoc UC | Escuela de Informática y Telecomunicaciones
