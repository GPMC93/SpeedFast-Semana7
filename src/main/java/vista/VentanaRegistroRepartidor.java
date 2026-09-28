package vista;

import dao.RepartidorDAO;

import javax.swing.*;
import java.awt.*;

/*
 * Ventana que permite registrar repartidores
 * directamente en la base de datos MySQl
 */
public class VentanaRegistroRepartidor extends JFrame {

    private JTextField campoNombre;
    private JButton botonGuardar;

    private RepartidorDAO repartidorDAO;

    public VentanaRegistroRepartidor() {

        repartidorDAO = new RepartidorDAO();

        setTitle("SpeedFast - Registrar repartidor");
        setSize(380, 180);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelFormulario =
                new JPanel(new GridLayout(2, 2, 10, 10));

        panelFormulario.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        campoNombre = new JTextField();
        botonGuardar = new JButton("Guardar");

        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(campoNombre);

        panelFormulario.add(new JLabel(""));
        panelFormulario.add(botonGuardar);

        add(panelFormulario);

        botonGuardar.addActionListener(
                e -> guardarRepartidor()
        );

        setVisible(true);
    }

    private void guardarRepartidor() {

        String nombre =
                campoNombre.getText().trim();

        if (nombre.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar el nombre del repartidor.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        repartidorDAO.guardar(nombre);

        JOptionPane.showMessageDialog(
                this,
                "Repartidor registrado correctamente.",
                "SpeedFast",
                JOptionPane.INFORMATION_MESSAGE
        );

        campoNombre.setText("");
    }
}