package pe.com.grupopalomino.agachadito.Adapters;

import android.content.Context;
import android.content.Intent;
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

import pe.com.grupopalomino.agachadito.Models.VPedidoBean;
import pe.com.grupopalomino.agachadito.R;
import pe.com.grupopalomino.agachadito.VDetallePedidoActivity;

public class VPedidosAdapter extends RecyclerView.Adapter<VPedidosAdapter.XmlPedido> {

    Context con;
    ArrayList<VPedidoBean> arrayList ;

    public VPedidosAdapter(Context context) {
        this.con = context;
        arrayList = new ArrayList<>();
    }

    public void setArrayList(ArrayList<VPedidoBean> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public XmlPedido onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View vi = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itempedido, viewGroup, false);
        return new XmlPedido(vi);
    }

    @Override
    public void onBindViewHolder(@NonNull XmlPedido xmlPedido, int i) {
        final VPedidoBean item = arrayList.get(i);
        xmlPedido.horapedido.setText(item.getHorapedido());
        xmlPedido.nombrecliente.setText(item.getNombrecliente());
        xmlPedido.totalcliente.setText(item.getPreciototal());
        xmlPedido.btndetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(con, VDetallePedidoActivity.class);
                intent.putExtra("id_venta", item.getId_venta());
                con.startActivity(intent);
            }
        });
        Glide.with(con).load(item.getFotourl()).centerCrop().fitCenter().into(xmlPedido.fotocliente);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class XmlPedido extends RecyclerView.ViewHolder {
        ImageView fotocliente;
        TextView nombrecliente;
        TextView horapedido;
        TextView totalcliente;
        Button btndetalle;

        public XmlPedido(@NonNull View itemView) {
            super(itemView);
            fotocliente = itemView.findViewById(R.id.fotocliente);
            nombrecliente = itemView.findViewById(R.id.nombrecliente);
            horapedido = itemView.findViewById(R.id.horapedido);
            totalcliente = itemView.findViewById(R.id.totalcliente);
            btndetalle = itemView.findViewById(R.id.btndetalle);
        }
    }
}
