package org.speedFast.model;

/**
 * Pedido de encomienda.
 * Tiempo: 20 minutos base + 1.5 minutos por kilómetro, redondeado a entero.
 * Incluye peso y si el bulto es frágil.
 */
public class PedidoEncomienda extends Pedido {

    private double peso;
    private boolean fragil;

    /**
     * @param idPedido         identificador del pedido
     * @param direccionEntrega dirección de destino
     * @param distanciaKm      distancia en kilómetros
     * @param peso             peso del bulto en kilogramos
     * @param fragil           true si el contenido es frágil
     */
    public PedidoEncomienda(int idPedido, String direccionEntrega, double distanciaKm,
                            double peso, boolean fragil) {
        super(idPedido, direccionEntrega, distanciaKm);
        setPeso(peso);
        setFragil(fragil);
    }

    /**
     * {@inheritDoc}
     * Fórmula: round(20 + (1.5 * km)).
     */
    @Override
    public double calcularTiempoEntrega() {
        return Math.round(20 + (1.5 * getDistanciaKm()));
    }

    /**
     * Asignación automática de repartidor para encomienda.
     */
    @Override
    public void asignarRepartidor() {
        setRepartidor("Daniela Tapia");
    }

    /**
     * Valida y registra si el bulto es frágil.
     *
     * @param fragil true si requiere manipulación cuidadosa
     */
    public void setFragil(boolean fragil) {
        this.fragil = fragil;
    }

    /**
     * @return true si el bulto está marcado como frágil
     */
    public boolean isFragil() {
        return fragil;
    }

    /**
     * Valida el peso del bulto.
     *
     * @param peso peso en kilogramos, mayor que 0
     * @throws IllegalArgumentException si el peso no es válido
     */
    public void setPeso(double peso) {
        if (peso <= 0) {
            throw new IllegalArgumentException("El peso de la encomienda debe ser mayor a 0 kg");
        }
        this.peso = peso;
    }

    /**
     * @return peso del bulto en kilogramos
     */
    public double getPeso() {
        return peso;
    }

    /**
     * Identifica si el bulto es pesado (más de 20 kg).
     *
     * @return true si el peso es mayor a 20 kg
     */
    public boolean esPesado() {
        return peso > 20;
    }
}
