package org.speedFast.strategy;

/**
 * Estrategia concreta: fórmula de comida.
 * 15 min de preparación + 2 min por cada kilómetro.
 *
 * Esta clase SOLO sabe calcular tiempo. No sabe de direcciones ni de estados.
 * Eso es Strategy: una responsabilidad, un algoritmo.
 */
public class TiempoComida implements EstrategiaTiempoEntrega {

    @Override
    public double calcular(double distanciaKm) {
        return 15 + (2 * distanciaKm);
    }
}
