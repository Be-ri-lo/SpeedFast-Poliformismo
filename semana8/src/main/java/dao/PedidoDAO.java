package dao;

import conexion.ConexionDB;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.PedidoComida;
import modelo.PedidoEncomienda;
import modelo.PedidoExpress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de pedidos. Métodos CRUD: create, readAll, update y delete.
 */
public class PedidoDAO {

    /** Inserta un pedido (dirección, tipo, estado, km, peso y frágil). */
    public void create(Pedido pedido) {
        validar(pedido);
        String sql = "INSERT INTO pedidos (direccion, tipo, estado, distancia_km, peso, fragil) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            cargarParametros(ps, pedido);
            ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    pedido.setIdPedido(claves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo registrar el pedido: " + e.getMessage());
        }
    }

    /** Devuelve todos los pedidos. */
    public List<Pedido> readAll() {
        return filtrar(null, null);
    }

    /** Lista pedidos con filtro opcional por tipo y/o estado. */
    public List<Pedido> filtrar(String tipo, String estado) {
        List<Pedido> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.id, p.direccion, p.tipo, p.estado, p.distancia_km, p.peso, p.fragil, "
                        + "(SELECT r.nombre FROM entregas e "
                        + "JOIN repartidores r ON r.id = e.id_repartidor "
                        + "WHERE e.id_pedido = p.id ORDER BY e.id DESC LIMIT 1) AS repartidor "
                        + "FROM pedidos p WHERE 1=1");
        if (tipo != null && !tipo.isBlank()) {
            sql.append(" AND p.tipo = ?");
        }
        if (estado != null && !estado.isBlank()) {
            sql.append(" AND p.estado = ?");
        }
        sql.append(" ORDER BY p.id");

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {
            int i = 1;
            if (tipo != null && !tipo.isBlank()) {
                ps.setString(i++, tipoBd(tipo));
            }
            if (estado != null && !estado.isBlank()) {
                ps.setString(i, estado.trim().toUpperCase());
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudieron listar los pedidos: " + e.getMessage());
        }
        return lista;
    }

    /** Actualiza dirección, tipo, estado, km, peso y frágil. */
    public void update(Pedido pedido) {
        validar(pedido);
        if (pedido.getIdPedido() <= 0) {
            throw new IllegalArgumentException("Debe indicar el id del pedido a editar.");
        }
        String sql = "UPDATE pedidos SET direccion = ?, tipo = ?, estado = ?, distancia_km = ?, peso = ?, fragil = ? WHERE id = ?";
        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            cargarParametros(ps, pedido);
            ps.setInt(7, pedido.getIdPedido());
            if (ps.executeUpdate() == 0) {
                throw new IllegalStateException("No existe el pedido con id " + pedido.getIdPedido());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo actualizar el pedido: " + e.getMessage());
        }
    }

