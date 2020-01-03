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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import pe.com.grupopalomino.agachadito.Models.ProductoBean;
import pe.com.grupopalomino.agachadito.R;
import pe.com.grupopalomino.agachadito.UDetalleComercioActivity;
import pe.com.grupopalomino.agachadito.UMenuActivity;
import pe.com.grupopalomino.agachadito.Utils.data.Utils;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.XMLProducto> {

    ArrayList<ProductoBean> productoBeans;//= new ArrayList<>();
    Context con;

    public ProductoAdapter(Context con) {
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
    public ProductoAdapter.XMLProducto onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View vi = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemproducto, viewGroup, false);
        return new XMLProducto(vi);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoAdapter.XMLProducto xmlProducto, int i) {
        final ProductoBean productoBean = productoBeans.get(i);
        xmlProducto.precio.setText("Precio : $" + productoBean.getPrecio().trim());
        xmlProducto.nombre.setText(productoBean.getNombre().trim());
        Glide.with(con).load(productoBean.getFoto()).centerCrop().fitCenter().into(xmlProducto.foto);
        xmlProducto.btnagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  ArrayList<ProductoBean> carttemp = Utils.carrito;
                  boolean noencontrado = true;
                if (Utils.carrito.size() > 0) {
                    for (int i = 0; i < carttemp.size(); i++) {
                        if (carttemp.get(i).getId_producto() == productoBean.getId_producto()) {
                            Utils.carrito.get(i).setCantidad(Utils.carrito.get(i).getCantidad() + 1);
                            UDetalleComercioActivity.cartmontotal.setText("$ " + Utils.getPrecioTotalCarrito());
                            UDetalleComercioActivity.cartcantidad.setText("" + Utils.getCantidadCarrito());
                            noencontrado = false;
                            break;
                        }
                    }
                    if(noencontrado)
                    {
                        productoBean.setCantidad(1);
                        Utils.carrito.add(productoBean);
                        UDetalleComercioActivity.cartmontotal.setText("$ " + Utils.getPrecioTotalCarrito());
                        UDetalleComercioActivity.cartcantidad.setText("" + Utils.getCantidadCarrito());
                    }
                }else
                {
                    productoBean.setCantidad(1);
                    Utils.carrito.add(productoBean);
                    UDetalleComercioActivity.cartmontotal.setText("$ "+productoBean.getPrecio());
                    UDetalleComercioActivity.cartcantidad.setText(""+Utils.getCantidadCarrito());
                }
                int cantidad = Utils.getCantidadCarrito();
                if (cantidad > 0) {
                    //textcantidad.setVisibility(View.VISIBLE);
                    UMenuActivity.textcantidad.setText(""+cantidad);
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
        TextView nombre;
        ImageView foto;
        TextView btnagregar;

        public XMLProducto(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreproducto);
            precio = itemView.findViewById(R.id.precioproducto);
            foto = itemView.findViewById(R.id.imageproducto);
            btnagregar = itemView.findViewById(R.id.btnAgregarCarrito);

        }

    }
}
