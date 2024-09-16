package Vistas.AgregarFuncion;

import Vistas.DB.ConexionDB;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class AgregarFuncion extends JFrame {
    private JPanel panelAgregarFuncion;
    private JComboBox<String> cmbFormato;
    private JTextField txt_ID_Sucursal;
    private JTextField txtHorario;
    private JTextField txtPelicula;
    private JTextField txt_ID_Sala;
    private JButton btnGuardar;

    public AgregarFuncion(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panelAgregarFuncion);
        this.setMinimumSize(new Dimension(600, 500));
        this.setLocationRelativeTo(null);
        cmbFormato.addItem("2D");
        cmbFormato.addItem("3D");
        limitarSoloNumeros(txt_ID_Sucursal);
        limitarSoloNumeros(txt_ID_Sala);
        agregarValidacionHorario(txtHorario);
        btnGuardar.addActionListener(e -> guardarFuncion());
    }

    private void guardarFuncion() {
        String idSucursalTexto = txt_ID_Sucursal.getText();
        String horario = txtHorario.getText();
        String peliculaTexto = txtPelicula.getText();
        String idSalaTexto = txt_ID_Sala.getText();
        String formato = (String) cmbFormato.getSelectedItem();

        if (idSucursalTexto.isEmpty() || horario.isEmpty() || peliculaTexto.isEmpty() || idSalaTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.");
            return;
        }

        if (!validarHorario(horario)) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un horario válido en formato 24 horas (HH:mm).");
            return;
        }

        int idSucursal;
        int idSala;
        int idPelicula;
        try {
            idSucursal = Integer.parseInt(idSucursalTexto);
            idSala = Integer.parseInt(idSalaTexto);
            idPelicula = Integer.parseInt(peliculaTexto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID Sucursal, ID Sala e ID Película deben ser números válidos.");
            return;
        }

        int formatoID;
        if ("2D".equals(formato)) {
            formatoID = 1;
        } else if ("3D".equals(formato)) {
            formatoID = 2;
        } else {
            JOptionPane.showMessageDialog(this, "Formato no válido.");
            return;
        }

        if (registrarFuncion(idSucursal, idSala, idPelicula, horario, formatoID)) {
            JOptionPane.showMessageDialog(this, "Función guardada exitosamente.");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo guardar la función. Inténtalo de nuevo.");
        }
    }

    private boolean registrarFuncion(int idSucursal, int idSala, int idPelicula, String horario, int formatoID) {
        String sql = "INSERT INTO Funciones (IdSucursal, IdSala, IdPelicula, Horario, IdFormato) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idSucursal);
            statement.setInt(2, idSala);
            statement.setInt(3, idPelicula);
            statement.setString(4, horario);
            statement.setInt(5, formatoID);

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

    private void agregarValidacionHorario(JTextField textField) {
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (textField.getText().length() >= 5) {
                    e.consume();
                }
            }
        });
    }

    private boolean validarHorario(String horario) {
        String regex = "^([01]\\d|2[0-3]):([0-5]\\d)$";
        return Pattern.matches(regex, horario);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AgregarFuncion frame = new AgregarFuncion("Agregar Función");
            frame.setVisible(true);
        });
    }
}