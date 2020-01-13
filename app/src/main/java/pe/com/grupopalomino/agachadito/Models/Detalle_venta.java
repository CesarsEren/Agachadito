package pe.com.grupopalomino.agachadito.Models;


public class Detalle_venta {

    int id_venta;
    int id_producto;
    String nombreproducto;
    double precioproducto;
    String fotoproducto;
    int cantidad;

    public Detalle_venta() {
    }

    public Detalle_venta(int id_venta, int id_producto, String nombreproducto, double precioproducto, String fotoproducto, int cantidad) {

        this.id_venta = id_venta;
        this.id_producto = id_producto;
        this.nombreproducto = nombreproducto;
        this.precioproducto = precioproducto;
        this.fotoproducto = fotoproducto;
        this.cantidad = cantidad;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public void setNombreproducto(String nombreproducto) {
        this.nombreproducto = nombreproducto;
    }

    public void setPrecioproducto(double precioproducto) {
        this.precioproducto = precioproducto;
    }

    public void setFotoproducto(String fotoproducto) {
        this.fotoproducto = fotoproducto;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getId_venta() {
        return id_venta;
    }

    public int getId_producto() {
        return id_producto;
    }

    public String getNombreproducto() {
        return nombreproducto;
    }

    public double getPrecioproducto() {
        return precioproducto;
    }

    public String getFotoproducto() {
        return fotoproducto;
    }

    public int getCantidad() {
        return cantidad;
    }
}
