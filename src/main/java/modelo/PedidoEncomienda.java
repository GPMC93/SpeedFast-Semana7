package modelo;

public class PedidoEncomienda extends Pedido {

    public PedidoEncomienda(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm);
    }

    @Override
    public int calcularTiempoEntrega() {
        double tiempo = 20 + 1.5 * getDistanciaKm();
        return (int) Math.round(tiempo);
    }
}