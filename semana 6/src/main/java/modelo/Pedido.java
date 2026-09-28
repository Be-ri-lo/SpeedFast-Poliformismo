package modelo;

/**
 * Clase abstracta base de los pedidos SpeedFast.
 */
public abstract class Pedido implements Despachable, Cancelable, Rastreable {

    private int idPedido;
    private String direccionEntrega;
    private double distanciaKm;
    private String repartidor;
    private EstadoPedido estado;

    public Pedido(int idPedido, String direccionEntrega, double distanciaKm) {
        if (idPedido <= 0) {
            throw new IllegalArgumentException("El ID del pedido debe ser un número mayor que 0");
        }
        if (direccionEntrega == null || direccionEntrega.isBlank()) {
            throw new IllegalArgumentException("La dirección de entrega es obligatoria");
        }
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega.trim();
        setDistanciaKm(distanciaKm);
        this.repartidor = "Sin asignar";
        this.estado = EstadoPedido.RESERVADO;
    }

    public abstract double calcularTiempoEntrega();

    public abstract void asignarRepartidor();

    public abstract String getTipo();

    public void asignarRepartidor(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del repartidor no puede estar vacío");
        }
        if (estado == EstadoPedido.CANCELADO) {
            throw new IllegalStateException("No se puede asignar un repartidor a un pedido cancelado");
        }
        if (estado == EstadoPedido.DESPACHADO) {
            throw new IllegalStateException("No se puede reasignar un pedido ya despachado");
        }
        this.repartidor = nombre.trim();
        this.estado = EstadoPedido.ASIGNADO;
    }

    @Override
    public void despachar() {
        if (estado == EstadoPedido.CANCELADO) {
            throw new IllegalStateException("No se puede despachar un pedido cancelado");
        }
        if (estado != EstadoPedido.ASIGNADO) {
            throw new IllegalStateException("Debe asignarse un repartidor antes de despachar");
        }
        this.estado = EstadoPedido.DESPACHADO;
    }

    @Override
    public void cancelar() {
        if (estado == EstadoPedido.DESPACHADO) {
            throw new IllegalStateException("No se puede cancelar un pedido ya despachado");
        }
        if (estado == EstadoPedido.CANCELADO) {
            throw new IllegalStateException("El pedido ya está cancelado");
        }
        this.estado = EstadoPedido.CANCELADO;
    }

    @Override
    public void verHistorial() {
        System.out.println(getTipo() + " #" + formatearId()
                + " | Estado: " + estado.getEtiqueta()
                + " | Repartidor: " + repartidor);
    }

    public void mostrarResumen() {
        System.out.println(getClass().getSimpleName() + " #" + formatearId());
        System.out.println("Dirección: " + direccionEntrega);
        System.out.println("Distancia: " + distanciaKm + " km");
        System.out.println("Repartidor asignado: " + repartidor);
        System.out.println("Estado: " + estado.getEtiqueta());
        System.out.println("Tiempo estimado: " + calcularTiempoEntrega() + " minutos");
    }

    public String formatearId() {
        return String.format("%03d", idPedido);
    }

    public void setDistanciaKm(double distanciaKm) {
        if (distanciaKm < 0.1 || distanciaKm > 100) {
            throw new IllegalArgumentException("La distancia de reparto debe estar entre 0.1 km y 100 km");
        }
        this.distanciaKm = distanciaKm;
    }

    public void setRepartidor(String repartidor) {
        this.repartidor = repartidor;
        this.estado = EstadoPedido.ASIGNADO;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public String getRepartidor() {
        return repartidor;
    }

    public EstadoPedido getEstado() {
        return estado;
    }
}
