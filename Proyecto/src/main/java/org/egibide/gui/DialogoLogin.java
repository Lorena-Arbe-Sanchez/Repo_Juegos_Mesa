package org.egibide.gui;

import org.egibide.utils.GestorUsuarios;
import org.egibide.utils.RegistroSesiones;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Frame;
import java.awt.GridLayout;
import java.io.IOException;

public class DialogoLogin extends JDialog {

    private JTextField campoUsuario = new JTextField();
    private JPasswordField campoContrasena = new JPasswordField();
    private JPasswordField campoRepetir = new JPasswordField();

    // true si todavía no hay ningún usuario y hay que crear el primero
    private boolean modoRegistro;

    private int intentosFallidos = 0;

    // Se queda a null si no consigue entrar. El Main lo consulta al cerrarse el diálogo
    private String usuarioAutenticado = null;

    public DialogoLogin() {
        super((Frame) null, "Acceso a la aplicación", true);
        modoRegistro = !GestorUsuarios.hayUsuarios();

        setLayout(new GridLayout(0, 2, 5, 5));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        if (modoRegistro) {
            setTitle("Primer acceso: crea un usuario");
            add(new JLabel("Elige un usuario:"));
        } else {
            add(new JLabel("Usuario:"));
        }
        add(campoUsuario);

        add(new JLabel("Contraseña:"));
        add(campoContrasena);

        if (modoRegistro) {
            add(new JLabel("Repite la contraseña:"));
            add(campoRepetir);
        }

        JButton botonSalir = new JButton("Salir");
        JButton botonEntrar = new JButton("Entrar");
        if (modoRegistro) {
            botonEntrar.setText("Crear usuario");
        }
        add(botonSalir);
        add(botonEntrar);

        botonSalir.addActionListener(e -> dispose());
        botonEntrar.addActionListener(e -> entrar());

        // Con esto, pulsar Enter equivale a pulsar el botón Entrar
        getRootPane().setDefaultButton(botonEntrar);

        pack();
        setLocationRelativeTo(null);
    }

    // El Main llama a este método para saber quién ha entrado (null = nadie)
    public String getUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    private void entrar() {
        String usuario = campoUsuario.getText().trim();

        // getPassword() devuelve un array de caracteres, se convierte a texto
        String contrasena = new String(campoContrasena.getPassword());

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe el usuario y la contraseña.");
            return;
        }

        try {
            if (modoRegistro) {
                // --- Crear el primer usuario ---
                String repetida = new String(campoRepetir.getPassword());

                if (!contrasena.equals(repetida)) {
                    JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.");
                    return;
                }

                if (contrasena.length() < 4) {
                    JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 4 caracteres.");
                    return;
                }

                GestorUsuarios.registrarUsuario(usuario, contrasena);
                RegistroSesiones.registrar(usuario, "USUARIO CREADO");
                RegistroSesiones.registrar(usuario, "ENTRADA");
                usuarioAutenticado = usuario;
                dispose();

            } else {
                // --- Iniciar sesión ---
                boolean correcto = GestorUsuarios.comprobar(usuario, contrasena);

                if (correcto) {
                    RegistroSesiones.registrar(usuario, "ENTRADA");
                    usuarioAutenticado = usuario;
                    dispose();
                } else {
                    intentosFallidos++;
                    RegistroSesiones.registrar(usuario, "ACCESO DENEGADO");

                    if (intentosFallidos >= 3) {
                        JOptionPane.showMessageDialog(this, "Demasiados intentos fallidos. La aplicación se cerrará.");
                        dispose();
                    } else {
                        // No se dice si falló el usuario o la contraseña, para no dar pistas
                        JOptionPane.showMessageDialog(this,
                                "Usuario o contraseña incorrectos. Intentos restantes: " + (3 - intentosFallidos));
                        campoContrasena.setText("");
                    }
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer o guardar las credenciales: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}