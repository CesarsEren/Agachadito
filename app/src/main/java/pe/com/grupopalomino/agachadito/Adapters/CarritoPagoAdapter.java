package pe.com.grupopalomino.agachadito.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import pe.com.grupopalomino.agachadito.Models.ProductoBean;
import pe.com.grupopalomino.agachadito.R;
import pe.com.grupopalomino.agachadito.UCarritoActivity;
import pe.com.grupopalomino.agachadito.UMenuActivity;
import pe.com.grupopalomino.agachadito.Utils.data.Utils;

public class CarritoPagoAdapter extends RecyclerView.Adapter<CarritoPagoAdapter.XMLProductopago> {

    ArrayList<ProductoBean> productoBeans;//= new ArrayList<>();
    Context con;


    public CarritoPagoAdapter(Context con) {
        productoBeans = new ArrayList<>();
        this.con = con;
    }

    public void setProductoBeans(ArrayList<ProductoBean> productoBeans) {
        this.productoBeans = productoBeans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public XMLProductopago onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View vi = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemcarritopago, viewGroup, false);
        return new XMLProductopago(vi);
    }

    @Override
    public void onBindViewHolder(@NonNull XMLProductopago xmlProductopago, int i) {
        final ProductoBean productoBean = productoBeans.get(i);
        xmlProductopago.precio.setText("Precio : S / " + productoBean.getPrecio().trim());
        xmlProductopago.nombre.setText(productoBean.getNombre().trim());
        xmlProductopago.subtotal.setText("Subtotal : S / " + productoBean.getSubtotal());
        xmlProductopago.cantidad.setText("Cantidad : " + productoBean.getCantidad());
        Glide.with(con).load(productoBean.getFoto()).centerCrop().fitCenter().into(xmlProductopago.foto);
    }

    @Override
    public int getItemCount() {
        return productoBeans.size();
    }

    public class XMLProductopago extends RecyclerView.ViewHolder {
        TextView precio;
        TextView subtotal;
        TextView nombre;
        TextView cantidad;
        ImageView foto;


        public XMLProductopago(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombrecartp);
            precio = itemView.findViewById(R.id.preciocartp);
            foto = itemView.findViewById(R.id.imgcartp);
            cantidad = itemView.findViewById(R.id.cantidadcartp);
            subtotal = itemView.findViewById(R.id.subtotalcartp);

        }
    }

}
