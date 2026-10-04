package dao;

import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.PedidoComida;
import modelo.PedidoEncomienda;
import modelo.PedidoExpress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    /*
     * CREATE
     * Guarda un nuevo pedido en MySQL
     * Retorna true si se guardó correctamente
     * y false si ocurrió un error
     */
    public boolean guardar(Pedido pedido) {

        String sql =
                "INSERT INTO pedido (direccion, tipo, estado) VALUES (?, ?, ?)";

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setString(1, pedido.getDireccionEntrega());
            ps.setString(2, obtenerTipo(pedido));
            ps.setString(3, pedido.getEstado().name());

            int filasAfectadas = ps.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al guardar pedido: "
                            + e.getMessage()
            );

            return false;
        }
    }

    /*
     * CREATE
     * Nombre solicitado por la pauta de Semana 8
     * Reutiliza guardar() para no duplicar código
     */
    public boolean create(Pedido pedido) {

        return guardar(pedido);
    }


    /*
     * READ
     * Consulta todos los pedidos almacenados
     */
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

                String direccion =
                        rs.getString("direccion");

                String tipo =
                        rs.getString("tipo");

                String estado =
                        rs.getString("estado");

                Pedido pedido;

                switch (tipo) {

                    case "COMIDA":
                        pedido =
                                new PedidoComida(
                                        id,
                                        direccion,
                                        0
                                );
                        break;

                    case "ENCOMIENDA":
                        pedido =
                                new PedidoEncomienda(
                                        id,
                                        direccion,
                                        0
                                );
                        break;

                    case "EXPRESS":
                        pedido =
                                new PedidoExpress(
                                        id,
                                        direccion,
                                        0
                                );
                        break;

                    default:
                        continue;
                }

                pedido.setEstado(
                        EstadoPedido.valueOf(estado)
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

    /*
     * READ
     * Nombre solicitado por la pauta.
     */
    public List<Pedido> readAll() {

        return listarTodos();
    }


    /*
     * UPDATE COMPLETO
     * Modifica dirección, tipo y estado
     */
    public void update(Pedido pedido) {

        String sql =
                "UPDATE pedido " +
                        "SET direccion = ?, tipo = ?, estado = ? " +
                        "WHERE id = ?";

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    pedido.getDireccionEntrega()
            );

            ps.setString(
                    2,
                    obtenerTipo(pedido)
            );

            ps.setString(
                    3,
                    pedido.getEstado().name()
            );

            ps.setInt(
                    4,
                    pedido.getIdPedido()
            );

            int filasAfectadas =
                    ps.executeUpdate();

            if (filasAfectadas > 0) {

                System.out.println(
                        "Pedido actualizado correctamente."
                );

            } else {

                System.out.println(
                        "No se encontró el pedido."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar pedido: "
                            + e.getMessage()
            );
        }
    }


    /*
     * Se mantiene porque la asignación de repartidor
     * cambia el pedido a EN_REPARTO
     */
    public void actualizarEstado(
            int idPedido,
            String nuevoEstado
    ) {

        String sql =
                "UPDATE pedido SET estado = ? WHERE id = ?";

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    nuevoEstado
            );

            ps.setInt(
                    2,
                    idPedido
            );

            ps.executeUpdate();

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar estado del pedido: "
                            + e.getMessage()
            );
        }
    }


    /*
     * DELETE
     * Elimina un pedido utilizando su ID
     */
    public void delete(int idPedido) {

        String sql =
                "DELETE FROM pedido WHERE id = ?";

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    idPedido
            );

            int filasAfectadas =
                    ps.executeUpdate();

            if (filasAfectadas > 0) {

                System.out.println(
                        "Pedido eliminado correctamente."
                );

            } else {

                System.out.println(
                        "No se encontró el pedido."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al eliminar pedido: "
                            + e.getMessage()
            );
        }
    }


    /*
     * Método auxiliar.
     * Determina qué tipo de pedido es el objeto recibido
     */
    private String obtenerTipo(Pedido pedido) {

        if (pedido instanceof PedidoComida) {

            return "COMIDA";

        } else if (pedido instanceof PedidoEncomienda) {

            return "ENCOMIENDA";

        } else if (pedido instanceof PedidoExpress) {

            return "EXPRESS";
        }

        return "";
    }
}