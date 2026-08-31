package org.speedFast.model;

import org.speedFast.strategy.TiempoEncomienda;

/**
 * Pedido de encomienda.
 * La fórmula de tiempo está en TiempoEncomienda.
 * peso y fragil siguen siendo datos propios de esta clase (no son Strategy).
 */
public class PedidoEncomienda extends Pedido {

    private double peso;
    private boolean fragil;

    public PedidoEncomienda(int idPedido, String direccionEntrega, double distanciaKm,
                            double peso, boolean fragil) {
        super(idPedido, direccionEntrega, distanciaKm, new TiempoEncomienda());
        setPeso(peso);
        setFragil(fragil);
    }

    @Override
    public void asignarRepartidor() {
        setRepartidor("Daniela Tapia");
    }

    public void setFragil(boolean fragil) {
        this.fragil = fragil;
    }

    public boolean isFragil() {
        return fragil;
    }

    public void setPeso(double peso) {
        if (peso <= 0) {
            throw new IllegalArgumentException("El peso de la encomienda debe ser mayor a 0 kg");
        }
        this.peso = peso;
    }

    public double getPeso() {
        return peso;
    }

    public boolean esPesado() {
        return peso > 20;
    }
}
