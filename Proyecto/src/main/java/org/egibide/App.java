package org.egibide;

import org.egibide.utils.DatabaseConnection;
import org.egibide.utils.ExportarDatos;
import org.egibide.utils.MetadatosBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

// TODO : En el manual poner que la clase ejecutable es esta (y no dejarla con demasiadas líneas; hacer funciones y separar código)

public class App {
    public static void main(String[] args) throws SQLException {

        DatabaseConnection dbc = DatabaseConnection.getInstance();

        try (Scanner sc = new Scanner(System.in)) {

            Connection conexion = dbc.getConnection();

            System.out.println("Conexión correcta.\n");

            // Mostrar los datos de la asociación al iniciar la aplicación
            mostrarDatosAsociacion(conexion);

            // Menú principal
            int opcion;

            do {

                System.out.println("\n===== MENÚ PRINCIPAL =====");
                System.out.println("1. Gestión de juegos");
                System.out.println("2. Gestión de socias");
                System.out.println("3. Gestión de partidas");
                System.out.println("4. Exportar datos XML y JSON");
                System.out.println("5. Metadatos de la BBDD");
                System.out.println("0. Salir");
                System.out.print("Elige una opción: ");

                opcion = Integer.parseInt(sc.nextLine());

                switch (opcion) {
                    case 1:
                        menuJuegos(sc);
                        break;
                    case 2:
                        menuSocias(sc);
                        break;
                    case 3:
                        menuPartidas(sc);
                        break;
                    case 4:
                        menuExportaciones(sc);
                        break;
                    case 5:
                        MetadatosBD.main(new String[0]);
                        break;
                    case 0:
                        System.out.println("¡Hasta pronto!");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }

            } while (opcion != 0);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Mostrar los datos principales de la asociación
    private static void mostrarDatosAsociacion(Connection conexion) throws Exception {

        // Con esta consulta se asegura que solo obtenga datos de 1 asociación (aun así en la BD solo habrá 1 guardada)
        String sql = "SELECT * FROM asociacion LIMIT 1";

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                System.out.println("\n==================================");
                System.out.println("   " + rs.getString("nombre"));
                System.out.println("==================================");
                System.out.println("Razón social: "
                        + rs.getString("razon_social"));
                System.out.println("Fecha de apertura: "
                        + rs.getDate("fecha_apertura"));
                System.out.println("Dirección: "
                        + rs.getString("direccion"));
                System.out.println("Teléfono: "
                        + rs.getString("telefono"));
                System.out.println("Email: "
                        + rs.getString("email"));
                System.out.println("CIF: "
                        + rs.getString("cif"));
            } else {
                System.out.println("No hay datos de la asociación.");
            }
        }
    }

    // Menú de juegos
    private static void menuJuegos(Scanner sc) {

        System.out.println("\n===== GESTIÓN DE JUEGOS =====");
        System.out.println("1. Listar juegos");
        System.out.println("2. Dar de alta un juego");
        System.out.println("3. Modificar un juego");
        System.out.println("4. Dar de baja un juego");
        System.out.println("0. Volver al menú principal");
        System.out.print("Elige una opción: ");

        int opcion = Integer.parseInt(sc.nextLine());

        // TODO : Hay que implementar todo lo que aparezca como "Pendiente"
        switch (opcion) {
            case 1:
                System.out.println("Pendiente: implementar listado de juegos.");
                break;
            case 2:
                System.out.println("Pendiente: implementar alta de juegos.");
                break;
            case 3:
                System.out.println("Pendiente: implementar modificación de juegos.");
                break;
            case 4:
                System.out.println("Pendiente: implementar baja de juegos.");
                break;
            case 0:
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    // Menú de socias
    private static void menuSocias(Scanner sc) {

        System.out.println("\n===== GESTIÓN DE SOCIAS =====");
        System.out.println("1. Listar socias");
        System.out.println("2. Consultar juegos y partidas de una socia");
        System.out.println("3. Dar de alta una socia");
        System.out.println("4. Modificar una socia");
        System.out.println("5. Dar de baja una socia");
        System.out.println("0. Volver al menú principal");
        System.out.print("Elige una opción: ");

        int opcion = Integer.parseInt(sc.nextLine());

        switch (opcion) {
            case 1:
                System.out.println("Pendiente: implementar listado de socias.");
                break;
            case 2:
                System.out.println("Pendiente: consultar juegos y partidas.");
                break;
            case 3:
                System.out.println("Pendiente: implementar alta de socias.");
                break;
            case 4:
                System.out.println("Pendiente: implementar modificación de socias.");
                break;
            case 5:
                System.out.println("Pendiente: implementar baja de socias.");
                break;
            case 0:
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    // Menú de partidas
    private static void menuPartidas(Scanner sc) {

        System.out.println("\n===== GESTIÓN DE PARTIDAS =====");
        System.out.println("1. Listar partidas");
        System.out.println("2. Dar de alta una partida");
        System.out.println("3. Modificar una partida");
        System.out.println("4. Dar de baja una partida");
        System.out.println("0. Volver al menú principal");
        System.out.print("Elige una opción: ");

        int opcion = Integer.parseInt(sc.nextLine());

        switch (opcion) {
            case 1:
                System.out.println("Pendiente: implementar listado de partidas.");
                break;
            case 2:
                System.out.println("Pendiente: implementar alta de partidas.");
                break;
            case 3:
                System.out.println("Pendiente: implementar modificación de partidas.");
                break;
            case 4:
                System.out.println("Pendiente: implementar baja de partidas.");
                break;
            case 0:
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    // TODO : Separar las exportaciones de XML y JSON en diferentes opciones
    // Menú de exportaciones
    private static void menuExportaciones(Scanner sc) throws Exception {

        System.out.println("\n===== EXPORTAR DATOS =====");
        System.out.println("1. Exportar juegos");
        System.out.println("2. Exportar socias");
        System.out.println("3. Exportar partidas");
        System.out.println("4. Exportar todo");
        System.out.println("0. Volver al menú principal");
        System.out.print("Elige una opción: ");

        int opcion = Integer.parseInt(sc.nextLine());

        switch (opcion) {
            case 1:
                ExportarDatos.exportarJuegosXML();
                ExportarDatos.exportarJuegosJSON();
                break;
            case 2:
                ExportarDatos.exportarSociasXML();
                ExportarDatos.exportarSociasJSON();
                break;
            case 3:
                ExportarDatos.exportarPartidasXML();
                ExportarDatos.exportarPartidasJSON();
                break;
            case 4:
                ExportarDatos.exportarJuegosXML();
                ExportarDatos.exportarJuegosJSON();
                ExportarDatos.exportarSociasXML();
                ExportarDatos.exportarSociasJSON();
                ExportarDatos.exportarPartidasXML();
                ExportarDatos.exportarPartidasJSON();
                break;
            case 0:
                break;
            default:
                System.out.println("Opción no válida.");
        }

        if (opcion >= 1 && opcion <= 4) {
            System.out.println("Exportación finalizada.");
        }
    }
}