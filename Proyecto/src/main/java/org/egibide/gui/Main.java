package org.egibide.gui;

/*
 * Main (interfaz gráfica)
 * Primero pide el acceso y solo si entra abre la ventana principal.
 * LLAMA A: DialogoLogin (para identificarse) y VentanaPrincipal (le pasa el usuario que ha entrado).
 */
public class Main {
    public static void main(String[] args) {
        // 1. Se muestra el login y el programa espera aquí hasta que se cierre
        DialogoLogin login = new DialogoLogin();
        login.setVisible(true);

        // 2. Se mira quién ha entrado
        String usuario = login.getUsuarioAutenticado();

        // 3. Si no ha conseguido entrar (cerró la ventana, pulsó Salir o falló 3 veces), termina
        if (usuario == null) {
            System.exit(0);
        }

        // 4. Si ha entrado, se abre la ventana principal con su nombre
        VentanaPrincipal ventana = new VentanaPrincipal(usuario);
        ventana.setVisible(true);
    }
}