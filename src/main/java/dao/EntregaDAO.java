package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import modelo.Entrega;

/*
 * Gestiona las operaciones de base de datos
 * relacionadas con las entregas
 */
public class EntregaDAO {

    public void guardar(Entrega entrega) {

        String sql =
                "INSERT INTO entrega " +
                        "(id_pedido, id_repartidor, fecha, hora) " +
                        "VALUES (?, ?, ?, ?)";

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
            ps.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            ps.setTime(4, java.sql.Time.valueOf(LocalTime.now()));

            ps.executeUpdate();

            System.out.println(
                    "Entrega guardada correctamente en MySQL."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Error al guardar entrega: "
                            + e.getMessage()
            );
        }
    }

    public String obtenerNombreRepartidorPorPedido(int idPedido) {

        String sql =
                "SELECT r.nombre " +
                        "FROM entrega e " +
                        "JOIN repartidor r ON e.id_repartidor = r.id " +
                        "WHERE e.id_pedido = ? " +
                        "ORDER BY e.id DESC " +
                        "LIMIT 1";

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setInt(1, idPedido);

            try (var rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getString("nombre");
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al obtener repartidor: "
                            + e.getMessage()
            );
        }

        return "Sin asignar";
    }
}