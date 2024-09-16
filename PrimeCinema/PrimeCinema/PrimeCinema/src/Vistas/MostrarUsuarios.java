package Vistas;

import Vistas.DB.ConexionDB;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MostrarUsuarios {
    private JPanel panel1;
    private JTable table1;
    private JLabel loginLabel;
    private JLabel nombreCompletoLabel;
    private JLabel DUILabel;
    private JLabel direccionLabel;
    private JLabel correoLabel;
    private JLabel telefonoLabel;

    public MostrarUsuarios() {

        initializeUI();

        List<UsuarioData> usuarios = cargarUsuariosDesdeDB();

        String[] columnNames = {"Login", "Nombre Completo", "DUI", "Dirección", "Correo", "Teléfono"};
        if (usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(panel1, "No hay ningún usuario registrado.", "Información", JOptionPane.INFORMATION_MESSAGE);
        }

        Object[][] data = usuarios.stream().map(u -> new Object[]{
                u.getLogin(),
                u.getNombreCompleto(),
                u.getDui(),
                u.getDireccion(),
                u.getCorreo(),
                u.getTelefono()
        }).toArray(Object[][]::new);

        table1.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    private void initializeUI() {
        panel1 = new JPanel();
        table1 = new JTable();
        loginLabel = new JLabel();
        nombreCompletoLabel = new JLabel();
        DUILabel = new JLabel();
        direccionLabel = new JLabel();
        correoLabel = new JLabel();
        telefonoLabel = new JLabel();

        panel1.add(new JScrollPane(table1));
        panel1.add(loginLabel);
        panel1.add(nombreCompletoLabel);
        panel1.add(DUILabel);
        panel1.add(direccionLabel);
        panel1.add(correoLabel);
        panel1.add(telefonoLabel);
    }

    private List<UsuarioData> cargarUsuariosDesdeDB() {
        List<UsuarioData> usuarios = new ArrayList<>();
        String query = "SELECT Nombre, Clave, DUI, Direccion, Correo, Telefono FROM Usuarios";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UsuarioData usuario = new UsuarioData(
                        rs.getString("Nombre"),
                        rs.getString("Clave"),
                        rs.getString("DUI"),
                        rs.getString("Direccion"),
                        rs.getString("Correo"),
                        rs.getString("Telefono")
                );
                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(panel1, "Error al cargar los usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return usuarios;
    }

    public JPanel getPanel1() {
        return panel1;
    }

    private class UsuarioData {
        private String login;
        private String nombreCompleto;
        private String dui;
        private String direccion;
        private String correo;
        private String telefono;

        public UsuarioData(String login, String nombreCompleto, String dui, String direccion, String correo, String telefono) {
            this.login = login;
            this.nombreCompleto = nombreCompleto;
            this.dui = dui;
            this.direccion = direccion;
            this.correo = correo;
            this.telefono = telefono;
        }

        public String getLogin() { return login; }
        public String getNombreCompleto() { return nombreCompleto; }
        public String getDui() { return dui; }
        public String getDireccion() { return direccion; }
        public String getCorreo() { return correo; }
        public String getTelefono() { return telefono; }
    }
}