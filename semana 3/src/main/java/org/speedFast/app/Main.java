package org.speedFast.app;

import org.speedFast.interfaces.Cancelable;
import org.speedFast.interfaces.Despachable;
import org.speedFast.interfaces.Rastreable;
import org.speedFast.model.Pedido;
import org.speedFast.model.PedidoComida;
import org.speedFast.model.PedidoEncomienda;
import org.speedFast.model.PedidoExpress;
import org.speedFast.service.ControladorDeEnvios;

/**
 * Punto de entrada de la simulación SpeedFast.
 * Muestra asignación automática y manual, tiempo estimado, despacho, cancelación e historial.
 */
public class Main {

    /**
     * Ejecuta el flujo de ejemplo con los pedidos de comida, encomienda y express.
     *
     * @param args argumentos de consola (no se utilizan)
     */
    public static void main(String[] args) {
        try {
            // Crea el controlador que coordina despacho, cancelación e historial
            ControladorDeEnvios controlador = new ControladorDeEnvios();

            // Crea los tres tipos de pedido (mismos datos de la semana 2)
            Pedido comida = new PedidoComida(1, "Av. Italia 456", 2.7);
            PedidoEncomienda encomienda = new PedidoEncomienda(2, "Av. Independencia 123", 6, 22, true);
            Pedido express = new PedidoExpress(3, "Av. Apoquindo 1500", 15);

            // Registra los pedidos en el controlador
            controlador.registrarPedido(comida);
            controlador.registrarPedido(encomienda);
            controlador.registrarPedido(express);

            // Asignación automática (método sobrescrito en cada subclase)
            encomienda.asignarRepartidor();
            express.asignarRepartidor();
            // Asignación manual (método sobrecargado: asignarRepartidor(String))
            comida.asignarRepartidor("Luis Díaz");

            // Despacho de la encomienda y visualización del tiempo estimado
            Despachable operacionDespacho = controlador;
            controlador.seleccionar(encomienda);
            operacionDespacho.despachar();
            encomienda.mostrarResumen();
            System.out.println("Frágil: " + (encomienda.isFragil() ? "Sí" : "No"));
            System.out.println("Peso: " + encomienda.getPeso() + " kg"
                    + (encomienda.esPesado() ? " (pesado)" : ""));
            System.out.println("Pedido despachado correctamente.");
            System.out.println();

            // Despacho del pedido de comida
            controlador.seleccionar(comida);
            operacionDespacho.despachar();
            comida.mostrarResumen();
            System.out.println("Pedido despachado correctamente.");
            System.out.println();

            // Cancelación del pedido express
            System.out.println("Cancelando Pedido Express #" + express.formatearId() + "...");
            Cancelable operacionCancelacion = controlador;
            controlador.seleccionar(express);
            operacionCancelacion.cancelar();
            System.out.println("→ Pedido cancelado exitosamente.");
            System.out.println();
            express.mostrarResumen();
            System.out.println();

            // Historial de entregas realizadas (ArrayList)
            Rastreable consultaHistorial = controlador;
            consultaHistorial.verHistorial();

        } catch (IllegalArgumentException | IllegalStateException e) {
            // Control de errores de validación o de estado inválido
            System.out.println("Error: " + e.getMessage());
        }
    }
}
