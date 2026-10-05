package controlador;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import modelo.Entrega;
import modelo.Pedido;
import modelo.Repartidor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Recibe las acciones de la GUI de repartidores y las pasa a los DAO.
 */
public class ControladorRepartidores {

    private final RepartidorDAO repartidorDAO = new RepartidorDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final EntregaDAO entregaDAO = new EntregaDAO();

    public void registrarRepartidor(Repartidor repartidor) {
        if (repartidor == null) {
            throw new IllegalArgumentException("El repartidor no puede ser nulo");
        }
        repartidorDAO.guardar(repartidor);
    }

    public List<Repartidor> listarTodos() {
        return repartidorDAO.listarTodos();
    }

    public Repartidor[] getRepartidores() {
        List<Repartidor> lista = repartidorDAO.listarTodos();
        return lista.toArray(new Repartidor[0]);
    }

    public void asignarPedido(Pedido pedido, Repartidor repartidor) {
        if (pedido == null) {
            throw new IllegalArgumentException("Debe seleccionar un pedido");
        }
        if (repartidor == null) {
            throw new IllegalArgumentException("Debe seleccionar un repartidor");
        }
        pedido.asignarRepartidor(repartidor.getNombre());
        pedidoDAO.actualizarEstado(pedido);
        Entrega entrega = new Entrega(
                pedido.getIdPedido(),
                repartidor.getId(),
                LocalDate.now(),
                LocalTime.now());
        entregaDAO.guardar(entrega);
    }
}
