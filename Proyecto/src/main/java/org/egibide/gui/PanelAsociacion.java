package org.egibide.gui;

import org.egibide.dao.AsociacionDaoImpl;
import org.egibide.idao.AsociacionDao;
import org.egibide.models.Asociacion;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PanelAsociacion extends JPanel {

    // El DAO es quien habla con la base de datos
    private AsociacionDao dao = new AsociacionDaoImpl();

    // Solo hay una asociación en la base de datos y su id es 1
    private int idAsociacion = 1;

    // Guarda la asociación que se ha cargado, para modificarla al pulsar Guardar
    private Asociacion asociacionActual;

    // Campos de texto (uno por cada dato de la asociación) =====
    private JTextField campoNombre = new JTextField();
    private JTextField campoRazonSocial = new JTextField();
    private JTextField campoFechaApertura = new JTextField();
    private JTextField campoDireccion = new JTextField();
    private JTextField campoTelefono = new JTextField();
    private JTextField campoEmail = new JTextField();
    private JTextField campoCif = new JTextField();

    public PanelAsociacion() {
        setLayout(new BorderLayout());

        // Margen alrededor del panel para que no quede pegado a los bordes
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Formulario: rejilla de 2 columnas (etiqueta | campo)
        JPanel formulario = new JPanel(new GridLayout(0, 2, 5, 10));

        formulario.add(new JLabel("Nombre:"));
        formulario.add(campoNombre);

        formulario.add(new JLabel("Razón social:"));
        formulario.add(campoRazonSocial);

        formulario.add(new JLabel("Fecha de apertura (aaaa-mm-dd):"));
        formulario.add(campoFechaApertura);

        formulario.add(new JLabel("Dirección:"));
        formulario.add(campoDireccion);

        formulario.add(new JLabel("Teléfono:"));
        formulario.add(campoTelefono);

        formulario.add(new JLabel("Email:"));
        formulario.add(campoEmail);

        formulario.add(new JLabel("CIF:"));
        formulario.add(campoCif);

        add(formulario, BorderLayout.NORTH);

        // Botones (zona de abajo)
        JPanel zonaBotones = new JPanel();
        JButton botonRecargar = new JButton("Recargar");
        JButton botonGuardar = new JButton("Guardar cambios");
        zonaBotones.add(botonRecargar);
        zonaBotones.add(botonGuardar);
        add(zonaBotones, BorderLayout.SOUTH);

        // Recargar descarta lo escrito y vuelve a leer los datos de la base de datos
        botonRecargar.addActionListener(e -> cargarDatos());
        botonGuardar.addActionListener(e -> guardar());

        // Al abrir el panel, se rellenan los campos con los datos de la base de datos
        cargarDatos();
    }

    // Pide la asociación al DAO y copia sus datos en los campos
    private void cargarDatos() {
        asociacionActual = dao.getAsociacion(idAsociacion);

        // Si no hay asociación (o falló la consulta), el DAO devuelve null
        if (asociacionActual == null) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los datos de la asociación.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        campoNombre.setText(asociacionActual.getNombre());
        campoRazonSocial.setText(asociacionActual.getRazonSocial());

        // La fecha puede ser null, así que se comprueba antes de pasarla a texto
        if (asociacionActual.getFechaApertura() != null) {
            campoFechaApertura.setText(asociacionActual.getFechaApertura().toString());
        } else {
            campoFechaApertura.setText("");
        }

        campoDireccion.setText(asociacionActual.getDireccion());
        campoTelefono.setText(asociacionActual.getTelefono());
        campoEmail.setText(asociacionActual.getEmail());
        campoCif.setText(asociacionActual.getCif());
    }

    // Lee los campos, valida los datos y guarda los cambios
    private void guardar() {
        if (asociacionActual == null) {
            JOptionPane.showMessageDialog(this, "No hay datos cargados que guardar.");
            return;
        }

        // Nombre y CIF (obligatorios)
        String nombre = campoNombre.getText();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
            return;
        }

        String cif = campoCif.getText();
        if (cif.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El CIF no puede estar vacío.");
            return;
        }

        // Fecha de apertura (puede quedar vacía)
        String textoFecha = campoFechaApertura.getText();
        LocalDate fecha = null;

        if (!textoFecha.isEmpty()) {
            try {
                fecha = LocalDate.parse(textoFecha);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Fecha con formato incorrecto. Ejemplo: 2015-03-21");
                return;
            }
        }

        // Se copian los datos de los campos al objeto
        asociacionActual.setNombre(nombre);
        asociacionActual.setRazonSocial(campoRazonSocial.getText());
        asociacionActual.setFechaApertura(fecha);
        asociacionActual.setDireccion(campoDireccion.getText());
        asociacionActual.setTelefono(campoTelefono.getText());
        asociacionActual.setEmail(campoEmail.getText());
        asociacionActual.setCif(cif);

        // Guardar en la base de datos
        // update() devuelve true si ha modificado alguna fila y false si ha fallado
        boolean guardado = dao.update(asociacionActual);

        if (guardado) {
            JOptionPane.showMessageDialog(this, "Datos de la asociación guardados correctamente.");
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron guardar los cambios. Revisa la conexión con la base de datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}