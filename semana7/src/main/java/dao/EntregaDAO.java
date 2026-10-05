package dao;

import conexion.ConexionBD;
import modelo.Entrega;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso JDBC a la tabla entrega (pedido + repartidor + fecha/hora).
 */
public class EntregaDAO {

    /** Inserta una entrega. */
    public void guardar(Entrega entrega) {
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet claves = null;
        try {
            conexion = ConexionBD.getConexion();
            ps = conexion.prepareStatement(
                    "INSERT INTO entrega (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
            ps.setDate(3, Date.valueOf(entrega.getFecha()));
            ps.setTime(4, Time.valueOf(entrega.getHora().withNano(0)));
            ps.executeUpdate();
            claves = ps.getGeneratedKeys();
            if (claves.next()) {
                entrega.setId(claves.getInt(1));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo guardar la entrega: " + e.getMessage());
        } finally {
            ConexionBD.cerrar(claves);
            ConexionBD.cerrar(ps);
            ConexionBD.cerrar(conexion);
        }
    }

    /** Lista entregas con dirección del pedido y nombre del repartidor. */
    public List<Entrega> listarTodos() {
        List<Entrega> entregas = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conexion = ConexionBD.getConexion();
            ps = conexion.prepareStatement(
                    "SELECT e.id, e.id_pedido, e.id_repartidor, e.fecha, e.hora, "
                            + "p.direccion, r.nombre "
                            + "FROM entrega e "
                            + "JOIN pedido p ON p.id = e.id_pedido "
                            + "JOIN repartidor r ON r.id = e.id_repartidor "
                            + "ORDER BY e.id");
            rs = ps.executeQuery();
            while (rs.next()) {
                LocalTime hora = rs.getTime("hora").toLocalTime();
                entregas.add(new Entrega(
                        rs.getInt("id"),
                        rs.getInt("id_pedido"),
                        rs.getInt("id_repartidor"),
                        rs.getDate("fecha").toLocalDate(),
                        hora,
                        rs.getString("direccion"),
                        rs.getString("nombre")));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudieron listar las entregas: " + e.getMessage());
        } finally {
            ConexionBD.cerrar(rs);
            ConexionBD.cerrar(ps);
            ConexionBD.cerrar(conexion);
        }
        return entregas;
    }
}
