package dao;

import modelo.Entrega;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;

import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {

    /*
     * CREATE
     * Guarda una entrega en la base de datos.
     */
    public void guardar(Entrega entrega) {

        String sql =
                "INSERT INTO entrega " +
                        "(id_pedido, id_repartidor, fecha, hora) " +
                        "VALUES (?, ?, ?, ?)";

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    entrega.getIdPedido()
            );

            ps.setInt(
                    2,
                    entrega.getIdRepartidor()
            );

            ps.setDate(
                    3,
                    Date.valueOf(entrega.getFecha())
            );

            ps.setTime(
                    4,
                    Time.valueOf(entrega.getHora())
            );

            ps.executeUpdate();

            System.out.println(
                    "Entrega guardada correctamente."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Error al guardar entrega: "
                            + e.getMessage()
            );
        }
    }


    /*
     * CREATE con nombre solicitado por Semana 8.
     */
    public void create(Entrega entrega) {

        guardar(entrega);
    }


    /*
     * READ
     * Obtiene todas las entregas almacenadas.
     */
    public List<Entrega> listarTodas() {

        List<Entrega> entregas =
                new ArrayList<>();

        String sql =
                "SELECT id, id_pedido, id_repartidor, fecha, hora " +
                        "FROM entrega";

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement ps =
                        conexion.prepareStatement(sql);
                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                int id =
                        rs.getInt("id");

                int idPedido =
                        rs.getInt("id_pedido");

                int idRepartidor =
                        rs.getInt("id_repartidor");

                Date fechaSQL =
                        rs.getDate("fecha");

                Time horaSQL =
                        rs.getTime("hora");

                Entrega entrega =
                        new Entrega(
                                id,
                                idPedido,
                                idRepartidor,
                                fechaSQL.toLocalDate(),
                                horaSQL.toLocalTime()
                        );

                entregas.add(entrega);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar entregas: "
                            + e.getMessage()
            );
        }

        return entregas;
    }


    /*
     * READ con nombre solicitado por la pauta.
     */
    public List<Entrega> readAll() {

        return listarTodas();
    }


    /*
     * UPDATE
     * Actualiza los datos de una entrega.
     */
    public void update(Entrega entrega) {

        String sql =
                "UPDATE entrega " +
                        "SET id_pedido = ?, " +
                        "id_repartidor = ?, " +
                        "fecha = ?, " +
                        "hora = ? " +
                        "WHERE id = ?";

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    entrega.getIdPedido()
            );

            ps.setInt(
                    2,
                    entrega.getIdRepartidor()
            );

            ps.setDate(
                    3,
                    Date.valueOf(entrega.getFecha())
            );

            ps.setTime(
                    4,
                    Time.valueOf(entrega.getHora())
            );

            ps.setInt(
                    5,
                    entrega.getId()
            );

            int filasAfectadas =
                    ps.executeUpdate();

            if (filasAfectadas > 0) {

                System.out.println(
                        "Entrega actualizada correctamente."
                );

            } else {

                System.out.println(
                        "No se encontró la entrega."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar entrega: "
                            + e.getMessage()
            );
        }
    }


    /*
     * DELETE
     * Elimina una entrega utilizando su ID.
     */
    public void delete(int idEntrega) {

        String sql =
                "DELETE FROM entrega WHERE id = ?";

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    idEntrega
            );

            int filasAfectadas =
                    ps.executeUpdate();

            if (filasAfectadas > 0) {

                System.out.println(
                        "Entrega eliminada correctamente."
                );

            } else {

                System.out.println(
                        "No se encontró la entrega."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al eliminar entrega: "
                            + e.getMessage()
            );
        }
    }


    /*
     * Permite mostrar el nombre del repartidor
     * asociado a un pedido.
     */
    public String obtenerNombreRepartidorPorPedido(
            int idPedido
    ) {

        String sql =
                "SELECT r.nombre " +
                        "FROM entrega e " +
                        "JOIN repartidor r " +
                        "ON e.id_repartidor = r.id " +
                        "WHERE e.id_pedido = ? " +
                        "ORDER BY e.id DESC " +
                        "LIMIT 1";

        try (
                Connection conexion =
                        ConexionBD.conectar();

                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    idPedido
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    return rs.getString(
                            "nombre"
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al buscar repartidor: "
                            + e.getMessage()
            );
        }

        return "Sin asignar";
    }


    /*
     * TRANSACCIÓN JDBC
     *
     * Inicia una entrega realizando dos operaciones:
     *
     * 1. INSERT en entrega.
     * 2. UPDATE del pedido a EN_REPARTO.
     *
     * Ambas utilizan la MISMA Connection.
     *
     * Si las dos funcionan -> COMMIT.
     * Si alguna falla -> ROLLBACK.
     */
    public boolean iniciarEntrega(Entrega entrega) {

        Connection conexion = null;

        String sqlEntrega =
                "INSERT INTO entrega " +
                        "(id_pedido, id_repartidor, fecha, hora) " +
                        "VALUES (?, ?, ?, ?)";

        String sqlPedido =
                "UPDATE pedido SET estado = ? WHERE id = ?";

        try {

            /*
             * Abrimos UNA conexión que será utilizada
             * por las dos operaciones.
             */
            conexion = ConexionBD.conectar();


            /*
             * Desactivamos el commit automático.
             *
             * Normalmente JDBC confirma cada operación
             * automáticamente.
             *
             * Con false podemos decidir cuándo confirmar
             * todas las operaciones juntas.
             */
            conexion.setAutoCommit(false);


            /*
             * OPERACIÓN 1:
             * INSERT de la entrega.
             */
            try (
                    PreparedStatement psEntrega =
                            conexion.prepareStatement(sqlEntrega)
            ) {

                psEntrega.setInt(
                        1,
                        entrega.getIdPedido()
                );

                psEntrega.setInt(
                        2,
                        entrega.getIdRepartidor()
                );

                psEntrega.setDate(
                        3,
                        Date.valueOf(entrega.getFecha())
                );

                psEntrega.setTime(
                        4,
                        Time.valueOf(entrega.getHora())
                );

                int filasEntrega =
                        psEntrega.executeUpdate();

                if (filasEntrega == 0) {

                    throw new SQLException(
                            "No se pudo registrar la entrega."
                    );
                }
            }


            /*
             * OPERACIÓN 2:
             * UPDATE del estado del pedido.
             *
             * Observa que usamos la misma variable
             * 'conexion'.
             */
            try (
                    PreparedStatement psPedido =
                            conexion.prepareStatement(sqlPedido)
            ) {

                psPedido.setString(
                        1,
                        "EN_REPARTO"
                );

                psPedido.setInt(
                        2,
                        entrega.getIdPedido()
                );

                int filasPedido =
                        psPedido.executeUpdate();

                if (filasPedido == 0) {

                    throw new SQLException(
                            "No se encontró el pedido para actualizar."
                    );
                }
            }


            /*
             * Si Java llegó hasta aquí significa que
             * las DOS operaciones funcionaron.
             *
             * COMMIT confirma definitivamente ambas.
             */
            conexion.commit();

            System.out.println(
                    "Entrega iniciada correctamente."
            );

            return true;


        } catch (SQLException e) {

            /*
             * Algo falló.
             *
             * ROLLBACK deshace las operaciones realizadas
             * dentro de esta transacción.
             */
            if (conexion != null) {

                try {

                    conexion.rollback();

                    System.out.println(
                            "Transacción revertida."
                    );

                } catch (SQLException errorRollback) {

                    System.out.println(
                            "Error al realizar rollback: "
                                    + errorRollback.getMessage()
                    );
                }
            }


            System.out.println(
                    "Error al iniciar entrega: "
                            + e.getMessage()
            );

            return false;


        } finally {

            /*
             * Finalmente cerramos la conexión,
             * tanto si funcionó como si hubo un error.
             */
            if (conexion != null) {

                try {

                    conexion.close();

                } catch (SQLException e) {

                    System.out.println(
                            "Error al cerrar conexión: "
                                    + e.getMessage()
                    );
                }
            }
        }
    }
}