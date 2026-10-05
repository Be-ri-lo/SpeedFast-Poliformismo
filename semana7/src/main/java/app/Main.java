package app;

import conexion.ConexionBD;
import vista.VentanaPrincipal;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Inicio de SpeedFast. Prueba la conexión a MySQL y abre la ventana principal.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UIManager.put("OptionPane.yesButtonText", "Sí");
            UIManager.put("OptionPane.noButtonText", "No");
            UIManager.put("OptionPane.okButtonText", "Aceptar");
            if (!probarConexion()) {
                return;
            }
            new VentanaPrincipal().setVisible(true);
        });
    }

    /** Si MySQL no responde, muestra el error y no abre la GUI. */
    private static boolean probarConexion() {
        Connection conexion = null;
        try {
            conexion = ConexionBD.getConexion();
            System.out.println("Conexión a speedfast_db correcta.");
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo conectar a MySQL:\n" + e.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            ConexionBD.cerrar(conexion);
        }
    }
}
