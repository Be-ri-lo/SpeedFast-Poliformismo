package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Abre y cierra la conexión JDBC con MySQL (speedfast_db).
 */
public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/speedfast_db";
    private static final String USUARIO = "root";
    private static final String CLAVE = "1234";

    /** Devuelve una Connection usando DriverManager. */
    public static Connection getConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, CLAVE);
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver JDBC de MySQL", e);
        }
    }

    /** Cierra Connection, PreparedStatement o ResultSet. */
    public static void cerrar(AutoCloseable recurso) {
        if (recurso == null) {
            return;
        }
        try {
            recurso.close();
        } catch (Exception e) {
            System.out.println("Error al cerrar el recurso: " + e.getMessage());
        }
    }
}
