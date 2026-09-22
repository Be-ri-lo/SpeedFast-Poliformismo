package controlador;

import modelo.Pedido;
import modelo.PedidoComida;
import modelo.PedidoEncomienda;
import modelo.PedidoExpress;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona los pedidos en memoria.
 */
public class ControladorPedidos {

    private final List<Pedido> pedidos = new ArrayList<>();
    private final List<Runnable> listeners = new ArrayList<>();

    public ControladorPedidos() {
        registrarPedido(new PedidoComida(101, "Av. Italia 456", 2.7));
        registrarPedido(new PedidoEncomienda(102, "Av. Independencia 123", 6, 12, true));
        registrarPedido(new PedidoExpress(103, "Av. Apoquindo 1500", 8));
    }

    public void addCambioListener(Runnable listener) {
        listeners.add(listener);
    }

    public void notificarCambio() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }

    public void registrarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }
        if (existeId(pedido.getIdPedido())) {
            throw new IllegalArgumentException("Ya existe un pedido con el ID " + pedido.getIdPedido());
        }
        pedidos.add(pedido);
        notificarCambio();
    }

    public boolean existeId(int idPedido) {
        return buscarPorId(idPedido) != null;
    }

    public Pedido buscarPorId(int idPedido) {
        for (Pedido pedido : pedidos) {
            if (pedido.getIdPedido() == idPedido) {
                return pedido;
            }
        }
        return null;
    }

    public void cancelarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Debe seleccionar un pedido");
        }
        pedido.cancelar();
        notificarCambio();
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }
}
