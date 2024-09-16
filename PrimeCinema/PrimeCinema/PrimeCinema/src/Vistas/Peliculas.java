package Vistas;

import Vistas.DB.ConexionDB;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Peliculas extends JFrame {
    private JPanel panelMain;
    private JButton buttonRegistrar;
    private JButton MostrarUsuariosButton;
    private JComboBox<String> cmbGenero;
    private JTextField txtPeliculas;
    private JComboBox<String> cmbClasificacion;

    public Peliculas(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panelMain);
        this.setSize(400, 500);
        this.setLocationRelativeTo(null);

        // Inicializar ComboBox con opciones para Género
        cmbGenero.addItem("Acción");
        cmbGenero.addItem("Comedia");
        cmbGenero.addItem("Drama");
        cmbGenero.addItem("Terror");
        cmbGenero.addItem("Ciencia Ficción");

        // Inicializar ComboBox con opciones para Clasificación
        cmbClasificacion.addItem("G");
        cmbClasificacion.addItem("PG");
        cmbClasificacion.addItem("PG-13");
        cmbClasificacion.addItem("R");
        cmbClasificacion.addItem("NC-17");

        // Acción para el botón Registrar
        buttonRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarPelicula();
            }
        });

        // Acción para el botón Mostrar Películas
        MostrarUsuariosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPeliculas();
            }
        });
    }

    // Método para registrar una película en la base de datos
    private void registrarPelicula() {
        String pelicula = txtPeliculas.getText();
        String genero = (String) cmbGenero.getSelectedItem();
        String clasificacion = (String) cmbClasificacion.getSelectedItem();

        // Validar que todos los campos no estén vacíos
        if (pelicula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa el campo de película.");
            return;
        }

        // Convertir el género y la clasificación a sus respectivos IDs
        int idGenero = obtenerIdGenero(genero);
        int idClasificacion = obtenerIdClasificacion(clasificacion);

        // Registrar la película en la base de datos
        if (guardarPelicula(pelicula, idGenero, idClasificacion)) {
            JOptionPane.showMessageDialog(this, "Película registrada exitosamente:\n" +
                    "Película: " + pelicula + "\n" +
                    "Género: " + genero + "\n" +
                    "Clasificación: " + clasificacion);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar la película. Inténtalo de nuevo.");
        }
    }

    // Método para convertir el género a su ID
    private int obtenerIdGenero(String genero) {
        switch (genero) {
            case "Acción": return 1;
            case "Comedia": return 2;
            case "Drama": return 3;
            case "Terror": return 4;
            case "Ciencia Ficción": return 5;
            default: return -1;
        }
    }

    // Método para convertir la clasificación a su ID
    private int obtenerIdClasificacion(String clasificacion) {
        switch (clasificacion) {
            case "G": return 1;
            case "PG": return 2;
            case "PG-13": return 3;
            case "R": return 4;
            case "NC-17": return 5;
            default: return -1;
        }
    }

    // Método para insertar la película en la base de datos
    private boolean guardarPelicula(String nombrePelicula, int idGenero, int idClasificacion) {
        String sql = "INSERT INTO Peliculas (NombrePelicula, IdGenero, IdClasificacion) VALUES (?, ?, ?)";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Asignar los parámetros a la sentencia SQL
            statement.setString(1, nombrePelicula);
            statement.setInt(2, idGenero);
            statement.setInt(3, idClasificacion);

            // Ejecutar la inserción
            int filasInsertadas = statement.executeUpdate();
            return filasInsertadas > 0;  // Devuelve true si se insertaron filas

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos: " + e.getMessage());
            return false;
        }
    }

    private void mostrarPeliculas() {
        JOptionPane.showMessageDialog(this, "Mostrando películas:\nPelicula1\nPelicula2\nPelicula3");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Peliculas frame = new Peliculas("Gestión de Películas");
            frame.setVisible(true);
        });
    }
}