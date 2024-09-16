package Vistas;

public class UsuarioData {
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
