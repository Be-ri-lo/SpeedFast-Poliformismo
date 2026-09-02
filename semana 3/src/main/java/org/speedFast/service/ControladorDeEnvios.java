package org.speedFast.service;

import org.speedFast.interfaces.Cancelable;
import org.speedFast.interfaces.Despachable;
import org.speedFast.interfaces.Rastreable;
import org.speedFast.model.Pedido;

import java.util.ArrayList;
import java.util.List;

/**
 * Coordina el despacho, la cancelación y el historial de entregas.
 * Implementa las interfaces funcionales para desacoplar esas operaciones.
 */
public class ControladorDeEnvios implements Despachable, Cancelable, Rastreable {

    private final List<Pedido> pedidos;
    private final List<String> historialEntregas;
    private Pedido pedidoSeleccionado;

    /**
     * Crea un controlador sin pedidos registrados.
     */
    public ControladorDeEnvios() {
        this.pedidos = new ArrayList<>();
        this.historialEntregas = new ArrayList<>();
    }

    /**
     * Incorpora un pedido a la gestión del controlador.
     *
     * @param pedido pedido a registrar
     */
    public void registrarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }

    /**
     * Define el pedido sobre el que operan {@link #despachar()} y {@link #cancelar()}.
     *
     * @param pedido pedido seleccionado
     */
    public void seleccionar(Pedido pedido) {
        this.pedidoSeleccionado = pedido;
    }

    /**
     * Despacha el pedido seleccionado.
     *
     * @throws IllegalStateException si no hay un pedido seleccionado
     */
    @Override
    public void despachar() {
        if (pedidoSeleccionado == null) {
            throw new IllegalStateException("Debe seleccionar un pedido antes de despacharlo");
        }
        despachar(pedidoSeleccionado);
    }

    /**
     * Despacha el pedido indicado y lo agrega al historial de entregas.
     *
     * @param pedido pedido a despachar
     */
    public void despachar(Pedido pedido) {
        pedido.despachar();
        historialEntregas.add(pedido.getClass().getSimpleName() + " #" + pedido.formatearId()
                + " – entregado por " + pedido.getRepartidor());
    }

    /**
     * Cancela el pedido seleccionado.
     *
     * @throws IllegalStateException si no hay un pedido seleccionado
     */
    @Override
    public void cancelar() {
        if (pedidoSeleccionado == null) {
            throw new IllegalStateException("Debe seleccionar un pedido antes de cancelarlo");
        }
        pedidoSeleccionado.cancelar();
    }

    /**
     * Imprime el historial de pedidos despachados.
     */
    @Override
    public void verHistorial() {
        System.out.println("Historial:");
        if (historialEntregas.isEmpty()) {
            System.out.println("Aún no hay entregas registradas.");
            return;
        }
        for (String registro : historialEntregas) {
            System.out.println("- " + registro);
        }
    }

    /**
     * @return pedidos registrados en el controlador
     */
    public List<Pedido> getPedidos() {
        return pedidos;
    }
}
