package Vistas.CalculoCambio;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculoCambio {
    // Los componentes gestionados automáticamente por el GUI Designer
    private JPanel panelCalculoCambio;
    private JTextField txtDineroEntregado;
    private JTextField txtTotalAPagar;
    private JLabel LblCambio;
    private JButton btnCalcularCambio;

    public CalculoCambio(double totalAPagar) {
        // Establecer el valor del total a pagar y deshabilitar el campo
        txtTotalAPagar.setText(String.format("%.2f", totalAPagar));
        txtTotalAPagar.setEnabled(false);

        // Acción al presionar el botón "Calcular Cambio"
        btnCalcularCambio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularCambio();
            }
        });
    }

    private void calcularCambio() {
        try {
            double totalAPagar = Double.parseDouble(txtTotalAPagar.getText());
            double dineroEntregado = Double.parseDouble(txtDineroEntregado.getText());

            if (dineroEntregado >= totalAPagar) {
                double cambio = dineroEntregado - totalAPagar;
                LblCambio.setText("Cambio: $" + String.format("%.2f", cambio));
            } else {
                LblCambio.setText("Dinero insuficiente.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese valores válidos.");
        }
    }

    // Método estático para abrir el formulario
    public static void openForm(double totalAPagar) {
        JFrame frame = new JFrame("Cálculo de Cambio");
        CalculoCambio calculoCambio = new CalculoCambio(totalAPagar);
        frame.setContentPane(calculoCambio.panelCalculoCambio); // Establece el panel gestionado automáticamente
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}