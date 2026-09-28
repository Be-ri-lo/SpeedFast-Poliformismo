package modelo;

/**
 * Pedido express.
 * Tiempo: 10 minutos base; si la distancia es mayor a 5 km se agregan 5 minutos.
 */
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
