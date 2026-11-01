package org.egibide.gui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        // Título, tamaño y comportamiento de la ventana
        setTitle("Asociación de juegos de mesa");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // al cerrar la ventana, termina el programa
        setLocationRelativeTo(null);                    // la centra en la pantalla

        // Cada panel se guarda en una variable para poder recargarlo después
        PanelJuegos panelJuegos = new PanelJuegos();
        PanelSocias panelSocias = new PanelSocias();
        PanelPartidas panelPartidas = new PanelPartidas();
        PanelAsociacion panelAsociacion = new PanelAsociacion();

        // Cada pestaña es un panel distinto
        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Juegos", panelJuegos);
        pestanas.addTab("Socias", panelSocias);
        pestanas.addTab("Partidas", panelPartidas);
        pestanas.addTab("Asociación", panelAsociacion);

        // Cada vez que se cambia de pestaña, se recargan los datos de la que se abre
        pestanas.addChangeListener(e -> {
            int pestanaElegida = pestanas.getSelectedIndex();

            if (pestanaElegida == 0) {
                panelJuegos.recargar();
            } else if (pestanaElegida == 1) {
                panelSocias.recargar();
            } else if (pestanaElegida == 2) {
                panelPartidas.recargar();
            }
        });

        add(pestanas);
    }
}