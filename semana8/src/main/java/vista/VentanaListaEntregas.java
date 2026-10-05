package vista;

import controlador.ControladorEntregas;
import controlador.ControladorPedidos;
import controlador.ControladorRepartidores;
import modelo.CargaRepartidor;
import modelo.Entrega;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.Repartidor;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entregas: asignar un repartidor a un pedido PENDIENTE,
 * además de editar o eliminar una entrega ya creada.
 * A la derecha se ve la cola del día (libres y pedidos de hoy).
 */
public class VentanaListaEntregas extends JFrame {

    private static final DateTimeFormatter FMT_FECHA = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter FMT_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final ControladorEntregas controladorEntregas;
    private final ControladorPedidos controladorPedidos;
    private final ControladorRepartidores controladorRepartidores;
    private final DefaultTableModel modeloTabla;
    private final JTable tabla;
    private final DefaultTableModel modeloCola;
    private final JTable tablaCola;
    private final JComboBox<Object> cboPedidoFiltro = new JComboBox<>();
    private final JComboBox<Object> cboRepartidorFiltro = new JComboBox<>();

    public VentanaListaEntregas(ControladorEntregas controladorEntregas,
                                ControladorPedidos controladorPedidos,
                                ControladorRepartidores controladorRepartidores) {
        this.controladorEntregas = controladorEntregas;
        this.controladorPedidos = controladorPedidos;
        this.controladorRepartidores = controladorRepartidores;
        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Pedido", "Dirección", "Repartidor", "Estado", "Fecha", "Hora"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Estilos.ocultarColumna(tabla, 0);
        Estilos.ocultarColumna(tabla, 1);
        modeloCola = new DefaultTableModel(
                new String[]{"Repartidor", "Pedidos hoy", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tablaCola = new JTable(modeloCola);
        tablaCola.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setTitle("SpeedFast - Entregas");
        setSize(1080, 480);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtros.add(new JLabel("Pedido:"));
        filtros.add(cboPedidoFiltro);
        filtros.add(new JLabel("Repartidor:"));
        filtros.add(cboRepartidorFiltro);
        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.addActionListener(e -> cargar());
        filtros.add(btnFiltrar);
        add(filtros, BorderLayout.NORTH);

        JPanel cola = new JPanel(new BorderLayout(6, 6));
        cola.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        cola.add(new JLabel("Cola del día (libres primero)"), BorderLayout.NORTH);
        JScrollPane scrollCola = new JScrollPane(tablaCola);
        scrollCola.setPreferredSize(new Dimension(280, 200));
        cola.add(scrollCola, BorderLayout.CENTER);

        JPanel centro = new JPanel(new BorderLayout(8, 8));
        centro.add(new JScrollPane(tabla), BorderLayout.CENTER);
        centro.add(cola, BorderLayout.EAST);
        add(centro, BorderLayout.CENTER);

        JButton btnAsignar = new JButton("Asignar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRecargar = new JButton("Recargar");
        btnAsignar.addActionListener(e -> asignar());
        btnEditar.addActionListener(e -> editar());
        btnEliminar.addActionListener(e -> eliminar());
        btnRecargar.addActionListener(e -> mostrarTodas());

        JPanel sur = new JPanel();
        sur.add(btnAsignar);
        sur.add(btnEditar);
        sur.add(btnEliminar);
        sur.add(btnRecargar);
        add(sur, BorderLayout.SOUTH);

        mostrarTodas();
        Runnable refrescar = () -> SwingUtilities.invokeLater(() -> {
            refrescarCombos();
            cargar();
        });
        controladorEntregas.addCambioListener(refrescar);
        controladorPedidos.addCambioListener(refrescar);
        controladorRepartidores.addCambioListener(refrescar);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                controladorEntregas.removeCambioListener(refrescar);
                controladorPedidos.removeCambioListener(refrescar);
                controladorRepartidores.removeCambioListener(refrescar);
            }
        });
    }

    /** Recarga combos y muestra pendientes + entregas (filtro en Todos). */
    private void mostrarTodas() {
        refrescarCombos();
        if (cboPedidoFiltro.getItemCount() > 0) {
            cboPedidoFiltro.setSelectedIndex(0);
        }
        if (cboRepartidorFiltro.getItemCount() > 0) {
            cboRepartidorFiltro.setSelectedIndex(0);
        }
        cargar();
    }

