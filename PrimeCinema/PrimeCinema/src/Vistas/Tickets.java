package Vistas;

import Vistas.DB.ConexionDB;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Tickets extends JFrame {
    private JPanel panelMain;
    private JButton btnCreaTicket;
    private JTextField txtFuncion;
    private JTextField txtAsiento;
    private JTextField txtPrecio;
    private JTextField txtTicket;  // Campo txtTicket desactivado
    private JTextField txtIdSala;
    private JTextField txtIdCompra;
    private JButton mostrarTicketsButton;

    public Tickets(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panelMain);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);

        // Inicializar y agregar acciones a los botones
        btnCreaTicket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarTicket();
            }
        });

        mostrarTicketsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarTickets();
            }
        });

        // Desactivar el campo txtTicket
        txtTicket.setEnabled(false);
    }

    // Método para registrar un ticket
    private void registrarTicket() {
        String funcion = txtFuncion.getText();
        String asiento = txtAsiento.getText();
        String precio = txtPrecio.getText();
        String idSala = txtIdSala.getText();
        String idCompra = txtIdCompra.getText();

        // Validar que todos los campos no estén vacíos (excepto txtTicket)
        if (funcion.isEmpty() || asiento.isEmpty() || precio.isEmpty() || idSala.isEmpty() || idCompra.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.");
            return;
        }

        // Guardar el ticket en la base de datos
        guardarTicket(funcion, asiento, precio, idSala, idCompra);
    }

    // Método para guardar un ticket en la base de datos
    private void guardarTicket(String funcion, String asiento, String precio, String idSala, String idCompra) {
        String query = "INSERT INTO Ticket (IdFuncion, IdAsiento, Precio, IdSala, IdCompra) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, funcion);
            stmt.setString(2, asiento);
            stmt.setString(3, precio);
            stmt.setString(4, idSala);
            stmt.setString(5, idCompra);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Ticket registrado exitosamente.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar el ticket: " + e.getMessage());
        }
    }

    // Método para mostrar tickets (simulación)
    private void mostrarTickets() {
        // Aquí puedes agregar la lógica para recuperar y mostrar los tickets de la base de datos
        JOptionPane.showMessageDialog(this, "Mostrando tickets:\nTicket1\nTicket2\nTicket3");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Tickets frame = new Tickets("Gestión de Tickets");
            frame.setVisible(true);
        });
    }
}
