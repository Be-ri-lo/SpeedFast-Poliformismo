package controlador;

import dao.PedidoDAO;
import modelo.Pedido;

import java.util.ArrayList;
import java.util.List;

/**
 * Recibe las acciones de la GUI de pedidos y las pasa al PedidoDAO.
 */
public class ControladorPedidos {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final List<Runnable> listeners = new ArrayList<>();

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
        pedidoDAO.guardar(pedido);
        notificarCambio();
    }

    public void actualizarEstado(Pedido pedido) {
        pedidoDAO.actualizarEstado(pedido);
        notificarCambio();
    }

    public Pedido buscarPorId(int idPedido) {
        return pedidoDAO.buscarPorId(idPedido);
    }

    public void cancelarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Debe seleccionar un pedido");
        }
        pedido.cancelar();
        pedidoDAO.actualizarEstado(pedido);
        notificarCambio();
    }

    public List<Pedido> getPedidos() {
        return pedidoDAO.listarTodos();
    }
}
