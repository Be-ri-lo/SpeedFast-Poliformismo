package org.speedFast.model;

public class PedidoComida extends Pedido {

    public PedidoComida(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm);
    }

    // tiempo = 15 min + 2 min por cada kilómetro
    @Override
    public double calcularTiempoEntrega() {
        return 15 + (2 * getDistanciaKm());
    }

    @Override
    public void asignarRepartidor() {
        confirmarAsignacion("Camila Soto (moto - alimentos)", "asignación automática");
    }

    @Override
    public void despachar() {
        super.despachar();
        registrarEvento("Comida despachada en envase térmico para conservar temperatura");
    }
}
