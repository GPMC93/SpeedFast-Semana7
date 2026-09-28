package vista;

import modelo.EstadoPedido;
import modelo.GestorPedidos;
import modelo.Pedido;

import javax.swing.*;
import java.awt.*;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import modelo.Repartidor;

import java.util.List;

/*
 * Ventana principalde SpeedFast
 * Permite acceder al registro, listado y gestión de entregas
 */
public class VentanaPrincipal extends JFrame {

    private GestorPedidos gestorPedidos;

    private JButton botonRegistrar;
    private JButton botonListar;
    private JButton botonEntrega;
    private JButton botonRegistrarRepartidor;

    private PedidoDAO pedidoDAO;
    private RepartidorDAO repartidorDAO;
    private EntregaDAO entregaDAO;

    public VentanaPrincipal() {

        gestorPedidos = new GestorPedidos();
        pedidoDAO = new PedidoDAO();
        repartidorDAO = new RepartidorDAO();
        entregaDAO = new EntregaDAO();

        setTitle("SpeedFast - Gestión de pedidos");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel(
                "Sistema de Gestión SpeedFast",
                SwingConstants.CENTER
        );

        JPanel panelBotones = new JPanel(
                new GridLayout(4, 1, 10, 10)
        );

        panelBotones.setBorder(
                BorderFactory.createEmptyBorder(20, 50, 30, 50)
        );

        botonRegistrar = new JButton("Registrar pedido");

        botonRegistrarRepartidor =
                new JButton("Registrar repartidor");

        botonListar = new JButton("Listar pedidos");

        botonEntrega = new JButton(
                "Asignar repartidor / Iniciar entrega"
        );

        panelBotones.add(botonRegistrar);
        panelBotones.add(botonRegistrarRepartidor);
        panelBotones.add(botonListar);
        panelBotones.add(botonEntrega);

        add(titulo, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);

        botonRegistrar.addActionListener(
                e -> new VentanaRegistroPedido()
        );

        botonListar.addActionListener(
                e -> new VentanaListaPedidos()
        );

        botonEntrega.addActionListener(
                e -> iniciarEntrega()
        );

        botonRegistrarRepartidor.addActionListener(
                e -> new VentanaRegistroRepartidor()
        );

        setVisible(true);
    }

    /*
     * Permite seleccionar un pedido pendiente
     * asignar un repartidor e iniciar su entrega
     */
    private void iniciarEntrega() {

        List<Pedido> pedidos = pedidoDAO.listarTodos();

        if (pedidos.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No existen pedidos registrados.",
                    "SpeedFast",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String textoId = JOptionPane.showInputDialog(
                this,
                "Ingrese el ID del pedido:"
        );

        if (textoId == null) {
            return;
        }

        int idPedido;

        try {
            idPedido = Integer.parseInt(textoId);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "El ID debe ser un número entero.",
                    "ID inválido",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Pedido pedidoEncontrado = null;

        for (Pedido pedido : pedidos) {

            if (pedido.getIdPedido() == idPedido) {
                pedidoEncontrado = pedido;
                break;
            }
        }

        if (pedidoEncontrado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No existe un pedido con ese ID.",
                    "Pedido no encontrado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (pedidoEncontrado.getEstado()
                != modelo.EstadoPedido.PENDIENTE) {

            JOptionPane.showMessageDialog(
                    this,
                    "El pedido no está pendiente.",
                    "Entrega no disponible",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        List<Repartidor> repartidores =
                repartidorDAO.listarTodos();

        if (repartidores.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No existen repartidores registrados.",
                    "SpeedFast",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Repartidor repartidorSeleccionado =
                (Repartidor) JOptionPane.showInputDialog(
                        this,
                        "Seleccione un repartidor:",
                        "Asignar repartidor",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        repartidores.toArray(),
                        repartidores.get(0)
                );

        if (repartidorSeleccionado == null) {
            return;
        }

        modelo.Entrega entrega = new modelo.Entrega(
                pedidoEncontrado.getIdPedido(),
                repartidorSeleccionado.getId()
        );

        entregaDAO.guardar(entrega);

        pedidoDAO.actualizarEstado(
                pedidoEncontrado.getIdPedido(),
                "EN_REPARTO"
        );

        JOptionPane.showMessageDialog(
                this,
                "Repartidor asignado correctamente.\n"
                        + "Pedido #"
                        + pedidoEncontrado.getIdPedido()
                        + " ahora está EN REPARTO.\n"
                        + "Repartidor: "
                        + repartidorSeleccionado.getNombre(),
                "Entrega iniciada",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}