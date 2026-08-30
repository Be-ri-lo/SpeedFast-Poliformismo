package org.speedFast.model;

public class PedidoEncomienda extends Pedido {

    public PedidoEncomienda(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm);
    }

    // tiempo = 20 min + 1.5 min por kilómetro (ajustar a entero)
    @Override
    public double calcularTiempoEntrega() {
        return Math.round(20 + (1.5 * getDistanciaKm()));
    }

    @Override
    public void asignarRepartidor() {
        confirmarAsignacion("Diego Rivas (furgón - encomiendas)", "asignación automática");
    }

    @Override
    public void despachar() {
        super.despachar();
        registrarEvento("Encomienda verificada y despachada con registro de bulto");
    }
}
