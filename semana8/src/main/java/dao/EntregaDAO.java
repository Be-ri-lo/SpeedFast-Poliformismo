package dao;

import conexion.ConexionDB;
import modelo.CargaRepartidor;
import modelo.Entrega;
import modelo.Repartidor;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de entregas. Métodos CRUD: create, readAll, update y delete.
 */
public class EntregaDAO {

    /** Inserta una entrega (pedido + repartidor + fecha/hora). */
    public void create(Entrega entrega) {
        validar(entrega);
        String sql = "INSERT INTO entregas (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";
        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
            ps.setDate(3, Date.valueOf(entrega.getFecha()));
            ps.setTime(4, Time.valueOf(entrega.getHora().withNano(0)));
            ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    entrega.setId(claves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo registrar la entrega: " + e.getMessage());
        }
    }

    /** Devuelve todas las entregas. */
    public List<Entrega> readAll() {
        return filtrar(0, 0);
    }

    /** Lista entregas filtrando por pedido y/o repartidor (0 = todos). */
    public List<Entrega> filtrar(int idPedido, int idRepartidor) {
        List<Entrega> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT e.id, e.id_pedido, e.id_repartidor, e.fecha, e.hora, "
                        + "p.direccion, r.nombre "
                        + "FROM entregas e "
                        + "JOIN pedidos p ON p.id = e.id_pedido "
                        + "JOIN repartidores r ON r.id = e.id_repartidor "
                        + "WHERE 1=1");
        if (idPedido > 0) {
            sql.append(" AND e.id_pedido = ?");
        }
        if (idRepartidor > 0) {
            sql.append(" AND e.id_repartidor = ?");
        }
        sql.append(" ORDER BY e.id");

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {
            int i = 1;
            if (idPedido > 0) {
                ps.setInt(i++, idPedido);
            }
            if (idRepartidor > 0) {
                ps.setInt(i, idRepartidor);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Entrega(
                            rs.getInt("id"),
                            rs.getInt("id_pedido"),
                            rs.getInt("id_repartidor"),
                            rs.getDate("fecha").toLocalDate(),
                            rs.getTime("hora").toLocalTime(),
                            rs.getString("direccion"),
                            rs.getString("nombre")));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudieron listar las entregas: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Cola del día: libres primero y, si no, el que lleva menos pedidos hoy.
     * Libre = no tiene pedidos en EN_REPARTO.
     */
    public List<CargaRepartidor> listarCargaHoy() {
        List<CargaRepartidor> lista = new ArrayList<>();
        String sql = "SELECT r.id, r.nombre, "
                + "(SELECT COUNT(*) FROM entregas e WHERE e.id_repartidor = r.id AND e.fecha = CURDATE()) AS pedidos_hoy, "
                + "(SELECT COUNT(*) FROM entregas e INNER JOIN pedidos p ON p.id = e.id_pedido "
                + "WHERE e.id_repartidor = r.id AND p.estado = 'EN_REPARTO') AS en_reparto "
                + "FROM repartidores r "
                + "ORDER BY en_reparto ASC, pedidos_hoy ASC, r.nombre ASC";
        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new CargaRepartidor(
                            new Repartidor(rs.getInt("id"), rs.getString("nombre")),
                            rs.getInt("pedidos_hoy"),
                            rs.getInt("en_reparto")));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo listar la carga de repartidores: " + e.getMessage());
        }
        return lista;
    }

    /** Actualiza pedido, repartidor, fecha y hora de una entrega. */
    public void update(Entrega entrega) {
        validar(entrega);
        if (entrega.getId() <= 0) {
            throw new IllegalArgumentException("Debe indicar el id de la entrega a editar.");
        }
        String sql = "UPDATE entregas SET id_pedido = ?, id_repartidor = ?, fecha = ?, hora = ? WHERE id = ?";
        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
            ps.setDate(3, Date.valueOf(entrega.getFecha()));
            ps.setTime(4, Time.valueOf(entrega.getHora().withNano(0)));
            ps.setInt(5, entrega.getId());
            if (ps.executeUpdate() == 0) {
                throw new IllegalStateException("No existe la entrega con id " + entrega.getId());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo actualizar la entrega: " + e.getMessage());
        }
    }

    /** Elimina una entrega por id. */
    public void delete(int id) {
        String sql = "DELETE FROM entregas WHERE id = ?";
        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new IllegalStateException("No existe la entrega con id " + id);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo eliminar la entrega: " + e.getMessage());
        }
    }

    /** Valida ids, fecha y hora antes de insertar o editar. */
    private void validar(Entrega entrega) {
        if (entrega == null) {
            throw new IllegalArgumentException("La entrega no puede ser nula.");
        }
        if (entrega.getIdPedido() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un pedido válido.");
        }
        if (entrega.getIdRepartidor() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un repartidor válido.");
        }
        if (entrega.getFecha() == null) {
            throw new IllegalArgumentException("La fecha de la entrega es obligatoria.");
        }
        if (entrega.getHora() == null) {
            throw new IllegalArgumentException("La hora de la entrega es obligatoria.");
        }
        if (entrega.getFecha().isAfter(java.time.LocalDate.now().plusDays(30))) {
            throw new IllegalArgumentException("La fecha no puede ser más de 30 días en el futuro.");
        }
        if (!existe("SELECT id FROM pedidos WHERE id = ?", entrega.getIdPedido())) {
            throw new IllegalArgumentException("El pedido indicado no existe.");
        }
        if (!existe("SELECT id FROM repartidores WHERE id = ?", entrega.getIdRepartidor())) {
            throw new IllegalArgumentException("El repartidor indicado no existe.");
        }
    }

    private boolean existe(String sql, int id) {
        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo validar la entrega: " + e.getMessage());
        }
    }
}
