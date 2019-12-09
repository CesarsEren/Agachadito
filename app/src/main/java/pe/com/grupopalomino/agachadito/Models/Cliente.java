package pe.com.grupopalomino.agachadito.Models;

//import lombok.Data;

//@Data
public class Cliente {
    private int id;
    private String nombre;
    private String apellido;
    private String password;

    public Cliente(int id, String nombre, String apellido, String password) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.password = password;
    }

    public Cliente() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
