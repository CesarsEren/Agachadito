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

import org.w3c.dom.Text;

import java.util.ArrayList;

import pe.com.grupopalomino.agachadito.Models.ProductoBean;
import pe.com.grupopalomino.agachadito.R;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.XMLProducto> {

    ArrayList<ProductoBean> productoBeans;//= new ArrayList<>();
    Context con;

    public ProductoAdapter( Context con) {
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
        xmlProducto.precio.setText("Precio : $"+productoBean.getPrecio().trim());
        xmlProducto.nombre.setText(productoBean.getNombre().trim());
        Glide.with(con).load(productoBean.getFoto()).centerCrop().fitCenter().into(xmlProducto.foto);
        xmlProducto.btnagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(con,"idCliente:"+productoBean.getNombre()+" vendedor "+productoBean.getId_vendedor(),Toast.LENGTH_LONG).show();
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
