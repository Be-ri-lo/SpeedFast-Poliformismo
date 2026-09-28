package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Repartidor con nombre y pedidos asignados.
 */
public class Repartidor {

    private final String nombre;
    private final List<Pedido> pedidosAsignados;

    public Repartidor(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del repartidor no puede estar vacío");
        }
        this.nombre = nombre.trim();
        this.pedidosAsignados = new ArrayList<>();
    }

    public void agregarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }
        pedido.asignarRepartidor(nombre);
        pedidosAsignados.add(pedido);
    }

    public String getNombre() {
        return nombre;
    }

    public List<Pedido> getPedidosAsignados() {
        return pedidosAsignados;
    }
}
