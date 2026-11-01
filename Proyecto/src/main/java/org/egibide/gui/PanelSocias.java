package org.egibide.gui;

import org.egibide.dao.JuegoDaoImpl;
import org.egibide.dao.PartidaDaoImpl;
import org.egibide.dao.SociaDaoImpl;
import org.egibide.idao.JuegoDao;
import org.egibide.idao.PartidaDao;
import org.egibide.idao.SociaDao;
import org.egibide.models.Juego;
import org.egibide.models.Partida;
import org.egibide.models.Socia;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.sql.SQLException;
import java.util.List;

public class PanelSocias extends JPanel {

    // El DAO es quien habla con la base de datos
    private SociaDao dao = new SociaDaoImpl();

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    public PanelSocias() {
        setLayout(new BorderLayout());

        // Tabla
        String[] columnas = {"Documento", "Nombre", "Apellidos", "Teléfono", "Edad", "Email"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloTabla);
        tabla.setDefaultEditor(Object.class, null); // celdas no editables
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        //  Botones
        JPanel zonaBotones = new JPanel();
        JButton botonRefrescar = new JButton("Refrescar");
        JButton botonAlta = new JButton("Alta");
        JButton botonModificar = new JButton("Modificar");
        JButton botonBaja = new JButton("Baja");
        JButton botonJuegosPartidas = new JButton("Juegos y partidas");
        zonaBotones.add(botonRefrescar);
        zonaBotones.add(botonAlta);
        zonaBotones.add(botonModificar);
        zonaBotones.add(botonBaja);
        zonaBotones.add(botonJuegosPartidas);
        add(zonaBotones, BorderLayout.SOUTH);

        botonRefrescar.addActionListener(e -> cargarSocias());
        botonAlta.addActionListener(e -> abrirAlta());
        botonModificar.addActionListener(e -> modificar());
        botonBaja.addActionListener(e -> darDeBaja());
        botonJuegosPartidas.addActionListener(e -> verJuegosYPartidas());

        cargarSocias();
    }

    // Pide las socias al DAO y las pone como filas de la tabla
    private void cargarSocias() {
        modeloTabla.setRowCount(0);

        try {
            List<Socia> socias = dao.listar();

            for (Socia s : socias) {
                // El segundo apellido puede ser null, así que se añade solo si existe
                String apellidos = s.getApellido1();
                if (s.getApellido2() != null) {
                    apellidos = apellidos + " " + s.getApellido2();
                }

                Object[] fila = {
                        s.getDocumento(),
                        s.getNombre(),
                        apellidos,
                        s.getTelefono(),
                        s.getEdad(),
                        s.getEmail()
                };
                modeloTabla.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Botón Alta: abre el diálogo vacío (null = no hay socia previa)
    private void abrirAlta() {
        DialogoSocia dialogo = new DialogoSocia(null);
        dialogo.setVisible(true);
        cargarSocias();
    }

    // Botón Modificar: abre el diálogo con los datos de la socia seleccionada
    private void modificar() {
        int filaElegida = tabla.getSelectedRow();

        if (filaElegida == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una socia de la tabla.");
            return;
        }

        // La primera columna (posición 0) es el documento
        String documento = modeloTabla.getValueAt(filaElegida, 0).toString();

        try {
            Socia socia = dao.buscarDocumento(documento);

            if (socia == null) {
                JOptionPane.showMessageDialog(this, "Esa socia ya no existe.");
                return;
            }

            DialogoSocia dialogo = new DialogoSocia(socia);
            dialogo.setVisible(true);
            cargarSocias();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Botón Baja: borra la socia seleccionada, pidiendo confirmación
    private void darDeBaja() {
        int filaElegida = tabla.getSelectedRow();

        if (filaElegida == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una socia de la tabla.");
            return;
        }

        String documento = modeloTabla.getValueAt(filaElegida, 0).toString();
        String nombre = modeloTabla.getValueAt(filaElegida, 1).toString();

        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres eliminar a " + nombre + "?",
                "Confirmar baja", JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            try {
                dao.eliminar(documento);
                cargarSocias();
            } catch (SQLException e) {
                // Si la socia tiene juegos o partidas, MariaDB no deja borrarla
                JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar la socia. Es posible que tenga juegos o partidas asociados.\nDetalle: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Botón Juegos y partidas: muestra en una ventana los juegos y partidas de la socia seleccionada
    private void verJuegosYPartidas() {
        int filaElegida = tabla.getSelectedRow();

        if (filaElegida == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una socia de la tabla.");
            return;
        }

        String documento = modeloTabla.getValueAt(filaElegida, 0).toString();
        String nombre = modeloTabla.getValueAt(filaElegida, 1).toString();

        try {
            JuegoDao juegoDao = new JuegoDaoImpl();
            PartidaDao partidaDao = new PartidaDaoImpl();

            String texto = "Juegos:\n";
            boolean tieneJuegos = false;

            // Se recorren todos los juegos y se quedan los que son de esta socia
            for (Juego j : juegoDao.listar()) {
                if (j.getSociaPropietaria() != null) {
                    if (j.getSociaPropietaria().getDocumento().equals(documento)) {
                        texto = texto + j.getId() + " - " + j.getNombre() + " (" + j.getEstado() + ")\n";
                        tieneJuegos = true;
                    }
                }
            }

            if (!tieneJuegos) {
                texto = texto + "No tiene juegos registrados.\n";
            }

            texto = texto + "\nPartidas:\n";
            boolean tienePartidas = false;

            // Se recorren todas las partidas y se miran sus jugadoras
            for (Partida p : partidaDao.listar()) {
                for (Socia jugadora : p.getListaJugadorasSocias()) {
                    if (jugadora.getDocumento().equals(documento)) {
                        texto = texto + "Partida " + p.getId() + " - " + p.getFecha() + " a las " + p.getHora() + "\n";
                        tienePartidas = true;
                    }
                }
            }

            if (!tienePartidas) {
                texto = texto + "No está inscrita en ninguna partida.\n";
            }

            JOptionPane.showMessageDialog(this, texto, "Juegos y partidas de " + nombre,
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}