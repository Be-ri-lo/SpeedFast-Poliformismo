package org.speedFast.strategy;

/**
 * Estrategia concreta: fórmula de encomienda.
 * 20 min base + 1.5 min por km, redondeado a entero.
 */
public class TiempoEncomienda implements EstrategiaTiempoEntrega {

    @Override
    public double calcular(double distanciaKm) {
        return Math.round(20 + (1.5 * distanciaKm));
    }
}
