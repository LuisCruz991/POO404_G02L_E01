package Vistas;

import Vistas.DB.ConexionDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MostrarTicket {
    private JLabel loginLabel;
    private JLabel nombreCompletoLabel;
    private JLabel DUILabel;
    private JLabel direccionLabel;
    private JLabel correoLabel;
    private JLabel telefonoLabel;
    private JTable table1;
    public JPanel panelMain;

    public MostrarTicket() {
        // Inicializar panelMain y sus componentes
        panelMain = new JPanel();
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));

        // Crear y agregar el JTable al panel
        table1 = new JTable();
        JScrollPane scrollPane = new JScrollPane(table1);
        panelMain.add(scrollPane);

        // Inicializar el modelo del JTable
        table1.setModel(new DefaultTableModel(
                new Object[]{"IdTicket", "Precio", "IdFuncion", "IdSala", "IdAsiento", "IdCompra"},
                0
        ));

        // Llamar al método para cargar los datos en la tabla
        cargarDatos();
    }

    // Método para cargar los datos de la base de datos en la tabla
    private void cargarDatos() {
        String query = "SELECT IdTicket, Precio, IdFuncion, IdSala, IdAsiento, IdCompra FROM Ticket";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            // Limpiar cualquier dato previo en la tabla
            model.setRowCount(0);

            while (rs.next()) {
                // Leer datos de cada fila del ResultSet
                int idTicket = rs.getInt("IdTicket");
                double precio = rs.getDouble("Precio");
                int idFuncion = rs.getInt("IdFuncion");
                int idSala = rs.getInt("IdSala");
                int idAsiento = rs.getInt("IdAsiento");
                int idCompra = rs.getInt("IdCompra");

                // Agregar datos al modelo de la tabla
                model.addRow(new Object[]{idTicket, precio, idFuncion, idSala, idAsiento, idCompra});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Crear y configurar el JFrame
            JFrame frame = new JFrame("Mostrar Tickets");
            MostrarTicket mostrarTicket = new MostrarTicket();
            frame.setContentPane(mostrarTicket.panelMain);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
