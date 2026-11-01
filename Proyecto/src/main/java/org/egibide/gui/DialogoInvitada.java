package org.egibide.gui;

import org.egibide.dao.InscripcionDaoImpl;
import org.egibide.idao.InscripcionDao;
import org.egibide.models.Invitada;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Frame;
import java.awt.GridLayout;
import java.sql.SQLException;

public class DialogoInvitada extends JDialog {

    private InscripcionDao dao = new InscripcionDaoImpl();

    // El documento ya se ha escrito antes de abrir el diálogo, así que no se vuelve a pedir
    private String documento;

    // Se pone a true solo si la invitada se guarda bien. El panel lo consulta al cerrarse
    private boolean guardada = false;

    private JTextField campoNombre = new JTextField();
    private JTextField campoApellido1 = new JTextField();
    private JTextField campoApellido2 = new JTextField();
    private JTextField campoTelefono = new JTextField();
    private JTextField campoEdad = new JTextField();
    private JTextField campoEmail = new JTextField();

    public DialogoInvitada(String documento) {
        super((Frame) null, "Registrar invitada", true);
        this.documento = documento;

        setLayout(new GridLayout(0, 2, 5, 5));

        add(new JLabel("Documento:"));
        add(new JLabel(documento));

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

        pack();
        setLocationRelativeTo(null);
    }

    // El panel llama a este método para saber si la invitada se llegó a guardar
    public boolean isGuardada() {
        return guardada;
    }

    private void guardar() {
        try {
            String nombre = campoNombre.getText();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
                return;
            }

            Invitada invitada = new Invitada();
            invitada.setDocumento(documento);
            invitada.setNombre(nombre);
            invitada.setApellido1(campoApellido1.getText());

            String apellido2 = campoApellido2.getText();
            if (apellido2.isEmpty()) {
                invitada.setApellido2(null);
            } else {
                invitada.setApellido2(apellido2);
            }

            invitada.setTelefono(campoTelefono.getText());
            invitada.setEdad(Integer.parseInt(campoEdad.getText()));
            invitada.setEmail(campoEmail.getText());

            dao.crearInvitada(invitada);
            guardada = true;
            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La edad debe ser un número entero.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}