package modelo;

/**
 * Resumen del día: pedidos asignados hoy y si el repartidor está libre.
 * La cola ordena libres primero y, si todos tienen carga, el que lleva menos.
 */
public class CargaRepartidor {

    private final Repartidor repartidor;
    private final int pedidosHoy;
    private final int enReparto;

    public CargaRepartidor(Repartidor repartidor, int pedidosHoy, int enReparto) {
        this.repartidor = repartidor;
        this.pedidosHoy = pedidosHoy;
        this.enReparto = enReparto;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public int getPedidosHoy() {
        return pedidosHoy;
    }

    public boolean estaLibre() {
        return enReparto == 0;
    }

    public String getEstado() {
        return estaLibre() ? "Libre" : "En ruta (" + enReparto + ")";
    }

    @Override
    public String toString() {
        return repartidor.getNombre() + " · " + getEstado() + " · " + pedidosHoy + " hoy";
    }
}
