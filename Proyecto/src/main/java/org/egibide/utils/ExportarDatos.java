package org.egibide.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// TODO : ¿Añadir funciones para exportar datos también de tablas secundarias?

public class ExportarDatos {

    // Exportar los juegos a XML
    public static void exportarJuegosXML() throws SQLException, IOException {
        exportarXML("juego", "datos/xml/Juegos.xml");
    }

    // Exportar las socias a XML
    public static void exportarSociasXML() throws SQLException, IOException {
        exportarXML("socia", "datos/xml/Socias.xml");
    }

    // Exportar las partidas a XML
    public static void exportarPartidasXML() throws SQLException, IOException {
        exportarXML("partida", "datos/xml/Partidas.xml");
    }

    // Exportar los juegos a JSON
    public static void exportarJuegosJSON() throws SQLException, IOException {
        exportarJSON("juego", "datos/json/Juegos.json");
    }

    // Exportar las socias a JSON
    public static void exportarSociasJSON() throws SQLException, IOException {
        exportarJSON("socia", "datos/json/Socias.json");
    }

    // Exportar las partidas a JSON
    public static void exportarPartidasJSON() throws SQLException, IOException {
        exportarJSON("partida", "datos/json/Partidas.json");
    }

    // Obtener los datos de una tabla de la base de datos
    private static List<Map<String, Object>> obtenerDatos(String tabla) throws SQLException {

        List<Map<String, Object>> lista = new ArrayList<>();

        // Solo se permiten estas tablas para evitar consultas no deseadas
        if (!tabla.equals("juego") && !tabla.equals("socia") && !tabla.equals("partida")) {
            throw new IllegalArgumentException("Tabla no permitida");
        }

        try (Connection conexion = DatabaseConnection.getInstance().getConnection();
             Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery("SELECT * FROM " + tabla)) {

            // Obtener los nombres de las columnas
            ResultSetMetaDataColumnas columnas = new ResultSetMetaDataColumnas(resultado);

            while (resultado.next()) {

                Map<String, Object> fila = new LinkedHashMap<>();

                for (int i = 1; i <= columnas.getCantidad(); i++) {
                    String nombre = columnas.getNombre(i);
                    Object valor = resultado.getObject(i);

                    // Convertir fechas y horas a texto para exportarlas
                    if (valor instanceof java.sql.Date || valor instanceof java.sql.Time
                            || valor instanceof java.sql.Timestamp) {
                        valor = valor.toString();
                    }

                    fila.put(nombre, valor);
                }

                lista.add(fila);
            }
        }

        return lista;
    }

    // Generar el fichero XML
    private static void exportarXML(String tabla, String ruta) throws SQLException, IOException {

        List<Map<String, Object>> datos = obtenerDatos(tabla);

        File archivo = new File(ruta);
        archivo.getParentFile().mkdirs();

        XStream xstream = new XStream();
        xstream.addPermission(AnyTypePermission.ANY);
        xstream.alias("registros", List.class);
        xstream.alias("registro", Map.class);

        try (FileWriter escritor = new FileWriter(archivo)) {
            xstream.toXML(datos, escritor);
        }

        System.out.println("XML exportado: " + ruta);
    }

    // Generar el fichero JSON
    private static void exportarJSON(String tabla, String ruta) throws SQLException, IOException {

        List<Map<String, Object>> datos = obtenerDatos(tabla);

        File archivo = new File(ruta);
        archivo.getParentFile().mkdirs();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter escritor = new FileWriter(archivo)) {
            gson.toJson(datos, escritor);
        }

        System.out.println("JSON exportado: " + ruta);
    }

    // Clase auxiliar para consultar las columnas del resultado
    private static class ResultSetMetaDataColumnas {

        private final java.sql.ResultSetMetaData metadatos;

        public ResultSetMetaDataColumnas(ResultSet resultado) throws SQLException {
            metadatos = resultado.getMetaData();
        }

        public int getCantidad() throws SQLException {
            return metadatos.getColumnCount();
        }

        public String getNombre(int indice) throws SQLException {
            return metadatos.getColumnName(indice);
        }
    }
}