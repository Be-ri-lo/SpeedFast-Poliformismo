![Duoc UC](https://www.duoc.cl/wp-content/uploads/2022/09/logo-0.png)

# Actividad Formativa – Semana 1
## Explorando la sobrecarga y sobreescritura

### Proyecto: SpeedFast – Polimorfismo

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

**SpeedFast** es una empresa de reparto a domicilio que ofrece tres tipos de servicio: comida (restaurantes), encomiendas (documentos o paquetes) y compras express (supermercado o farmacia). Cada tipo de pedido aplica criterios distintos al momento de asignar un repartidor.

Este proyecto implementa una jerarquía de clases en Java para representar esos pedidos, aplicando **polimorfismo** mediante:

- **Sobrescritura (`@Override`)**: el método `asignarRepartidor()` se comporta de forma distinta en cada subclase.
- **Sobrecarga**: el método `asignarRepartidor(String nombreRepartidor)` permite asignar un repartidor concreto e imprimir las validaciones propias del tipo de pedido.

---

## Estructura de paquetes y clases

```
src/main/java/org/speedFast/
├── Pedido.java              → Clase base: idPedido, direccionEntrega, tipoPedido
├── PedidoComida.java        → Subclase: verifica mochila térmica
├── PedidoEncomienda.java    → Subclase: valida peso y embalaje
├── PedidoExpress.java       → Subclase: busca repartidor cercano disponible
└── Main.java                → Prueba las tres subclases y ambas versiones del método
```

> No se requiere lectura de archivos externos ni persistencia: los objetos de ejemplo se crean directamente en `Main`.

---

## Relaciones entre clases

| Relación | Tipo | Descripción |
|---|---|---|
| `PedidoComida`, `PedidoEncomienda`, `PedidoExpress` → `Pedido` | **Herencia** | Heredan atributos comunes y sobrescriben `asignarRepartidor()` |
| `Pedido.asignarRepartidor()` / `Pedido.asignarRepartidor(String)` | **Sobrecarga** | Misma funcionalidad con distintas firmas |
| Subclases → `asignarRepartidor()` / `asignarRepartidor(String)` | **Sobrescritura** | Cada tipo de pedido imprime su propia lógica de asignación |
| `Main` → subclases de `Pedido` | Uso | Instancia un objeto de cada tipo y demuestra el polimorfismo |

---

## Instrucciones para ejecutar el programa

### Requisitos previos

- Java JDK 17 o superior (el proyecto fue compilado y probado con JDK 26)
- Maven 3.x (o abrir directamente en IntelliJ IDEA)

### Opción A – Desde IntelliJ IDEA (recomendada)

1. Abrir el proyecto como proyecto Maven en IntelliJ IDEA.
2. Navegar a `src/main/java/org/speedFast/Main.java`.
3. Hacer clic derecho → **Run 'Main.main()'**.

### Opción B – Desde terminal con Maven

```bash
# Desde la raíz del proyecto
mvn compile
mvn exec:java -Dexec.mainClass="org.speedFast.Main"
```

### Opción C – Desde terminal (sin Maven)

```bash
# Desde la raíz del proyecto
mkdir -p out
javac -encoding UTF-8 -d out $(find src/main/java -name "*.java")
java -cp out org.speedFast.Main
```

---

## Salida esperada por consola

```
[Pedido Comida]
Asignando repartidor...
-> Verificando mochila térmica... OK
Pedido asignado a Rosa Rojas

[Pedido Encomienda]
Asignando repartidor...
-> Validando peso y embalaje... OK
Pedido asignado a Juanito De Los Palotes

[Pedido Express]
Asignando repartidor...
-> Repartidor más cercano con disponibilidad inmediata encontrado.
Pedido asignado a Toribio Toro
```

---

## Buenas prácticas aplicadas

- Encapsulamiento de atributos (`private`) en la clase base `Pedido`, con getters y setters.
- Constructor completo y funcional en la clase base, reutilizado por las subclases con `super(...)`.
- Método base genérico `asignarRepartidor()` como punto de partida para la herencia.
- Sobrescritura del método en cada subclase con lógica diferenciada según el tipo de pedido.
- Sobrecarga `asignarRepartidor(String nombreRepartidor)` para asignar un repartidor por nombre.
- Demostración de polimorfismo desde `Main` instanciando las tres subclases.
- Salida por consola clara y diferenciada por tipo de pedido.

---

**Repositorio GitHub:** https://github.com/Be-ri-lo/SpeedFast-Poliformismo

**Fecha de entrega:** Semana 1 – Agosto 2026

© Duoc UC | Escuela de Informática y Telecomunicaciones
