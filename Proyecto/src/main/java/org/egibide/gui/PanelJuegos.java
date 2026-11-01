package org.egibide.gui;

import org.egibide.dao.JuegoDaoImpl;
import org.egibide.idao.JuegoDao;
import org.egibide.models.Juego;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.sql.SQLException;
import java.util.List;

public class PanelJuegos extends JPanel {

    // El DAO es quien habla con la base de datos
    private JuegoDao dao = new JuegoDaoImpl();

    // modeloTabla guarda los datos de la tabla, tabla es lo que se ve en pantalla
    private DefaultTableModel modeloTabla;
    private JTable tabla;

    public PanelJuegos() {
        setLayout(new BorderLayout());

        // ===== Tabla =====
        String[] columnas = {"Id", "Nombre", "Jugadores", "Duración", "Temática", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloTabla);

        // Evita que se puedan editar las celdas con doble clic
        tabla.setDefaultEditor(Object.class, null);

        // El JScrollPane añade la barra de desplazamiento si hay muchas filas
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // ===== Botones (zona de abajo) =====
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

        // Cuando pulsen un botón, se ejecuta el método que le toca
        botonRefrescar.addActionListener(e -> cargarJuegos());
        botonAlta.addActionListener(e -> abrirAlta());
        botonModificar.addActionListener(e -> modificar());
        botonBaja.addActionListener(e -> darDeBaja());

        // Al abrir el panel, se llena la tabla con los juegos de la base de datos
        cargarJuegos();
    }

    // Pide los juegos al DAO y los pone como filas de la tabla
    private void cargarJuegos() {
        // Vacía la tabla antes de llenarla para no duplicar filas
        modeloTabla.setRowCount(0);

        try {
            List<Juego> juegos = dao.listar();

            for (Juego j : juegos) {
                // Cada fila tiene un dato por columna, en el mismo orden que "columnas"
                Object[] fila = {
                        j.getId(),
                        j.getNombre(),
                        j.getMinJugadores() + " - " + j.getMaxJugadores(),
                        j.getDuracion() + " min",
                        j.getTematica(),
                        j.getEstado()
                };
                modeloTabla.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Botón Alta: abre el diálogo vacío (null = no hay juego previo)
    private void abrirAlta() {
        DialogoJuego dialogo = new DialogoJuego(null);
        dialogo.setVisible(true);

        // Cuando se cierra el diálogo, se recarga la tabla para ver el juego nuevo
        cargarJuegos();
    }

    // Botón Modificar: abre el diálogo con los datos del juego seleccionado
    private void modificar() {
        int filaElegida = tabla.getSelectedRow();

        // -1 significa que no hay ninguna fila seleccionada
        if (filaElegida == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un juego de la tabla.");
            return;
        }

        // La primera columna (posición 0) es el id
        int id = Integer.parseInt(modeloTabla.getValueAt(filaElegida, 0).toString());

        try {
            // La tabla solo muestra algunas columnas, así que se pide el juego completo al DAO
            Juego juego = dao.buscarPorId(id);

            if (juego == null) {
                JOptionPane.showMessageDialog(this, "Ese juego ya no existe.");
                return;
            }

            DialogoJuego dialogo = new DialogoJuego(juego);
            dialogo.setVisible(true);
            cargarJuegos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Botón Baja: borra el juego seleccionado, pidiendo confirmación
    private void darDeBaja() {
        int filaElegida = tabla.getSelectedRow();

        if (filaElegida == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un juego de la tabla.");
            return;
        }

        int id = Integer.parseInt(modeloTabla.getValueAt(filaElegida, 0).toString());
        String nombre = modeloTabla.getValueAt(filaElegida, 1).toString();

        // Ventana de confirmación con botones Sí / No
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres eliminar el juego " + nombre + "?",
                "Confirmar baja", JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            try {
                dao.eliminar(id);
                cargarJuegos();
            } catch (SQLException e) {
                // Si el juego tiene partidas asociadas, MariaDB no deja borrarlo
                JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar el juego. Es posible que tenga partidas asociadas.\nDetalle: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}