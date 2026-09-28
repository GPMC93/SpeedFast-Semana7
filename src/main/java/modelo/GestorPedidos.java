package modelo;

import java.util.ArrayList;

/*
 * Gestiona los pedidos registrados en la aplicación.
 * La misma instancia será compartida por las distintas ventanas.
 */
public class GestorPedidos {

    private ArrayList<Pedido> pedidos;

    public GestorPedidos() {
        pedidos = new ArrayList<>();
    }

    public void agregarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }

    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }

    public boolean existePedido(int idPedido) {

        for (Pedido pedido : pedidos) {
            if (pedido.getIdPedido() == idPedido) {
                return true;
            }
        }

        return false;
    }
}