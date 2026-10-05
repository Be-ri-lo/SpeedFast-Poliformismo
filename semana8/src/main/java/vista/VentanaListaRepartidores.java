package vista;

import controlador.ControladorRepartidores;
import modelo.Repartidor;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * CRUD de repartidores: listar, registrar, editar y eliminar.
 */
public class VentanaListaRepartidores extends JFrame {

    private final ControladorRepartidores controlador;
    private final DefaultTableModel modeloTabla;
    private final JTable tabla;

    public VentanaListaRepartidores(ControladorRepartidores controlador) {
        this.controlador = controlador;
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Estilos.ocultarColumna(tabla, 0);
        setTitle("SpeedFast - Repartidores");
        setSize(560, 380);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JButton btnNuevo = new JButton("Nuevo");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRecargar = new JButton("Recargar");
        btnNuevo.addActionListener(e -> nuevo());
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
        try {
            for (Repartidor repartidor : controlador.readAll()) {
                modeloTabla.addRow(new Object[]{repartidor.getId(), repartidor.getNombre()});
            }
        } catch (IllegalStateException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void nuevo() {
        JTextField txtNombre = new JTextField();
        int opcion = JOptionPane.showConfirmDialog(this,
                new Object[]{"Nombre:", txtNombre},
                "Registrar repartidor", JOptionPane.OK_CANCEL_OPTION);
        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            String nombre = ValidacionFormulario.nombreRepartidor(txtNombre.getText());
            controlador.create(new Repartidor(nombre));
            JOptionPane.showMessageDialog(this, "Repartidor registrado.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalStateException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void editar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un repartidor.",
                    "Editar", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        JTextField txtNombre = new JTextField(modeloTabla.getValueAt(fila, 1).toString());
        int opcion = JOptionPane.showConfirmDialog(this,
                new Object[]{"Nombre:", txtNombre},
                "Editar repartidor ID " + id, JOptionPane.OK_CANCEL_OPTION);
        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            String nombre = ValidacionFormulario.nombreRepartidor(txtNombre.getText());
            controlador.update(new Repartidor(id, nombre));
            JOptionPane.showMessageDialog(this, "Repartidor actualizado.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalStateException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un repartidor.",
                    "Eliminar", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = modeloTabla.getValueAt(fila, 1).toString();
        String[] opciones = {"Sí", "No"};
        int opcion = JOptionPane.showOptionDialog(this,
                "¿Eliminar al repartidor " + nombre + " (ID " + id + ")?",
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                null, opciones, opciones[1]);
        if (opcion != 0) {
            return;
        }
        try {
            controlador.delete(id);
            JOptionPane.showMessageDialog(this, "Repartidor eliminado.");
        } catch (IllegalStateException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Base de datos", JOptionPane.ERROR_MESSAGE);
    }
}
