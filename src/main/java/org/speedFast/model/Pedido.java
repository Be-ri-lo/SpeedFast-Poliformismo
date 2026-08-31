package org.speedFast.model;

public abstract class Pedido {

    private int idPedido;
    private String direccionEntrega;
    private double distanciaKm;

    public Pedido(int idPedido, String direccionEntrega, double distanciaKm) {
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        setDistanciaKm(distanciaKm);
    }

    //metodo concreto
    public void mostrarResumen() {
        System.out.println(getClass().getSimpleName() + " #" + String.format("%03d", idPedido));
        System.out.println("Dirección: " + direccionEntrega);
        System.out.println("Distancia: " + distanciaKm + " km");
    }

    //metodo abstracto
    public abstract double calcularTiempoEntrega();

    //getter and setter
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
}
