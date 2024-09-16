import Vistas.Usuario;

import javax.swing.*;


public class Main extends JFrame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Formulario  para el registro de usuarios");
        frame.setContentPane(new Usuario().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
