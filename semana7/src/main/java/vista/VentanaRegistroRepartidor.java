package vista;

import controlador.ControladorRepartidores;
import modelo.Repartidor;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Frame;
import java.awt.GridLayout;

/** Formulario para registrar un repartidor. */
public class VentanaRegistroRepartidor extends JFrame {

    private final Frame menuPrincipal;
    private final ControladorRepartidores controladorRepartidores;
    private final JTextField txtNombre = new JTextField();

    public VentanaRegistroRepartidor(Frame menuPrincipal, ControladorRepartidores controladorRepartidores) {
        super("SpeedFast - Registrar repartidor");
        this.menuPrincipal = menuPrincipal;
        this.controladorRepartidores = controladorRepartidores;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(420, 180);
        setLocationRelativeTo(null);
        crearComponentes();
    }

    private void crearComponentes() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JButton btnGuardar = new JButton("Guardar repartidor");
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel(""));
        panel.add(btnGuardar);
        add(panel);

        btnGuardar.addActionListener(e -> guardar());
    }

    private void guardar() {
        try {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar el nombre del repartidor.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            controladorRepartidores.registrarRepartidor(new Repartidor(nombre));
            JOptionPane.showMessageDialog(this, "Repartidor registrado en la base de datos.");
            dispose();
            if (menuPrincipal != null) {
                menuPrincipal.toFront();
            }
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "No se pudo guardar", JOptionPane.ERROR_MESSAGE);
        }
    }
}
