package Vistas.AgregarSucursales;

import Vistas.DB.ConexionDB;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AgregarSucursales extends JFrame {
    private JTextField txtNombreSucursal;
    private JTextField txtGerente;
    private JTextField txtDireccion;
    private JTextField txtTelefono;
    private JButton agregarSucursalButton;
    private JPanel panelAgregarSucursales;

    public AgregarSucursales(String title) {
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panelAgregarSucursales);
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);

        limitarSoloNumeros(txtTelefono);

        agregarSucursalButton.addActionListener(e -> agregarSucursal());
    }

    private void agregarSucursal() {
        String nombreSucursal = txtNombreSucursal.getText();
        String gerente = txtGerente.getText();
        String direccion = txtDireccion.getText();
        String telefonoTexto = txtTelefono.getText();
        if (nombreSucursal.isEmpty() || gerente.isEmpty() || direccion.isEmpty() || telefonoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.");
            return;
        }

        int telefono;
        try {
            telefono = Integer.parseInt(telefonoTexto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El teléfono debe ser un número válido.");
            return;
        }

        if (registrarSucursal(nombreSucursal, gerente, direccion, telefono)) {
            JOptionPane.showMessageDialog(this, "Sucursal agregada exitosamente.");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo agregar la sucursal. Inténtalo de nuevo.");
        }
    }

    private boolean registrarSucursal(String nombreSucursal, String gerente, String direccion, int telefono) {
        String sql = "INSERT INTO Sucursales (NombreSucursal, Encargado, Direccion, Telefono) VALUES (?, ?, ?, ?)";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nombreSucursal);
            statement.setString(2, gerente);
            statement.setString(3, direccion);
            statement.setInt(4, telefono);

            int filasInsertadas = statement.executeUpdate();
            return filasInsertadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos: " + e.getMessage());
            return false;
        }
    }

    private void limitarSoloNumeros(JTextField textField) {
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AgregarSucursales frame = new AgregarSucursales("Agregar Sucursal");
            frame.setVisible(true);
        });
    }
}
