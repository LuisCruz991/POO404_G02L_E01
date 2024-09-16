package Vistas;

import Vistas.DB.ConexionDB; // Asegúrate de importar la clase ConexionDB
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuLogin {
    private JTextField txtUsuario;
    private JPasswordField txtUserContrasena;
    private JButton btnLogearse;
    private JPanel PanelForm;

    private Connection connection;

    public MenuLogin() {
        txtUsuario = new JTextField(20);
        txtUserContrasena = new JPasswordField(20);
        btnLogearse = new JButton("Logearse");
        PanelForm = new JPanel();
        PanelForm.setLayout(new GridLayout(3, 2));
        PanelForm.add(new JLabel("Usuario:"));
        PanelForm.add(txtUsuario);
        PanelForm.add(new JLabel("Contraseña:"));
        PanelForm.add(txtUserContrasena);
        PanelForm.add(new JLabel());
        PanelForm.add(btnLogearse);
        btnLogearse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = txtUsuario.getText();
                String contrasena = new String(txtUserContrasena.getPassword());
                if (validarCredenciales(usuario, contrasena)) {
                    JOptionPane.showMessageDialog(PanelForm, "Inicio de sesión exitoso.");
                } else {
                    JOptionPane.showMessageDialog(PanelForm, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        try {
            connection = ConexionDB.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(PanelForm, "No se pudo conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCredenciales(String usuario, String contrasena) {
        String query = "SELECT * FROM Usuarios WHERE Nombre = ? AND Clave = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, usuario);
            statement.setString(2, contrasena);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(PanelForm, "Error al validar las credenciales.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public JPanel getPanelForm() {
        return PanelForm;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Login");
            MenuLogin menuLogin = new MenuLogin();

            frame.setContentPane(menuLogin.getPanelForm());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(350, 150);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}