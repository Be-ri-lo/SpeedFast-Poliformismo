package org.speedFast.strategy;

/**
 * PATRÓN STRATEGY — el contrato (la "estrategia").
 *
 * Idea en una frase:
 *   "El algoritmo puede cambiar sin cambiar la clase que lo usa."
 *
 * Aquí el algoritmo es: cómo se calcula el tiempo de entrega.
 *
 * SIN Strategy (tu entrega de semana 3):
 *   PedidoComida, PedidoEncomienda y PedidoExpress CADA UNA escribe su fórmula
 *   dentro de calcularTiempoEntrega(). Eso es herencia + sobrescritura.
 *
 * CON Strategy (esta carpeta):
 *   Pedido NO conoce la fórmula. Solo dice: estrategiaTiempo.calcular(distancia).
 *   Quien tiene la fórmula es otra clase (TiempoComida, TiempoEncomienda, TiempoExpress).
 *
 * Analogía:
 *   Pedido es un GPS. La estrategia es "cómo calcular la ruta"
 *   (más corta, más rápida, evitar peajes). El GPS no cambia; cambia la ruta.
 */
public interface EstrategiaTiempoEntrega {

    /**
     * Calcula el tiempo de entrega para una distancia dada.
     *
     * @param distanciaKm kilómetros a recorrer
     * @return minutos estimados
     */
    double calcular(double distanciaKm);
}
