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

    // Si es null, el diálogo es un alta. Si trae un juego, es una modificación
    private Juego juegoRecibido;

    // ===== Campos de texto (uno por cada dato que se escribe a mano) =====
    private JTextField campoNombre = new JTextField();
    private JTextField campoAutor1 = new JTextField();
    private JTextField campoAutor2 = new JTextField();
    private JTextField campoDescripcion = new JTextField();
    private JTextField campoMin = new JTextField();
    private JTextField campoMax = new JTextField();
    private JTextField campoDuracion = new JTextField();
    private JTextField campoTematica = new JTextField();
    private JTextField campoDocumento = new JTextField();

    // ===== Opciones de los desplegables =====
    private String[] opcionesTipo = {"Competitivo", "Cooperativo"};
    private String[] opcionesUbicacion = {"Estantería 1 (>12 años)", "Estantería 2 (5-11 años)", "Estantería 3 (Infantil)"};
    private String[] opcionesEstado = {"disponible", "prestado", "en reparación"};
    private String[] opcionesPropietaria = {"Asociación", "Socia"};

    private JComboBox<String> comboTipo = new JComboBox<>(opcionesTipo);
    private JComboBox<String> comboUbicacion = new JComboBox<>(opcionesUbicacion);
    private JComboBox<String> comboEstado = new JComboBox<>(opcionesEstado);
    private JComboBox<String> comboPropietaria = new JComboBox<>(opcionesPropietaria);

    public DialogoJuego(Juego juegoRecibido) {
        // El true final hace la ventana "modal": la de detrás se bloquea hasta cerrar esta
        super((Frame) null, "Datos del juego", true);
        this.juegoRecibido = juegoRecibido;

        // Rejilla de 2 columnas (etiqueta | campo) con tantas filas como haga falta
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

        add(new JLabel("Estado:"));
        add(comboEstado);

        add(new JLabel("Propietaria:"));
        add(comboPropietaria);

        add(new JLabel("Documento de la socia (solo si es de una socia):"));
        add(campoDocumento);

        // ===== Botones =====
        JButton botonCancelar = new JButton("Cancelar");
        JButton botonGuardar = new JButton("Guardar");
        add(botonCancelar);
        add(botonGuardar);

        // dispose() cierra la ventana sin guardar nada
        botonCancelar.addActionListener(e -> dispose());
        botonGuardar.addActionListener(e -> guardar());

        // Según haya o no juego recibido, cambia el título y se rellenan los campos
        if (juegoRecibido == null) {
            setTitle("Alta de juego");
        } else {
            setTitle("Modificar juego");
            rellenarCampos();
        }

        // pack() ajusta el tamaño de la ventana al contenido
        pack();
        setLocationRelativeTo(null);
    }

    // Copia los datos del juego recibido en los campos de la ventana (solo al modificar)
    private void rellenarCampos() {
        campoNombre.setText(juegoRecibido.getNombre());
        campoAutor1.setText(juegoRecibido.getAutor1());

        // El segundo autor puede ser null, y setText(null) daría problemas
        if (juegoRecibido.getAutor2() != null) {
            campoAutor2.setText(juegoRecibido.getAutor2());
        }

        campoDescripcion.setText(juegoRecibido.getDescripcion());
        campoMin.setText(String.valueOf(juegoRecibido.getMinJugadores()));
        campoMax.setText(String.valueOf(juegoRecibido.getMaxJugadores()));
        campoDuracion.setText(String.valueOf(juegoRecibido.getDuracion()));
        campoTematica.setText(juegoRecibido.getTematica());

        if (juegoRecibido.isCompetitivo()) {
            comboTipo.setSelectedIndex(0);
        } else {
            comboTipo.setSelectedIndex(1);
        }

        // Selecciona en el desplegable la opción que coincide con el texto guardado
        comboUbicacion.setSelectedItem(juegoRecibido.getUbicacion());
        comboEstado.setSelectedItem(juegoRecibido.getEstado());

        if (juegoRecibido.getAsociacionPropietaria() != null) {
            comboPropietaria.setSelectedIndex(0);
        } else {
            comboPropietaria.setSelectedIndex(1);
            if (juegoRecibido.getSociaPropietaria() != null) {
                campoDocumento.setText(juegoRecibido.getSociaPropietaria().getDocumento());
            }
        }
    }

    // Lee los campos, valida los datos y guarda (alta o modificación)
    private void guardar() {
        try {
            Juego juego;

            // Alta: juego nuevo con la fecha de hoy. Modificación: se reutiliza el que llegó
            if (juegoRecibido == null) {
                juego = new Juego();
                juego.setFechaAlta(LocalDate.now());
            } else {
                juego = juegoRecibido;
            }

            // --- Nombre (obligatorio) ---
            String nombre = campoNombre.getText();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
                return;
            }
            juego.setNombre(nombre);

            // --- Autores ---
            juego.setAutor1(campoAutor1.getText());

            String autor2 = campoAutor2.getText();
            if (autor2.isEmpty()) {
                juego.setAutor2(null);
            } else {
                juego.setAutor2(autor2);
            }

            juego.setDescripcion(campoDescripcion.getText());

            // --- Número de jugadores (se comprueba que el máximo no sea menor que el mínimo) ---
            int minJugadores = Integer.parseInt(campoMin.getText());
            int maxJugadores = Integer.parseInt(campoMax.getText());

            if (maxJugadores < minJugadores) {
                JOptionPane.showMessageDialog(this, "El máximo no puede ser menor que el mínimo.");
                return;
            }
            juego.setMinJugadores(minJugadores);
            juego.setMaxJugadores(maxJugadores);

            // --- Duración y temática ---
            juego.setDuracion(Integer.parseInt(campoDuracion.getText()));
            juego.setTematica(campoTematica.getText());

            // --- Tipo, estantería y estado (vienen de los desplegables) ---
            if (comboTipo.getSelectedIndex() == 0) {
                juego.setCompetitivo(true);
            } else {
                juego.setCompetitivo(false);
            }

            juego.setUbicacion(comboUbicacion.getSelectedItem().toString());
            juego.setEstado(comboEstado.getSelectedItem().toString());

            // --- Propietaria: la asociación o una socia (la otra queda a null) ---
            if (comboPropietaria.getSelectedIndex() == 0) {
                Asociacion asociacion = new Asociacion();
                asociacion.setId(1);
                juego.setAsociacionPropietaria(asociacion);
                juego.setSociaPropietaria(null);
            } else {
                Socia socia = sociaDao.buscarDocumento(campoDocumento.getText());

                if (socia == null) {
                    JOptionPane.showMessageDialog(this, "No existe ninguna socia con ese documento.");
                    return;
                }
                juego.setSociaPropietaria(socia);
                juego.setAsociacionPropietaria(null);
            }

            // --- Guardar en la base de datos ---
            if (juegoRecibido == null) {
                dao.insertar(juego);
                JOptionPane.showMessageDialog(this, "Juego dado de alta correctamente.");
            } else {
                dao.actualizar(juego);
                JOptionPane.showMessageDialog(this, "Juego modificado correctamente.");
            }
            dispose();

        } catch (NumberFormatException e) {
            // Salta si en mínimo, máximo o duración se escribe algo que no es un número
            JOptionPane.showMessageDialog(this, "Mínimo, máximo y duración deben ser números enteros.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}