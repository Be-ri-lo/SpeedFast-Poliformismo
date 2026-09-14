package org.speedFast.model;

import java.util.Random;

/**
 * Repartidor es el encargado de retirar los pedidos de la zona de carga y entregarlos.
 * Repartidor de SpeedFast.
 * Cada uno corre en su propio hilo (implements Runnable)
 * y retira pedidos de la MISMA zona de carga.
 */
public class Repartidor implements Runnable {

    private  final String nombre;
    private  final ZonaDeCarga zonaDeCarga;
    private  final Random random;

    /**
     * @param nombre       nombre del repartidor
     * @param zonaDeCarga  zona compartida (la misma instancia para todos)
     */
    public Repartidor(String nombre, ZonaDeCarga zonaDeCarga) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del repartidor no puede estar vacio");
        }
        if (zonaDeCarga == null) {
            throw new IllegalArgumentException("La zona de carga no puede ser nula");
        }
        this.nombre = nombre.trim();
        this.zonaDeCarga = zonaDeCarga;
        this.random = new Random();
    }

    /**
     * Mientras haya pedidos, retira uno, lo marca EN_REPARTO,
     * simula la entrega con sleep y lo deja ENTREGADO.
     */
    @Override
    public void run() {
        while (true) {
            Pedido pedido = zonaDeCarga.retirarPedido();

            // null = no quedan pedidos en la zona de carga
            if (pedido == null) {
                System.out.println("[Repartidor - " + nombre + "] No quedan pedidos en la zona de carga");
                break;
            }

            try {
                System.out.println("[Repartidor - " + nombre + "] Retirando pedido #" + pedido.getId() + "...");
                pedido.setEstado("EN_REPARTO");
                System.out.println("[Repartidor - " + nombre + "] Estado: " + pedido.getEstado());

                System.out.println("[Repartidor - " + nombre + "] Entregando pedido #" + pedido.getId() + "...");
                // simula el viaje (entre 1 y 3 segundos)
                int espera = 1000 + random.nextInt(2000);
                Thread.sleep(espera);

                pedido.setEstado("ENTREGADO");
                System.out.println("[Repartidor - " + nombre + "] Estado: " + pedido.getEstado());

            } catch (InterruptedException e) {
                System.out.println("[Repartidor - " + nombre + "] Se interrumpio la entrega");
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public String getNombre() {
        return nombre;
    }
}
