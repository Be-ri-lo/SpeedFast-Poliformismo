package org.speedFast.model;

import org.speedFast.interfaces.Cancelable;
import org.speedFast.interfaces.Despachable;
import org.speedFast.interfaces.Rastreable;
import org.speedFast.util.EstadoPedido;

/**
 * Clase abstracta base de los pedidos SpeedFast.
 * Define atributos comunes, el resumen y la sobrecarga de asignación de repartidor.
 * Las subclases implementan el cálculo de tiempo y la asignación automática.
 */
public abstract class Pedido implements Despachable, Cancelable, Rastreable {

    private int idPedido;
    private String direccionEntrega;
    private double distanciaKm;
    private String repartidor;
    private EstadoPedido estado;

    /**
     * Crea un pedido reservado, sin repartidor asignado.
     *
     * @param idPedido          identificador del pedido
     * @param direccionEntrega  dirección de destino
     * @param distanciaKm       distancia en kilómetros
     * @throws IllegalArgumentException si la distancia está fuera de 0.1 a 100 km
     */
    public Pedido(int idPedido, String direccionEntrega, double distanciaKm) {
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        setDistanciaKm(distanciaKm);
        this.repartidor = "Sin asignar";
        this.estado = EstadoPedido.RESERVADO;
    }

    /**
     * Muestra por consola los datos comunes del pedido.
     */
    public void mostrarResumen() {
        System.out.println(getClass().getSimpleName() + " #" + formatearId());
        System.out.println("Dirección: " + direccionEntrega);
        System.out.println("Distancia: " + distanciaKm + " km");
        System.out.println("Repartidor asignado: " + repartidor);
        System.out.println("Estado: " + estado.getEtiqueta());
        System.out.println("Tiempo estimado: " + calcularTiempoEntrega() + " minutos");
    }

    /**
     * Calcula el tiempo estimado de entrega según el tipo de pedido.
     *
     * @return tiempo en minutos
     */
    public abstract double calcularTiempoEntrega();

    /**
     * Asigna un repartidor de forma automática.
     * Cada subclase sobrescribe este método con su propio criterio.
     */
    public abstract void asignarRepartidor();

    /**
     * Asigna un repartidor de forma manual (sobrecarga).
     *
     * @param nombre nombre del repartidor
     * @throws IllegalArgumentException si el nombre está vacío
     * @throws IllegalStateException    si el pedido ya fue cancelado
     */
    public void asignarRepartidor(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del repartidor no puede estar vacío");
        }
        if (estado == EstadoPedido.CANCELADO) {
            throw new IllegalStateException("No se puede asignar un repartidor a un pedido cancelado");
        }
        this.repartidor = nombre.trim();
        this.estado = EstadoPedido.ASIGNADO;
    }

    /**
     * Despacha el pedido. Requiere tener un repartidor asignado.
     *
     * @throws IllegalStateException si está cancelado o aún no tiene repartidor
     */
    @Override
    public void despachar() {
        if (estado == EstadoPedido.CANCELADO) {
            throw new IllegalStateException("No se puede despachar un pedido cancelado");
        }
        if (estado != EstadoPedido.ASIGNADO) {
            throw new IllegalStateException("Debe asignarse un repartidor antes de despachar");
        }
        this.estado = EstadoPedido.DESPACHADO;
    }

    /**
     * Cancela el pedido si todavía no ha sido despachado.
     *
     * @throws IllegalStateException si ya está despachado o cancelado
     */
    @Override
    public void cancelar() {
        if (estado == EstadoPedido.DESPACHADO) {
            throw new IllegalStateException("No se puede cancelar un pedido ya despachado");
        }
        if (estado == EstadoPedido.CANCELADO) {
            throw new IllegalStateException("El pedido ya está cancelado");
        }
        this.estado = EstadoPedido.CANCELADO;
    }

    /**
     * Muestra el estado actual y el repartidor del pedido.
     */
    @Override
    public void verHistorial() {
        System.out.println(getClass().getSimpleName() + " #" + formatearId()
                + " | Estado: " + estado.getEtiqueta()
                + " | Repartidor: " + repartidor);
    }

    /**
     * @return identificador con tres dígitos, por ejemplo 001
     */
    public String formatearId() {
        return String.format("%03d", idPedido);
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    /**
     * @param distanciaKm distancia entre 0.1 y 100 km
     * @throws IllegalArgumentException si el valor está fuera de rango
     */
    public void setDistanciaKm(double distanciaKm) {
        if (distanciaKm < 0.1 || distanciaKm > 100) {
            throw new IllegalArgumentException("La distancia de reparto debe estar entre 0.1 km (100 metros) y 100 km");
        }
        this.distanciaKm = distanciaKm;
    }

    public String getRepartidor() {
        return repartidor;
    }

    /**
     * Asigna el repartidor y deja el pedido en estado ASIGNADO.
     *
     * @param repartidor nombre del repartidor
     */
    public void setRepartidor(String repartidor) {
        this.repartidor = repartidor;
        this.estado = EstadoPedido.ASIGNADO;
    }

    public EstadoPedido getEstado() {
        return estado;
    }
}
