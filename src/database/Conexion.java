package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Conexion {
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DB = "puntoventa";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    private static Conexion instancia;
    private Connection connection;

    // Constructor privado para evitar múltiples instancias
    private Conexion() {
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(URL + DB, USER, PASSWORD);
            System.out.println("Conexión establecida correctamente.");
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de conexión: " + e.getMessage());
        }
    }

    // Método para obtener una única instancia
    public static Conexion getInstance() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    // Método para obtener la conexión
    public Connection conectar() {
        return connection;
    }

    // Método para cerrar la conexión
    public void desconectar() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cerrar conexión: " + e.getMessage());
        }
    }
}
