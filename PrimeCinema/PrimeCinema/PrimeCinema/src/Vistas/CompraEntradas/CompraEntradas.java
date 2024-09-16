package Vistas.CompraEntradas;

import Vistas.DB.ConexionDB;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CompraEntradas extends JFrame {
    private JPanel panelCompraEntradas;
    private JTextField txtCompraEntradas;
    private JLabel Lbltotal;
    private JButton btnCalcularTotal;
    private JButton btnIrAlCambio;
    private JComboBox<String> CmbFormato;
    private JComboBox<String> cmbEdad;

    // Precios según formato y edad
    private final double precioEntradaTradicionalTerceraEdad = 3.90;
    private final double precioEntradaTradicionalAdulto = 5.00;
    private final double precioEntrada3DTerceraEdad = 5.60;
    private final double precioEntrada3DAdulto = 6.55;

    public CompraEntradas(String title) {
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panelCompraEntradas);
        this.setMinimumSize(new Dimension(600, 500));
        this.setLocationRelativeTo(null);

        btnCalcularTotal.addActionListener(e -> calcularTotal());
        btnIrAlCambio.addActionListener(e -> abrirFormularioCambio());

        CmbFormato.addItem("Tradicional");
        CmbFormato.addItem("3D");

        cmbEdad.addItem("Tercera edad");
        cmbEdad.addItem("Adulto");
    }

    private void calcularTotal() {
        try {
            int cantidadEntradas = Integer.parseInt(txtCompraEntradas.getText());
            if (cantidadEntradas <= 0) {
                JOptionPane.showMessageDialog(null, "La cantidad de entradas debe ser mayor a 0.");
                return;
            }

            String formatoSeleccionado = (String) CmbFormato.getSelectedItem();
            String edadSeleccionada = (String) cmbEdad.getSelectedItem();

            double precioEntrada;
            if (formatoSeleccionado.equals("3D")) {
                precioEntrada = edadSeleccionada.equals("Tercera edad") ? precioEntrada3DTerceraEdad : precioEntrada3DAdulto;
            } else { // Tradicional
                precioEntrada = edadSeleccionada.equals("Tercera edad") ? precioEntradaTradicionalTerceraEdad : precioEntradaTradicionalAdulto;
            }

            double totalAPagar = cantidadEntradas * precioEntrada;
            Lbltotal.setText("Total a pagar: $" + String.format("%.2f", totalAPagar));

            txtCompraEntradas.setEnabled(false);
            CmbFormato.setEnabled(false);
            cmbEdad.setEnabled(false);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese una cantidad válida.");
        }
    }

    private void abrirFormularioCambio() {
        String cantidadEntradasTexto = txtCompraEntradas.getText();
        if (cantidadEntradasTexto.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese una cantidad de entradas.");
            return;
        }

        double totalAPagar;
        int cantidadEntradas;
        try {
            cantidadEntradas = Integer.parseInt(cantidadEntradasTexto);
            if (cantidadEntradas <= 0) {
                JOptionPane.showMessageDialog(null, "La cantidad de entradas debe ser mayor a 0.");
                return;
            }

            String formatoSeleccionado = (String) CmbFormato.getSelectedItem();
            String edadSeleccionada = (String) cmbEdad.getSelectedItem();

            double precioEntrada;
            if (formatoSeleccionado.equals("3D")) {
                precioEntrada = edadSeleccionada.equals("Tercera edad") ? precioEntrada3DTerceraEdad : precioEntrada3DAdulto;
            } else { // Tradicional
                precioEntrada = edadSeleccionada.equals("Tercera edad") ? precioEntradaTradicionalTerceraEdad : precioEntradaTradicionalAdulto;
            }

            totalAPagar = cantidadEntradas * precioEntrada;

            System.out.println("Cantidad Entradas: " + cantidadEntradas);
            System.out.println("Total a Pagar: " + totalAPagar);

            boolean compraRegistrada = registrarCompra(cantidadEntradas, totalAPagar);

            if (compraRegistrada) {
                this.dispose();
                Vistas.CalculoCambio.CalculoCambio.openForm(totalAPagar);
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo registrar la compra. Inténtalo de nuevo.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese una cantidad válida.");
        }
    }

    private boolean registrarCompra(int cantidadEntradas, double totalAPagar) {
        String sql = "INSERT INTO Compras (CantidadEntradas, PrecioTotal) VALUES (?, ?)";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cantidadEntradas);
            statement.setDouble(2, totalAPagar);

            System.out.println("Ejecutando query: " + sql);
            System.out.println("Cantidad de entradas: " + cantidadEntradas);
            System.out.println("Total a pagar: " + totalAPagar);

            int filasInsertadas = statement.executeUpdate();
            if (filasInsertadas > 0) {
                JOptionPane.showMessageDialog(null, "Compra registrada exitosamente.");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Error: No se insertaron registros.");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CompraEntradas frame = new CompraEntradas("Compra de Entradas");
            frame.setVisible(true);
        });
    }
}
