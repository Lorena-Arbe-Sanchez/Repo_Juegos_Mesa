package org.egibide.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

// TODO : Debería de imprimirlo por la terminal antes de de exportarlo al fichero txt
public class MetadatosBD {

    public static void main(String[] args) {

        String nombreArchivo = "datos/metadatos/metadatos_bbdd.txt";

        try (Connection conexion = DatabaseConnection.getInstance().getConnection();
             BufferedWriter escritor = new BufferedWriter(new FileWriter(nombreArchivo))) {

            // Obtener los metadatos de la base de datos
            DatabaseMetaData metadatos = conexion.getMetaData();

            // Datos generales de la conexión
            escritor.write("METADATOS DE LA BASE DE DATOS");
            escritor.newLine();
            escritor.write("Nombre BBDD: " + conexion.getCatalog());
            escritor.newLine();
            escritor.write("Esquema: " + conexion.getCatalog());
            escritor.newLine();
            escritor.write("Driver: " + metadatos.getDriverName());
            escritor.newLine();
            escritor.write("Versión del driver: " + metadatos.getDriverVersion());
            escritor.newLine();
            escritor.write("URL: " + metadatos.getURL());
            escritor.newLine();
            escritor.newLine();

            // Obtener las tablas de la base de datos
            ResultSet tablas = metadatos.getTables(
                    conexion.getCatalog(),
                    null,
                    "%",
                    new String[]{"TABLE"}
            );

            while (tablas.next()) {

                String nombreTabla = tablas.getString("TABLE_NAME");
                String esquema = tablas.getString("TABLE_CAT");

                escritor.write("TABLA: " + nombreTabla);
                escritor.newLine();
                escritor.write("Esquema: " + esquema);
                escritor.newLine();

                // Obtener las claves primarias
                ResultSet claves = metadatos.getPrimaryKeys(
                        conexion.getCatalog(),
                        esquema,
                        nombreTabla
                );

                escritor.write("Claves primarias: ");

                boolean tieneClave = false;

                while (claves.next()) {
                    escritor.write(claves.getString("COLUMN_NAME") + " ");
                    tieneClave = true;
                }

                if (!tieneClave) {
                    escritor.write("No tiene");
                }

                escritor.newLine();
                claves.close();

                // Obtener las columnas de la tabla
                ResultSet columnas = metadatos.getColumns(
                        conexion.getCatalog(),
                        esquema,
                        nombreTabla,
                        "%"
                );

                escritor.write("Columnas:");
                escritor.newLine();

                while (columnas.next()) {

                    String nombreColumna =
                            columnas.getString("COLUMN_NAME");

                    String tipo =
                            columnas.getString("TYPE_NAME");

                    boolean permiteNulos =
                            columnas.getInt("NULLABLE")
                                    == DatabaseMetaData.columnNullable;

                    escritor.write(
                            "- " + nombreColumna
                                    + " | Tipo: " + tipo
                                    + " | Permite nulos: "
                                    + (permiteNulos ? "Sí" : "No")
                    );

                    escritor.newLine();
                }

                columnas.close();

                escritor.newLine();
            }

            tablas.close();

            System.out.println(
                    "Metadatos exportados en: " + nombreArchivo
            );

        } catch (SQLException | IOException e) {
            System.out.println(
                    "Error al obtener los metadatos: " + e.getMessage()
            );
        }
    }
}