package org.speedFast.model;

import org.speedFast.interfaces.Cancelable;
import org.speedFast.interfaces.Despachable;
import org.speedFast.interfaces.Rastreable;

import java.util.ArrayList;
import java.util.List;

public abstract class Pedido implements Despachable, Cancelable, Rastreable {

    private int idPedido;
    private String direccionEntrega;
    private double distanciaKm;
    private String repartidor;
    private EstadoPedido estado;
    private final List<String> eventos;

    public Pedido(int idPedido, String direccionEntrega, double distanciaKm) {
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        setDistanciaKm(distanciaKm);
        this.repartidor = "Sin asignar";
        this.estado = EstadoPedido.RESERVADO;
        this.eventos = new ArrayList<>();
        registrarEvento("Pedido reservado");
    }

    public void mostrarResumen() {
        System.out.println(getClass().getSimpleName() + " #" + formatearId());
        System.out.println("Dirección: " + direccionEntrega);
        System.out.println("Distancia: " + distanciaKm + " km");
        System.out.println("Repartidor: " + repartidor);
        System.out.println("Estado: " + estado.getEtiqueta());
    }

    public abstract double calcularTiempoEntrega();

    public abstract void asignarRepartidor();

    public void asignarRepartidor(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del repartidor no puede estar vacío");
        }
        if (estado == EstadoPedido.CANCELADO) {
            throw new IllegalStateException("No se puede asignar un repartidor a un pedido cancelado");
        }
        confirmarAsignacion(nombre.trim(), "asignación manual");
    }

    @Override
    public void despachar() {
        if (estado == EstadoPedido.CANCELADO) {
            throw new IllegalStateException("No se puede despachar un pedido cancelado");
        }
        if (estado != EstadoPedido.ASIGNADO) {
            throw new IllegalStateException("Debe asignarse un repartidor antes de despachar");
        }
        this.estado = EstadoPedido.DESPACHADO;
        registrarEvento("Pedido despachado. Tiempo estimado: " + calcularTiempoEntrega() + " min");
    }

    @Override
    public void cancelar() {
        if (estado == EstadoPedido.DESPACHADO) {
            throw new IllegalStateException("No se puede cancelar un pedido ya despachado");
        }
        if (estado == EstadoPedido.CANCELADO) {
            throw new IllegalStateException("El pedido ya está cancelado");
        }
        this.estado = EstadoPedido.CANCELADO;
        registrarEvento("Pedido cancelado");
    }

    @Override
    public void verHistorial() {
        System.out.println("Historial de " + getClass().getSimpleName() + " #" + formatearId());
        for (String evento : eventos) {
            System.out.println("  - " + evento);
        }
    }

    protected void confirmarAsignacion(String nombre, String criterio) {
        this.repartidor = nombre;
        this.estado = EstadoPedido.ASIGNADO;
        registrarEvento("Repartidor asignado (" + criterio + "): " + nombre);
    }

    protected void registrarEvento(String detalle) {
        eventos.add(detalle);
    }

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

    public void setDistanciaKm(double distanciaKm) {
        if (distanciaKm < 0.1 || distanciaKm > 100) {
            throw new IllegalArgumentException("La distancia de reparto debe estar entre 0.1 km (100 metros) y 100 km");
        }
        this.distanciaKm = distanciaKm;
    }

    public String getRepartidor() {
        return repartidor;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public List<String> getEventos() {
        return eventos;
    }
}
