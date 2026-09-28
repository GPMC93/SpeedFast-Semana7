package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 *  conexión entre la aplicación SpeedFast
 * y la base de datos MySQL
 */
public class ConexionBD {

    private static final String URL =
            "jdbc:mysql://localhost:3306/speedfast_db";

    private static final String USER = "root";

    private static final String PASSWORD = "Giovmena@123";

    public static Connection conectar() throws SQLException {

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }
}