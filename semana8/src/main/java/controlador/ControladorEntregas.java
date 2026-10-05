package controlador;

import dao.EntregaDAO;
import dao.PedidoDAO;
import modelo.CargaRepartidor;
import modelo.Entrega;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.Repartidor;
import tareas.TareaEntrega;

import javax.swing.JTextArea;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador de entregas.
 * Une un pedido con un repartidor, guarda la fila en MySQL
 * y puede lanzar el hilo que simula el viaje.
 */
public class ControladorEntregas {

    private final ControladorPedidos controladorPedidos;
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final EntregaDAO entregaDAO = new EntregaDAO();
    private final JTextArea txtActividad;
    private final List<Runnable> listeners = new ArrayList<>();

    /**
     * @param controladorPedidos para refrescar también la tabla de pedidos
     * @param txtActividad área de texto de la ventana principal
     */
    public ControladorEntregas(ControladorPedidos controladorPedidos, JTextArea txtActividad) {
        this.controladorPedidos = controladorPedidos;
        this.txtActividad = txtActividad;
    }

    /** Registra una ventana para recargar la tabla de entregas. */
    public void addCambioListener(Runnable listener) {
        listeners.add(listener);
    }

    /** Quita el listener al cerrar la ventana. */
    public void removeCambioListener(Runnable listener) {
        listeners.remove(listener);
    }

    /** Recarga entregas y también los pedidos (cambia el estado). */
    public void notificarCambio() {
        for (Runnable listener : listeners) {
            listener.run();
        }
        controladorPedidos.notificarCambio();
    }

    /**
     * INSERT: registra la entrega.
     * Si el pedido estaba pendiente, lo pasa a EN_REPARTO.
     */
    public void create(Entrega entrega) {
        if (entrega == null) {
            throw new IllegalArgumentException("La entrega no puede ser nula");
        }
        entregaDAO.create(entrega);
        Pedido pedido = pedidoDAO.buscarPorId(entrega.getIdPedido());
        if (pedido != null && pedido.getEstado() == EstadoPedido.RESERVADO) {
            pedido.setEstado(EstadoPedido.ASIGNADO);
            pedidoDAO.update(pedido);
        }
        notificarCambio();
    }

    /** SELECT: lista todas las entregas. */
    public List<Entrega> readAll() {
        return entregaDAO.readAll();
    }

    /**
     * SELECT filtrado. id 0 significa “todos”.
     * Sirve para listar por pedido o por repartidor.
     */
    public List<Entrega> filtrar(int idPedido, int idRepartidor) {
        return entregaDAO.filtrar(idPedido, idRepartidor);
    }

    /** Cola del día: libres primero, después los que llevan menos pedidos. */
    public List<CargaRepartidor> listarCargaHoy() {
        return entregaDAO.listarCargaHoy();
    }

    /** UPDATE: cambia pedido, repartidor, fecha u hora. */
    public void update(Entrega entrega) {
        if (entrega == null) {
            throw new IllegalArgumentException("Debe seleccionar una entrega");
        }
        entregaDAO.update(entrega);
        notificarCambio();
    }

    /** DELETE: elimina la entrega y deja el pedido otra vez en PENDIENTE. */
    public void delete(int id) {
        Pedido pedido = null;
        for (Entrega entrega : entregaDAO.readAll()) {
            if (entrega.getId() == id) {
                pedido = pedidoDAO.buscarPorId(entrega.getIdPedido());
                break;
            }
        }
        entregaDAO.delete(id);
        if (pedido != null && pedido.getEstado() != EstadoPedido.DESPACHADO) {
            pedido.setEstado(EstadoPedido.RESERVADO);
            pedido.setNombreRepartidor("Sin asignar");
            pedidoDAO.actualizarEstado(pedido);
        }
        notificarCambio();
    }

    /**
     * Asigna un repartidor al pedido y guarda la entrega (sin iniciar el hilo).
     */
    public void asignar(Pedido pedido, Repartidor repartidor) {
        if (pedido == null) {
            throw new IllegalArgumentException("Debe seleccionar un pedido");
        }
        if (repartidor == null) {
            throw new IllegalArgumentException("Debe seleccionar un repartidor");
        }
        entregaDAO.create(new Entrega(
                pedido.getIdPedido(),
                repartidor.getId(),
                java.time.LocalDate.now(),
                java.time.LocalTime.now()));
        pedido.setNombreRepartidor(repartidor.getNombre());
        pedido.setEstado(EstadoPedido.ASIGNADO);
        pedidoDAO.actualizarEstado(pedido);
        txtActividad.append("Pedido #" + pedido.formatearId()
                + " asignado a " + repartidor.getNombre() + "\n");
        notificarCambio();
    }

    /**
     * Crea la entrega, asigna el pedido si falta y arranca TareaEntrega.
     * El hilo escribe el progreso en txtActividad.
     */
    public void iniciarEntrega(Pedido pedido, Repartidor repartidor) {
        if (pedido == null) {
            throw new IllegalArgumentException("Debe seleccionar un pedido");
        }
        if (repartidor == null) {
            throw new IllegalArgumentException("Debe seleccionar un repartidor");
        }
        if (pedido.getEstado() == EstadoPedido.RESERVADO) {
            pedido.asignarRepartidor(repartidor.getNombre());
            pedidoDAO.update(pedido);
        }
        Entrega entrega = new Entrega(
                pedido.getIdPedido(),
                repartidor.getId(),
                java.time.LocalDate.now(),
                java.time.LocalTime.now());
        entregaDAO.create(entrega);
        txtActividad.append("Entrega registrada: pedido #" + pedido.formatearId()
                + " → " + repartidor.getNombre() + "\n");
        Repartidor hiloRepartidor = new Repartidor(repartidor.getId(), repartidor.getNombre());
        hiloRepartidor.agregarPedido(pedido);
        txtActividad.append("Hilo iniciado: " + repartidor.getNombre()
                + " con pedido #" + pedido.formatearId() + "\n");
        TareaEntrega tarea = new TareaEntrega(hiloRepartidor, txtActividad, this);
        Thread hilo = new Thread(tarea, "Entrega-" + pedido.getIdPedido());
        hilo.setDaemon(true);
        hilo.start();
        notificarCambio();
    }

    /**
     * Lanza un hilo por cada repartidor que tenga pedidos en EN_REPARTO.
     */
    public void simularEntregas() {
        Map<String, List<Pedido>> porRepartidor = new LinkedHashMap<>();
        for (Pedido pedido : controladorPedidos.readAll()) {
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
            String nombre = entrada.getKey() == null || entrada.getKey().isBlank()
                    ? "Sin asignar" : entrada.getKey();
            Repartidor hiloRepartidor = new Repartidor(nombre);
            for (Pedido pedido : entrada.getValue()) {
                hiloRepartidor.agregarPedido(pedido);
            }
            txtActividad.append("Hilo iniciado: " + nombre
                    + " (" + entrada.getValue().size() + " pedido(s))\n");
            TareaEntrega tarea = new TareaEntrega(hiloRepartidor, txtActividad, this);
            Thread hilo = new Thread(tarea, "Repartidor-" + nombre);
            hilo.setDaemon(true);
            hilo.start();
        }
        txtActividad.setCaretPosition(txtActividad.getDocument().getLength());
    }

    /** Lo llama el hilo al terminar: deja el pedido en ENTREGADO. */
    public void marcarDespachado(Pedido pedido) {
        pedidoDAO.actualizarEstado(pedido);
        javax.swing.SwingUtilities.invokeLater(this::notificarCambio);
    }
}
