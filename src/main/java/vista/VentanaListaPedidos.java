package vista;

import dao.EntregaDAO;
import dao.PedidoDAO;

import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.PedidoComida;
import modelo.PedidoEncomienda;
import modelo.PedidoExpress;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/*
 * Ventana que muestra los pedidos almacenados
 * en la base de datos MySQL
 */
public class VentanaListaPedidos extends JFrame {

    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;

    private JButton botonEditar;
    private JButton botonEliminar;
    private JButton botonActualizar;

    private PedidoDAO pedidoDAO;
    private EntregaDAO entregaDAO;

    public VentanaListaPedidos() {

        pedidoDAO = new PedidoDAO();
        entregaDAO = new EntregaDAO();

        setTitle("SpeedFast - Lista de pedidos");
        setSize(750, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

        botonEditar =
                new JButton("Editar");

        botonEliminar =
                new JButton("Eliminar");

        botonActualizar =
                new JButton("Actualizar");

        JPanel panelInferior =
                new JPanel();

        panelInferior.add(botonEditar);
        panelInferior.add(botonEliminar);
        panelInferior.add(botonActualizar);

        add(scrollTabla, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);


        /*
         * ACTUALIZAR TABLA
         */
        botonActualizar.addActionListener(
                e -> cargarPedidos()
        );


        /*
         * EDITAR PEDIDO
         */
        botonEditar.addActionListener(e -> {

            int filaSeleccionada =
                    tablaPedidos.getSelectedRow();

            if (filaSeleccionada == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Selecciona un pedido para editar."
                );

                return;
            }


            int id =
                    (int) modeloTabla.getValueAt(
                            filaSeleccionada,
                            0
                    );


            String direccionActual =
                    modeloTabla.getValueAt(
                            filaSeleccionada,
                            1
                    ).toString();


            String tipoActual =
                    modeloTabla.getValueAt(
                            filaSeleccionada,
                            2
                    ).toString();


            String estadoActual =
                    modeloTabla.getValueAt(
                            filaSeleccionada,
                            3
                    ).toString();


            String nuevaDireccion =
                    JOptionPane.showInputDialog(
                            this,
                            "Nueva dirección:",
                            direccionActual
                    );


            if (nuevaDireccion == null ||
                    nuevaDireccion.trim().isEmpty()) {

                return;
            }


            String[] tipos = {
                    "COMIDA",
                    "ENCOMIENDA",
                    "EXPRESS"
            };


            String nuevoTipo =
                    (String) JOptionPane.showInputDialog(
                            this,
                            "Selecciona el tipo:",
                            "Editar pedido",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            tipos,
                            tipoActual.toUpperCase()
                    );


            if (nuevoTipo == null) {
                return;
            }


            String[] estados = {
                    "PENDIENTE",
                    "EN_REPARTO",
                    "ENTREGADO"
            };


            String nuevoEstado =
                    (String) JOptionPane.showInputDialog(
                            this,
                            "Selecciona el estado:",
                            "Editar pedido",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            estados,
                            estadoActual
                    );


            if (nuevoEstado == null) {
                return;
            }


            Pedido pedidoActualizado;


            switch (nuevoTipo) {

                case "COMIDA":

                    pedidoActualizado =
                            new PedidoComida(
                                    id,
                                    nuevaDireccion.trim(),
                                    0
                            );

                    break;


                case "ENCOMIENDA":

                    pedidoActualizado =
                            new PedidoEncomienda(
                                    id,
                                    nuevaDireccion.trim(),
                                    0
                            );

                    break;


                case "EXPRESS":

                    pedidoActualizado =
                            new PedidoExpress(
                                    id,
                                    nuevaDireccion.trim(),
                                    0
                            );

                    break;


                default:
                    return;
            }


            pedidoActualizado.setEstado(
                    EstadoPedido.valueOf(nuevoEstado)
            );


            pedidoDAO.update(
                    pedidoActualizado
            );


            cargarPedidos();
        });


        /*
         * ELIMINAR PEDIDO
         */
        botonEliminar.addActionListener(e -> {

            int filaSeleccionada =
                    tablaPedidos.getSelectedRow();

            if (filaSeleccionada == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Selecciona un pedido para eliminar."
                );

                return;
            }


            int id =
                    (int) modeloTabla.getValueAt(
                            filaSeleccionada,
                            0
                    );


            int confirmacion =
                    JOptionPane.showConfirmDialog(
                            this,
                            "¿Eliminar el pedido " + id + "?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION
                    );


            if (confirmacion ==
                    JOptionPane.YES_OPTION) {

                pedidoDAO.delete(id);

                cargarPedidos();
            }
        });


        cargarPedidos();

        setLocationRelativeTo(null);
        setVisible(true);
    }


    /*
     * Consulta los pedidos desde MySQL
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