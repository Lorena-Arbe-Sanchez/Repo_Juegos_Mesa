package org.egibide;

import org.egibide.utils.DatabaseConnection;
import org.egibide.utils.ExportarDatos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {

        try {
            DatabaseConnection dbc = DatabaseConnection.getInstance();
            Connection conexion = dbc.getConnection();

            if (conexion != null && !conexion.isClosed()) {
                System.out.println("Conexión correcta.\n");
            }

            // Exportar los juegos
            ExportarDatos.exportarJuegosXML();
            ExportarDatos.exportarJuegosJSON();

            // Exportar las socias
            ExportarDatos.exportarSociasXML();
            ExportarDatos.exportarSociasJSON();

            // Exportar las partidas
            ExportarDatos.exportarPartidasXML();
            ExportarDatos.exportarPartidasJSON();

            System.out.println("\nTodas las exportaciones se han realizado correctamente.");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}