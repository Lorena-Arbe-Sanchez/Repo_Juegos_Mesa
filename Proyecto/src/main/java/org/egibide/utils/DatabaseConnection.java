package org.egibide.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws SQLException {
        try {
            String driver = "org.mariadb.jdbc.Driver";
            Class.forName(driver);
            // Normalmente el puerto será "3306" (dependiendo de la configuración de XAMPP puede ser "3307")
            String url = "jdbc:mariadb://localhost:3307/juegos";
            String user = "root";
            String password = "";
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se ha encontrado el driver de MariaDB.", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

}
