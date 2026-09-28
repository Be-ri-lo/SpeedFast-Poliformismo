package vista;

import controlador.ControladorEntregas;
import modelo.Entrega;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.time.format.DateTimeFormatter;

/** Listado de entregas. Queda vacío hasta asignar o iniciar una entrega. */
public class VentanaListaEntregas extends JFrame {

    private final ControladorEntregas controladorEntregas;
    private final DefaultTableModel modeloTabla;
    private final JTable tabla;

    public VentanaListaEntregas(ControladorEntregas controladorEntregas) {
        this.controladorEntregas = controladorEntregas;
        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Pedido", "Dirección", "Repartidor", "Fecha", "Hora"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        setTitle("SpeedFast - Entregas");
        setSize(720, 360);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargar());
        JPanel sur = new JPanel();
        sur.add(btnActualizar);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(sur, BorderLayout.SOUTH);
        cargar();
    }

    private void cargar() {
        modeloTabla.setRowCount(0);
        try {
            DateTimeFormatter fecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter hora = DateTimeFormatter.ofPattern("HH:mm:ss");
            for (Entrega entrega : controladorEntregas.listarEntregas()) {
                modeloTabla.addRow(new Object[]{
                        entrega.getId(),
                        String.format("%03d", entrega.getIdPedido()),
                        entrega.getDireccionPedido(),
                        entrega.getNombreRepartidor(),
                        entrega.getFecha().format(fecha),
                        entrega.getHora().format(hora)
                });
            }
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Base de datos", JOptionPane.ERROR_MESSAGE);
        }
        modeloTabla.fireTableDataChanged();
    }
}
