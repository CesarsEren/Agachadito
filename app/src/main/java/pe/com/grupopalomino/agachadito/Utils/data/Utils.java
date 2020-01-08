package pe.com.grupopalomino.agachadito.Utils.data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import pe.com.grupopalomino.agachadito.Models.ProductoBean;
import pe.com.grupopalomino.agachadito.Models.PuestosBean;

public class Utils {
    //public static String URLBASE = "http://172.16.11.85:8090/JM/";
    public static String URLBASE ="http://service.millenniumprogramers.com:8087/neocomer/";
    public static ArrayList<ProductoBean> carrito = new ArrayList<>();

    public  static ArrayList<PuestosBean>ubicaciones  = new ArrayList<>();
    //public static int click = 0;
    public static int zindextemp=0;
    public static int getCantidadCarrito() {
        int cantidad = 0;
        for (int i = 0; i < carrito.size(); i++) {
            cantidad += carrito.get(i).getCantidad();
        }
        return cantidad;
    }

    public static double getPrecioTotalCarrito() {
        double preciototal= 0;
        for (int i = 0; i < carrito.size(); i++) {
            preciototal +=  carrito.get(i).getSubtotal();
        }
        return preciototal;
    }
}

