package Vistas;

import Vistas.CompraEntradas.CompraEntradas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuUser extends JFrame {
    private JButton comprarTicketButton;
    private JButton registrarTicketButton;
    private JPanel MainPanel;

    public MenuUser(String title) {
        super(title);

        comprarTicketButton = new JButton("Comprar Ticket");
        registrarTicketButton = new JButton("Registrar Ticket");

        configurarBotones();

        MainPanel = new JPanel();
        MainPanel.setLayout(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        buttonPanel.add(comprarTicketButton);
        buttonPanel.add(registrarTicketButton);

        MainPanel.add(buttonPanel, BorderLayout.CENTER);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(MainPanel);
        this.setMinimumSize(new Dimension(400, 300));
        this.setLocationRelativeTo(null);
    }

    private void configurarBotones() {

        comprarTicketButton.setFont(new Font("Arial", Font.BOLD, 14));
        registrarTicketButton.setFont(new Font("Arial", Font.BOLD, 14));

        comprarTicketButton.setBackground(new Color(100, 150, 255));
        registrarTicketButton.setBackground(new Color(100, 150, 255));

        comprarTicketButton.setForeground(Color.WHITE);
        registrarTicketButton.setForeground(Color.WHITE);

        comprarTicketButton.setFocusPainted(false);
        registrarTicketButton.setFocusPainted(false);

        comprarTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                CompraEntradas compraEntradas = new CompraEntradas("Compra de Entradas");
                compraEntradas.setVisible(true);
            }
        });

        registrarTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Tickets tickets = new Tickets("Tickets");
                tickets.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuUser menuUser = new MenuUser("Menu de Usuario");
            menuUser.setVisible(true);
        });
    }
}
