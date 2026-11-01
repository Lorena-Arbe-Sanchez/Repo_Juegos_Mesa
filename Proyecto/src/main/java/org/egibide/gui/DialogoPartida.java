package org.egibide.gui;

import org.egibide.dao.JuegoDaoImpl;
import org.egibide.dao.PartidaDaoImpl;
import org.egibide.idao.JuegoDao;
import org.egibide.idao.PartidaDao;
import org.egibide.models.Juego;
import org.egibide.models.Partida;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Frame;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DialogoPartida extends JDialog {

    private PartidaDao dao = new PartidaDaoImpl();
    private JuegoDao juegoDao = new JuegoDaoImpl();

    // Si es null, el diálogo es un alta. Si trae una partida, es una modificación
    private Partida partidaRecibida;

    // Lista de juegos y desplegable: la posición elegida en el desplegable
    // es la misma posición del juego dentro de la lista
    private List<Juego> listaJuegos = new ArrayList<>();
    private JComboBox<String> comboJuegos = new JComboBox<>();

    // ===== Campos de texto =====
    private JTextField campoFecha = new JTextField();
    private JTextField campoHora = new JTextField();
    private JTextField campoDeseados = new JTextField();

    public DialogoPartida(Partida partidaRecibida) {
        super((Frame) null, "Datos de la partida", true);
        this.partidaRecibida = partidaRecibida;

        setLayout(new GridLayout(0, 2, 5, 5));

        // Llena el desplegable con los juegos de la base de datos
        cargarJuegosEnDesplegable();

        add(new JLabel("Juego:"));
        add(comboJuegos);

        add(new JLabel("Fecha (aaaa-mm-dd):"));
        add(campoFecha);

        add(new JLabel("Hora (hh:mm):"));
        add(campoHora);

        add(new JLabel("Jugadores deseados:"));
        add(campoDeseados);

        JButton botonCancelar = new JButton("Cancelar");
        JButton botonGuardar = new JButton("Guardar");
        add(botonCancelar);
        add(botonGuardar);

        botonCancelar.addActionListener(e -> dispose());
        botonGuardar.addActionListener(e -> guardar());

        if (partidaRecibida == null) {
            setTitle("Alta de partida");
        } else {
            setTitle("Modificar partida");
            rellenarCampos();
        }

        pack();
        setLocationRelativeTo(null);
    }

    // Pide los juegos al DAO y los añade al desplegable con el formato "id - nombre"
    private void cargarJuegosEnDesplegable() {
        try {
            listaJuegos = juegoDao.listar();

            for (Juego j : listaJuegos) {
                comboJuegos.addItem(j.getId() + " - " + j.getNombre());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Copia los datos de la partida recibida en los campos (solo al modificar)
    private void rellenarCampos() {
        // Busca en la lista el juego de la partida y lo selecciona en el desplegable
        if (partidaRecibida.getJuego() != null) {
            for (int i = 0; i < listaJuegos.size(); i++) {
                if (listaJuegos.get(i).getId() == partidaRecibida.getJuego().getId()) {
                    comboJuegos.setSelectedIndex(i);
                }
            }
        }

        // El juego no se puede cambiar al modificar, porque cambiaría el rango de jugadores
        comboJuegos.setEnabled(false);

        campoFecha.setText(partidaRecibida.getFecha().toString());
        campoHora.setText(partidaRecibida.getHora().toString());
        campoDeseados.setText(String.valueOf(partidaRecibida.getJugadoresDeseados()));
    }

    // Lee los campos, valida los datos y guarda (alta o modificación)
    private void guardar() {
        try {
            // --- Juego elegido ---
            int posicion = comboJuegos.getSelectedIndex();

            if (posicion == -1) {
                JOptionPane.showMessageDialog(this, "No hay ningún juego seleccionado.");
                return;
            }
            Juego juego = listaJuegos.get(posicion);

            // --- Fecha y hora (si el formato es incorrecto, salta DateTimeParseException) ---
            LocalDate fecha = LocalDate.parse(campoFecha.getText().trim());
            LocalTime hora = LocalTime.parse(campoHora.getText().trim());

            // --- Jugadores deseados (dentro del rango del juego) ---
            int deseados = Integer.parseInt(campoDeseados.getText().trim());

            if (deseados < juego.getMinJugadores() || deseados > juego.getMaxJugadores()) {
                JOptionPane.showMessageDialog(this,
                        "Los jugadores deseados deben estar entre " + juego.getMinJugadores()
                                + " y " + juego.getMaxJugadores() + " para este juego.");
                return;
            }

            // --- Guardar en la base de datos ---
            if (partidaRecibida == null) {
                Partida nueva = new Partida();
                nueva.setJuego(juego);
                nueva.setFecha(fecha);
                nueva.setHora(hora);
                nueva.setJugadoresDeseados(deseados);
                nueva.setPlazasLibres(deseados);

                int idNueva = dao.insertar(nueva);
                JOptionPane.showMessageDialog(this, "Partida dada de alta con id " + idNueva + ".");
            } else {
                // Las plazas libres suben o bajan lo mismo que cambian los jugadores deseados
                int diferencia = deseados - partidaRecibida.getJugadoresDeseados();
                int plazasNuevas = partidaRecibida.getPlazasLibres() + diferencia;

                if (plazasNuevas < 0) {
                    plazasNuevas = 0;
                }

                partidaRecibida.setFecha(fecha);
                partidaRecibida.setHora(hora);
                partidaRecibida.setJugadoresDeseados(deseados);
                partidaRecibida.setPlazasLibres(plazasNuevas);

                dao.actualizar(partidaRecibida);
                JOptionPane.showMessageDialog(this, "Partida modificada correctamente.");
            }
            dispose();

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Fecha u hora con formato incorrecto. Ejemplos: 2026-11-15 y 18:30");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Los jugadores deseados deben ser un número entero.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}