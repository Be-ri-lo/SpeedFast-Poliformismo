package controlador;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import modelo.Entrega;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.Repartidor;
import tareas.TareaEntrega;

import javax.swing.JTextArea;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Asigna entregas, las guarda en MySQL y lanza el hilo de simulación.
 */
public class ControladorEntregas {

    private final ControladorPedidos controladorPedidos;
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final EntregaDAO entregaDAO = new EntregaDAO();
    private final RepartidorDAO repartidorDAO = new RepartidorDAO();
    private final JTextArea txtActividad;

    public ControladorEntregas(ControladorPedidos controladorPedidos, JTextArea txtActividad) {
        this.controladorPedidos = controladorPedidos;
        this.txtActividad = txtActividad;
    }

    public void iniciarEntrega(Pedido pedido, Repartidor repartidor) {
        if (pedido == null) {
            throw new IllegalArgumentException("Debe seleccionar un pedido");
        }
        if (repartidor == null) {
            throw new IllegalArgumentException("Debe seleccionar un repartidor");
        }
        if (pedido.getEstado() != EstadoPedido.ASIGNADO) {
            pedido.asignarRepartidor(repartidor.getNombre());
            pedidoDAO.actualizarEstado(pedido);
        }

        registrarEntrega(pedido, repartidor);
        Repartidor hiloRepartidor = new Repartidor(repartidor.getId(), repartidor.getNombre());
        hiloRepartidor.agregarPedido(pedido);
        txtActividad.append("Hilo iniciado: " + repartidor.getNombre()
                + " con pedido #" + pedido.formatearId() + "\n");
        lanzarTarea(hiloRepartidor, "Entrega-" + pedido.getIdPedido());
        controladorPedidos.notificarCambio();
    }

    public void simularEntregas() {
        Map<String, List<Pedido>> porRepartidor = new LinkedHashMap<>();
        for (Pedido pedido : controladorPedidos.getPedidos()) {
            if (pedido.getEstado() == EstadoPedido.ASIGNADO) {
                porRepartidor.computeIfAbsent(pedido.getRepartidor(), clave -> new ArrayList<>())
                        .add(pedido);
            }
        }
        if (porRepartidor.isEmpty()) {
            throw new IllegalStateException("Debe asignar al menos un pedido antes de simular.");
        }

        txtActividad.append("--- Nueva simulación ---\n");
        for (Map.Entry<String, List<Pedido>> entrada : porRepartidor.entrySet()) {
            Repartidor repartidor = repartidorDAO.buscarPorNombre(entrada.getKey());
            if (repartidor == null) {
                continue;
            }
            Repartidor hiloRepartidor = new Repartidor(repartidor.getId(), repartidor.getNombre());
            for (Pedido pedido : entrada.getValue()) {
                registrarEntrega(pedido, repartidor);
                hiloRepartidor.agregarPedido(pedido);
            }
            txtActividad.append("Hilo iniciado: " + entrada.getKey()
                    + " (" + entrada.getValue().size() + " pedido(s))\n");
            lanzarTarea(hiloRepartidor, "Repartidor-" + entrada.getKey());
        }
        controladorPedidos.notificarCambio();
    }

    public List<Entrega> listarEntregas() {
        return entregaDAO.listarTodos();
    }

    public void marcarDespachado(Pedido pedido) {
        pedidoDAO.actualizarEstado(pedido);
        controladorPedidos.notificarCambio();
    }

    private void registrarEntrega(Pedido pedido, Repartidor repartidor) {
        Entrega entrega = new Entrega(
                pedido.getIdPedido(),
                repartidor.getId(),
                LocalDate.now(),
                LocalTime.now());
        entregaDAO.guardar(entrega);
        txtActividad.append("Entrega registrada en BD: pedido #" + pedido.formatearId()
                + " → " + repartidor.getNombre() + "\n");
    }

    private void lanzarTarea(Repartidor repartidor, String nombreHilo) {
        TareaEntrega tarea = new TareaEntrega(repartidor, txtActividad, this);
        Thread hilo = new Thread(tarea, nombreHilo);
        hilo.setDaemon(true);
        hilo.start();
    }
}
