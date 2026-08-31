package org.speedFast.service;

import org.speedFast.interfaces.Cancelable;
import org.speedFast.interfaces.Despachable;
import org.speedFast.interfaces.Rastreable;
import org.speedFast.model.Pedido;

import java.util.ArrayList;
import java.util.List;

/**
 * El controlador NO cambia con Strategy.
 * Sigue despachando y cancelando pedidos.
 * El tiempo lo resuelve cada Pedido a través de su estrategia.
 */
public class ControladorDeEnvios implements Despachable, Cancelable, Rastreable {

    private final List<Pedido> pedidos;
    private final List<String> historialEntregas;
    private Pedido pedidoSeleccionado;

    public ControladorDeEnvios() {
        this.pedidos = new ArrayList<>();
        this.historialEntregas = new ArrayList<>();
    }

    public void registrarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }

    public void seleccionar(Pedido pedido) {
        this.pedidoSeleccionado = pedido;
    }

    @Override
    public void despachar() {
        if (pedidoSeleccionado == null) {
            throw new IllegalStateException("Debe seleccionar un pedido antes de despacharlo");
        }
        despachar(pedidoSeleccionado);
    }

    public void despachar(Pedido pedido) {
        pedido.despachar();
        historialEntregas.add(pedido.getClass().getSimpleName() + " #" + pedido.formatearId()
                + " – entregado por " + pedido.getRepartidor());
    }

    @Override
    public void cancelar() {
        if (pedidoSeleccionado == null) {
            throw new IllegalStateException("Debe seleccionar un pedido antes de cancelarlo");
        }
        pedidoSeleccionado.cancelar();
    }

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

    public List<Pedido> getPedidos() {
        return pedidos;
    }
}
