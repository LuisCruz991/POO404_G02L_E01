package Vistas;

import Vistas.DB.ConexionDB;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Usuario {
    private JButton buttonRegistrar;
    private JTextField textContraseña;
    private JTextField txtUsuario;
    private JTextField textNombreCompleto;
    private JTextField textdui;
    private JTextField textDireccion;
    private JTextField textcorreo;
    private JTextField txtTelefono;
    public JPanel panelMain;

    public Usuario() {
        // Configura el tamaño preferido del panel
        panelMain.setPreferredSize(new Dimension(600, 550));

        buttonRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });
    }

    // Método para registrar un usuario
    private void registrarUsuario() {
        String login = txtUsuario.getText();
        String contraseña = textContraseña.getText();
        String nombreCompleto = textNombreCompleto.getText();
        String dui = textdui.getText();
        String direccion = textDireccion.getText();
        String correo = textcorreo.getText();
        String telefono = txtTelefono.getText();

        // Validar que todos los campos no estén vacíos
        if (login.isEmpty() || contraseña.isEmpty() || nombreCompleto.isEmpty() || dui.isEmpty() || direccion.isEmpty() || correo.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos.");
            return;
        }

        // Guardar el usuario en la base de datos
        guardarUsuario(login, contraseña, nombreCompleto, dui, direccion, correo, telefono);
    }

    // Método para guardar un usuario en la base de datos
    private void guardarUsuario(String login, String contraseña, String nombreCompleto, String dui, String direccion, String correo, String telefono) {
        String query = "INSERT INTO Usuarios (Nombre, Clave, DUI, Direccion, Correo, Telefono) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nombreCompleto);
            stmt.setString(2, contraseña);
            stmt.setString(3, dui);
            stmt.setString(4, direccion);
            stmt.setString(5, correo);
            stmt.setString(6, telefono);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar el usuario: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Usuario frame = new Usuario();
            JFrame jFrame = new JFrame("Gestión de Usuarios");
            jFrame.setContentPane(frame.panelMain);
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jFrame.pack();
            jFrame.setVisible(true);
        });
    }
}
