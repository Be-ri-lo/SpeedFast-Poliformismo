package modelo;

/**
 * Pedido de comida a domicilio.
 * Tiempo: 15 minutos base + 2 minutos por cada kilómetro.
 */
public class PedidoComida extends Pedido {

    public PedidoComida(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm);
    }

    @Override
    public double calcularTiempoEntrega() {
        return 15 + (2 * getDistanciaKm());
    }

    @Override
    public void asignarRepartidor() {
        setRepartidor("Camila Soto");
    }

    @Override
    public String getTipo() {
        return "Comida";
    }
}
