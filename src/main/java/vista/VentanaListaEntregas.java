package vista;

import dao.EntregaDAO;
import modelo.Entrega;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class VentanaListaEntregas extends JFrame {

    private JTable tablaEntregas;
    private DefaultTableModel modeloTabla;

    private JButton botonEditar;
    private JButton botonEliminar;
    private JButton botonActualizar;

    private EntregaDAO entregaDAO;

    public VentanaListaEntregas() {

        entregaDAO = new EntregaDAO();

        setTitle("SpeedFast - Lista de entregas");
        setSize(750, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "ID Pedido",
                        "ID Repartidor",
                        "Fecha",
                        "Hora"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaEntregas = new JTable(modeloTabla);

        JScrollPane scrollTabla =
                new JScrollPane(tablaEntregas);

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


        botonActualizar.addActionListener(e -> {
            cargarEntregas();
        });


        botonEditar.addActionListener(e -> {

            int filaSeleccionada =
                    tablaEntregas.getSelectedRow();

            if (filaSeleccionada == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Selecciona una entrega para editar."
                );

                return;
            }

            int id =
                    (int) modeloTabla.getValueAt(
                            filaSeleccionada,
                            0
                    );

            int idPedidoActual =
                    (int) modeloTabla.getValueAt(
                            filaSeleccionada,
                            1
                    );

            int idRepartidorActual =
                    (int) modeloTabla.getValueAt(
                            filaSeleccionada,
                            2
                    );

            LocalDate fechaActual =
                    LocalDate.parse(
                            modeloTabla.getValueAt(
                                    filaSeleccionada,
                                    3
                            ).toString()
                    );

            LocalTime horaActual =
                    LocalTime.parse(
                            modeloTabla.getValueAt(
                                    filaSeleccionada,
                                    4
                            ).toString()
                    );


            String nuevoIdPedido =
                    JOptionPane.showInputDialog(
                            this,
                            "ID del pedido:",
                            idPedidoActual
                    );

            if (nuevoIdPedido == null) {
                return;
            }


            String nuevoIdRepartidor =
                    JOptionPane.showInputDialog(
                            this,
                            "ID del repartidor:",
                            idRepartidorActual
                    );

            if (nuevoIdRepartidor == null) {
                return;
            }


            try {

                int idPedido =
                        Integer.parseInt(
                                nuevoIdPedido
                        );

                int idRepartidor =
                        Integer.parseInt(
                                nuevoIdRepartidor
                        );

                Entrega entregaActualizada =
                        new Entrega(
                                id,
                                idPedido,
                                idRepartidor,
                                fechaActual,
                                horaActual
                        );

                entregaDAO.update(
                        entregaActualizada
                );

                cargarEntregas();

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Los ID deben ser números enteros."
                );
            }
        });


        botonEliminar.addActionListener(e -> {

            int filaSeleccionada =
                    tablaEntregas.getSelectedRow();

            if (filaSeleccionada == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Selecciona una entrega para eliminar."
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
                            "¿Eliminar la entrega " + id + "?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION
                    );

            if (confirmacion ==
                    JOptionPane.YES_OPTION) {

                entregaDAO.delete(id);

                cargarEntregas();
            }
        });


        cargarEntregas();

        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void cargarEntregas() {

        modeloTabla.setRowCount(0);

        List<Entrega> entregas =
                entregaDAO.listarTodas();

        for (Entrega entrega : entregas) {

            Object[] fila = {
                    entrega.getId(),
                    entrega.getIdPedido(),
                    entrega.getIdRepartidor(),
                    entrega.getFecha(),
                    entrega.getHora()
            };

            modeloTabla.addRow(fila);
        }
    }
}