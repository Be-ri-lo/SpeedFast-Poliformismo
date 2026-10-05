package vista;

import controlador.ControladorEntregas;
import controlador.ControladorPedidos;
import controlador.ControladorRepartidores;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.GridLayout;

/**
 * Menú principal. Abre las ventanas de CRUD y muestra la actividad.
 */
public class VentanaPrincipal extends JFrame {

    private final ControladorPedidos controladorPedidos;
    private final ControladorRepartidores controladorRepartidores;
    private final ControladorEntregas controladorEntregas;
    private final JTextArea txtActividad = new JTextArea(6, 30);

    public VentanaPrincipal() {
        super("SpeedFast - Gestión CRUD");
        this.controladorPedidos = new ControladorPedidos();
        this.controladorRepartidores = new ControladorRepartidores();
        this.controladorEntregas = new ControladorEntregas(controladorPedidos, txtActividad);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(520, 560);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(Estilos.padding());

        JPanel encabezado = new JPanel(new GridLayout(2, 1, 0, 4));
        encabezado.add(Estilos.titulo("SpeedFast"));
        encabezado.add(Estilos.subtitulo("Pedidos, repartidores y entregas"));
        panel.add(encabezado, BorderLayout.NORTH);

        JButton btnRepartidores = Estilos.boton("Gestionar repartidores");
        JButton btnPedidos = Estilos.boton("Gestionar pedidos");
        JButton btnEntregas = Estilos.boton("Gestionar entregas");
        JButton btnSimular = Estilos.boton("Simular entregas");

        btnRepartidores.addActionListener(e -> {
            registrarActividad("Abriendo gestión de repartidores...");
            new VentanaListaRepartidores(controladorRepartidores).setVisible(true);
        });
        btnPedidos.addActionListener(e -> {
            registrarActividad("Abriendo gestión de pedidos...");
            new VentanaListaPedidos(controladorPedidos).setVisible(true);
        });
        btnEntregas.addActionListener(e -> {
            registrarActividad("Abriendo gestión de entregas...");
            new VentanaListaEntregas(controladorEntregas, controladorPedidos, controladorRepartidores)
                    .setVisible(true);
        });
        btnSimular.addActionListener(e -> simularEntregas());

        JPanel botones = new JPanel(new GridLayout(4, 1, 8, 8));
        botones.add(btnRepartidores);
        botones.add(btnPedidos);
        botones.add(btnEntregas);
        botones.add(btnSimular);

        txtActividad.setEditable(false);
        txtActividad.setLineWrap(true);
        txtActividad.setWrapStyleWord(true);
        txtActividad.setForeground(Estilos.TEXTO);
        txtActividad.append("Sistema iniciado. Esperando actividad...\n");

        JPanel centro = new JPanel(new BorderLayout(8, 8));
        centro.add(botones, BorderLayout.NORTH);
        centro.add(new JScrollPane(txtActividad), BorderLayout.CENTER);
        panel.add(centro, BorderLayout.CENTER);
        add(panel);
    }

    private void simularEntregas() {
        try {
            controladorEntregas.simularEntregas();
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Simulación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void registrarActividad(String mensaje) {
        txtActividad.append(mensaje + "\n");
        txtActividad.setCaretPosition(txtActividad.getDocument().getLength());
    }
}
