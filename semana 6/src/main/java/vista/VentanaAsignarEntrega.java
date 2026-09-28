package vista;

import controlador.ControladorEntregas;
import controlador.ControladorPedidos;
import controlador.ControladorRepartidores;
import modelo.Pedido;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

/**
 * Asignación de repartidor e inicio de entrega.
 */
public class VentanaAsignarEntrega extends JFrame {

    private final ControladorPedidos controladorPedidos;
    private final ControladorRepartidores controladorRepartidores;
    private final ControladorEntregas controladorEntregas;
    private final DefaultTableModel modeloTabla;
    private final JTable tablaPedidos;
    private final JComboBox<String> cboRepartidor;

    public VentanaAsignarEntrega(ControladorPedidos controladorPedidos,
                                 ControladorRepartidores controladorRepartidores,
                                 ControladorEntregas controladorEntregas) {
        this.controladorPedidos = controladorPedidos;
        this.controladorRepartidores = controladorRepartidores;
        this.controladorEntregas = controladorEntregas;
        this.cboRepartidor = new JComboBox<>(controladorRepartidores.getNombres());
        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Tipo", "Dirección", "Repartidor", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tablaPedidos = new JTable(modeloTabla);
        configurarVentana();
        crearComponentes();
        cargarPedidos();
        controladorPedidos.addCambioListener(() -> SwingUtilities.invokeLater(this::cargarPedidos));
    }

    private void configurarVentana() {
        setTitle("SpeedFast - Asignar repartidor / Iniciar entrega");
        setSize(760, 420);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void crearComponentes() {
        setLayout(new BorderLayout(10, 10));
        tablaPedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tablaPedidos), BorderLayout.CENTER);

        JButton btnAsignar = new JButton("Asignar repartidor");
        JButton btnIniciar = new JButton("Iniciar entrega");
        JButton btnCancelar = new JButton("Cancelar pedido");

        btnAsignar.addActionListener(e -> asignar());
        btnIniciar.addActionListener(e -> iniciar());
        btnCancelar.addActionListener(e -> cancelar());

        JPanel sur = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        sur.add(new JLabel("Repartidor:"));
        sur.add(cboRepartidor);
        sur.add(btnAsignar);
        sur.add(btnIniciar);
        sur.add(btnCancelar);
        add(sur, BorderLayout.SOUTH);
    }

    private void cargarPedidos() {
        int filaSeleccionada = tablaPedidos.getSelectedRow();
        modeloTabla.setRowCount(0);
        for (Pedido pedido : controladorPedidos.getPedidos()) {
            modeloTabla.addRow(new Object[]{
                    pedido.formatearId(),
                    pedido.getTipo(),
                    pedido.getDireccionEntrega(),
                    pedido.getRepartidor(),
                    pedido.getEstado().getEtiqueta()
            });
        }
        if (filaSeleccionada >= 0 && filaSeleccionada < modeloTabla.getRowCount()) {
            tablaPedidos.setRowSelectionInterval(filaSeleccionada, filaSeleccionada);
        }
    }

    private Pedido pedidoSeleccionado() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido de la tabla.",
                    "Selección requerida", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        String idTexto = modeloTabla.getValueAt(fila, 0).toString();
        return controladorPedidos.buscarPorId(Integer.parseInt(idTexto));
    }

    private void asignar() {
        Pedido pedido = pedidoSeleccionado();
        if (pedido == null) {
            return;
        }
        try {
            String nombre = (String) cboRepartidor.getSelectedItem();
            controladorRepartidores.asignarPedido(pedido, nombre);
            controladorPedidos.notificarCambio();
            JOptionPane.showMessageDialog(this,
                    "El pedido #" + pedido.formatearId() + " quedó asignado a " + nombre + ".");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "No se pudo asignar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void iniciar() {
        Pedido pedido = pedidoSeleccionado();
        if (pedido == null) {
            return;
        }
        try {
            controladorEntregas.iniciarEntrega(pedido);
            JOptionPane.showMessageDialog(this,
                    "La entrega del pedido #" + pedido.formatearId() + " comenzó.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "No se pudo iniciar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelar() {
        Pedido pedido = pedidoSeleccionado();
        if (pedido == null) {
            return;
        }
        String[] opciones = {"Sí", "No"};
        int opcion = JOptionPane.showOptionDialog(this,
                "¿Cancelar el pedido #" + pedido.formatearId() + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, opciones, opciones[0]);
        if (opcion != 0) {
            return;
        }
        try {
            controladorPedidos.cancelarPedido(pedido);
            JOptionPane.showMessageDialog(this, "Pedido cancelado.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "No se pudo cancelar", JOptionPane.ERROR_MESSAGE);
        }
    }
}
