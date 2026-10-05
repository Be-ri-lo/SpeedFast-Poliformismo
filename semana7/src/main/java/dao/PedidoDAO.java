package dao;

import conexion.ConexionBD;
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
 * Acceso JDBC a la tabla pedido.
 * Guardar inserta dirección, tipo, estado, km, peso y frágil.
 */
public class PedidoDAO {

    /** Inserta el pedido y copia el id generado al objeto. */
    public void guardar(Pedido pedido) {
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet claves = null;
        try {
            conexion = ConexionBD.getConexion();
            ps = conexion.prepareStatement(
                    "INSERT INTO pedido (direccion, tipo, estado, distancia_km, peso, fragil) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
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
            ps.executeUpdate();
            claves = ps.getGeneratedKeys();
            if (claves.next()) {
                pedido.setIdPedido(claves.getInt(1));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo guardar el pedido: " + e.getMessage());
        } finally {
            ConexionBD.cerrar(claves);
            ConexionBD.cerrar(ps);
            ConexionBD.cerrar(conexion);
        }
    }

    /** Actualiza el estado del pedido en MySQL. */
    public void actualizarEstado(Pedido pedido) {
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = ConexionBD.getConexion();
            ps = conexion.prepareStatement("UPDATE pedido SET estado = ? WHERE id = ?");
            ps.setString(1, estadoBd(pedido.getEstado()));
            ps.setInt(2, pedido.getIdPedido());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo actualizar el pedido: " + e.getMessage());
        } finally {
            ConexionBD.cerrar(ps);
            ConexionBD.cerrar(conexion);
        }
    }

    /** Devuelve todos los pedidos. Incluye el último repartidor asignado. */
    public List<Pedido> listarTodos() {
        List<Pedido> pedidos = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conexion = ConexionBD.getConexion();
            ps = conexion.prepareStatement(
                    "SELECT p.id, p.direccion, p.tipo, p.estado, p.distancia_km, p.peso, p.fragil, "
                            + "(SELECT r.nombre FROM entrega e "
                            + "JOIN repartidor r ON r.id = e.id_repartidor "
                            + "WHERE e.id_pedido = p.id ORDER BY e.id DESC LIMIT 1) AS repartidor "
                            + "FROM pedido p ORDER BY p.id");
            rs = ps.executeQuery();
            while (rs.next()) {
                pedidos.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudieron listar los pedidos: " + e.getMessage());
        } finally {
            ConexionBD.cerrar(rs);
            ConexionBD.cerrar(ps);
            ConexionBD.cerrar(conexion);
        }
        return pedidos;
    }

    /** Busca un pedido por id. */
    public Pedido buscarPorId(int idPedido) {
        for (Pedido pedido : listarTodos()) {
            if (pedido.getIdPedido() == idPedido) {
                return pedido;
            }
        }
        return null;
    }

    /** Convierte una fila SQL en PedidoComida, PedidoEncomienda o PedidoExpress. */
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

    /** Pasa el tipo de la GUI a mayúsculas para la columna tipo. */
    private String tipoBd(String tipo) {
        if (tipo == null) {
            return "COMIDA";
        }
        return tipo.trim().toUpperCase();
    }

    /** Enum Java → valor de la columna estado (PENDIENTE, EN_REPARTO, ENTREGADO). */
    private String estadoBd(EstadoPedido estado) {
        return switch (estado) {
            case ASIGNADO -> "EN_REPARTO";
            case DESPACHADO -> "ENTREGADO";
            case CANCELADO -> "CANCELADO";
            default -> "PENDIENTE";
        };
    }

    /** Valor de MySQL → enum EstadoPedido. */
    private EstadoPedido estadoModelo(String estado) {
        if (estado == null) {
            return EstadoPedido.RESERVADO;
        }
        return switch (estado.toUpperCase()) {
            case "EN_REPARTO" -> EstadoPedido.ASIGNADO;
            case "ENTREGADO" -> EstadoPedido.DESPACHADO;
            case "CANCELADO" -> EstadoPedido.CANCELADO;
            default -> EstadoPedido.RESERVADO;
        };
    }
}
