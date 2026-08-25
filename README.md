![Duoc UC](https://www.duoc.cl/wp-content/uploads/2022/09/logo-0.png)

# Actividad Formativa – Semana 2
## Definiendo una clase abstracta y su jerarquía

### Proyecto: SpeedFast – Clases abstractas

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

**SpeedFast** es una empresa de reparto a domicilio que ofrece tres tipos de servicio: comida, encomiendas y compras express. En esta etapa, el sistema modela el **tiempo estimado de entrega** según el tipo de pedido y la distancia en kilómetros.

Se implementa una **clase abstracta** `Pedido` con atributos y comportamientos comunes, y tres subclases que personalizan el cálculo del tiempo de entrega:

| Tipo de pedido | Fórmula de tiempo |
|---|---|
| `PedidoComida` | 15 min + 2 min por cada km |
| `PedidoEncomienda` | 20 min + 1.5 min por km (redondeado a entero) |
| `PedidoExpress` | 10 min base; si distancia > 5 km, se agregan 5 min extra |

Además, se valida que la distancia esté entre **0.1 km (100 m)** y **100 km**, y el error se controla en `Main` con `try-catch` para mostrar un mensaje claro por consola.

---

## Estructura de paquetes y clases

```
src/main/java/org/speedFast/
├── model/
│   ├── Pedido.java              → Clase abstracta: idPedido, direccionEntrega, distanciaKm
│   ├── PedidoComida.java        → Implementa calcularTiempoEntrega() para comida
│   ├── PedidoEncomienda.java    → Implementa calcularTiempoEntrega() para encomienda
│   └── PedidoExpress.java       → Implementa calcularTiempoEntrega() para express
└── app/
    └── Main.java                → Ejecución principal; polimorfismo y manejo de excepciones
```

> No se requiere lectura de archivos externos ni persistencia: los objetos de ejemplo se crean directamente en `Main`.

---

## Relaciones entre clases

| Relación | Tipo | Descripción |
|---|---|---|
| `Pedido` | **Clase abstracta** | Define atributos comunes, `mostrarResumen()` e impone `calcularTiempoEntrega()` |
| `PedidoComida`, `PedidoEncomienda`, `PedidoExpress` → `Pedido` | **Herencia** | Reutilizan el constructor y el resumen; implementan su propia fórmula de tiempo |
| Subclases → `calcularTiempoEntrega()` | **Método abstracto / sobrescritura** | Cada tipo calcula el tiempo de forma distinta |
| `Main` → `Pedido` | **Polimorfismo** | Usa un arreglo `Pedido[]` para recorrer los distintos tipos |
| `Pedido` → `IllegalArgumentException` | **Validación** | Controla distancias fuera del rango permitido |
| `Main` → excepción | **Manejo de errores** | Captura el error y muestra un mensaje amigable |

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
# Desde la raíz del proyecto
mvn compile
mvn exec:java -Dexec.mainClass="org.speedFast.app.Main"
```

### Opción C – Desde terminal (sin Maven)

```bash
# Desde la raíz del proyecto
mkdir -p out
javac -encoding UTF-8 -d out $(find src/main/java -name "*.java")
java -cp out org.speedFast.app.Main
```

---

## Salida esperada por consola

```
=== Tiempos estimados de entrega SpeedFast ===

PedidoComida #001
Dirección: Av. Italia 456
Distancia: 2.7 km
Tiempo estimado de entrega: 20.4 minutos

PedidoEncomienda #002
Dirección: Av. Independencia 123
Distancia: 6.0 km
Tiempo estimado de entrega: 29.0 minutos

PedidoExpress #003
Dirección: Av. Apoquindo 1500
Distancia: 15.0 km
Tiempo estimado de entrega: 15.0 minutos

=== Comparación rápida ===
Comida (#001):      20.4 min
Encomienda (#002):  29.0 min
Express (#003):     15.0 min
```

### Ejemplo de error controlado

Si se ingresa una distancia inválida (menor a 0.1 km o mayor a 100 km), la consola muestra:

```
Error: La distancia de reparto debe estar entre 0.1 km (100 metros) y 100 km
```

---

## Buenas prácticas aplicadas

- Clase abstracta `Pedido` con atributos comunes encapsulados (`private`) y constructor completo.
- Método concreto `mostrarResumen()` reutilizado por todas las subclases.
- Método abstracto `calcularTiempoEntrega()` implementado de forma diferenciada en cada subclase.
- Herencia funcional con `super(...)` en los constructores de las clases derivadas.
- Polimorfismo explícito en `Main` mediante arreglo `Pedido[]`.
- Validación de distancia en `setDistanciaKm(...)`, reutilizada desde el constructor.
- Manejo controlado de `IllegalArgumentException` con `try-catch` en `Main`.
- Separación de responsabilidades en paquetes `model` (dominio) y `app` (ejecución).
- Salida por consola clara y comparativa entre tipos de pedido.

---

**Repositorio GitHub:** https://github.com/Be-ri-lo/SpeedFast-Poliformismo

**Fecha de entrega:** Semana 2 – Agosto 2026

© Duoc UC | Escuela de Informática y Telecomunicaciones
