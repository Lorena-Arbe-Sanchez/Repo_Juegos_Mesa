package org.egibide;

import org.egibide.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {

        try {
            DatabaseConnection dbc = DatabaseConnection.getInstance();
            Connection conexion = dbc.getConnection();

            if (conexion != null && !conexion.isClosed()) {
                System.out.println("Conexión correcta.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}