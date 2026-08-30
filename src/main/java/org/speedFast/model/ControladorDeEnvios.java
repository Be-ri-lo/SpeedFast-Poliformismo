package org.speedFast.model;

import org.speedFast.interfaces.Cancelable;
import org.speedFast.interfaces.Despachable;
import org.speedFast.interfaces.Rastreable;

import java.util.ArrayList;
import java.util.List;

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
        historialEntregas.add("RESERVADO  | Pedido #" + pedido.formatearId()
                + " (" + pedido.getClass().getSimpleName() + ") en " + pedido.getDireccionEntrega());
    }

    public void seleccionar(Pedido pedido) {
        this.pedidoSeleccionado = pedido;
    }

    public void asignarAutomaticamente() {
        for (Pedido pedido : pedidos) {
            if (pedido.getEstado() == EstadoPedido.RESERVADO) {
                pedido.asignarRepartidor();
            }
        }
    }

    @Override
    public void despachar() {
        if (pedidoSeleccionado != null) {
            despachar(pedidoSeleccionado);
            return;
        }
        for (Pedido pedido : new ArrayList<>(pedidos)) {
            if (pedido.getEstado() == EstadoPedido.ASIGNADO) {
                despachar(pedido);
            }
        }
    }

    public void despachar(Pedido pedido) {
        pedido.despachar();
        historialEntregas.add("DESPACHADO | Pedido #" + pedido.formatearId()
                + " (" + pedido.getClass().getSimpleName() + ") → " + pedido.getRepartidor()
                + " | " + pedido.calcularTiempoEntrega() + " min");
    }

    @Override
    public void cancelar() {
        if (pedidoSeleccionado == null) {
            throw new IllegalStateException("Debe seleccionar un pedido antes de cancelarlo");
        }
        cancelar(pedidoSeleccionado);
    }

    public void cancelar(Pedido pedido) {
        pedido.cancelar();
        historialEntregas.add("CANCELADO  | Pedido #" + pedido.formatearId()
                + " (" + pedido.getClass().getSimpleName() + ") en " + pedido.getDireccionEntrega());
    }

    @Override
    public void verHistorial() {
        System.out.println("=== Historial de entregas SpeedFast ===");
        if (historialEntregas.isEmpty()) {
            System.out.println("Aún no hay movimientos registrados.");
            return;
        }
        for (String registro : historialEntregas) {
            System.out.println("  • " + registro);
        }
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public List<String> getHistorialEntregas() {
        return historialEntregas;
    }
}
