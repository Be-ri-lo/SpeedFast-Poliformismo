package dao;

import conexion.ConexionBD;
import modelo.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso JDBC a la tabla repartidor.
 */
public class RepartidorDAO {

    /** Inserta el repartidor y guarda el id generado. */
    public void guardar(Repartidor repartidor) {
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet claves = null;
        try {
            conexion = ConexionBD.getConexion();
            ps = conexion.prepareStatement(
                    "INSERT INTO repartidor (nombre) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, repartidor.getNombre());
            ps.executeUpdate();
            claves = ps.getGeneratedKeys();
            if (claves.next()) {
                repartidor.setId(claves.getInt(1));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo guardar el repartidor: " + e.getMessage());
        } finally {
            ConexionBD.cerrar(claves);
            ConexionBD.cerrar(ps);
            ConexionBD.cerrar(conexion);
        }
    }

    /** Devuelve todos los repartidores. */
    public List<Repartidor> listarTodos() {
        List<Repartidor> repartidores = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conexion = ConexionBD.getConexion();
            ps = conexion.prepareStatement("SELECT id, nombre FROM repartidor ORDER BY id");
            rs = ps.executeQuery();
            while (rs.next()) {
                repartidores.add(new Repartidor(rs.getInt("id"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudieron listar los repartidores: " + e.getMessage());
        } finally {
            ConexionBD.cerrar(rs);
            ConexionBD.cerrar(ps);
            ConexionBD.cerrar(conexion);
        }
        return repartidores;
    }

    /** Busca un repartidor por nombre. */
    public Repartidor buscarPorNombre(String nombre) {
        if (nombre == null) {
            return null;
        }
        for (Repartidor repartidor : listarTodos()) {
            if (repartidor.getNombre().equalsIgnoreCase(nombre.trim())) {
                return repartidor;
            }
        }
        return null;
    }
}
