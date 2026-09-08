package org.speedFast.model;

/**
 * Pedido express.
 * Tiempo: 10 minutos base; si la distancia es mayor a 5 km se agregan 5 minutos.
 */
public class PedidoExpress extends Pedido {

    /**
     * @param idPedido         identificador del pedido
     * @param direccionEntrega dirección de destino
     * @param distanciaKm      distancia en kilómetros
     */
    public PedidoExpress(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm);
    }

    /**
     * {@inheritDoc}
     * 10 minutos; +5 si distancia; 5 km.
     */
    @Override
    public double calcularTiempoEntrega() {
        int tiempoBase = 10;
        if (getDistanciaKm() > 5) {
            return tiempoBase + 5;
        }
        return tiempoBase;
    }

    /**
     * Asignación automática de repartidor para envíos express.
     */
    @Override
    public void asignarRepartidor() {
        setRepartidor("Carlos Soto");
    }
}
