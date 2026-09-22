package controlador;

import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.Repartidor;
import tareas.TareaEntrega;

import javax.swing.JTextArea;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Gestiona las entregas y lanza los hilos del paquete tareas.
 */
public class ControladorEntregas {

    private final ControladorPedidos controladorPedidos;
    private final JTextArea txtActividad;

    public ControladorEntregas(ControladorPedidos controladorPedidos, JTextArea txtActividad) {
        this.controladorPedidos = controladorPedidos;
        this.txtActividad = txtActividad;
    }

    public void iniciarEntrega(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Debe seleccionar un pedido");
        }
        if (pedido.getEstado() != EstadoPedido.ASIGNADO) {
            throw new IllegalStateException("Debe asignar un repartidor antes de iniciar la entrega");
        }

        Repartidor repartidor = new Repartidor(pedido.getRepartidor());
        repartidor.agregarPedido(pedido);
        txtActividad.append("Hilo iniciado: " + repartidor.getNombre()
                + " con pedido #" + pedido.formatearId() + "\n");
        lanzarTarea(repartidor, "Entrega-" + pedido.getIdPedido());
    }

    public void simularEntregas() {
        Map<String, List<Pedido>> porRepartidor = new LinkedHashMap<>();
        for (Pedido pedido : controladorPedidos.getPedidos()) {
            if (pedido.getEstado() == EstadoPedido.ASIGNADO) {
                porRepartidor.computeIfAbsent(pedido.getRepartidor(), clave -> new ArrayList<>())
                        .add(pedido);
            }
        }
        if (porRepartidor.isEmpty()) {
            throw new IllegalStateException("Debe asignar al menos un pedido antes de simular.");
        }

        txtActividad.append("--- Nueva simulación ---\n");
        for (Map.Entry<String, List<Pedido>> entrada : porRepartidor.entrySet()) {
            Repartidor repartidor = new Repartidor(entrada.getKey());
            for (Pedido pedido : entrada.getValue()) {
                repartidor.agregarPedido(pedido);
            }
            txtActividad.append("Hilo iniciado: " + entrada.getKey()
                    + " (" + entrada.getValue().size() + " pedido(s))\n");
            lanzarTarea(repartidor, "Repartidor-" + entrada.getKey());
        }
    }

    private void lanzarTarea(Repartidor repartidor, String nombreHilo) {
        TareaEntrega tarea = new TareaEntrega(repartidor, txtActividad, controladorPedidos);
        Thread hilo = new Thread(tarea, nombreHilo);
        hilo.setDaemon(true);
        hilo.start();
    }
}
