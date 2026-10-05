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
 * Formulario para registrar un pedido. El id lo genera MySQL.
 */
public class VentanaRegistroPedido extends JFrame {

    private final Frame menuPrincipal;
    private final ControladorPedidos controladorPedidos;
    private final JTextField txtDireccion = new JTextField();
    private final JTextField txtDistancia = new JTextField();
    private final JComboBox<String> cboTipo = new JComboBox<>(new String[]{
            "COMIDA", "ENCOMIENDA", "EXPRESS"
    });
    private final JComboBox<String> cboEstado = new JComboBox<>(new String[]{
            "PENDIENTE", "EN_REPARTO", "ENTREGADO"
    });
    private final JTextField txtPeso = new JTextField();
    private final JCheckBox chkFragil = new JCheckBox("Contenido frágil");
    private final JLabel lblPeso = new JLabel("Peso (kg):");

    public VentanaRegistroPedido(Frame menuPrincipal, ControladorPedidos controladorPedidos) {
        super("SpeedFast - Registrar pedido");
        this.menuPrincipal = menuPrincipal;
        this.controladorPedidos = controladorPedidos;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(450, 400);
        setLocationRelativeTo(null);
        crearComponentes();
    }

    private void crearComponentes() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JButton btnGuardar = new JButton("Guardar pedido");
        JButton btnLimpiar = new JButton("Limpiar");

        panel.add(new JLabel("Dirección:"));
        panel.add(txtDireccion);
        panel.add(new JLabel("Distancia (km):"));
        panel.add(txtDistancia);
        panel.add(new JLabel("Tipo:"));
        panel.add(cboTipo);
        panel.add(new JLabel("Estado:"));
        panel.add(cboEstado);
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
        boolean encomienda = "ENCOMIENDA".equals(cboTipo.getSelectedItem());
        lblPeso.setEnabled(encomienda);
        txtPeso.setEnabled(encomienda);
        chkFragil.setEnabled(encomienda);
    }

    private void guardar() {
        try {
            String direccion = ValidacionFormulario.direccion(txtDireccion.getText());
            double distancia = ValidacionFormulario.distancia(txtDistancia.getText());
            String tipo = (String) cboTipo.getSelectedItem();

            Pedido pedido;
            if ("ENCOMIENDA".equals(tipo)) {
                double peso = ValidacionFormulario.peso(txtPeso.getText());
                pedido = new PedidoEncomienda(0, direccion, distancia, peso, chkFragil.isSelected());
            } else if ("EXPRESS".equals(tipo)) {
                pedido = new PedidoExpress(0, direccion, distancia);
            } else {
                pedido = new PedidoComida(0, direccion, distancia);
            }
            pedido.setEstado(VentanaListaPedidos.estadoModelo((String) cboEstado.getSelectedItem()));

            controladorPedidos.create(pedido);
            JOptionPane.showMessageDialog(this, "Pedido registrado correctamente.");
            dispose();
            if (menuPrincipal != null) {
                menuPrincipal.toFront();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Distancia y peso deben ser números.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {
        txtDireccion.setText("");
        txtDistancia.setText("");
        txtPeso.setText("");
        chkFragil.setSelected(false);
        cboTipo.setSelectedIndex(0);
        cboEstado.setSelectedIndex(0);
        actualizarCamposExtra();
    }
}
