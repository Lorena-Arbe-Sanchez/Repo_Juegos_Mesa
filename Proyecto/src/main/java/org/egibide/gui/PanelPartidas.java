package org.egibide.gui;

import org.egibide.dao.PartidaDaoImpl;
import org.egibide.idao.PartidaDao;
import org.egibide.models.Partida;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.sql.SQLException;
import java.util.List;

public class PanelPartidas extends JPanel {

    // El DAO es quien habla con la base de datos
    private PartidaDao dao = new PartidaDaoImpl();

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    public PanelPartidas() {
        setLayout(new BorderLayout());

        // ===== Tabla =====
        String[] columnas = {"Id", "Juego", "Fecha", "Hora", "Jugadores deseados", "Plazas libres"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloTabla);
        tabla.setDefaultEditor(Object.class, null); // celdas no editables
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // ===== Botones =====
        JPanel zonaBotones = new JPanel();
        JButton botonRefrescar = new JButton("Refrescar");
        JButton botonAlta = new JButton("Alta");
        JButton botonModificar = new JButton("Modificar");
        JButton botonBaja = new JButton("Baja");
        zonaBotones.add(botonRefrescar);
        zonaBotones.add(botonAlta);
        zonaBotones.add(botonModificar);
        zonaBotones.add(botonBaja);
        add(zonaBotones, BorderLayout.SOUTH);

        botonRefrescar.addActionListener(e -> cargarPartidas());
        botonAlta.addActionListener(e -> abrirAlta());
        botonModificar.addActionListener(e -> modificar());
        botonBaja.addActionListener(e -> darDeBaja());

        cargarPartidas();
    }

    // Pide las partidas al DAO y las pone como filas de la tabla
    private void cargarPartidas() {
        modeloTabla.setRowCount(0);

        try {
            List<Partida> partidas = dao.listar();

            for (Partida p : partidas) {
                // Si por algún motivo la partida no tiene juego, se muestra un texto
                String nombreJuego = "Sin juego";
                if (p.getJuego() != null) {
                    nombreJuego = p.getJuego().getNombre();
                }

                Object[] fila = {
                        p.getId(),
                        nombreJuego,
                        p.getFecha(),
                        p.getHora(),
                        p.getJugadoresDeseados(),
                        p.getPlazasLibres()
                };
                modeloTabla.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Botón Alta: abre el diálogo vacío (null = no hay partida previa)
    private void abrirAlta() {
        DialogoPartida dialogo = new DialogoPartida(null);
        dialogo.setVisible(true);
        cargarPartidas();
    }

    // Botón Modificar: abre el diálogo con los datos de la partida seleccionada
    private void modificar() {
        int filaElegida = tabla.getSelectedRow();

        if (filaElegida == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una partida de la tabla.");
            return;
        }

        // La primera columna (posición 0) es el id
        int id = Integer.parseInt(modeloTabla.getValueAt(filaElegida, 0).toString());

        try {
            Partida partida = dao.buscarporId(id);

            if (partida == null) {
                JOptionPane.showMessageDialog(this, "Esa partida ya no existe.");
                return;
            }

            DialogoPartida dialogo = new DialogoPartida(partida);
            dialogo.setVisible(true);
            cargarPartidas();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Botón Baja: borra la partida seleccionada, pidiendo confirmación
    private void darDeBaja() {
        int filaElegida = tabla.getSelectedRow();

        if (filaElegida == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una partida de la tabla.");
            return;
        }

        int id = Integer.parseInt(modeloTabla.getValueAt(filaElegida, 0).toString());
        String fecha = modeloTabla.getValueAt(filaElegida, 2).toString();

        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres eliminar la partida " + id + " del " + fecha + "?",
                "Confirmar baja", JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            try {
                dao.eliminar(id);
                cargarPartidas();
            } catch (SQLException e) {
                // Si la partida tiene jugadoras inscritas, MariaDB puede no dejar borrarla
                JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar la partida. Es posible que tenga jugadoras inscritas.\nDetalle: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}