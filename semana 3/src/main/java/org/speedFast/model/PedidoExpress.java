package org.speedFast.model;

public class PedidoExpress extends Pedido {

    public PedidoExpress(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm);
    }

    // tiempo = 10 min base; si distancia > 5 km, se agregan 5 min extra
    @Override
    public double calcularTiempoEntrega() {
        int tiempoBase = 10;
        if (getDistanciaKm() > 5) {
            return tiempoBase + 5;
        }
        return tiempoBase;
    }

    @Override
    public void asignarRepartidor() {
        confirmarAsignacion("Ana Torres (bici eléctrica - express)", "asignación automática");
    }

    @Override
    public void despachar() {
        super.despachar();
        registrarEvento("Despacho prioritario express activado");
    }
}
