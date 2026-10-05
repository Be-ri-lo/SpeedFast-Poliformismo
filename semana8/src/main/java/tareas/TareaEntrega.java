package tareas;

import controlador.ControladorEntregas;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.Repartidor;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.util.Random;

/**
 * Hilo que simula el viaje: espera, despacha el pedido y actualiza MySQL.
 * El texto de la ventana se escribe en el hilo de Swing.
 */
public class TareaEntrega implements Runnable {

    private final Repartidor repartidor;
    private final JTextArea txtActividad;
    private final ControladorEntregas controladorEntregas;
    private final Random random = new Random();

    public TareaEntrega(Repartidor repartidor, JTextArea txtActividad,
                        ControladorEntregas controladorEntregas) {
        this.repartidor = repartidor;
        this.txtActividad = txtActividad;
        this.controladorEntregas = controladorEntregas;
    }

    @Override
    public void run() {
        for (Pedido pedido : repartidor.getPedidosAsignados()) {
            try {
                if (pedido.getEstado() == EstadoPedido.CANCELADO) {
                    escribir("[" + repartidor.getNombre() + "] Pedido #"
                            + pedido.formatearId() + " cancelado. No se entrega.\n");
                    continue;
                }

                escribir("[" + repartidor.getNombre() + "] Entregando "
                        + pedido.getTipo() + " #" + pedido.formatearId() + "...\n");

                Thread.sleep(1000 + random.nextInt(2000));

                pedido.despachar();
                controladorEntregas.marcarDespachado(pedido);
                escribir("[" + repartidor.getNombre() + "] Pedido #"
                        + pedido.formatearId() + " entregado.\n");
            } catch (InterruptedException e) {
                escribir("[" + repartidor.getNombre() + "] Se interrumpió la entrega.\n");
                Thread.currentThread().interrupt();
                return;
            } catch (IllegalStateException e) {
                escribir("[" + repartidor.getNombre() + "] Error: " + e.getMessage() + "\n");
            }
        }
    }

    private void escribir(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            txtActividad.append(mensaje);
            txtActividad.setCaretPosition(txtActividad.getDocument().getLength());
        });
    }
}
