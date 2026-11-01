package org.egibide.gui;

import org.egibide.dao.SociaDaoImpl;
import org.egibide.idao.SociaDao;
import org.egibide.models.Socia;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Frame;
import java.awt.GridLayout;
import java.sql.SQLException;

public class DialogoSocia extends JDialog {

    private SociaDao dao = new SociaDaoImpl();

    // Si es null, el diálogo es un alta. Si trae una socia, es una modificación
    private Socia sociaRecibida;

    // Campos de texto
    private JTextField campoDocumento = new JTextField();
    private JTextField campoNombre = new JTextField();
    private JTextField campoApellido1 = new JTextField();
    private JTextField campoApellido2 = new JTextField();
    private JTextField campoTelefono = new JTextField();
    private JTextField campoEdad = new JTextField();
    private JTextField campoEmail = new JTextField();

    public DialogoSocia(Socia sociaRecibida) {
        super((Frame) null, "Datos de la socia", true);
        this.sociaRecibida = sociaRecibida;

        setLayout(new GridLayout(0, 2, 5, 5));

        add(new JLabel("Documento (DNI/NIE/Pasaporte):"));
        add(campoDocumento);

        add(new JLabel("Nombre:"));
        add(campoNombre);

        add(new JLabel("Primer apellido:"));
        add(campoApellido1);

        add(new JLabel("Segundo apellido (vacío si no tiene):"));
        add(campoApellido2);

        add(new JLabel("Teléfono:"));
        add(campoTelefono);

        add(new JLabel("Edad:"));
        add(campoEdad);

        add(new JLabel("Email:"));
        add(campoEmail);

        JButton botonCancelar = new JButton("Cancelar");
        JButton botonGuardar = new JButton("Guardar");
        add(botonCancelar);
        add(botonGuardar);

        botonCancelar.addActionListener(e -> dispose());
        botonGuardar.addActionListener(e -> guardar());

        if (sociaRecibida == null) {
            setTitle("Alta de socia");
        } else {
            setTitle("Modificar socia");
            rellenarCampos();
        }

        pack();
        setLocationRelativeTo(null);
    }

    // Copia los datos de la socia recibida en los campos (solo al modificar)
    private void rellenarCampos() {
        campoDocumento.setText(sociaRecibida.getDocumento());

        // El documento identifica a la socia en la base de datos, así que no se puede cambiar
        campoDocumento.setEditable(false);

        campoNombre.setText(sociaRecibida.getNombre());
        campoApellido1.setText(sociaRecibida.getApellido1());

        if (sociaRecibida.getApellido2() != null) {
            campoApellido2.setText(sociaRecibida.getApellido2());
        }

        campoTelefono.setText(sociaRecibida.getTelefono());
        campoEdad.setText(String.valueOf(sociaRecibida.getEdad()));
        campoEmail.setText(sociaRecibida.getEmail());
    }

    // Lee los campos, valida los datos y guarda (alta o modificación)
    private void guardar() {
        try {
            Socia socia;

            if (sociaRecibida == null) {
                socia = new Socia();

                // --- Documento (obligatorio y no repetido) ---
                String documento = campoDocumento.getText();
                if (documento.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El documento no puede estar vacío.");
                    return;
                }

                if (dao.buscarDocumento(documento) != null) {
                    JOptionPane.showMessageDialog(this, "Ya existe una socia con ese documento.");
                    return;
                }
                socia.setDocumento(documento);
            } else {
                socia = sociaRecibida;
            }

            // Nombre y apellidos
            String nombre = campoNombre.getText();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
                return;
            }
            socia.setNombre(nombre);
            socia.setApellido1(campoApellido1.getText());

            String apellido2 = campoApellido2.getText();
            if (apellido2.isEmpty()) {
                socia.setApellido2(null);
            } else {
                socia.setApellido2(apellido2);
            }

            // Teléfono, edad y email
            socia.setTelefono(campoTelefono.getText());
            socia.setEdad(Integer.parseInt(campoEdad.getText()));
            socia.setEmail(campoEmail.getText());

            // Guardar en la base de datos
            if (sociaRecibida == null) {
                dao.insertar(socia);
                JOptionPane.showMessageDialog(this, "Socia dada de alta correctamente.");
            } else {
                dao.actualizar(socia);
                JOptionPane.showMessageDialog(this, "Socia modificada correctamente.");
            }
            dispose();

        } catch (NumberFormatException e) {
            // Salta si en la edad se escribe algo que no es un número
            JOptionPane.showMessageDialog(this, "La edad debe ser un número entero.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}