package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entrega: une un pedido con un repartidor, fecha y hora.
 * Dirección y nombre se rellenan con JOIN solo para mostrarlos en la tabla.
 */
public class Entrega {

    private int id;
    private int idPedido;
    private int idRepartidor;
    private LocalDate fecha;
    private LocalTime hora;

    private String direccionPedido;
    private String nombreRepartidor;

    public Entrega(int idPedido, int idRepartidor, LocalDate fecha, LocalTime hora) {
        this.idPedido = idPedido;
        this.idRepartidor = idRepartidor;
        this.fecha = fecha;
        this.hora = hora;
    }

    public Entrega(int id, int idPedido, int idRepartidor, LocalDate fecha, LocalTime hora,
                   String direccionPedido, String nombreRepartidor) {
        this.id = id;
        this.idPedido = idPedido;
        this.idRepartidor = idRepartidor;
        this.fecha = fecha;
        this.hora = hora;
        this.direccionPedido = direccionPedido;
        this.nombreRepartidor = nombreRepartidor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdRepartidor() {
        return idRepartidor;
    }

    public void setIdRepartidor(int idRepartidor) {
        this.idRepartidor = idRepartidor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getDireccionPedido() {
        return direccionPedido;
    }

    public String getNombreRepartidor() {
        return nombreRepartidor;
    }
}
