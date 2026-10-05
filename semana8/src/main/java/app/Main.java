package app;

import conexion.ConexionDB;
import vista.VentanaPrincipal;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Inicio de SpeedFast (semana 8). Prueba MySQL y abre la ventana principal.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UIManager.put("OptionPane.yesButtonText", "Sí");
            UIManager.put("OptionPane.noButtonText", "No");
            UIManager.put("OptionPane.okButtonText", "Aceptar");
            UIManager.put("OptionPane.cancelButtonText", "Cancelar");
            if (!probarConexion()) {
                return;
            }
            new VentanaPrincipal().setVisible(true);
        });
    }

    /** Si MySQL no responde, muestra el error y no abre la GUI. */
    private static boolean probarConexion() {
        try (Connection conexion = ConexionDB.getConexion()) {
            System.out.println("Conexión a speedfast_db correcta.");
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo conectar a MySQL:\n" + e.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
