package dao;

import modelo.Pedido;
import modelo.PedidoComida;
import modelo.PedidoEncomienda;
import modelo.PedidoExpress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/*
 * Gestiona las operaciones de base de datos
 * relacionadas con los pedidos
 */
public class PedidoDAO {

    public void guardar(Pedido pedido) {

        String sql =
                "INSERT INTO pedido (direccion, tipo, estado) VALUES (?, ?, ?)";

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setString(1, pedido.getDireccionEntrega());
            ps.setString(2, obtenerTipo(pedido));
            ps.setString(3, pedido.getEstado().name());

            ps.executeUpdate();

            System.out.println("Pedido guardado correctamente en MySQL.");

        } catch (SQLException e) {

            System.out.println(
                    "Error al guardar pedido: " + e.getMessage()
            );
        }
    }

    private String obtenerTipo(Pedido pedido) {

        if (pedido instanceof PedidoComida) {
            return "COMIDA";
        }

        if (pedido instanceof PedidoEncomienda) {
            return "ENCOMIENDA";
        }

        if (pedido instanceof PedidoExpress) {
            return "EXPRESS";
        }

        return "DESCONOCIDO";
    }

    public List<Pedido> listarTodos() {

        List<Pedido> pedidos = new ArrayList<>();

        String sql =
                "SELECT id, direccion, tipo, estado FROM pedido";

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement ps = conexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                int id = rs.getInt("id");
                String direccion = rs.getString("direccion");
                String tipo = rs.getString("tipo");
                String estado = rs.getString("estado");

                Pedido pedido;

                if ("COMIDA".equals(tipo)) {

                    pedido = new PedidoComida(
                            id,
                            direccion,
                            0
                    );

                } else if ("ENCOMIENDA".equals(tipo)) {

                    pedido = new PedidoEncomienda(
                            id,
                            direccion,
                            0
                    );

                } else {

                    pedido = new PedidoExpress(
                            id,
                            direccion,
                            0
                    );
                }

                pedido.setEstado(
                        modelo.EstadoPedido.valueOf(estado)
                );

                pedidos.add(pedido);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar pedidos: "
                            + e.getMessage()
            );
        }

        return pedidos;
    }

    public void actualizarEstado(int idPedido, String nuevoEstado) {

        String sql =
                "UPDATE pedido SET estado = ? WHERE id = ?";

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idPedido);

            ps.executeUpdate();

            System.out.println(
                    "Estado del pedido actualizado correctamente."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar estado: "
                            + e.getMessage()
            );
        }
    }
}