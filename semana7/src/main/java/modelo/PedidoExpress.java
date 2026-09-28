package modelo;

/** Pedido express: 10 min; si hay más de 5 km se suman 5. */
public class PedidoExpress extends Pedido {

    public PedidoExpress(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm);
    }

    @Override
    public double calcularTiempoEntrega() {
        return getDistanciaKm() > 5 ? 15 : 10;
    }

    @Override
    public void asignarRepartidor() {
        setRepartidor("Carlos Soto");
    }

    @Override
    public String getTipo() {
        return "Express";
    }
}