    private void refrescarCombos() {
        int idPedidoSel = extraerIdPedido(cboPedidoFiltro.getSelectedItem());
        int idRepartidorSel = extraerIdRepartidor(cboRepartidorFiltro.getSelectedItem());
        cboPedidoFiltro.removeAllItems();
        cboPedidoFiltro.addItem("Todos");
        for (Pedido pedido : controladorPedidos.readAll()) {
            cboPedidoFiltro.addItem(pedido);
        }
        cboRepartidorFiltro.removeAllItems();
        cboRepartidorFiltro.addItem("Todos");
        for (Repartidor repartidor : controladorRepartidores.readAll()) {
            cboRepartidorFiltro.addItem(repartidor);
        }
        if (idPedidoSel > 0) {
            for (int i = 0; i < cboPedidoFiltro.getItemCount(); i++) {
                if (extraerIdPedido(cboPedidoFiltro.getItemAt(i)) == idPedidoSel) {
                    cboPedidoFiltro.setSelectedIndex(i);
                    break;
                }
            }
        }
        if (idRepartidorSel > 0) {
            for (int i = 0; i < cboRepartidorFiltro.getItemCount(); i++) {
                if (extraerIdRepartidor(cboRepartidorFiltro.getItemAt(i)) == idRepartidorSel) {
                    cboRepartidorFiltro.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void cargar() {
        modeloTabla.setRowCount(0);
        int idPedido = extraerIdPedido(cboPedidoFiltro.getSelectedItem());
        int idRepartidor = extraerIdRepartidor(cboRepartidorFiltro.getSelectedItem());
        try {
            Set<Integer> conEntrega = new HashSet<>();
            for (Entrega entrega : controladorEntregas.filtrar(idPedido, idRepartidor)) {
                Pedido pedido = controladorPedidos.buscarPorId(entrega.getIdPedido());
                String estado = pedido == null
                        ? "EN_REPARTO"
                        : VentanaListaPedidos.estadoBd(pedido.getEstado());
                modeloTabla.addRow(new Object[]{
                        entrega.getId(),
                        entrega.getIdPedido(),
                        entrega.getDireccionPedido(),
                        entrega.getNombreRepartidor(),
                        estado,
                        entrega.getFecha().format(FMT_FECHA),
                        entrega.getHora().format(FMT_HORA)
                });
                conEntrega.add(entrega.getIdPedido());
            }
            // Los PENDIENTE no tienen fila en entregas: se muestran para asignar.
            if (idRepartidor == 0) {
                for (Pedido pedido : controladorPedidos.readAll()) {
                    if (idPedido > 0 && pedido.getIdPedido() != idPedido) {
                        continue;
                    }
                    if (conEntrega.contains(pedido.getIdPedido())) {
                        continue;
                    }
                    if (pedido.getEstado() == EstadoPedido.DESPACHADO) {
                        continue;
                    }
                    modeloTabla.addRow(new Object[]{
                            0,
                            pedido.getIdPedido(),
                            pedido.getDireccionEntrega(),
                            "Sin asignar",
                            "PENDIENTE",
                            "-",
                            "-"
                    });
                }
            }
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Base de datos", JOptionPane.ERROR_MESSAGE);
        }
        cargarCola();
    }

    /** Libres primero; si hay carga, el que lleva menos pedidos hoy. */
    private void cargarCola() {
        modeloCola.setRowCount(0);
        try {
            for (CargaRepartidor carga : controladorEntregas.listarCargaHoy()) {
                modeloCola.addRow(new Object[]{
                        carga.getRepartidor().getNombre(),
                        carga.getPedidosHoy(),
                        carga.getEstado()
                });
            }
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Registrar entrega: combos de pedido y repartidor cargados desde MySQL. */
    private void asignar() {
        JComboBox<Pedido> cboPedido = new JComboBox<>();
        Pedido preseleccionado = pedidoPendienteOpcional();
        for (Pedido pedido : controladorPedidos.readAll()) {
            if (pedido.getEstado() == EstadoPedido.DESPACHADO) {
                continue;
            }
            boolean yaAsignado = false;
            for (Entrega entrega : controladorEntregas.readAll()) {
                if (entrega.getIdPedido() == pedido.getIdPedido()) {
                    yaAsignado = true;
                    break;
                }
            }
            if (!yaAsignado) {
                cboPedido.addItem(pedido);
            }
        }
        JComboBox<Repartidor> cboRepartidor = new JComboBox<>();
        List<CargaRepartidor> cola = controladorEntregas.listarCargaHoy();
        for (CargaRepartidor carga : cola) {
            cboRepartidor.addItem(carga.getRepartidor());
        }
        if (cboPedido.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay pedidos pendientes para asignar.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (cboRepartidor.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Debe existir al menos un repartidor.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (preseleccionado != null) {
            for (int i = 0; i < cboPedido.getItemCount(); i++) {
                if (cboPedido.getItemAt(i).getIdPedido() == preseleccionado.getIdPedido()) {
                    cboPedido.setSelectedIndex(i);
                    break;
                }
            }
        }
        if (tablaCola.getSelectedRow() >= 0 && tablaCola.getSelectedRow() < cboRepartidor.getItemCount()) {
            cboRepartidor.setSelectedIndex(tablaCola.getSelectedRow());
        }
        int opcion = JOptionPane.showConfirmDialog(this, new Object[]{
                "Pedido:", cboPedido,
                "Repartidor:", cboRepartidor
        }, "Registrar entrega", JOptionPane.OK_CANCEL_OPTION);
        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }
        Pedido pedido = (Pedido) cboPedido.getSelectedItem();
        Repartidor repartidor = (Repartidor) cboRepartidor.getSelectedItem();
        if (pedido == null || repartidor == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar pedido y repartidor.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            controladorEntregas.asignar(pedido, repartidor);
            JOptionPane.showMessageDialog(this,
                    "Pedido #" + pedido.formatearId() + " asignado. Se guardó la entrega.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "No se pudo asignar", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Si hay una fila PENDIENTE seleccionada, se preelige en el combo. */
    private Pedido pedidoPendienteOpcional() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return null;
        }
        int idEntrega = (int) modeloTabla.getValueAt(fila, 0);
        if (idEntrega > 0) {
            return null;
        }
        int idPedido = (int) modeloTabla.getValueAt(fila, 1);
        return controladorPedidos.buscarPorId(idPedido);
    }

    private void editar() {
        Entrega actual = entregaSeleccionada("Editar");
        if (actual == null) {
            return;
        }
        JComboBox<Pedido> cboPedido = new JComboBox<>();
        for (Pedido pedido : controladorPedidos.readAll()) {
            cboPedido.addItem(pedido);
        }
        JComboBox<Repartidor> cboRepartidor = new JComboBox<>();
        for (Repartidor repartidor : controladorRepartidores.readAll()) {
            cboRepartidor.addItem(repartidor);
        }
        if (cboPedido.getItemCount() == 0 || cboRepartidor.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Debe existir al menos un pedido y un repartidor.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        seleccionarPedido(cboPedido, actual.getIdPedido());
        seleccionarRepartidor(cboRepartidor, actual.getIdRepartidor());
        JTextField txtFecha = new JTextField(actual.getFecha().format(FMT_FECHA));
        JTextField txtHora = new JTextField(actual.getHora().format(FMT_HORA));

        int opcion = JOptionPane.showConfirmDialog(this, new Object[]{
                "Pedido:", cboPedido,
                "Repartidor:", cboRepartidor,
                "Fecha (dd-MM-yyyy):", txtFecha,
                "Hora (HH:mm:ss):", txtHora
        }, "Editar entrega", JOptionPane.OK_CANCEL_OPTION);
        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }

        Pedido pedido = (Pedido) cboPedido.getSelectedItem();
        Repartidor repartidor = (Repartidor) cboRepartidor.getSelectedItem();
        if (pedido == null || repartidor == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar pedido y repartidor.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            actual.setIdPedido(pedido.getIdPedido());
            actual.setIdRepartidor(repartidor.getId());
            actual.setFecha(LocalDate.parse(txtFecha.getText().trim(), FMT_FECHA));
            actual.setHora(LocalTime.parse(txtHora.getText().trim(), FMT_HORA));
            controladorEntregas.update(actual);
            JOptionPane.showMessageDialog(this, "Entrega actualizada.");
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Use fecha dd-MM-yyyy y hora HH:mm:ss.",
                    "Validación", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        Entrega actual = entregaSeleccionada("Eliminar");
        if (actual == null) {
            return;
        }
        String[] opciones = {"Sí", "No"};
        int opcion = JOptionPane.showOptionDialog(this,
                "¿Eliminar la entrega del pedido seleccionado?",
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                null, opciones, opciones[1]);
        if (opcion != 0) {
            return;
        }
        try {
            controladorEntregas.delete(actual.getId());
            JOptionPane.showMessageDialog(this, "Entrega eliminada.");
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Entrega entregaSeleccionada(String accion) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una entrega.",
                    accion, JOptionPane.WARNING_MESSAGE);
            return null;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        if (id <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Ese pedido aún no tiene entrega. Use Asignar.",
                    accion, JOptionPane.WARNING_MESSAGE);
            return null;
        }
        int idPedido = (int) modeloTabla.getValueAt(fila, 1);
        String fecha = modeloTabla.getValueAt(fila, 5).toString();
        String hora = modeloTabla.getValueAt(fila, 6).toString();
        String nombreRepartidor = modeloTabla.getValueAt(fila, 3).toString();
        Entrega actual = new Entrega(id, idPedido, 0,
                LocalDate.parse(fecha, FMT_FECHA),
                LocalTime.parse(hora, FMT_HORA),
                modeloTabla.getValueAt(fila, 2).toString(),
                nombreRepartidor);
        for (Repartidor repartidor : controladorRepartidores.readAll()) {
            if (repartidor.getNombre().equals(nombreRepartidor)) {
                actual.setIdRepartidor(repartidor.getId());
                break;
            }
        }
        return actual;
    }

    private void seleccionarPedido(JComboBox<Pedido> combo, int id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getIdPedido() == id) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void seleccionarRepartidor(JComboBox<Repartidor> combo, int id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getId() == id) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private int extraerIdPedido(Object item) {
        return item instanceof Pedido pedido ? pedido.getIdPedido() : 0;
    }

    private int extraerIdRepartidor(Object item) {
        return item instanceof Repartidor repartidor ? repartidor.getId() : 0;
    }
}
