package org.speedFast.model;

import org.speedFast.strategy.TiempoExpress;

/**
 * Pedido express.
 * Elige TiempoExpress al construirse. No contiene la fórmula 10 / +5.
 */
public class PedidoExpress extends Pedido {

    public PedidoExpress(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm, new TiempoExpress());
    }

    @Override
    public void asignarRepartidor() {
        setRepartidor("Carlos Soto");
    }
}
