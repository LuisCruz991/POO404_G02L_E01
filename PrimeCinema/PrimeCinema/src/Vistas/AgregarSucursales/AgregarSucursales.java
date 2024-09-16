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

        // Configuración básica de la ventana
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panelAgregarSucursales);
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);

        // Limitar el campo de teléfono a solo números
        limitarSoloNumeros(txtTelefono);

        // Acción para el botón de agregar sucursal
        agregarSucursalButton.addActionListener(e -> agregarSucursal());
    }

    // Método para agregar la sucursal (validaciones incluidas)
    private void agregarSucursal() {
        String nombreSucursal = txtNombreSucursal.getText();
        String gerente = txtGerente.getText();
        String direccion = txtDireccion.getText();
        String telefonoTexto = txtTelefono.getText();

        // Validar que los campos no estén vacíos
        if (nombreSucursal.isEmpty() || gerente.isEmpty() || direccion.isEmpty() || telefonoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.");
            return;
        }

        // Validar que el teléfono sea un número entero
        int telefono;
        try {
            telefono = Integer.parseInt(telefonoTexto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El teléfono debe ser un número válido.");
            return;
        }

        // Registrar la sucursal en la base de datos
        if (registrarSucursal(nombreSucursal, gerente, direccion, telefono)) {
            JOptionPane.showMessageDialog(this, "Sucursal agregada exitosamente.");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo agregar la sucursal. Inténtalo de nuevo.");
        }
    }

    // Método para insertar la sucursal en la base de datos
    private boolean registrarSucursal(String nombreSucursal, String gerente, String direccion, int telefono) {
        String sql = "INSERT INTO Sucursales (NombreSucursal, Encargado, Direccion, Telefono) VALUES (?, ?, ?, ?)";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Asignar los parámetros a la sentencia SQL
            statement.setString(1, nombreSucursal);
            statement.setString(2, gerente);
            statement.setString(3, direccion);
            statement.setInt(4, telefono);

            // Ejecutar la inserción
            int filasInsertadas = statement.executeUpdate();
            return filasInsertadas > 0;  // Devuelve true si se insertaron filas

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos: " + e.getMessage());
            return false;
        }
    }

    // Limitar el JTextField a solo números
    private void limitarSoloNumeros(JTextField textField) {
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();  // Ignorar la tecla si no es un número
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
