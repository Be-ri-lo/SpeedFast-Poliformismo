package modelo;

/**
 * Pedido de encomienda.
 * Tiempo: 20 minutos base + 1.5 minutos por kilómetro, redondeado a entero.
 */
public class PedidoEncomienda extends Pedido {

    private double peso;
    private boolean fragil;

    public PedidoEncomienda(int idPedido, String direccionEntrega, double distanciaKm,
                            double peso, boolean fragil) {
        super(idPedido, direccionEntrega, distanciaKm);
        setPeso(peso);
        this.fragil = fragil;
    }

    @Override
    public double calcularTiempoEntrega() {
        return Math.round(20 + (1.5 * getDistanciaKm()));
    }

    @Override
    public void asignarRepartidor() {
        setRepartidor("Daniela Tapia");
    }

    @Override
    public String getTipo() {
        return "Encomienda";
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

    public boolean isFragil() {
        return fragil;
    }

    public boolean esPesado() {
        return peso > 20;
    }
}
