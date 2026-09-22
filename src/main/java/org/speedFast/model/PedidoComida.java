package org.speedFast.model;

/**
 * Pedido de comida a domicilio.
 * Tiempo: 15 minutos base + 2 minutos por cada kilómetro.
 */
public class PedidoComida extends Pedido {

    /**
     * @param idPedido         identificador del pedido
     * @param direccionEntrega dirección de destino
     * @param distanciaKm      distancia en kilómetros
     */
    public PedidoComida(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm);
    }

    /**
     * {@inheritDoc}
     * Fórmula: 15 + (2 * km).
     */
    @Override
    public double calcularTiempoEntrega() {
        return 15 + (2 * getDistanciaKm());
    }

    /**
     * Asignación automática de repartidor para comida.
     */
    @Override
    public void asignarRepartidor() {
        setRepartidor("Camila Soto");
    }
}
