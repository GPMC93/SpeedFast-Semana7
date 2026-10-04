package dao;

import modelo.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * Gestiona las operaciones de base de datos
 * relacionadas con los repartidores
 */
public class RepartidorDAO {

    public List<Repartidor> listarTodos() {

        List<Repartidor> repartidores = new ArrayList<>();

        String sql = "SELECT id, nombre FROM repartidor";

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement ps = conexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");

                Repartidor repartidor =
                        new Repartidor(id, nombre);

                repartidores.add(repartidor);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar repartidores: "
                            + e.getMessage()
            );
        }

        return repartidores;
    }

    public void guardar(String nombre) {

        String sql =
                "INSERT INTO repartidor (nombre) VALUES (?)";

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setString(1, nombre);

            ps.executeUpdate();

            System.out.println(
                    "Repartidor guardado correctamente en MySQL."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Error al guardar repartidor: "
                            + e.getMessage()
            );
        }
    }

    public void actualizar(int id, String nombre) {

        String sql =
                "UPDATE repartidor SET nombre = ? WHERE id = ?";

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setString(1, nombre);
            ps.setInt(2, id);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Repartidor actualizado correctamente.");
            } else {
                System.out.println("No se encontró el repartidor.");
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar repartidor: "
                            + e.getMessage()
            );
        }
    }

    public void eliminar(int id) {

        String sql =
                "DELETE FROM repartidor WHERE id = ?";

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setInt(1, id);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Repartidor eliminado correctamente.");
            } else {
                System.out.println("No se encontró el repartidor.");
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al eliminar repartidor: "
                            + e.getMessage()
            );
        }
    }
}