package controlador;

import modelo.Pedido;

/**
 * Gestiona los repartidores disponibles.
 */
public class ControladorRepartidores {

    private final String[] nombres = {
            "Camila Soto", "Luis Díaz", "Daniela Tapia", "Carlos Soto"
    };

    public String[] getNombres() {
        return nombres;
    }

    public void asignarPedido(Pedido pedido, String nombre) {
        if (pedido == null) {
            throw new IllegalArgumentException("Debe seleccionar un pedido");
        }
        pedido.asignarRepartidor(nombre);
    }
}
