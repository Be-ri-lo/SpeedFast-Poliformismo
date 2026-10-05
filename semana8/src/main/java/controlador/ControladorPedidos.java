package controlador;

import dao.PedidoDAO;
import modelo.Pedido;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador de pedidos.
 * La vista llama aquí; esta clase valida y delega el SQL a PedidoDAO.
 * Después de create, update o delete avisa a las ventanas abiertas
 * para que recarguen la JTable.
 */
public class ControladorPedidos {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final List<Runnable> listeners = new ArrayList<>();

    /** Registra una ventana para que se actualice cuando cambien los pedidos. */
    public void addCambioListener(Runnable listener) {
        listeners.add(listener);
    }

    /** Quita el listener al cerrar la ventana. */
    public void removeCambioListener(Runnable listener) {
        listeners.remove(listener);
    }

    /** Recarga las tablas de pedidos que estén abiertas. */
    public void notificarCambio() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }

    /** INSERT: guarda un pedido nuevo en MySQL. */
    public void create(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }
        pedidoDAO.create(pedido);
        notificarCambio();
    }

    /** SELECT: lista todos los pedidos. */
    public List<Pedido> readAll() {
        return pedidoDAO.readAll();
    }

    /**
     * SELECT con filtro opcional.
     * tipo o estado en null significa “todos”.
     */
    public List<Pedido> filtrar(String tipo, String estado) {
        return pedidoDAO.filtrar(tipo, estado);
    }

    /** UPDATE: modifica un pedido ya existente. */
    public void update(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Debe seleccionar un pedido");
        }
        pedidoDAO.update(pedido);
        notificarCambio();
    }

    /** DELETE: borra el pedido por id. Falla si tiene entregas asociadas. */
    public void delete(int id) {
        pedidoDAO.delete(id);
        notificarCambio();
    }

    /** Busca un pedido por id. Devuelve null si no existe. */
    public Pedido buscarPorId(int idPedido) {
        return pedidoDAO.buscarPorId(idPedido);
    }
}
