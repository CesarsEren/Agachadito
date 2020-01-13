package pe.com.grupopalomino.agachadito.Models;

public class SpinerMedioPago {

    String spinerText;
    int id_mediopago;
    int spinerImg;
    String dueño;

    public String getDueño() {
        return dueño;
    }

    public void setDueño(String dueño) {
        this.dueño = dueño;
    }

    public SpinerMedioPago(String spinerText) {
        this.spinerText = spinerText;
    }

    public SpinerMedioPago() {
    }

    public SpinerMedioPago(String spinerText, int id_mediopago, int spinerImg) {
        this.spinerText = spinerText;
        this.id_mediopago = id_mediopago;
        this.spinerImg = spinerImg;
    }

    public String getSpinerText() {
        return spinerText;
    }

    public void setSpinerText(String spinerText) {
        this.spinerText = spinerText;
    }

    public int getId_mediopago() {
        return id_mediopago;
    }

    public void setId_mediopago(int id_mediopago) {
        this.id_mediopago = id_mediopago;
    }

    public int getSpinerImg() {
        return spinerImg;
    }

    public void setSpinerImg(int spinerImg) {
        this.spinerImg = spinerImg;
    }
}
