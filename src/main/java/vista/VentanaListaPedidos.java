package vista;

import dao.PedidoDAO;
import modelo.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import dao.EntregaDAO;

/*
 * Ventana que muestra los pedidos almacenados
 * en la base de datos MySqL
 */
public class VentanaListaPedidos extends JFrame {

    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;
    private JButton botonActualizar;

    private PedidoDAO pedidoDAO;
    private EntregaDAO entregaDAO;

    public VentanaListaPedidos() {

        pedidoDAO = new PedidoDAO();
        entregaDAO = new EntregaDAO();

        setTitle("SpeedFast - Lista de pedidos");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Dirección",
                        "Tipo",
                        "Estado",
                        "Repartidor"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaPedidos = new JTable(modeloTabla);

        JScrollPane scrollTabla =
                new JScrollPane(tablaPedidos);

        botonActualizar =
                new JButton("Actualizar");

        JPanel panelInferior =
                new JPanel();

        panelInferior.add(botonActualizar);

        add(scrollTabla, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        botonActualizar.addActionListener(
                e -> cargarPedidos()
        );

        cargarPedidos();

        setVisible(true);
    }

    /*
     * Consulta los pedidos desde MySqL
     * y actualiza el contenido de la JTable
     */
    private void cargarPedidos() {

        modeloTabla.setRowCount(0);

        List<Pedido> pedidos =
                pedidoDAO.listarTodos();

        for (Pedido pedido : pedidos) {

            String tipo =
                    pedido.getClass()
                            .getSimpleName()
                            .replace("Pedido", "");

            String repartidor =
                    entregaDAO.obtenerNombreRepartidorPorPedido(
                            pedido.getIdPedido()
                    );

            modeloTabla.addRow(
                    new Object[]{
                            pedido.getIdPedido(),
                            pedido.getDireccionEntrega(),
                            tipo,
                            pedido.getEstado(),
                            repartidor
                    }
            );
        }
    }
}