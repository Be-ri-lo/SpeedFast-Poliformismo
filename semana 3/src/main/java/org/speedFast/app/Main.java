package org.speedFast.app;

import org.speedFast.interfaces.Cancelable;
import org.speedFast.interfaces.Despachable;
import org.speedFast.interfaces.Rastreable;
import org.speedFast.model.ControladorDeEnvios;
import org.speedFast.model.Pedido;
import org.speedFast.model.PedidoComida;
import org.speedFast.model.PedidoEncomienda;
import org.speedFast.model.PedidoExpress;

public class Main {
    public static void main(String[] args) {
        try {
            ControladorDeEnvios controlador = new ControladorDeEnvios();

            Pedido comida = new PedidoComida(1, "Av. Italia 456", 2.7);
            Pedido encomienda = new PedidoEncomienda(2, "Av. Independencia 123", 6);
            Pedido express = new PedidoExpress(3, "Av. Apoquindo 1500", 15);
            Pedido comidaManual = new PedidoComida(4, "Av. Providencia 890", 3.2);

            controlador.registrarPedido(comida);
            controlador.registrarPedido(encomienda);
            controlador.registrarPedido(express);
            controlador.registrarPedido(comidaManual);

            System.out.println("==============================================");
            System.out.println("   SPEEDFAST - SISTEMA INTEGRAL DE ENTREGAS");
            System.out.println("==============================================\n");

            System.out.println("--- 1. Asignación automática de repartidores ---\n");
            comida.asignarRepartidor();
            encomienda.asignarRepartidor();
            express.asignarRepartidor();
            System.out.println("PedidoComida #" + comida.formatearId()
                    + " → " + comida.getRepartidor());
            System.out.println("PedidoEncomienda #" + encomienda.formatearId()
                    + " → " + encomienda.getRepartidor());
            System.out.println("PedidoExpress #" + express.formatearId()
                    + " → " + express.getRepartidor());

            System.out.println("\n--- 2. Asignación manual de repartidor ---\n");
            comidaManual.asignarRepartidor("Luis Pérez");
            System.out.println("PedidoComida #" + comidaManual.formatearId()
                    + " → " + comidaManual.getRepartidor() + " (asignación manual)");

            System.out.println("\n--- 3. Cálculo y visualización del tiempo estimado ---\n");
            Pedido[] pedidos = {comida, encomienda, express, comidaManual};
            for (Pedido pedido : pedidos) {
                pedido.mostrarResumen();
                System.out.println("Tiempo estimado de entrega: " + pedido.calcularTiempoEntrega() + " minutos");
                System.out.println();
            }

            System.out.println("--- 4. Despacho de pedidos ---\n");
            Despachable operacionDespacho = controlador;
            controlador.seleccionar(comida);
            operacionDespacho.despachar();
            System.out.println("Despachado PedidoComida #" + comida.formatearId()
                    + " | Estado: " + comida.getEstado().getEtiqueta());

            controlador.seleccionar(encomienda);
            operacionDespacho.despachar();
            System.out.println("Despachado PedidoEncomienda #" + encomienda.formatearId()
                    + " | Estado: " + encomienda.getEstado().getEtiqueta());

            controlador.seleccionar(express);
            operacionDespacho.despachar();
            System.out.println("Despachado PedidoExpress #" + express.formatearId()
                    + " | Estado: " + express.getEstado().getEtiqueta());

            System.out.println("\n--- 5. Cancelación de un pedido ---\n");
            Cancelable operacionCancelacion = controlador;
            controlador.seleccionar(comidaManual);
            operacionCancelacion.cancelar();
            System.out.println("Cancelado PedidoComida #" + comidaManual.formatearId()
                    + " | Estado: " + comidaManual.getEstado().getEtiqueta());

            System.out.println("\n--- 6. Historial de entregas realizadas ---\n");
            Rastreable consultaHistorial = controlador;
            consultaHistorial.verHistorial();

            System.out.println("\n--- 7. Trazabilidad individual de un pedido ---\n");
            comida.verHistorial();

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
