package org.speedFast.model;

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