    /** Elimina un pedido por id. */
    public void delete(int id) {
        String sql = "DELETE FROM pedidos WHERE id = ?";
        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new IllegalStateException("No existe el pedido con id " + id);
            }
        } catch (SQLException e) {
            throw new IllegalStateException(mensajeFk("No se pudo eliminar el pedido", e));
        }
    }

    /** Cambia solo el estado (asignar / entregar / volver a pendiente). */
    public void actualizarEstado(Pedido pedido) {
        if (pedido == null || pedido.getIdPedido() <= 0) {
            throw new IllegalArgumentException("Debe indicar el pedido.");
        }
        String sql = "UPDATE pedidos SET estado = ? WHERE id = ?";
        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, estadoBd(pedido.getEstado()));
            ps.setInt(2, pedido.getIdPedido());
            if (ps.executeUpdate() == 0) {
                throw new IllegalStateException("No existe el pedido con id " + pedido.getIdPedido());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo actualizar el estado: " + e.getMessage());
        }
    }

    /** Busca un pedido por id. */
    public Pedido buscarPorId(int id) {
        for (Pedido pedido : readAll()) {
            if (pedido.getIdPedido() == id) {
                return pedido;
            }
        }
        return null;
    }

    /** Valida dirección, tipo, km y peso antes de insertar o editar. */
    private void validar(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo.");
        }
        ValidacionDatos.exigirTextoConLetras(pedido.getDireccionEntrega(), "dirección");
        if (pedido.getDireccionEntrega().trim().length() > 100) {
            throw new IllegalArgumentException("La dirección no puede superar 100 caracteres.");
        }
        String tipo = tipoBd(pedido.getTipo());
        if (!tipo.equals("COMIDA") && !tipo.equals("ENCOMIENDA") && !tipo.equals("EXPRESS")) {
            throw new IllegalArgumentException("El tipo debe ser COMIDA, ENCOMIENDA o EXPRESS.");
        }
        if (pedido.getDistanciaKm() < 0.1 || pedido.getDistanciaKm() > 100) {
            throw new IllegalArgumentException("La distancia debe estar entre 0.1 km y 100 km.");
        }
        if (pedido instanceof PedidoEncomienda encomienda) {
            if (encomienda.getPeso() <= 0 || encomienda.getPeso() > 100) {
                throw new IllegalArgumentException("El peso de la encomienda debe estar entre 0.1 y 100 kg.");
            }
        }
        if (pedido.getEstado() == null) {
            throw new IllegalArgumentException("El estado del pedido es obligatorio.");
        }
    }

    private void cargarParametros(PreparedStatement ps, Pedido pedido) throws SQLException {
        ps.setString(1, pedido.getDireccionEntrega());
        ps.setString(2, tipoBd(pedido.getTipo()));
        ps.setString(3, estadoBd(pedido.getEstado()));
        ps.setDouble(4, pedido.getDistanciaKm());
        if (pedido instanceof PedidoEncomienda encomienda) {
            ps.setDouble(5, encomienda.getPeso());
            ps.setBoolean(6, encomienda.isFragil());
        } else {
            ps.setNull(5, Types.DECIMAL);
            ps.setBoolean(6, false);
        }
    }

    private Pedido mapear(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String direccion = rs.getString("direccion");
        String tipo = rs.getString("tipo");
        double distancia = rs.getDouble("distancia_km");
        if (distancia < 0.1) {
            distancia = 0.1;
        }
        Pedido pedido;
        if ("ENCOMIENDA".equalsIgnoreCase(tipo)) {
            double peso = rs.getObject("peso") == null ? 1.0 : rs.getDouble("peso");
            if (peso <= 0) {
                peso = 1.0;
            }
            pedido = new PedidoEncomienda(id, direccion, distancia, peso, rs.getBoolean("fragil"));
        } else if ("EXPRESS".equalsIgnoreCase(tipo)) {
            pedido = new PedidoExpress(id, direccion, distancia);
        } else {
            pedido = new PedidoComida(id, direccion, distancia);
        }
        pedido.setEstado(estadoModelo(rs.getString("estado")));
        pedido.setNombreRepartidor(rs.getString("repartidor"));
        return pedido;
    }

    private String tipoBd(String tipo) {
        if (tipo == null) {
            return "COMIDA";
        }
        return tipo.trim().toUpperCase();
    }

    private String estadoBd(EstadoPedido estado) {
        return switch (estado) {
            case ASIGNADO -> "EN_REPARTO";
            case DESPACHADO -> "ENTREGADO";
            default -> "PENDIENTE";
        };
    }

    private EstadoPedido estadoModelo(String estado) {
        if (estado == null) {
            return EstadoPedido.RESERVADO;
        }
        return switch (estado.toUpperCase()) {
            case "EN_REPARTO" -> EstadoPedido.ASIGNADO;
            case "ENTREGADO" -> EstadoPedido.DESPACHADO;
            default -> EstadoPedido.RESERVADO;
        };
    }

    private String mensajeFk(String accion, SQLException e) {
        String detalle = e.getMessage() == null ? "" : e.getMessage().toLowerCase();
        if (detalle.contains("foreign key") || detalle.contains("cannot delete")) {
            return accion + ": tiene entregas asociadas.";
        }
        return accion + ": " + e.getMessage();
    }
}
