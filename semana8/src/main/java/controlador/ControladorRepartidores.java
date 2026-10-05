package controlador;

import dao.RepartidorDAO;
import modelo.Repartidor;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador de repartidores.
 * La vista no habla con MySQL: llama a create, readAll, update o delete
 * y esta clase se lo pide a RepartidorDAO.
 */
public class ControladorRepartidores {

    private final RepartidorDAO repartidorDAO = new RepartidorDAO();
    private final List<Runnable> listeners = new ArrayList<>();

    /** Registra una ventana para que recargue su tabla o combo. */
    public void addCambioListener(Runnable listener) {
        listeners.add(listener);
    }

    /** Quita el listener al cerrar la ventana. */
    public void removeCambioListener(Runnable listener) {
        listeners.remove(listener);
    }

    /** Avisa a las ventanas que la lista de repartidores cambió. */
    public void notificarCambio() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }

    /** INSERT: registra un repartidor (solo el nombre). */
    public void create(Repartidor repartidor) {
        if (repartidor == null) {
            throw new IllegalArgumentException("El repartidor no puede ser nulo");
        }
        repartidorDAO.create(repartidor);
        notificarCambio();
    }

    /** SELECT: lista todos los repartidores (para JTable y JComboBox). */
    public List<Repartidor> readAll() {
        return repartidorDAO.readAll();
    }

    /** UPDATE: cambia el nombre del repartidor seleccionado. */
    public void update(Repartidor repartidor) {
        if (repartidor == null) {
            throw new IllegalArgumentException("Debe seleccionar un repartidor");
        }
        repartidorDAO.update(repartidor);
        notificarCambio();
    }

    /** DELETE: elimina el repartidor. Falla si tiene entregas asociadas. */
    public void delete(int id) {
        repartidorDAO.delete(id);
        notificarCambio();
    }

    /** Busca un repartidor por id. Devuelve null si no existe. */
    public Repartidor buscarPorId(int id) {
        return repartidorDAO.buscarPorId(id);
    }
}
