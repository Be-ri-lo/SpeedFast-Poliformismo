package modelo;

import java.util.ArrayList;
import java.util.List;

/** Persona que realiza las entregas. toString() muestra el nombre en el combo. */
public class Repartidor {

    private int id;
    private String nombre;
    private final List<Pedido> pedidosAsignados;

    public Repartidor(String nombre) {
        this(0, nombre);
    }

    public Repartidor(int id, String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del repartidor no puede estar vacío");
        }
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del repartidor no puede estar vacío");
        }
        this.nombre = nombre.trim();
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return id > 0 ? id + " - " + nombre : nombre;
    }

    public List<Pedido> getPedidosAsignados() {
        return pedidosAsignados;
    }
}
