package pe.com.grupopalomino.agachadito.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificacionBean implements Parcelable {

    int id_venta;
    String urlfotonoti;
    String nombrenegocio;
    String fechapedido;
    double preciototal;

    String EstadoApp;

    public String getEstadoApp() {
        return EstadoApp;
    }

    public void setEstadoApp(String estadoApp) {
        EstadoApp = estadoApp;
    }

    protected NotificacionBean(Parcel in) {
        id_venta = in.readInt();
        urlfotonoti = in.readString();
        nombrenegocio = in.readString();
        fechapedido = in.readString();
        preciototal = in.readDouble();
    }

    public static final Creator<NotificacionBean> CREATOR = new Creator<NotificacionBean>() {
        @Override
        public NotificacionBean createFromParcel(Parcel in) {
            return new NotificacionBean(in);
        }

        @Override
        public NotificacionBean[] newArray(int size) {
            return new NotificacionBean[size];
        }
    };

    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    public NotificacionBean() {
    }

    public NotificacionBean(String urlfotonoti, String nombrenegocio, String fechapedido, double preciototal) {
        this.urlfotonoti = urlfotonoti;
        this.nombrenegocio = nombrenegocio;
        this.fechapedido = fechapedido;
        this.preciototal = preciototal;
    }

    public String getUrlfotonoti() {
        return urlfotonoti;
    }

    public void setUrlfotonoti(String urlfotonoti) {
        this.urlfotonoti = urlfotonoti;
    }

    public String getNombrenegocio() {
        return nombrenegocio;
    }

    public void setNombrenegocio(String nombrenegocio) {
        this.nombrenegocio = nombrenegocio;
    }

    public String getFechapedido() {
        return fechapedido;
    }

    public void setFechapedido(String fechapedido) {
        this.fechapedido = fechapedido;
    }

    public double getPreciototal() {
        return preciototal;
    }

    public void setPreciototal(double preciototal) {
        this.preciototal = preciototal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id_venta);
        parcel.writeString(urlfotonoti);
        parcel.writeString(nombrenegocio);
        parcel.writeString(fechapedido);
        parcel.writeDouble(preciototal);
    }
}
