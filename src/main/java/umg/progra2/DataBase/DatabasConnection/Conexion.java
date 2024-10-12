package umg.progra2.DataBase.DbConnection;

import java.sql.*;

public class Conexion {
    private static final String URL = "jdbc:sqlite:C:/sqlite/sqlite-tools-win-x64-3460100/db_telebot.db";
    // Ajusta la ruta a tu base de datos

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection(URL);
            System.out.println("Conexión a la base de datos establecida en: " + URL);

            statement = connection.createStatement();

            // Intentar consultar la tabla tb_producto
            String sql = "SELECT * FROM tb_producto";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int id = resultSet.getInt("id_producto");
                String descripcion = resultSet.getString("descripcion");
                String origen = resultSet.getString("origen");
                int cantidad = resultSet.getInt("cantidad");
                double precio = resultSet.getDouble("precio");
                int existencia = resultSet.getInt("existencia"); // Agregar existencia aquí

                // Imprimir los resultados
                System.out.println("ID: " + id + ", Descripción: " + descripcion + ", Origen: " + origen +
                        ", Cantidad: " + cantidad + ", Precio: " + precio + ", Existencia: " + existencia);
            }
        } catch (SQLException e) {
            System.out.println("Error de SQL: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                System.out.println("Error al cerrar la conexión: " + ex.getMessage());
            }
        }
    }
}
