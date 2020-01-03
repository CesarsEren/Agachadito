package pe.com.grupopalomino.agachadito.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;

import java.util.ArrayList;
import java.util.List;

import pe.com.grupopalomino.agachadito.BuildConfig;
import pe.com.grupopalomino.agachadito.Models.ProductoBean;
import pe.com.grupopalomino.agachadito.R;
import pe.com.grupopalomino.agachadito.UCarritoActivity;
import pe.com.grupopalomino.agachadito.UDetalleComercioActivity;
import pe.com.grupopalomino.agachadito.UMenuActivity;
import pe.com.grupopalomino.agachadito.Utils.data.Utils;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.XMLProducto> {

    ArrayList<ProductoBean> productoBeans;//= new ArrayList<>();
    Context con;

    public CarritoAdapter(Context con) {
        this.productoBeans = new ArrayList<>();
        this.con = con;
    }

    public void setProductoBeans(ArrayList<ProductoBean> productoBeans) {
        this.productoBeans.clear();
        this.productoBeans = productoBeans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CarritoAdapter.XMLProducto onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View vi = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemcarrito, viewGroup, false);
        return new CarritoAdapter.XMLProducto(vi);
    }

    @Override
    public void onBindViewHolder(@NonNull final XMLProducto xmlProducto, int i) {
        final ProductoBean productoBean = productoBeans.get(i);
        xmlProducto.precio.setText("Precio : $ " + productoBean.getPrecio().trim());
        xmlProducto.nombre.setText(productoBean.getNombre().trim());
        xmlProducto.subtotal.setText("$ " + productoBean.getSubtotal());
        xmlProducto.cantidad.setText("" + productoBean.getCantidad());
        Glide.with(con).load(productoBean.getFoto()).centerCrop().fitCenter().into(xmlProducto.foto);
        xmlProducto.btnmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ProductoBean> carttemp = Utils.carrito;
                for (int i = 0; i < carttemp.size(); i++) {
                    if (carttemp.get(i).getId_producto() == productoBean.getId_producto()) {
                        Utils.carrito.get(i).setCantidad(carttemp.get(i).getCantidad() + 1);
                        xmlProducto.cantidad.setText("" + productoBean.getCantidad());
                        xmlProducto.subtotal.setText("$ " + productoBean.getSubtotal());
                        UCarritoActivity.preciototal.setText("$ " + Utils.getPrecioTotalCarrito());
                    }
                }
                int cantidad = Utils.getCantidadCarrito();
                if (cantidad > 0) {
                    UMenuActivity.textcantidad.setText("" + cantidad);
                } else {
                    UMenuActivity.textcantidad.setVisibility(View.INVISIBLE);
                }
            }
        });

        xmlProducto.btnless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ProductoBean> carttemp = new ArrayList<>();
                carttemp.addAll(Utils.carrito);
                boolean eliminado = false;
                for (int i = 0; i < carttemp.size(); i++) {
                    if (carttemp.get(i).getId_producto() == productoBean.getId_producto()) {
                        Utils.carrito.get(i).setCantidad(productoBean.getCantidad() - 1);
                        xmlProducto.cantidad.setText("" + productoBean.getCantidad());
                        xmlProducto.subtotal.setText("$ " + productoBean.getSubtotal());
                        UCarritoActivity.preciototal.setText("$ " + Utils.getPrecioTotalCarrito());
                        if (Utils.carrito.get(i).getCantidad() == 0) {
                            carttemp.remove(i);
                            UCarritoActivity.adapter.setProductoBeans(carttemp);
                            break;
                        }
                    }
                }
                Utils.carrito.clear();
                Utils.carrito.addAll(carttemp);
                int cantidad = Utils.getCantidadCarrito();
                if (cantidad > 0) {
                    UMenuActivity.textcantidad.setText("" + cantidad);
                } else {
                    UMenuActivity.textcantidad.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productoBeans.size();
    }

    public class XMLProducto extends RecyclerView.ViewHolder {

        TextView precio;
        TextView subtotal;
        TextView nombre;
        TextView cantidad;
        ImageView foto;

        Button btnmore;
        Button btnless;

        public XMLProducto(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombrecart);
            precio = itemView.findViewById(R.id.preciocart);
            foto = itemView.findViewById(R.id.imgcart);
            cantidad = itemView.findViewById(R.id.cantidadcart);
            btnmore = itemView.findViewById(R.id.btnmore);
            btnless = itemView.findViewById(R.id.btnless);
            subtotal = itemView.findViewById(R.id.subtotalcart);
        }
    }
}
