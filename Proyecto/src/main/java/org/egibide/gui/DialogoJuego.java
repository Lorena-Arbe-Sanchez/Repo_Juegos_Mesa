package org.egibide.gui;

import org.egibide.dao.JuegoDaoImpl;
import org.egibide.dao.SociaDaoImpl;
import org.egibide.idao.JuegoDao;
import org.egibide.idao.SociaDao;
import org.egibide.models.Asociacion;
import org.egibide.models.Juego;
import org.egibide.models.Socia;

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

public class DialogoJuego extends JDialog {

    private JuegoDao dao = new JuegoDaoImpl();
    private SociaDao sociaDao = new SociaDaoImpl();

    private JTextField campoNombre = new JTextField();
    private JTextField campoAutor1 = new JTextField();
    private JTextField campoAutor2 = new JTextField();
    private JTextField campoDescripcion = new JTextField();
    private JTextField campoMin = new JTextField();
    private JTextField campoMax = new JTextField();
    private JTextField campoDuracion = new JTextField();
    private JTextField campoTematica = new JTextField();
    private JTextField campoDocumento = new JTextField();

    private String[] opcionesTipo = {"Competitivo", "Cooperativo"};
    private String[] opcionesUbicacion = {"Estantería 1 (>12 años)", "Estantería 2 (5-11 años)", "Estantería 3 (Infantil)"};
    private String[] opcionesPropietaria = {"Asociación", "Socia"};

    private JComboBox<String> comboTipo = new JComboBox<>(opcionesTipo);
    private JComboBox<String> comboUbicacion = new JComboBox<>(opcionesUbicacion);
    private JComboBox<String> comboPropietaria = new JComboBox<>(opcionesPropietaria);

    public DialogoJuego() {
        super((Frame) null, "Alta de juego", true);
        setLayout(new GridLayout(0, 2, 5, 5));

        add(new JLabel("Nombre del juego:"));
        add(campoNombre);

        add(new JLabel("Primer autor:"));
        add(campoAutor1);

        add(new JLabel("Segundo autor (vacío si no hay):"));
        add(campoAutor2);

        add(new JLabel("Descripción:"));
        add(campoDescripcion);

        add(new JLabel("Número mínimo de jugadores:"));
        add(campoMin);

        add(new JLabel("Número máximo de jugadores:"));
        add(campoMax);

        add(new JLabel("Duración (minutos):"));
        add(campoDuracion);

        add(new JLabel("Temática:"));
        add(campoTematica);

        add(new JLabel("Tipo:"));
        add(comboTipo);

        add(new JLabel("Estantería:"));
        add(comboUbicacion);

        add(new JLabel("Propietaria:"));
        add(comboPropietaria);

        add(new JLabel("Documento de la socia (solo si es de una socia):"));
        add(campoDocumento);

        JButton botonCancelar = new JButton("Cancelar");
        JButton botonGuardar = new JButton("Guardar");
        add(botonCancelar);
        add(botonGuardar);

        botonCancelar.addActionListener(e -> dispose());
        botonGuardar.addActionListener(e -> guardar());

        pack();
        setLocationRelativeTo(null);
    }

    private void guardar() {
        try {
            Juego nuevo = new Juego();

            String nombre = campoNombre.getText();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
                return;
            }
            nuevo.setNombre(nombre);

            nuevo.setAutor1(campoAutor1.getText());

            String autor2 = campoAutor2.getText();
            if (autor2.isEmpty()) {
                nuevo.setAutor2(null);
            } else {
                nuevo.setAutor2(autor2);
            }

            nuevo.setDescripcion(campoDescripcion.getText());

            int minJugadores = Integer.parseInt(campoMin.getText());
            int maxJugadores = Integer.parseInt(campoMax.getText());

            if (maxJugadores < minJugadores) {
                JOptionPane.showMessageDialog(this, "El máximo no puede ser menor que el mínimo.");
                return;
            }
            nuevo.setMinJugadores(minJugadores);
            nuevo.setMaxJugadores(maxJugadores);

            nuevo.setDuracion(Integer.parseInt(campoDuracion.getText()));
            nuevo.setTematica(campoTematica.getText());

            if (comboTipo.getSelectedIndex() == 0) {
                nuevo.setCompetitivo(true);
            } else {
                nuevo.setCompetitivo(false);
            }

            nuevo.setUbicacion(comboUbicacion.getSelectedItem().toString());
            nuevo.setEstado("disponible");
            nuevo.setFechaAlta(LocalDate.now());

            if (comboPropietaria.getSelectedIndex() == 0) {
                Asociacion asociacion = new Asociacion();
                asociacion.setId(1);
                nuevo.setAsociacionPropietaria(asociacion);
            } else {
                Socia socia = sociaDao.buscarDocumento(campoDocumento.getText());

                if (socia == null) {
                    JOptionPane.showMessageDialog(this, "No existe ninguna socia con ese documento.");
                    return;
                }
                nuevo.setSociaPropietaria(socia);
            }

            dao.insertar(nuevo);
            JOptionPane.showMessageDialog(this, "Juego dado de alta correctamente.");
            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Mínimo, máximo y duración deben ser números enteros.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}