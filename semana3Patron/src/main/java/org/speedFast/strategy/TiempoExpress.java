package org.speedFast.strategy;

/**
 * Estrategia concreta: fórmula express.
 * 10 min base; si hay más de 5 km se agregan 5 min.
 */
public class TiempoExpress implements EstrategiaTiempoEntrega {

    @Override
    public double calcular(double distanciaKm) {
        int tiempoBase = 10;
        if (distanciaKm > 5) {
            return tiempoBase + 5;
        }
        return tiempoBase;
    }
}
