package org.speedFast.model;

public class PedidoComida extends Pedido {

    public PedidoComida(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm);
    }

    // tiempo = 15 min + 2 min por cada kilómetro
    @Override
    public double calcularTiempoEntrega() {
        double tiempoEntrega = 15 + (2 * getDistanciaKm());
        return tiempoEntrega;
    }
}
