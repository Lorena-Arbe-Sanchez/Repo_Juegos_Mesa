package org.egibide.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        // Título, tamaño y comportamiento de la ventana
        setTitle("Asociación de juegos de mesa");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // al cerrar la ventana, termina el programa
        setLocationRelativeTo(null);                    // la centra en la pantalla

        // Cada pestaña es un panel distinto
        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Juegos", new PanelJuegos());
        pestanas.addTab("Socias", new PanelSocias());
        pestanas.addTab("Partidas", new JPanel()); // provisional, se sustituirá por PanelPartidas

        add(pestanas);
    }
}