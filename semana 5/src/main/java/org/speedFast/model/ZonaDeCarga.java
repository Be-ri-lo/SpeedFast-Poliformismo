package org.speedFast.model;

import org.speedFast.util.EstadoPedido;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Zona de carga es el lugar donde se almacenan los pedidos pendientes de ser entregados.
 * Recurso compartido: la zona de carga de SpeedFast.
 * Los pedidos se guardan en un BlockingQueue.
 * Los metodos van con synchronized para que dos hilos
 * no retiren el mismo pedido.
 */
public class ZonaDeCarga {

    private final BlockingQueue<Pedido> pedidos;

    public ZonaDeCarga() {
        this.pedidos = new LinkedBlockingQueue<>();
        System.out.println("[Zona de carga inicializada]");
    }

    public synchronized void agregarPedido(Pedido p) {
        if (p == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }

        //La zona de carga almacena solo pedidos pendientes de ser entregados
        if (p.getEstado() != EstadoPedido.PENDIENTE) {
            throw new IllegalArgumentException("Solo se pueden agregar pedidos con estado PENDIENTE");
        }

        pedidos.add(p);
        System.out.println("Pedido #" + p.getId() + " agregado. Destino: " + p.getDireccionEntrega());
    }

    public synchronized Pedido retirarPedido() {
        Pedido pedido = pedidos.poll();

        if (pedido != null && pedidos.isEmpty()) {
            System.out.println("[Zona de carga vacía]");
        }

        return pedido;
    }
}
