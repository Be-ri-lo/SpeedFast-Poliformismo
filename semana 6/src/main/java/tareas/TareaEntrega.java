package tareas;

import controlador.ControladorPedidos;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.Repartidor;

import javax.swing.JTextArea;
import java.util.Random;

/**
 * Hilo de entrega: recorre los pedidos del repartidor y escribe en el JTextArea.
 */
public class TareaEntrega implements Runnable {

    private final Repartidor repartidor;
    private final JTextArea txtActividad;
    private final ControladorPedidos controladorPedidos;
    private final Random random = new Random();

    public TareaEntrega(Repartidor repartidor, JTextArea txtActividad,
                        ControladorPedidos controladorPedidos) {
        this.repartidor = repartidor;
        this.txtActividad = txtActividad;
        this.controladorPedidos = controladorPedidos;
    }

    @Override
    public void run() {
        for (Pedido pedido : repartidor.getPedidosAsignados()) {
            try {
                if (pedido.getEstado() == EstadoPedido.CANCELADO) {
                    txtActividad.append("[" + repartidor.getNombre() + "] Pedido #"
                            + pedido.formatearId() + " cancelado. No se entrega.\n");
                    continue;
                }

                txtActividad.append("[" + repartidor.getNombre() + "] Entregando "
                        + pedido.getTipo() + " #" + pedido.formatearId() + "...\n");

                Thread.sleep(1000 + random.nextInt(2000));

                pedido.despachar();
                controladorPedidos.notificarCambio();
                txtActividad.append("[" + repartidor.getNombre() + "] Pedido #"
                        + pedido.formatearId() + " entregado.\n");
            } catch (InterruptedException e) {
                txtActividad.append("[" + repartidor.getNombre() + "] Se interrumpió la entrega.\n");
                Thread.currentThread().interrupt();
                return;
            } catch (IllegalStateException e) {
                txtActividad.append("[" + repartidor.getNombre() + "] Error: " + e.getMessage() + "\n");
            }
        }
    }
}
