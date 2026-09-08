package org.speedFast.model;

import org.speedFast.util.EstadoPedido;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Repartidor de SpeedFast.
 * Implementa Runnable para correr como un hilo independiente.
 * Recorre su lista de pedidos, simula cada entrega con Thread.sleep()
 * y muestra el avance por consola.
 */
// implements Runnable = esta clase se puede ejecutar en un hilo
public class Repartidor implements Runnable {

    private String nombre;
    private List<Pedido> pedidosAsignados;
    private Random random;

    /**
     * Crea un repartidor sin pedidos asignados.
     *
     * @param nombre nombre del repartidor
     * @throws IllegalArgumentException si el nombre está vacío
     */
    public Repartidor(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del repartidor no puede estar vacío");
        }
        this.nombre = nombre.trim();
        this.pedidosAsignados = new ArrayList<>();
        this.random = new Random();
    }

    /**
     * Agrega un pedido a la ruta y lo asigna a este repartidor.
     *
     * @param pedido pedido que va a entregar
     * @throws IllegalArgumentException si el pedido es nulo
     */
    public void agregarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }
        // el pedido queda ASIGNADO a este nombre (hace falta para poder despacharlo)
        pedido.asignarRepartidor(nombre);
        pedidosAsignados.add(pedido);
    }

    /**
     * Entrega los pedidos uno por uno.
     * Por cada pedido imprime un mensaje, espera un tiempo aleatorio
     * y luego lo marca como despachado.
     */
    // Este metodo lo llama el hilo cuando arranca (ExecutorService.execute)
    // Cada repartidor entrega SUS pedidos en orden, pero los 3 hilos corren a la vez
    @Override
    public void run() {
        for (Pedido pedido : pedidosAsignados) {
            try {
                // si ya está cancelado, no se entrega
                if (pedido.getEstado() == EstadoPedido.CANCELADO) {
                    System.out.println("[Repartidor: " + nombre + "] Pedido #"
                            + pedido.getIdPedido() + " cancelado. No se entrega.");
                    continue;
                }

                System.out.println("[Repartidor: " + nombre + "] Entregando "
                        + pedido.getClass().getSimpleName() + " #" + pedido.getIdPedido() + "...");

                int espera = 1000 + random.nextInt(2000);
                Thread.sleep(espera);

                pedido.despachar(); // ASIGNADO -> DESPACHADO
                System.out.println("[Repartidor: " + nombre + "] Pedido #" + pedido.getIdPedido() + " entregado.");

            } catch (InterruptedException e) {
                // alguien interrumpió el sleep, se corta la ruta de este repartidor
                System.out.println("[Repartidor: " + nombre + "] Se interrumpió la entrega");
                return;
            } catch (IllegalStateException e) {
                // por ejemplo si el pedido no tenía repartidor asignado
                System.out.println("[Repartidor: " + nombre + "] Error: " + e.getMessage());
            }
        }
    }

    /**
     * @return nombre del repartidor
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return lista de pedidos asignados
     */
    public List<Pedido> getPedidosAsignados() {
        return pedidosAsignados;
    }
}
