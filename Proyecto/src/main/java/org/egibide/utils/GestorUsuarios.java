package org.egibide.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class GestorUsuarios {

    // Fichero binario donde se guardan los usuarios
    private static final String CARPETA = "datos";
    private static final String FICHERO = "datos/credenciales.dat";

    // Cuántas veces se "licúa" la contraseña (más vueltas = más difícil de adivinar por fuerza bruta)
    private static final int VUELTAS = 65536;
    private static final int BITS_DEL_HASH = 256;

    // Devuelve true si el fichero existe y ya tiene algún usuario
    public static boolean hayUsuarios() {
        File fichero = new File(FICHERO);
        return fichero.exists() && fichero.length() > 0;
    }

    // Comprueba si ya existe un usuario con ese nombre
    public static boolean existeUsuario(String usuario) throws IOException {
        File fichero = new File(FICHERO);

        if (!fichero.exists()) {
            return false;
        }

        try (DataInputStream entrada = new DataInputStream(new FileInputStream(fichero))) {
            // available() dice cuántos bytes quedan por leer: mientras queden, hay más usuarios
            while (entrada.available() > 0) {
                String usuarioLeido = entrada.readUTF();

                byte[] sal = new byte[entrada.readInt()];
                entrada.readFully(sal);

                byte[] hashGuardado = new byte[entrada.readInt()];
                entrada.readFully(hashGuardado);

                if (usuarioLeido.equals(usuario)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Guarda un usuario nuevo al final del fichero: nombre, sal y hash (nunca la contraseña)
    public static void registrarUsuario(String usuario, String contrasena) throws IOException {
        new File(CARPETA).mkdirs();

        // La sal es una pizca de bytes aleatorios, distinta para cada usuario
        byte[] sal = new byte[16];
        SecureRandom azar = new SecureRandom();
        azar.nextBytes(sal);

        byte[] hash = calcularHash(contrasena, sal);

        // El true del FileOutputStream significa "añadir al final" en vez de borrar el fichero
        try (DataOutputStream salida = new DataOutputStream(new FileOutputStream(FICHERO, true))) {
            salida.writeUTF(usuario);
            salida.writeInt(sal.length);
            salida.write(sal);
            salida.writeInt(hash.length);
            salida.write(hash);
        }
    }

    // Comprueba usuario y contraseña. Devuelve true solo si coinciden
    public static boolean comprobar(String usuario, String contrasena) throws IOException {
        File fichero = new File(FICHERO);

        if (!fichero.exists()) {
            return false;
        }

        try (DataInputStream entrada = new DataInputStream(new FileInputStream(fichero))) {
            while (entrada.available() > 0) {
                String usuarioLeido = entrada.readUTF();

                byte[] sal = new byte[entrada.readInt()];
                entrada.readFully(sal);

                byte[] hashGuardado = new byte[entrada.readInt()];
                entrada.readFully(hashGuardado);

                if (usuarioLeido.equals(usuario)) {
                    // Se licúa lo que ha escrito con la sal de ese usuario y se comparan los dos batidos
                    byte[] hashDelIntento = calcularHash(contrasena, sal);
                    return MessageDigest.isEqual(hashGuardado, hashDelIntento);
                }
            }
        }
        return false;
    }

    // La licuadora: convierte contraseña + sal en un hash que no se puede deshacer
    private static byte[] calcularHash(String contrasena, byte[] sal) throws IOException {
        try {
            PBEKeySpec receta = new PBEKeySpec(contrasena.toCharArray(), sal, VUELTAS, BITS_DEL_HASH);
            SecretKeyFactory licuadora = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return licuadora.generateSecret(receta).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IOException("No se pudo cifrar la contraseña: " + e.getMessage());
        }
    }
}