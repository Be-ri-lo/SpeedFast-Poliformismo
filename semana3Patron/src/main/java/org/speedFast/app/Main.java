package org.speedFast.app;

import org.speedFast.interfaces.Cancelable;
import org.speedFast.interfaces.Despachable;
import org.speedFast.interfaces.Rastreable;
import org.speedFast.model.Pedido;
import org.speedFast.model.PedidoComida;
import org.speedFast.model.PedidoEncomienda;
import org.speedFast.model.PedidoExpress;
import org.speedFast.service.ControladorDeEnvios;
import org.speedFast.strategy.TiempoComida;
import org.speedFast.strategy.TiempoExpress;

/**
 * Misma simulación de semana 3, más una demo de Strategy.
 * Lee los comentarios en orden: 1) flujo normal  2) cambio de estrategia.
 */
public class Main {

    public static void main(String[] args) {
        try {
            ControladorDeEnvios controlador = new ControladorDeEnvios();

            Pedido comida = new PedidoComida(1, "Av. Italia 456", 2.7);
            PedidoEncomienda encomienda = new PedidoEncomienda(2, "Av. Independencia 123", 6, 22, true);
            Pedido express = new PedidoExpress(3, "Av. Apoquindo 1500", 15);

            controlador.registrarPedido(comida);
            controlador.registrarPedido(encomienda);
            controlador.registrarPedido(express);

            encomienda.asignarRepartidor();
            express.asignarRepartidor();
            comida.asignarRepartidor("Luis Díaz");

            // --- DEMO STRATEGY (léeme) ---
            // comida SIGUE siendo PedidoComida. No cambiamos su clase.
            // Solo le cambiamos el objeto que calcula el tiempo.
            System.out.println("=== Demo Strategy: mismo pedido, otra fórmula ===");
            System.out.println("Comida con TiempoComida: " + comida.calcularTiempoEntrega() + " min");
            comida.setEstrategiaTiempo(new TiempoExpress());
            System.out.println("Comida con TiempoExpress: " + comida.calcularTiempoEntrega() + " min");
            // Volvemos a la fórmula original para el resto de la simulación
            comida.setEstrategiaTiempo(new TiempoComida());
            System.out.println("Comida otra vez con TiempoComida: " + comida.calcularTiempoEntrega() + " min");
            System.out.println();

            Despachable operacionDespacho = controlador;
            controlador.seleccionar(encomienda);
            operacionDespacho.despachar();
            encomienda.mostrarResumen();
            System.out.println("Frágil: " + (encomienda.isFragil() ? "Sí" : "No"));
            System.out.println("Peso: " + encomienda.getPeso() + " kg"
                    + (encomienda.esPesado() ? " (pesado)" : ""));
            System.out.println("Pedido despachado correctamente.");
            System.out.println();

            controlador.seleccionar(comida);
            operacionDespacho.despachar();
            comida.mostrarResumen();
            System.out.println("Pedido despachado correctamente.");
            System.out.println();

            System.out.println("Cancelando Pedido Express #" + express.formatearId() + "...");
            Cancelable operacionCancelacion = controlador;
            controlador.seleccionar(express);
            operacionCancelacion.cancelar();
            System.out.println("→ Pedido cancelado exitosamente.");
            System.out.println();
            express.mostrarResumen();
            System.out.println();

            Rastreable consultaHistorial = controlador;
            consultaHistorial.verHistorial();

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
