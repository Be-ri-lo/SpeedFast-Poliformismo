package vista;

import controlador.ControladorPedidos;
import modelo.Pedido;
import modelo.PedidoComida;
import modelo.PedidoEncomienda;
import modelo.PedidoExpress;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Frame;
import java.awt.GridLayout;

/**
 * Formulario para registrar un pedido.
 */
public class VentanaRegistroPedido extends JFrame {

    private final Frame menuPrincipal;
    private final ControladorPedidos controladorPedidos;
    private final JTextField txtId = new JTextField();
    private final JTextField txtDireccion = new JTextField();
    private final JTextField txtDistancia = new JTextField();
    private final JComboBox<String> cboTipo = new JComboBox<>(new String[]{
            "Comida", "Encomienda", "Express"
    });
    private final JTextField txtPeso = new JTextField();
    private final JCheckBox chkFragil = new JCheckBox("Contenido frágil");
    private final JLabel lblPeso = new JLabel("Peso (kg):");

    public VentanaRegistroPedido(Frame menuPrincipal, ControladorPedidos controladorPedidos) {
        super("SpeedFast - Registrar pedido");
        this.menuPrincipal = menuPrincipal;
        this.controladorPedidos = controladorPedidos;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(450, 380);
        setLocationRelativeTo(null);
        crearComponentes();
    }

    private void crearComponentes() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JButton btnGuardar = new JButton("Guardar pedido");
        JButton btnLimpiar = new JButton("Limpiar");

        panel.add(new JLabel("ID:"));
        panel.add(txtId);
        panel.add(new JLabel("Dirección:"));
        panel.add(txtDireccion);
        panel.add(new JLabel("Distancia (km):"));
        panel.add(txtDistancia);
        panel.add(new JLabel("Tipo:"));
        panel.add(cboTipo);
        panel.add(lblPeso);
        panel.add(txtPeso);
        panel.add(new JLabel(""));
        panel.add(chkFragil);
        panel.add(new JLabel(""));
        panel.add(btnGuardar);
        panel.add(new JLabel(""));
        panel.add(btnLimpiar);

        add(panel);

        btnGuardar.addActionListener(e -> guardar());
        btnLimpiar.addActionListener(e -> limpiar());
        cboTipo.addActionListener(e -> actualizarCamposExtra());
        actualizarCamposExtra();
    }

    private void actualizarCamposExtra() {
        boolean encomienda = "Encomienda".equals(cboTipo.getSelectedItem());
        lblPeso.setEnabled(encomienda);
        txtPeso.setEnabled(encomienda);
        chkFragil.setEnabled(encomienda);
    }

    private void guardar() {
        try {
            String idTexto = txtId.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String distanciaTexto = txtDistancia.getText().trim().replace(',', '.');

            if (idTexto.isEmpty() || direccion.isEmpty() || distanciaTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debe completar ID, dirección y distancia.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idTexto);
            double distancia = Double.parseDouble(distanciaTexto);
            String tipo = (String) cboTipo.getSelectedItem();

            Pedido pedido;
            if ("Encomienda".equals(tipo)) {
                String pesoTexto = txtPeso.getText().trim().replace(',', '.');
                if (pesoTexto.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Debe ingresar el peso de la encomienda.",
                            "Validación", JOptionPane.WARNING_MESSAGE);
                    txtPeso.requestFocus();
                    return;
                }
                double peso = Double.parseDouble(pesoTexto);
                pedido = new PedidoEncomienda(id, direccion, distancia, peso, chkFragil.isSelected());
            } else if ("Express".equals(tipo)) {
                pedido = new PedidoExpress(id, direccion, distancia);
            } else {
                pedido = new PedidoComida(id, direccion, distancia);
            }

            controladorPedidos.registrarPedido(pedido);
            JOptionPane.showMessageDialog(this, "Pedido registrado correctamente.");
            dispose();
            if (menuPrincipal != null) {
                menuPrincipal.toFront();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "ID, distancia y peso deben ser números.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "No se pudo guardar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {
        txtId.setText("");
        txtDireccion.setText("");
        txtDistancia.setText("");
        txtPeso.setText("");
        chkFragil.setSelected(false);
        cboTipo.setSelectedIndex(0);
        actualizarCamposExtra();
    }
}
