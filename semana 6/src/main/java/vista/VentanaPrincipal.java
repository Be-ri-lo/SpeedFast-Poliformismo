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
 * Ventana de inicio del sistema SpeedFast.
 */
public class VentanaPrincipal extends JFrame {

    private final ControladorPedidos controladorPedidos;
    private final ControladorRepartidores controladorRepartidores;
    private final ControladorEntregas controladorEntregas;
    private final JTextArea txtActividad = new JTextArea(6, 30);

    public VentanaPrincipal() {
        super("SpeedFast - Gestión de entregas");
        this.controladorPedidos = new ControladorPedidos();
        this.controladorRepartidores = new ControladorRepartidores();
        this.controladorEntregas = new ControladorEntregas(controladorPedidos, txtActividad);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(520, 480);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(Estilos.padding());

        JPanel encabezado = new JPanel(new GridLayout(2, 1, 0, 4));
        encabezado.add(Estilos.titulo("SpeedFast"));
        encabezado.add(Estilos.subtitulo("Gestión de entregas"));
        panel.add(encabezado, BorderLayout.NORTH);

        JButton btnRegistrar = Estilos.boton("Registrar pedido");
        JButton btnListar = Estilos.boton("Listar pedidos");
        JButton btnAsignar = Estilos.boton("Asignar repartidor / Iniciar entrega");
        JButton btnSimular = Estilos.boton("Simular entregas");

        btnRegistrar.addActionListener(e -> {
            registrarActividad("Abriendo registro de pedido...");
            new VentanaRegistroPedido(this, controladorPedidos).setVisible(true);
        });
        btnListar.addActionListener(e -> {
            registrarActividad("Abriendo listado de pedidos...");
            new VentanaListaPedidos(controladorPedidos).setVisible(true);
        });
        btnAsignar.addActionListener(e -> {
            registrarActividad("Abriendo asignación de entregas...");
            new VentanaAsignarEntrega(controladorPedidos, controladorRepartidores, controladorEntregas)
                    .setVisible(true);
        });
        btnSimular.addActionListener(e -> simularEntregas());

        JPanel botones = new JPanel(new GridLayout(4, 1, 8, 8));
        botones.add(btnRegistrar);
        botones.add(btnListar);
        botones.add(btnAsignar);
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
