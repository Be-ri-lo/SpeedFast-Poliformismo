package org.speedFast.app;

import org.speedFast.model.PedidoComida;
import org.speedFast.model.PedidoEncomienda;
import org.speedFast.model.PedidoExpress;
import org.speedFast.model.Repartidor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Camila
        Repartidor camila = new Repartidor("Camila");
        camila.agregarPedido(new PedidoComida(101, "Av. Italia 456", 2.7));
        camila.agregarPedido(new PedidoEncomienda(103, "Av. Independencia 123", 6, 12, true));

        // Luis
        Repartidor luis = new Repartidor("Luis");
        luis.agregarPedido(new PedidoExpress(102, "Av. Apoquindo 1500", 8));
        luis.agregarPedido(new PedidoComida(104, "Av. Providencia 890", 3.5));

        PedidoExpress pedidoCancelado = new PedidoExpress(107, "Av. Vitacura 800", 3);
        luis.agregarPedido(pedidoCancelado);
        System.out.println("Cancelando PedidoExpress #" + pedidoCancelado.getIdPedido() + "...");
        pedidoCancelado.cancelar(); // pasa a EstadoPedido.CANCELADO
        System.out.println("Estado actual: " + pedidoCancelado.getEstado());
        System.out.println();

        // Daniela
        Repartidor daniela = new Repartidor("Daniela");
        daniela.agregarPedido(new PedidoExpress(105, "Av. Las Condes 2000", 4));
        daniela.agregarPedido(new PedidoEncomienda(106, "Av. Matta 321", 5, 22, false));

        // cada execute lanza un hilo (Repartidor implementa Runnable)
        executor.execute(camila);
        executor.execute(luis);
        executor.execute(daniela);

        // no se aceptan mas tareas, los hilos que ya estan siguen hasta terminar
        executor.shutdown();

        try {
            // espera a que los 3 repartidores terminen sus entregas
            if (!executor.awaitTermination(2, TimeUnit.MINUTES)) {
                System.out.println("[Main] Algunos hilos no finalizaron correctamente.");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("[Main] Sistema finalizado.");
    }
}
