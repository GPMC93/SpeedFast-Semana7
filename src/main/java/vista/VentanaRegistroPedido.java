package vista;

import dao.PedidoDAO;
import modelo.Pedido;
import modelo.PedidoComida;
import modelo.PedidoEncomienda;
import modelo.PedidoExpress;

import javax.swing.*;
import java.awt.*;

/*
 * Ventana que permite registrar nuevos pedidos
 * almacenarlos en la base de datos MySqL
 */
public class VentanaRegistroPedido extends JFrame {

    private JTextField campoDireccion;
    private JComboBox<String> comboTipo;
    private JButton botonGuardar;

    private PedidoDAO pedidoDAO;

    public VentanaRegistroPedido() {

        pedidoDAO = new PedidoDAO();

        setTitle("SpeedFast - Registrar pedido");
        setSize(400, 220);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelFormulario =
                new JPanel(new GridLayout(3, 2, 10, 10));

        panelFormulario.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        campoDireccion = new JTextField();

        comboTipo = new JComboBox<>(new String[]{
                "Comida",
                "Encomienda",
                "Express"
        });

        botonGuardar = new JButton("Guardar");

        panelFormulario.add(new JLabel("Dirección:"));
        panelFormulario.add(campoDireccion);

        panelFormulario.add(new JLabel("Tipo:"));
        panelFormulario.add(comboTipo);

        panelFormulario.add(new JLabel(""));
        panelFormulario.add(botonGuardar);

        add(panelFormulario);

        botonGuardar.addActionListener(
                e -> guardarPedido()
        );

        setVisible(true);
    }

    /*
     * Valida los datos y registra el pedido
     * en la base de datos mediante PedidoDAO.
     */
    private void guardarPedido() {

        String direccion =
                campoDireccion.getText().trim();

        if (direccion.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar una dirección.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String tipo =
                (String) comboTipo.getSelectedItem();

        Pedido pedido;

        if ("Comida".equals(tipo)) {

            pedido =
                    new PedidoComida(
                            0,
                            direccion,
                            0
                    );

        } else if ("Encomienda".equals(tipo)) {

            pedido =
                    new PedidoEncomienda(
                            0,
                            direccion,
                            0
                    );

        } else {

            pedido =
                    new PedidoExpress(
                            0,
                            direccion,
                            0
                    );
        }

        pedidoDAO.guardar(pedido);

        JOptionPane.showMessageDialog(
                this,
                "Pedido registrado correctamente.",
                "SpeedFast",
                JOptionPane.INFORMATION_MESSAGE
        );

        campoDireccion.setText("");
        comboTipo.setSelectedIndex(0);
    }
}