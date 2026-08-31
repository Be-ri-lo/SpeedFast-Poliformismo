package org.speedFast.model;

import org.speedFast.strategy.TiempoComida;

/**
 * Pedido de comida.
 * Ya NO escribe la fórmula aquí. Solo elige la estrategia TiempoComida al construirse.
 *
 * Compara con semana 3: allá calcularTiempoEntrega() tenía "return 15 + (2 * km)".
 * Aquí ese return está en TiempoComida.calcular(...).
 */
public class PedidoComida extends Pedido {

    public PedidoComida(int idPedido, String direccionEntrega, double distanciaKm) {
        // super recibe la estrategia: "calcula el tiempo como comida"
        super(idPedido, direccionEntrega, distanciaKm, new TiempoComida());
    }

    @Override
    public void asignarRepartidor() {
        setRepartidor("Camila Soto");
    }
}
