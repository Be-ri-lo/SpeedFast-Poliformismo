package conexion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Abre la conexión JDBC con MySQL.
 * Usuario y clave se leen de conexion.properties (ese archivo no se sube a GitHub).
 */
public class ConexionDB {

    /** Devuelve una Connection usando DriverManager. */
    public static Connection getConexion() throws SQLException {
        Properties datos = cargarDatos();
        String url = datos.getProperty("db.url");
        String usuario = datos.getProperty("db.usuario");
        String clave = datos.getProperty("db.clave");
        if (url == null || usuario == null || clave == null
                || url.isBlank() || usuario.isBlank() || clave.isBlank()) {
            throw new SQLException("conexion.properties está incompleto. Copie conexion.properties.ejemplo y complete db.clave.");
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url.trim(), usuario.trim(), clave);
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver JDBC de MySQL", e);
        }
    }

    private static Properties cargarDatos() throws SQLException {
        File archivo = buscarArchivo();
        if (archivo == null) {
            throw new SQLException(
                    "No se encontró conexion.properties.\n"
                            + "Copie semana8/conexion.properties.ejemplo a semana8/conexion.properties y complete la clave.");
        }
        Properties datos = new Properties();
        try (FileInputStream entrada = new FileInputStream(archivo)) {
            datos.load(entrada);
        } catch (IOException e) {
            throw new SQLException("No se pudo leer " + archivo.getAbsolutePath(), e);
        }
        return datos;
    }

    private static File buscarArchivo() {
        String[] candidatos = {
                "conexion.properties",
                "semana8/conexion.properties"
        };
        for (String ruta : candidatos) {
            File archivo = new File(ruta);
            if (archivo.isFile()) {
                return archivo;
            }
        }
        return null;
    }
}
