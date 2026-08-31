![Duoc UC](https://www.duoc.cl/wp-content/uploads/2022/09/logo-0.png)

# Actividad Sumativa – Semana 3
## Diseñando un sistema orientado a objetos con clases abstractas, polimorfismo e interfaces

### Proyecto: SpeedFast – Sistema integral de entregas

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

**SpeedFast** es una empresa de reparto a domicilio que gestiona de forma integral tres tipos de servicio: comida, encomiendas y compras express. Cada tipo de pedido tiene reglas propias para **asignar repartidor** y **calcular el tiempo de entrega**.

Esta versión incorpora de manera conjunta:

- Una **clase abstracta** `Pedido` con atributos comunes, `mostrarResumen()` y el método abstracto `calcularTiempoEntrega()`.
- **Polimorfismo** por sobrescritura (`asignarRepartidor()`) y por sobrecarga (`asignarRepartidor(String nombre)`).
- **Interfaces** `Despachable`, `Cancelable` y `Rastreable` para desacoplar las operaciones funcionales.
- Un **controlador** `ControladorDeEnvios` que coordina despacho, cancelación e historial.

| Tipo de pedido | Fórmula de tiempo (semana 2) |
|---|---|
| `PedidoComida` | 15 min + 2 min por cada km |
| `PedidoEncomienda` | 20 min + 1.5 min por km (redondeado a entero) |
| `PedidoExpress` | 10 min base; si distancia > 5 km, se agregan 5 min extra |

La distancia se valida entre **0.1 km** y **100 km**. Los errores de negocio se controlan en `Main` con `try-catch`.

---

## Estructura de paquetes y clases

```
src/main/java/org/speedFast/
├── interfaces/
│   ├── Despachable.java         → despachar()
│   ├── Cancelable.java          → cancelar()
│   └── Rastreable.java          → verHistorial()
├── model/
│   ├── Pedido.java              → Clase abstracta (atributos, resumen, sobrecarga)
│   ├── PedidoComida.java        → Tiempo y asignación de comida
│   ├── PedidoEncomienda.java    → Tiempo y asignación de encomienda
│   └── PedidoExpress.java       → Tiempo y asignación express
├── service/
│   └── ControladorDeEnvios.java → Coordina operaciones e historial (ArrayList)
├── util/
│   └── EstadoPedido.java        → RESERVADO, ASIGNADO, DESPACHADO, CANCELADO
└── app/
    └── Main.java                → Simulación completa del flujo
```

---

## Diagrama de clases

```mermaid
classDiagram
    class Despachable {
        <<interface>>
        +despachar()
    }
    class Cancelable {
        <<interface>>
        +cancelar()
    }
    class Rastreable {
        <<interface>>
        +verHistorial()
    }

    class Pedido {
        <<abstract>>
        -int idPedido
        -String direccionEntrega
        -double distanciaKm
        -String repartidor
        -EstadoPedido estado
        +mostrarResumen()
        +calcularTiempoEntrega()* double
        +asignarRepartidor()*
        +asignarRepartidor(String nombre)
        +despachar()
        +cancelar()
        +verHistorial()
    }

    class PedidoComida {
        +calcularTiempoEntrega() double
        +asignarRepartidor()
    }
    class PedidoEncomienda {
        +calcularTiempoEntrega() double
        +asignarRepartidor()
    }
    class PedidoExpress {
        +calcularTiempoEntrega() double
        +asignarRepartidor()
    }

    class ControladorDeEnvios {
        -List~Pedido~ pedidos
        -List~String~ historialEntregas
        +registrarPedido(Pedido)
        +despachar()
        +cancelar()
        +verHistorial()
    }

    class EstadoPedido {
        <<enumeration>>
        RESERVADO
        ASIGNADO
        DESPACHADO
        CANCELADO
    }

    Despachable <|.. Pedido
    Cancelable <|.. Pedido
    Rastreable <|.. Pedido
    Despachable <|.. ControladorDeEnvios
    Cancelable <|.. ControladorDeEnvios
    Rastreable <|.. ControladorDeEnvios
    Pedido <|-- PedidoComida
    Pedido <|-- PedidoEncomienda
    Pedido <|-- PedidoExpress
    Pedido --> EstadoPedido
    ControladorDeEnvios o-- Pedido
```

### Relaciones

| Relación | Tipo | Descripción |
|---|---|---|
| `Pedido` | **Clase abstracta** | Define atributos comunes, `mostrarResumen()` y obliga a implementar `calcularTiempoEntrega()` y `asignarRepartidor()` |
| Subclases → `Pedido` | **Herencia** | Reutilizan constructor y resumen; implementan tiempo y asignación |
| `asignarRepartidor()` | **Sobrescritura** | Cada tipo elige un perfil de repartidor distinto |
| `asignarRepartidor(String)` | **Sobrecarga** | Permite asignar un nombre de forma manual |
| `Despachable`, `Cancelable`, `Rastreable` | **Interfaces** | Separan las operaciones funcionales del modelo de pedido |
| `ControladorDeEnvios` | **Composición / coordinación** | Usa las interfaces y guarda el historial en un `ArrayList` |
| `Main` → `Pedido` | **Polimorfismo** | Recorre distintos tipos como `Pedido` y opera también a través de interfaces |

---

## Cómo contribuye el diseño a la calidad del software

- **Escalabilidad:** un nuevo tipo de pedido solo requiere una subclase de `Pedido` con su fórmula de tiempo y su asignación, sin modificar `Main` ni el controlador.
- **Reutilización:** `mostrarResumen()`, validación de distancia, despacho y cancelación viven una sola vez en la clase abstracta.
- **Mantenibilidad:** las interfaces desacoplan el “qué se puede hacer” (despachar, cancelar, rastrear) del “quién lo hace”. `Main` puede trabajar con `Despachable` o `Rastreable` sin conocer la clase concreta.

---

## Instrucciones para ejecutar el programa

### Requisitos previos

- Java JDK 17 o superior (el proyecto fue compilado y probado con JDK 26)
- Maven 3.x (o abrir directamente en IntelliJ IDEA)

### Opción A – Desde IntelliJ IDEA (recomendada)

1. Abrir el proyecto como proyecto Maven en IntelliJ IDEA.
2. Navegar a `src/main/java/org/speedFast/app/Main.java`.
3. Hacer clic derecho → **Run 'Main.main()'**.

### Opción B – Desde terminal con Maven

```bash
mvn compile
mvn exec:java -Dexec.mainClass="org.speedFast.app.Main"
```

### Opción C – Desde terminal (sin Maven)

```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src/main/java -name "*.java")
java -cp out org.speedFast.app.Main
```

---

## Salida esperada por consola

```
PedidoEncomienda #002
Dirección: Av. Independencia 123
Distancia: 6.0 km
Repartidor asignado: Daniela Tapia
Estado: Despachado
Tiempo estimado: 29.0 minutos
Pedido despachado correctamente.

PedidoComida #001
Dirección: Av. Italia 456
Distancia: 2.7 km
Repartidor asignado: Luis Díaz
Estado: Despachado
Tiempo estimado: 20.4 minutos
Pedido despachado correctamente.

Cancelando Pedido Express #003...
→ Pedido cancelado exitosamente.

PedidoExpress #003
Dirección: Av. Apoquindo 1500
Distancia: 15.0 km
Repartidor asignado: Carlos Soto
Estado: Cancelado
Tiempo estimado: 15.0 minutos

Historial:
- PedidoEncomienda #002 – entregado por Daniela Tapia
- PedidoComida #001 – entregado por Luis Díaz
```

---

## Buenas prácticas aplicadas

- Clase abstracta `Pedido` con atributos encapsulados y constructor completo.
- Método concreto `mostrarResumen()` reutilizado por todas las subclases.
- Método abstracto `calcularTiempoEntrega()` implementado de forma diferenciada en cada subclase.
- Sobrescritura de `asignarRepartidor()` y sobrecarga `asignarRepartidor(String)`.
- Interfaces funcionales implementadas por `Pedido` y por `ControladorDeEnvios`.
- Historial de entregas en `ArrayList`.
- Validación de distancia y de estados inválidos (`IllegalArgumentException` / `IllegalStateException`).
- Separación de responsabilidades en paquetes `interfaces`, `model`, `service`, `util` y `app`.

---

**Repositorio GitHub:** https://github.com/Be-ri-lo/SpeedFast-Poliformismo

**Fecha de entrega:** Semana 3 – Agosto 2026

© Duoc UC | Escuela de Informática y Telecomunicaciones
