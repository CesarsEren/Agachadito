package pe.com.grupopalomino.agachadito.Models;

public class PuestosBean {
    int id_vendedor;
    String nombrepuesto;
    String categoriapuesto;//REFERENCIA
    double preciopromedio;
    String urlimg;

    public PuestosBean() {
    }

    public PuestosBean(String nombrepuesto, String categoriapuesto, double preciopromedio, String urlimg) {

        this.nombrepuesto = nombrepuesto;
        this.categoriapuesto = categoriapuesto;
        this.preciopromedio = preciopromedio;
        this.urlimg = urlimg;
    }

    public void setId_vendedor(int id_vendedor) {
        this.id_vendedor = id_vendedor;
    }

    public int getId_vendedor() {
        return id_vendedor;
    }

    @Override
    public String toString() {
        return "PuestosBean{" +
                "nombrepuesto='" + nombrepuesto + '\'' +
                ", categoriapuesto='" + categoriapuesto + '\'' +
                ", preciopromedio='" + preciopromedio + '\'' +
                ", urlimg='" + urlimg + '\'' +
                '}';
    }

    public String getNombrepuesto() {
        return nombrepuesto;
    }

    public void setNombrepuesto(String nombrepuesto) {
        this.nombrepuesto = nombrepuesto;
    }

    public String getCategoriapuesto() {
        return categoriapuesto;
    }

    public void setCategoriapuesto(String categoriapuesto) {
        this.categoriapuesto = categoriapuesto;
    }

    public double getPreciopromedio() {
        return preciopromedio;
    }

    public void setPreciopromedio(double preciopromedio) {
        this.preciopromedio = preciopromedio;
    }

    public String getUrlimg() {
        return urlimg;
    }

    public void setUrlimg(String urlimg) {
        this.urlimg = urlimg;
    }
}
