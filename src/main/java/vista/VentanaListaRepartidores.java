package vista;

import dao.RepartidorDAO;
import modelo.Repartidor;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.util.List;

public class VentanaListaRepartidores extends JFrame {

    private JTable tablaRepartidores;
    private DefaultTableModel modeloTabla;

    private JButton botonEditar;
    private JButton botonEliminar;
    private JButton botonActualizar;

    public VentanaListaRepartidores() {

        setTitle("SpeedFast - Lista de repartidores");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        modeloTabla = new DefaultTableModel();

        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");

        tablaRepartidores = new JTable(modeloTabla);

        JScrollPane scrollPane =
                new JScrollPane(tablaRepartidores);

        add(scrollPane, BorderLayout.CENTER);

        botonEditar = new JButton("Editar");
        botonEliminar = new JButton("Eliminar");
        botonActualizar = new JButton("Actualizar");

        JPanel panelBotones = new JPanel();

        panelBotones.add(botonEditar);
        panelBotones.add(botonEliminar);
        panelBotones.add(botonActualizar);

        add(panelBotones, BorderLayout.SOUTH);

        botonActualizar.addActionListener(e -> {

            cargarRepartidores();
        });

        botonEditar.addActionListener(e -> {

            int filaSeleccionada =
                    tablaRepartidores.getSelectedRow();

            if (filaSeleccionada == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Selecciona un repartidor para editar."
                );

                return;
            }

            int id =
                    (int) modeloTabla.getValueAt(
                            filaSeleccionada,
                            0
                    );

            String nombreActual =
                    modeloTabla.getValueAt(
                            filaSeleccionada,
                            1
                    ).toString();

            String nuevoNombre =
                    JOptionPane.showInputDialog(
                            this,
                            "Nuevo nombre:",
                            nombreActual
                    );

            if (nuevoNombre != null &&
                    !nuevoNombre.trim().isEmpty()) {

                RepartidorDAO repartidorDAO =
                        new RepartidorDAO();

                repartidorDAO.actualizar(
                        id,
                        nuevoNombre.trim()
                );

                cargarRepartidores();
            }
        });

        botonEliminar.addActionListener(e -> {

            int filaSeleccionada =
                    tablaRepartidores.getSelectedRow();

            if (filaSeleccionada == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Selecciona un repartidor para eliminar."
                );

                return;
            }

            int id =
                    Integer.parseInt(
                            modeloTabla.getValueAt(
                                    filaSeleccionada,
                                    0
                            ).toString()
                    );

            String nombre =
                    modeloTabla.getValueAt(
                            filaSeleccionada,
                            1
                    ).toString();

            int confirmacion =
                    JOptionPane.showConfirmDialog(
                            this,
                            "¿Eliminar al repartidor " + nombre + "?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION
                    );

            if (confirmacion == JOptionPane.YES_OPTION) {

                RepartidorDAO repartidorDAO =
                        new RepartidorDAO();

                repartidorDAO.eliminar(id);

                cargarRepartidores();
            }
        });

        cargarRepartidores();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void cargarRepartidores() {

        modeloTabla.setRowCount(0);

        RepartidorDAO repartidorDAO =
                new RepartidorDAO();

        List<Repartidor> repartidores =
                repartidorDAO.listarTodos();

        for (Repartidor repartidor : repartidores) {

            Object[] fila = {
                    repartidor.getId(),
                    repartidor.getNombre()
            };

            modeloTabla.addRow(fila);
        }
    }
}