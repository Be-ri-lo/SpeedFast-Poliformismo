package vista;

import controlador.ControladorPedidos;
import modelo.Pedido;
import modelo.PedidoEncomienda;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;

/**
 * Listado de pedidos en una JTable.
 */
public class VentanaListaPedidos extends JFrame {

    private final ControladorPedidos controladorPedidos;
    private final DefaultTableModel modeloTabla;
    private final JTable tablaPedidos;

    public VentanaListaPedidos(ControladorPedidos controladorPedidos) {
        this.controladorPedidos = controladorPedidos;
        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Tipo", "Dirección", "Km", "Repartidor", "Estado", "Tiempo (min)", "Detalle"}, 0) {
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
        setTitle("SpeedFast - Pedidos");
        setSize(820, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void crearComponentes() {
        setLayout(new BorderLayout(10, 10));

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarPedidos());

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnActualizar);

        add(new JScrollPane(tablaPedidos), BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);
    }

    private void cargarPedidos() {
        modeloTabla.setRowCount(0);
        for (Pedido pedido : controladorPedidos.getPedidos()) {
            modeloTabla.addRow(new Object[]{
                    pedido.formatearId(),
                    pedido.getTipo(),
                    pedido.getDireccionEntrega(),
                    String.format("%.1f", pedido.getDistanciaKm()),
                    pedido.getRepartidor(),
                    pedido.getEstado().getEtiqueta(),
                    String.format("%.1f", pedido.calcularTiempoEntrega()),
                    detalle(pedido)
            });
        }
        modeloTabla.fireTableDataChanged();
        tablaPedidos.revalidate();
        tablaPedidos.repaint();
    }

    private String detalle(Pedido pedido) {
        if (pedido instanceof PedidoEncomienda encomienda) {
            return encomienda.getPeso() + " kg"
                    + (encomienda.isFragil() ? " · frágil" : "")
                    + (encomienda.esPesado() ? " · pesado" : "");
        }
        return "-";
    }
}
