package org.egibide.gui;

import org.egibide.dao.InscripcionDaoImpl;
import org.egibide.dao.PartidaDaoImpl;
import org.egibide.dao.SociaDaoImpl;
import org.egibide.idao.InscripcionDao;
import org.egibide.idao.PartidaDao;
import org.egibide.idao.SociaDao;
import org.egibide.models.Invitada;
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

public class PanelPartidas extends JPanel {

    // Los DAO son quienes hablan con la base de datos
    private PartidaDao dao = new PartidaDaoImpl();
    private InscripcionDao inscripcionDao = new InscripcionDaoImpl(); // NUEVO
    private SociaDao sociaDao = new SociaDaoImpl();                   // NUEVO

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    // Permite recargar la tabla desde fuera (lo usa VentanaPrincipal al cambiar de pestaña)
    public void recargar() {
        cargarPartidas();
    }

    public PanelPartidas() {
        setLayout(new BorderLayout());

        // Tabla
        String[] columnas = {"Id", "Juego", "Fecha", "Hora", "Jugadores deseados", "Plazas libres"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloTabla);
        tabla.setDefaultEditor(Object.class, null); // celdas no editables
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Botones
        JPanel zonaBotones = new JPanel();
        JButton botonRefrescar = new JButton("Refrescar");
        JButton botonAlta = new JButton("Alta");
        JButton botonModificar = new JButton("Modificar");
        JButton botonBaja = new JButton("Baja");
        JButton botonInscritas = new JButton("Ver inscritas");  // NUEVO
        JButton botonApuntarse = new JButton("Apuntarse");      // NUEVO
        zonaBotones.add(botonRefrescar);
        zonaBotones.add(botonAlta);
        zonaBotones.add(botonModificar);
        zonaBotones.add(botonBaja);
        zonaBotones.add(botonInscritas);
        zonaBotones.add(botonApuntarse);
        add(zonaBotones, BorderLayout.SOUTH);

        botonRefrescar.addActionListener(e -> cargarPartidas());
        botonAlta.addActionListener(e -> abrirAlta());
        botonModificar.addActionListener(e -> modificar());
        botonBaja.addActionListener(e -> darDeBaja());
        botonInscritas.addActionListener(e -> verInscritas());   // NUEVO
        botonApuntarse.addActionListener(e -> apuntarse());      // NUEVO

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

    // NUEVO - Botón Ver inscritas: muestra en una ventana las socias e invitadas de la partida
    private void verInscritas() {
        int filaElegida = tabla.getSelectedRow();

        if (filaElegida == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una partida de la tabla.");
            return;
        }

        int id = Integer.parseInt(modeloTabla.getValueAt(filaElegida, 0).toString());

        try {
            List<Socia> socias = inscripcionDao.listarSocias(id);
            List<Invitada> invitadas = inscripcionDao.listarInvitadas(id);

            String texto = "Socias:\n";

            if (socias.isEmpty()) {
                texto = texto + "Ninguna\n";
            } else {
                for (Socia s : socias) {
                    texto = texto + s.getDocumento() + " - " + s.getNombre() + " " + s.getApellido1() + "\n";
                }
            }

            texto = texto + "\nInvitadas:\n";

            if (invitadas.isEmpty()) {
                texto = texto + "Ninguna\n";
            } else {
                for (Invitada i : invitadas) {
                    texto = texto + i.getDocumento() + " - " + i.getNombre() + " " + i.getApellido1() + "\n";
                }
            }

            JOptionPane.showMessageDialog(this, texto, "Inscritas en la partida " + id,
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // NUEVO - Botón Apuntarse: pregunta si es socia o invitada y la apunta a la partida seleccionada
    private void apuntarse() {
        int filaElegida = tabla.getSelectedRow();

        if (filaElegida == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una partida de la tabla.");
            return;
        }

        int id = Integer.parseInt(modeloTabla.getValueAt(filaElegida, 0).toString());

        try {
            // Se vuelve a pedir la partida a la base de datos para tener las plazas libres actualizadas
            Partida partida = dao.buscarporId(id);

            if (partida == null) {
                JOptionPane.showMessageDialog(this, "Esa partida ya no existe.");
                return;
            }

            if (partida.getPlazasLibres() <= 0) {
                JOptionPane.showMessageDialog(this, "No quedan plazas libres en esta partida.");
                return;
            }

            // Ventana con tres botones: Socia, Invitada y Cancelar
            Object[] opciones = {"Socia", "Invitada", "Cancelar"};
            int eleccion = JOptionPane.showOptionDialog(this,
                    "¿Quién se apunta a la partida?", "Apuntarse",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, opciones, opciones[0]);

            if (eleccion == 0) {
                apuntarSocia(id);
            } else if (eleccion == 1) {
                apuntarInvitada(id);
            }

            // Se recarga la tabla para ver las plazas libres actualizadas
            cargarPartidas();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // NUEVO - Apunta una socia que ya existe
    private void apuntarSocia(int idPartida) throws SQLException {
        String documento = JOptionPane.showInputDialog(this, "Documento de la socia:");

        // Si se cancela la ventana, devuelve null y no se hace nada
        if (documento == null) {
            return;
        }
        documento = documento.trim();

        if (documento.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El documento no puede estar vacío.");
            return;
        }

        Socia socia = sociaDao.buscarDocumento(documento);

        if (socia == null) {
            JOptionPane.showMessageDialog(this, "No existe ninguna socia con ese documento.");
            return;
        }

        if (inscripcionDao.sociaApuntada(idPartida, documento)) {
            JOptionPane.showMessageDialog(this, "Esa socia ya está apuntada a la partida.");
            return;
        }

        inscripcionDao.apuntarSocia(idPartida, documento);
        JOptionPane.showMessageDialog(this, socia.getNombre() + " se ha apuntado a la partida.");
    }

    // NUEVO - Apunta una invitada; si no está registrada, ofrece registrarla antes
    private void apuntarInvitada(int idPartida) throws SQLException {
        String documento = JOptionPane.showInputDialog(this, "Documento de la invitada:");

        if (documento == null) {
            return;
        }
        documento = documento.trim();

        if (documento.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El documento no puede estar vacío.");
            return;
        }

        Invitada invitada = inscripcionDao.buscarInvitada(documento);

        if (invitada == null) {
            int respuesta = JOptionPane.showConfirmDialog(this,
                    "Esa invitada no está registrada. ¿Quieres registrarla ahora?",
                    "Invitada nueva", JOptionPane.YES_NO_OPTION);

            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }

            DialogoInvitada dialogo = new DialogoInvitada(documento);
            dialogo.setVisible(true);

            // Si cerró el diálogo sin guardar, no se apunta
            if (!dialogo.isGuardada()) {
                return;
            }
        }

        if (inscripcionDao.invitadaApuntada(idPartida, documento)) {
            JOptionPane.showMessageDialog(this, "Esa invitada ya está apuntada a la partida.");
            return;
        }

        inscripcionDao.apuntarInvitada(idPartida, documento);
        JOptionPane.showMessageDialog(this, "La invitada se ha apuntado a la partida.");
    }
}