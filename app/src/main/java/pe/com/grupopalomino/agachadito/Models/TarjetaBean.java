package pe.com.grupopalomino.agachadito.Models;

public class TarjetaBean {

    private int id_cliente;
    private int id_mediopago;
    private String imgtarjeta;
    private String tvdueño;
    private String tvtarjeta;
    private String btndelete;


    public TarjetaBean(int id_cliente, int id_mediopago, String imgtarjeta, String tvdueño, String tvtarjeta, String btndelete) {
        this.id_cliente = id_cliente;
        this.id_mediopago = id_mediopago;
        this.imgtarjeta = imgtarjeta;
        this.tvdueño = tvdueño;
        this.tvtarjeta = tvtarjeta;
        this.btndelete = btndelete;
    }

    public TarjetaBean() {
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_mediopago() {
        return id_mediopago;
    }

    public void setId_mediopago(int id_mediopago) {
        this.id_mediopago = id_mediopago;
    }

    public String getImgtarjeta() {
        return imgtarjeta;
    }

    public void setImgtarjeta(String imgtarjeta) {
        this.imgtarjeta = imgtarjeta;
    }

    public String getTvdueño() {
        return tvdueño;
    }

    public void setTvdueño(String tvdueño) {
        this.tvdueño = tvdueño;
    }

    public String getTvtarjeta() {
        return tvtarjeta;
    }

    public void setTvtarjeta(String tvtarjeta) {
        this.tvtarjeta = tvtarjeta;
    }

    public String getBtndelete() {
        return btndelete;
    }

    public void setBtndelete(String btndelete) {
        this.btndelete = btndelete;
    }
}
