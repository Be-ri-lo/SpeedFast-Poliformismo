package org.speedFast.app;

import org.speedFast.model.Pedido;
import org.speedFast.model.PedidoComida;
import org.speedFast.model.PedidoEncomienda;
import org.speedFast.model.PedidoExpress;

public class Main {
    public static void main(String[] args) {
        try {
            Pedido[] pedidos = {
                    //muestra de validacion de distancia
                    //new PedidoComida(1, "Av. Italia 456", 0),
                    new PedidoComida(1, "Av. Italia 456", 2.7),
                    new PedidoEncomienda(2, "Av. Independencia 123", 6),
                    new PedidoExpress(3, "Av. Apoquindo 1500", 15)
            };

            System.out.println("=== Tiempos estimados de entrega SpeedFast ===\n");

            for (Pedido pedido : pedidos) {
                pedido.mostrarResumen();
                System.out.println("Tiempo estimado de entrega: " + pedido.calcularTiempoEntrega() + " minutos");
                System.out.println();
            }

            System.out.println("=== Comparación rápida ===");
            System.out.println("Comida (#001):      " + pedidos[0].calcularTiempoEntrega() + " min");
            System.out.println("Encomienda (#002):  " + pedidos[1].calcularTiempoEntrega() + " min");
            System.out.println("Express (#003):     " + pedidos[2].calcularTiempoEntrega() + " min");

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
