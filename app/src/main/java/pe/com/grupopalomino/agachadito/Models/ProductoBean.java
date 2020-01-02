package pe.com.grupopalomino.agachadito.Models;

public class ProductoBean {

    int id_vendedor;
    int id_producto;
    String precio;
    String nombre;
    String foto;

    public ProductoBean() {
    }

    public void setId_vendedor(int id_vendedor) {
        this.id_vendedor = id_vendedor;
    }

    public int getId_vendedor() {
        return id_vendedor;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public ProductoBean(int id_vendedor, int id_producto, String precio, String nombre, String foto) {
        this.id_vendedor = id_vendedor;
        this.id_producto = id_producto;
        this.precio = precio;
        this.nombre = nombre;
        this.foto = foto;
    }

}