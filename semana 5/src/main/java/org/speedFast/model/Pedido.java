package org.speedFast.model;

import org.speedFast.util.EstadoPedido;

/**
 * Pedido / encomienda de SpeedFast.
 * Llega a la zona de carga y un solo repartidor lo retira.
 */
public class Pedido {

    private int id;
    private String direccionEntrega;
    private EstadoPedido estado;

    /**
     * Crea un pedido pendiente, listo para entrar a la zona de carga.
     *
     * @param id                numero del pedido
     * @param direccionEntrega  destino
     */
    public Pedido(int id, String direccionEntrega) {
        if (id <= 0) {
            throw new IllegalArgumentException("El id del pedido debe ser mayor a 0");
        }
        if (direccionEntrega == null || direccionEntrega.isBlank()) {
            throw new IllegalArgumentException("La direccion de entrega no puede estar vacia");
        }
        this.id = id;
        // trim saca los espacios de los extremos, no une la direccion
        this.direccionEntrega = direccionEntrega.trim();
        this.estado = EstadoPedido.PENDIENTE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        this.estado = nuevoEstado;
    }

    // las instrucciones piden este metodo con String para actualizar el estado
    public void setEstado(String nuevoEstado) {
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            throw new IllegalArgumentException("El estado no puede estar vacio");
        }
        this.estado = EstadoPedido.valueOf(nuevoEstado.trim().toUpperCase());
    }

    @Override
    public String toString() {
        return "Pedido #" + id + " | Destino: " + direccionEntrega + " | Estado: " + estado;
    }
}
