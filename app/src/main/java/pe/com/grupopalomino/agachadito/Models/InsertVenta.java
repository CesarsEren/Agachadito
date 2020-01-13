package pe.com.grupopalomino.agachadito.Models;

import java.util.List;

public class InsertVenta {
    Ventas cabecera;
    List<Detalle_venta> body;

    public InsertVenta() {
    }

    public InsertVenta(Ventas cabecera, List<Detalle_venta> body) {
        this.cabecera = cabecera;
        this.body = body;
    }

    public Ventas getCabecera() {
        return cabecera;
    }

    public void setCabecera(Ventas cabecera) {
        this.cabecera = cabecera;
    }

    public List<Detalle_venta> getBody() {
        return body;
    }

    public void setBody(List<Detalle_venta> body) {
        this.body = body;
    }
}
