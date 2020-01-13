package pe.com.grupopalomino.agachadito.Models;

public class VPedidoBean {
    int id_venta;
    String fotourl;
    String preciototal;
    String nombrecliente;
    String horapedido;

    public VPedidoBean() {
    }

    public VPedidoBean(int id_venta, String fotourl, String preciototal, String nombrecliente, String horapedido) {
        this.id_venta = id_venta;
        this.fotourl = fotourl;
        this.preciototal = preciototal;
        this.nombrecliente = nombrecliente;
        this.horapedido = horapedido;
    }

    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    public String getFotourl() {
        return fotourl;
    }

    public void setFotourl(String fotourl) {
        this.fotourl = fotourl;
    }

    public String getPreciototal() {
        return preciototal;
    }

    public void setPreciototal(String preciototal) {
        this.preciototal = preciototal;
    }

    public String getNombrecliente() {
        return nombrecliente;
    }

    public void setNombrecliente(String nombrecliente) {
        this.nombrecliente = nombrecliente;
    }

    public String getHorapedido() {
        return horapedido;
    }

    public void setHorapedido(String horapedido) {
        this.horapedido = horapedido;
    }
}
