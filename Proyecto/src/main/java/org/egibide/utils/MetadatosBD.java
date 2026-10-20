
package org.egibide.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MetadatosBD {

    // Mostrar los metadatos por terminal
    public static void mostrarMetadatos() throws SQLException {
        System.out.println(obtenerMetadatos());
    }

    // Exportar los metadatos a un fichero TXT
    public static void exportarMetadatos() throws SQLException, IOException {

        String nombreArchivo = "datos/metadatos/metadatos_bbdd.txt";

        // Escribir los metadatos en el fichero
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(nombreArchivo))) {
            escritor.write(obtenerMetadatos());
        }

        System.out.println("Metadatos exportados en: " + nombreArchivo + "\n");
    }

    // Obtener todos los metadatos de la base de datos
    private static String obtenerMetadatos() throws SQLException {

        StringBuilder texto = new StringBuilder();

        Connection conexion = DatabaseConnection.getInstance().getConnection();

        DatabaseMetaData metadatos = conexion.getMetaData();

        // Añadir información general de la base de datos
        texto.append("METADATOS DE LA BASE DE DATOS\n");
        texto.append("Nombre BBDD: ").append(conexion.getCatalog()).append("\n");
        texto.append("Esquema: ").append(conexion.getCatalog()).append("\n");
        texto.append("Driver: ").append(metadatos.getDriverName()).append("\n");
        texto.append("Versión del driver: ").append(metadatos.getDriverVersion()).append("\n");
        texto.append("URL: ").append(metadatos.getURL()).append("\n\n");

        // Recorrer las tablas de la BD
        try (ResultSet tablas = metadatos.getTables(conexion.getCatalog(), null,
                "%", new String[]{"TABLE"})) {

            while (tablas.next()) {

                String nombreTabla = tablas.getString("TABLE_NAME");
                String esquema = tablas.getString("TABLE_CAT");

                texto.append("TABLA: ").append(nombreTabla).append("\n");
                texto.append("Esquema: ").append(esquema).append("\n");

                // Obtener las claves primarias de la tabla
                texto.append("Claves primarias: ");

                boolean tieneClave = false;

                try (ResultSet claves = metadatos.getPrimaryKeys(conexion.getCatalog(), null, nombreTabla)) {

                    while (claves.next()) {
                        texto.append(claves.getString("COLUMN_NAME")).append(" ");
                        tieneClave = true;
                    }
                }

                // Indicar si la tabla no tiene clave primaria
                if (!tieneClave) {
                    texto.append("No tiene");
                }

                texto.append("\nColumnas:\n");

                // Obtener las columnas y sus características
                try (ResultSet columnas = metadatos.getColumns(conexion.getCatalog(), null,
                        nombreTabla, "%")) {

                    while (columnas.next()) {

                        String nombreColumna = columnas.getString("COLUMN_NAME");

                        String tipo = columnas.getString("TYPE_NAME");

                        // Comprobar si la columna permite valores nulos
                        boolean permiteNulos = columnas.getInt("NULLABLE")
                                == DatabaseMetaData.columnNullable;

                        // Añadir la información de la columna
                        texto.append("- ").append(nombreColumna)
                                .append(" | Tipo: ").append(tipo)
                                .append(" | Permite nulos: ")
                                .append(permiteNulos ? "Sí" : "No")
                                .append("\n");
                    }
                }
            }
        }

        // Devolver todos los metadatos como texto
        return texto.toString();
    }
}