package vista;

import controlador.ControladorPedidos;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.PedidoComida;
import modelo.PedidoEncomienda;
import modelo.PedidoExpress;

import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * CRUD de pedidos con filtros por tipo y estado.
 */
public class VentanaListaPedidos extends JFrame {

    private final ControladorPedidos controlador;
    private final DefaultTableModel modeloTabla;
    private final JTable tabla;
    private final JComboBox<String> cboTipoFiltro = new JComboBox<>(
            new String[]{"Todos", "COMIDA", "ENCOMIENDA", "EXPRESS"});
    private final JComboBox<String> cboEstadoFiltro = new JComboBox<>(
            new String[]{"Todos", "PENDIENTE", "EN_REPARTO", "ENTREGADO"});

    public VentanaListaPedidos(ControladorPedidos controlador) {
        this.controlador = controlador;
        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Tipo", "Dirección", "Km", "Estado", "Tiempo (min)", "Detalle"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Estilos.ocultarColumna(tabla, 0);
        setTitle("SpeedFast - Pedidos");
        setSize(880, 440);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtros.add(new JLabel("Tipo:"));
        filtros.add(cboTipoFiltro);
        filtros.add(new JLabel("Estado:"));
        filtros.add(cboEstadoFiltro);
        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.addActionListener(e -> cargar());
        filtros.add(btnFiltrar);
        add(filtros, BorderLayout.NORTH);

        JButton btnNuevo = new JButton("Nuevo");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRecargar = new JButton("Recargar");
        btnNuevo.addActionListener(e -> new VentanaRegistroPedido(this, controlador).setVisible(true));
        btnEditar.addActionListener(e -> editar());
        btnEliminar.addActionListener(e -> eliminar());
        btnRecargar.addActionListener(e -> cargar());

        JPanel sur = new JPanel();
        sur.add(btnNuevo);
        sur.add(btnEditar);
        sur.add(btnEliminar);
        sur.add(btnRecargar);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(sur, BorderLayout.SOUTH);

        cargar();
        Runnable refrescar = () -> SwingUtilities.invokeLater(this::cargar);
        controlador.addCambioListener(refrescar);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                controlador.removeCambioListener(refrescar);
            }
        });
    }

    private void cargar() {
        modeloTabla.setRowCount(0);
        String tipo = "Todos".equals(cboTipoFiltro.getSelectedItem()) ? null : (String) cboTipoFiltro.getSelectedItem();
        String estado = "Todos".equals(cboEstadoFiltro.getSelectedItem()) ? null : (String) cboEstadoFiltro.getSelectedItem();
        try {
            for (Pedido pedido : controlador.filtrar(tipo, estado)) {
                modeloTabla.addRow(new Object[]{
                        pedido.getIdPedido(),
                        pedido.getTipo().toUpperCase(),
                        pedido.getDireccionEntrega(),
                        String.format("%.1f", pedido.getDistanciaKm()),
                        estadoBd(pedido.getEstado()),
                        String.format("%.1f", pedido.calcularTiempoEntrega()),
                        detalle(pedido)
                });
            }
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un pedido.",
                    "Editar", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        Pedido actual = controlador.buscarPorId(id);
        if (actual == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el pedido.",
                    "Editar", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField txtDireccion = new JTextField(actual.getDireccionEntrega());
        JTextField txtKm = new JTextField(String.valueOf(actual.getDistanciaKm()));
        JComboBox<String> cboTipo = new JComboBox<>(new String[]{"COMIDA", "ENCOMIENDA", "EXPRESS"});
        cboTipo.setSelectedItem(actual.getTipo().toUpperCase());
        JComboBox<String> cboEstado = new JComboBox<>(new String[]{"PENDIENTE", "EN_REPARTO", "ENTREGADO"});
        cboEstado.setSelectedItem(estadoBd(actual.getEstado()));
        JTextField txtPeso = new JTextField();
        JCheckBox chkFragil = new JCheckBox("Frágil");
        if (actual instanceof PedidoEncomienda encomienda) {
            txtPeso.setText(String.valueOf(encomienda.getPeso()));
            chkFragil.setSelected(encomienda.isFragil());
        }

        int opcion = JOptionPane.showConfirmDialog(this, new Object[]{
                "Dirección:", txtDireccion,
                "Distancia (km):", txtKm,
                "Tipo:", cboTipo,
                "Estado:", cboEstado,
                "Peso (encomienda):", txtPeso,
                chkFragil
        }, "Editar pedido ID " + id, JOptionPane.OK_CANCEL_OPTION);
        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String direccion = ValidacionFormulario.direccion(txtDireccion.getText());
            double distancia = ValidacionFormulario.distancia(txtKm.getText());
            String tipo = (String) cboTipo.getSelectedItem();
            Pedido pedido = crearPedido(id, direccion, distancia, tipo, txtPeso.getText(), chkFragil.isSelected());
            pedido.setEstado(estadoModelo((String) cboEstado.getSelectedItem()));
            controlador.update(pedido);
            JOptionPane.showMessageDialog(this, "Pedido actualizado.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Distancia y peso deben ser números.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un pedido.",
                    "Eliminar", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String[] opciones = {"Sí", "No"};
        int opcion = JOptionPane.showOptionDialog(this,
                "¿Eliminar el pedido ID " + id + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                null, opciones, opciones[1]);
        if (opcion != 0) {
            return;
        }
        try {
            controlador.delete(id);
            JOptionPane.showMessageDialog(this, "Pedido eliminado.");
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    static Pedido crearPedido(int id, String direccion, double distancia, String tipo,
                              String pesoTexto, boolean fragil) {
        if ("ENCOMIENDA".equalsIgnoreCase(tipo)) {
            return new PedidoEncomienda(id, direccion, distancia,
                    ValidacionFormulario.peso(pesoTexto), fragil);
        }
        if ("EXPRESS".equalsIgnoreCase(tipo)) {
            return new PedidoExpress(id, direccion, distancia);
        }
        return new PedidoComida(id, direccion, distancia);
    }

    static String estadoBd(EstadoPedido estado) {
        return switch (estado) {
            case ASIGNADO -> "EN_REPARTO";
            case DESPACHADO -> "ENTREGADO";
            default -> "PENDIENTE";
        };
    }

    static EstadoPedido estadoModelo(String estado) {
        if (estado == null) {
            return EstadoPedido.RESERVADO;
        }
        return switch (estado.toUpperCase()) {
            case "EN_REPARTO" -> EstadoPedido.ASIGNADO;
            case "ENTREGADO" -> EstadoPedido.DESPACHADO;
            default -> EstadoPedido.RESERVADO;
        };
    }

    private String detalle(Pedido pedido) {
        if (pedido instanceof PedidoEncomienda encomienda) {
            return encomienda.getPeso() + " kg"
                    + (encomienda.isFragil() ? " · frágil" : "");
        }
        return "-";
    }
}
