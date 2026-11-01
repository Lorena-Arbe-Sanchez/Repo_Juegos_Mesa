package org.egibide.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegistroSesiones {

    // Fichero de texto donde se apunta quién entra y cuándo
    private static final String FICHERO = "datos/sesiones.txt";

    // Añade una línea con fecha y hora, usuario y evento. Ejemplo:
    // 2026-11-02 18:30:15 | aitor | ENTRADA
    public static void registrar(String usuario, String evento) {
        new File("datos").mkdirs();

        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String linea = ahora.format(formato) + " | " + usuario + " | " + evento;

        // El true del FileWriter significa "añadir al final" para no borrar lo anterior
        try (PrintWriter escritor = new PrintWriter(new FileWriter(FICHERO, true))) {
            escritor.println(linea);
        } catch (IOException e) {
            System.out.println("No se pudo escribir en el registro de sesiones: " + e.getMessage());
        }
    }
}