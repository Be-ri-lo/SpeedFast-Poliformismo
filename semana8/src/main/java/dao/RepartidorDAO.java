package dao;

import conexion.ConexionDB;
import modelo.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de repartidores. Métodos CRUD: create, readAll, update y delete.
 * En la guía aparece como ClienteDAO: SpeedFast no tiene clientes, la tabla es repartidores.
 */
public class RepartidorDAO {

    /** Inserta un repartidor y guarda el id generado. */
    public void create(Repartidor repartidor) {
        validar(repartidor);
        String sql = "INSERT INTO repartidores (nombre) VALUES (?)";
        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, repartidor.getNombre());
            ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    repartidor.setId(claves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo registrar el repartidor: " + e.getMessage());
        }
    }

    /** Devuelve todos los repartidores. */
    public List<Repartidor> readAll() {
        List<Repartidor> lista = new ArrayList<>();
        String sql = "SELECT id, nombre FROM repartidores ORDER BY id";
        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Repartidor(rs.getInt("id"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudieron listar los repartidores: " + e.getMessage());
        }
        return lista;
    }

    /** Actualiza el nombre del repartidor. */
    public void update(Repartidor repartidor) {
        validar(repartidor);
        if (repartidor.getId() <= 0) {
            throw new IllegalArgumentException("Debe indicar el id del repartidor a editar.");
        }
        String sql = "UPDATE repartidores SET nombre = ? WHERE id = ?";
        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, repartidor.getNombre());
            ps.setInt(2, repartidor.getId());
            if (ps.executeUpdate() == 0) {
                throw new IllegalStateException("No existe el repartidor con id " + repartidor.getId());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo actualizar el repartidor: " + e.getMessage());
        }
    }

    /** Elimina un repartidor por id. */
    public void delete(int id) {
        String sql = "DELETE FROM repartidores WHERE id = ?";
        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new IllegalStateException("No existe el repartidor con id " + id);
            }
        } catch (SQLException e) {
            throw new IllegalStateException(mensajeFk("No se pudo eliminar el repartidor", e));
        }
    }

    /** Busca un repartidor por id. */
    public Repartidor buscarPorId(int id) {
        String sql = "SELECT id, nombre FROM repartidores WHERE id = ?";
        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Repartidor(rs.getInt("id"), rs.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo buscar el repartidor: " + e.getMessage());
        }
        return null;
    }

    /** Valida el nombre antes de insertar o editar. */
    private void validar(Repartidor repartidor) {
        if (repartidor == null) {
            throw new IllegalArgumentException("El repartidor no puede ser nulo.");
        }
        ValidacionDatos.exigirTextoConLetras(repartidor.getNombre(), "nombre del repartidor");
        String nombre = repartidor.getNombre().trim();
        if (nombre.length() < 3 || nombre.length() > 100) {
            throw new IllegalArgumentException("El nombre debe tener entre 3 y 100 caracteres.");
        }
    }

    private String mensajeFk(String accion, SQLException e) {
        String detalle = e.getMessage() == null ? "" : e.getMessage().toLowerCase();
        if (detalle.contains("foreign key") || detalle.contains("cannot delete")) {
            return accion + ": tiene entregas asociadas.";
        }
        return accion + ": " + e.getMessage();
    }
}
