package pe.com.grupopalomino.agachadito.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import pe.com.grupopalomino.agachadito.Models.ProductoBean;
import pe.com.grupopalomino.agachadito.R;
import pe.com.grupopalomino.agachadito.UDetalleComercioActivity;
import pe.com.grupopalomino.agachadito.UMenuActivity;
import pe.com.grupopalomino.agachadito.Utils.data.Utils;
import pe.com.grupopalomino.agachadito.VActualizarProductosActivity;

public class ProductosVendedorAdapter extends RecyclerView.Adapter<ProductosVendedorAdapter.XMLProductoVendedor> {


    ArrayList<ProductoBean> productoBeans;//= new ArrayList<>();
    Context con;

    public ProductosVendedorAdapter(Context con) {
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
    public XMLProductoVendedor onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View vi = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemproducto, viewGroup, false);
        return new XMLProductoVendedor(vi);
    }

    @Override
    public void onBindViewHolder(@NonNull final XMLProductoVendedor xmlProductoVendedor, int i) {


        final ProductoBean productoBean = productoBeans.get(i);
        xmlProductoVendedor.precio.setText("Precio : $" + productoBean.getPrecio().trim());
        xmlProductoVendedor.nombre.setText(productoBean.getNombre().trim());
        xmlProductoVendedor.btnactualizar.setText("ACTUALIZAR");
        xmlProductoVendedor.btnactualizar.setBackgroundColor(con.getResources().getColor(R.color.colorAccent));

        xmlProductoVendedor.btnactualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(con, VActualizarProductosActivity.class);
                String[] data = new String[4];
                data[0]=productoBean.getId_producto() + "";
                data[1]=productoBean.getNombre().trim();
                data[2]=productoBean.getPrecio().trim();
                data[3]=productoBean.getFoto();

                Set<String> categorias = new HashSet<>();
                for (ProductoBean producto : productoBeans) {
                    categorias.add(producto.getCategoria());
                }
                String[] categoriasarray = new String[categorias.size()];
                int x = 0;
                for (String item : categorias) {
                    categoriasarray[x] = item;
                    x++;
                }
                intent.putExtra("producto", data);
                intent.putExtra("categorias", categoriasarray);
                con.getApplicationContext().startActivity(intent);
            }
        });

        Glide.with(con).load(productoBean.getFoto()).centerCrop().fitCenter().into(xmlProductoVendedor.foto);
    }

    @Override
    public int getItemCount() {
        return productoBeans.size();
    }

    public class XMLProductoVendedor extends RecyclerView.ViewHolder {

        TextView precio;
        TextView nombre;
        ImageView foto;
        TextView btnactualizar;

        public XMLProductoVendedor(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreproducto);
            precio = itemView.findViewById(R.id.precioproducto);
            foto = itemView.findViewById(R.id.imageproducto);
            btnactualizar = itemView.findViewById(R.id.btnAgregarCarrito);
        }
    }
}
