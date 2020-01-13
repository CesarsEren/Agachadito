package pe.com.grupopalomino.agachadito.Models;

public class Ventas {

    private int id_venta;
    private int id_cliente;
    private int id_vendedor;
    private String DNI;
    private String NombreCompleto;
    private String FechaEmision;
    private String Serie;
    private String Numero;
    private String EstadoApp;

    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_vendedor() {
        return id_vendedor;
    }

    public void setId_vendedor(int id_vendedor) {
        this.id_vendedor = id_vendedor;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getNombreCompleto() {
        return NombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        NombreCompleto = nombreCompleto;
    }

    public String getFechaEmision() {
        return FechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        FechaEmision = fechaEmision;
    }

    public String getSerie() {
        return Serie;
    }

    public void setSerie(String serie) {
        Serie = serie;
    }

    public String getNumero() {
        return Numero;
    }

    public void setNumero(String numero) {
        Numero = numero;
    }

    public String getEstadoApp() {
        return EstadoApp;
    }

    public void setEstadoApp(String estadoApp) {
        EstadoApp = estadoApp;
    }

    public Ventas() {
    }


    public Ventas(int id_venta, int id_cliente, int id_vendedor, String DNI, String nombreCompleto, String fechaEmision, String serie, String numero, String estadoApp) {

        this.id_venta = id_venta;
        this.id_cliente = id_cliente;
        this.id_vendedor = id_vendedor;
        this.DNI = DNI;
        NombreCompleto = nombreCompleto;
        FechaEmision = fechaEmision;
        Serie = serie;
        Numero = numero;
        EstadoApp = estadoApp;
    }
}
