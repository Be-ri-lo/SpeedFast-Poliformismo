package org.speedFast.app;

import org.speedFast.model.Pedido;
import org.speedFast.model.Repartidor;
import org.speedFast.model.ZonaDeCarga;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Simula la zona de carga de SpeedFast:
 * 5 pedidos y 3 repartidores en paralelo.
 */
public class Main {

    public static void main(String[] args) {
        // se crea la zona de carga
        ZonaDeCarga zona = new ZonaDeCarga();

        // los pedidos llegan a la zona de carga (recurso compartido)
        zona.agregarPedido(new Pedido(1, "Santiago Centro"));
        zona.agregarPedido(new Pedido(2, "Providencia"));
        zona.agregarPedido(new Pedido(3, "Ñuñoa"));
        zona.agregarPedido(new Pedido(4, "Recoleta"));
        zona.agregarPedido(new Pedido(5, "Las Condes"));

        // Se crean 3 hilos de tipo Repartidor
        ExecutorService executor = Executors.newFixedThreadPool(3);
        // se crean los repartidores (hilos)
        executor.execute(new Repartidor("Juan", zona));
        executor.execute(new Repartidor("Camila", zona));
        executor.execute(new Repartidor("Pedro", zona));

        //Se espera hasta que todos los hilos terminen
        executor.shutdown();

        try {
            if (!executor.awaitTermination(
                30, // tiempo de espera
                TimeUnit.SECONDS)) {
                System.out.println("[Main] Algunos hilos no finalizaron correctamente."); // si no terminan, se interrumpen
                executor.shutdownNow(); // se interrumpen los hilos
            }
        } catch (InterruptedException e) {
            executor.shutdownNow(); // se interrumpen los hilos 
            Thread.currentThread().interrupt();
            System.out.println("[Main] Interrupcion al esperar la terminacion de los hilos");
        }

        System.out.println("Todos los pedidos han sido entregados correctamente.");
    }
}
