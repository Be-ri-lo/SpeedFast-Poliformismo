package org.speedFast;

public class Main {
    public static void main(String[] args) {
        PedidoComida comida = new PedidoComida(1, "Av. Central 123", "Comida");
        PedidoEncomienda encomienda = new PedidoEncomienda(2, "Calle Sur 45", "Encomienda");
        PedidoExpress express = new PedidoExpress(3, "Pasaje Norte 9", "Express");

        comida.asignarRepartidor("Rosa Rojas");
        System.out.println();

        encomienda.asignarRepartidor("Juanito De Los Palotes");
        System.out.println();

        express.asignarRepartidor("Toribio Toro");
    }
}