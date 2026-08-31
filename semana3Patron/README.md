# Semana 3 — carpeta de estudio: patrón Strategy

Esta carpeta **no es la entrega oficial**. La entrega sigue en `semana 3`.
Aquí Strategy está implementado solo para que lo entiendas, con comentarios en el código.

## Qué problema resuelve

Tienes tres formas de calcular el tiempo (comida, encomienda, express).

- **Sin Strategy (tu entrega):** cada subclase escribe su fórmula en `calcularTiempoEntrega()`.
- **Con Strategy:** la fórmula vive en otra clase. `Pedido` solo dice: “usa la estrategia que te pasé”.

## Las 3 piezas

1. **Contrato:** `EstrategiaTiempoEntrega` → método `calcular(distanciaKm)`
2. **Algoritmos:** `TiempoComida`, `TiempoEncomienda`, `TiempoExpress`
3. **Contexto:** `Pedido` guarda `estrategiaTiempo` y llama `estrategiaTiempo.calcular(...)`

## Cómo leerlo (orden)

1. Abre `strategy/EstrategiaTiempoEntrega.java` (la idea general).
2. Abre `TiempoComida.java` (una fórmula suelta).
3. Abre `model/Pedido.java` y busca el campo `estrategiaTiempo`.
4. Abre `PedidoComida.java`: el `super(..., new TiempoComida())` elige la fórmula.
5. Abre `Main.java`: la demo cambia la estrategia de comida a express **sin cambiar de clase**.

## La demo que debes entender

En consola vas a ver algo así para 2.7 km:

- Con `TiempoComida`: 20.4 min (15 + 2×2.7)
- Con `TiempoExpress`: 10 min (menos de 5 km no suma extra)
- Otra vez `TiempoComida`: 20.4 min

Sigue siendo un `PedidoComida`. Solo cambió el objeto que calcula.

## Cómo ejecutarlo en IntelliJ

1. File → Open → esta carpeta `semana3Patron` (tiene su propio `pom.xml`).
2. Abre `src/main/java/org/speedFast/app/Main.java`.
3. Run.

O desde la raíz del repo, sin abrir otro proyecto:

```bash
javac -encoding UTF-8 -d out-patron $(find semana3Patron/src/main/java -name "*.java")
java -cp out-patron org.speedFast.app.Main
```
