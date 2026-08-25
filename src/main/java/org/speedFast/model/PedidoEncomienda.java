package org.speedFast.model;

public class PedidoEncomienda extends Pedido {

    public PedidoEncomienda(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm);
    }

    // tiempo = 20 min + 1.5 min por kilómetro (ajustar a entero)
    @Override
    public double calcularTiempoEntrega() {
        double tiempoEntrega = Math.round(20 + (1.5 * getDistanciaKm()));
        return tiempoEntrega;
    }
}
