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
        super(title);  // Establece el título de la ventana

        // Configurar la ventana principal
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panelAgregarFuncion);  // Usar el panel que creaste en el diseñador
        this.setMinimumSize(new Dimension(600, 500));  // Ajustar tamaño mínimo
        this.setLocationRelativeTo(null);  // Centrar la ventana en la pantalla

        // Inicializar ComboBox con opciones
        cmbFormato.addItem("2D");
        cmbFormato.addItem("3D");

        // Agregar restricciones a los campos
        limitarSoloNumeros(txt_ID_Sucursal);
        limitarSoloNumeros(txt_ID_Sala);
        agregarValidacionHorario(txtHorario);

        // Asignar funcionalidad al botón
        btnGuardar.addActionListener(e -> guardarFuncion());
    }

    private void guardarFuncion() {
        // Obtener los valores de los campos
        String idSucursalTexto = txt_ID_Sucursal.getText();
        String horario = txtHorario.getText();
        String peliculaTexto = txtPelicula.getText();
        String idSalaTexto = txt_ID_Sala.getText();
        String formato = (String) cmbFormato.getSelectedItem();

        // Validar que todos los campos estén completos
        if (idSucursalTexto.isEmpty() || horario.isEmpty() || peliculaTexto.isEmpty() || idSalaTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.");
            return;
        }

        // Validar el formato del horario
        if (!validarHorario(horario)) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un horario válido en formato 24 horas (HH:mm).");
            return;
        }

        // Convertir los campos numéricos
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

        // Convertir el formato a 1 o 2
        int formatoID;
        if ("2D".equals(formato)) {
            formatoID = 1;
        } else if ("3D".equals(formato)) {
            formatoID = 2;
        } else {
            JOptionPane.showMessageDialog(this, "Formato no válido.");
            return;
        }

        // Registrar la función en la base de datos
        if (registrarFuncion(idSucursal, idSala, idPelicula, horario, formatoID)) {
            JOptionPane.showMessageDialog(this, "Función guardada exitosamente.");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo guardar la función. Inténtalo de nuevo.");
        }
    }

    // Método para insertar la función en la base de datos
    private boolean registrarFuncion(int idSucursal, int idSala, int idPelicula, String horario, int formatoID) {
        String sql = "INSERT INTO Funciones (IdSucursal, IdSala, IdPelicula, Horario, IdFormato) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Asignar los parámetros a la sentencia SQL
            statement.setInt(1, idSucursal);
            statement.setInt(2, idSala);
            statement.setInt(3, idPelicula);
            statement.setString(4, horario);  // Horario en formato "HH:mm"
            statement.setInt(5, formatoID);

            // Ejecutar la inserción
            int filasInsertadas = statement.executeUpdate();
            return filasInsertadas > 0;  // Devuelve true si se insertaron filas

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos: " + e.getMessage());
            return false;
        }
    }

    // Limitar los JTextFields a solo números
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

    // Validar que el horario esté en formato 24 horas (HH:mm)
    private void agregarValidacionHorario(JTextField textField) {
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Restringir la longitud a 5 caracteres para el formato HH:mm
                if (textField.getText().length() >= 5) {
                    e.consume();
                }
            }
        });
    }

    // Método para validar que el horario esté en formato HH:mm (24 horas)
    private boolean validarHorario(String horario) {
        // Expresión regular para validar formato de 24 horas (HH:mm)
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