package modelo;

/**
 * Estados del pedido en Java.
 * PedidoDAO los traduce a PENDIENTE, EN_REPARTO o ENTREGADO en MySQL.
 */
public enum EstadoPedido {
    RESERVADO("Reservado"),
    ASIGNADO("Asignado"),
    DESPACHADO("Despachado"),
    CANCELADO("Cancelado");

    private final String etiqueta;

    EstadoPedido(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
