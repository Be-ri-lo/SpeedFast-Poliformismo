package org.speedFast.model;

import org.speedFast.interfaces.Cancelable;
import org.speedFast.interfaces.Despachable;
import org.speedFast.interfaces.Rastreable;
import org.speedFast.strategy.EstrategiaTiempoEntrega;
import org.speedFast.util.EstadoPedido;

/**
 * Pedido con patrón Strategy para el tiempo de entrega.
 *
 * Mira este campo: {@code estrategiaTiempo}.
 * Pedido NO tiene if/else "si soy comida... si soy express...".
 * Delega el cálculo a un objeto que implementa EstrategiaTiempoEntrega.
 *
 * Relación: Pedido *tiene una* estrategia (composición), no *es* la fórmula (herencia).
 */
public abstract class Pedido implements Despachable, Cancelable, Rastreable {

    private int idPedido;
    private String direccionEntrega;
    private double distanciaKm;
    private String repartidor;
    private EstadoPedido estado;

    /**
     * Aquí vive Strategy: el pedido guarda "cómo" calcular el tiempo.
     * Se puede cambiar después con setEstrategiaTiempo(...) sin crear otro Pedido.
     */
    private EstrategiaTiempoEntrega estrategiaTiempo;

    /**
     * @param estrategiaTiempo algoritmo de tiempo que usará este pedido
     */
    public Pedido(int idPedido, String direccionEntrega, double distanciaKm,
                  EstrategiaTiempoEntrega estrategiaTiempo) {
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        setDistanciaKm(distanciaKm);
        this.repartidor = "Sin asignar";
        this.estado = EstadoPedido.RESERVADO;
        this.estrategiaTiempo = estrategiaTiempo;
    }

    public void mostrarResumen() {
        System.out.println(getClass().getSimpleName() + " #" + formatearId());
        System.out.println("Dirección: " + direccionEntrega);
        System.out.println("Distancia: " + distanciaKm + " km");
        System.out.println("Repartidor asignado: " + repartidor);
        System.out.println("Estado: " + estado.getEtiqueta());
        System.out.println("Estrategia de tiempo: " + estrategiaTiempo.getClass().getSimpleName());
        System.out.println("Tiempo estimado: " + calcularTiempoEntrega() + " minutos");
    }

    /**
     * Ya no es abstracto: todas las hijas usan ESTE método.
     * La diferencia no está en la hija, está en la estrategia que le pasaron.
     *
     * @return minutos según la estrategia actual
     */
    public double calcularTiempoEntrega() {
        return estrategiaTiempo.calcular(distanciaKm);
    }

    /**
     * Cambia el algoritmo en tiempo de ejecución.
     * Eso es lo que la herencia sola no hace tan fácil:
     * un PedidoComida podría calcular "como express" sin dejar de ser PedidoComida.
     *
     * @param estrategiaTiempo nueva fórmula a usar
     */
    public void setEstrategiaTiempo(EstrategiaTiempoEntrega estrategiaTiempo) {
        this.estrategiaTiempo = estrategiaTiempo;
    }

    public EstrategiaTiempoEntrega getEstrategiaTiempo() {
        return estrategiaTiempo;
    }

    public abstract void asignarRepartidor();

    public void asignarRepartidor(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del repartidor no puede estar vacío");
        }
        if (estado == EstadoPedido.CANCELADO) {
            throw new IllegalStateException("No se puede asignar un repartidor a un pedido cancelado");
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
        System.out.println(getClass().getSimpleName() + " #" + formatearId()
                + " | Estado: " + estado.getEtiqueta()
                + " | Repartidor: " + repartidor);
    }

    public String formatearId() {
        return String.format("%03d", idPedido);
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(double distanciaKm) {
        if (distanciaKm < 0.1 || distanciaKm > 100) {
            throw new IllegalArgumentException("La distancia de reparto debe estar entre 0.1 km (100 metros) y 100 km");
        }
        this.distanciaKm = distanciaKm;
    }

    public String getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(String repartidor) {
        this.repartidor = repartidor;
        this.estado = EstadoPedido.ASIGNADO;
    }

    public EstadoPedido getEstado() {
        return estado;
    }
}
