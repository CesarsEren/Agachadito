package pe.com.grupopalomino.agachadito.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

import pe.com.grupopalomino.agachadito.LoginActivity;
import pe.com.grupopalomino.agachadito.Models.PuestosBean;
import pe.com.grupopalomino.agachadito.R;
import pe.com.grupopalomino.agachadito.UDetalleComercioActivity;
import pe.com.grupopalomino.agachadito.URegistrarClienteActivity;

public class PuestosAdapter extends RecyclerView.Adapter<PuestosAdapter.Puestos> {

    ArrayList<PuestosBean> puestosBeanArrayList;
    Context context;
    public PuestosAdapter(Context con) {
        puestosBeanArrayList = new ArrayList<>();
        context =con;
    }

    public void setPuestos(ArrayList<PuestosBean> puestos) {
        this.puestosBeanArrayList.clear();
        this.puestosBeanArrayList = puestos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Puestos onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View vi = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itempuesto, viewGroup, false);
        return new Puestos(vi);
    }

    @Override
    public void onBindViewHolder(@NonNull Puestos puestos, int i) {
    final PuestosBean puesto = this.puestosBeanArrayList.get(i);
    puestos.nombrepuesto.setText(puesto.getNombrepuesto());
        puestos.preciopromedio.setText(puesto.getPreciopromedio());
        puestos.categoriapuesto.setText(puesto.getCategoriapuesto());
        //puestos.imgpuesto.(puesto.getUrlimg());
        Glide.with(context).load(puesto.getUrlimg()).centerCrop().fitCenter().into(puestos.imgpuesto);

        puestos.btnverproductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentDetalle = new Intent(context, UDetalleComercioActivity.class);
                //Intent intentMap = new Intent(URegistrarClienteActivity.this,URegistroP2Activity.class);
                intentDetalle .putExtra("id_vendedor", puesto.getId_vendedor());
                intentDetalle .putExtra("fotopuesto", puesto.getUrlimg());
                intentDetalle .putExtra("nombrepuesto", puesto.getNombrepuesto());
                intentDetalle .putExtra("categoriapuesto", puesto.getCategoriapuesto());
                context.startActivity(intentDetalle );
            }
        });
    }

    @Override
    public int getItemCount() {
        return puestosBeanArrayList.size();
    }

    public class Puestos extends RecyclerView.ViewHolder {

        TextView nombrepuesto;
        TextView categoriapuesto;//REFERENCIA
        TextView preciopromedio;
        ImageView imgpuesto;
        TextView btnverproductos;

        public Puestos(@NonNull View itemView) {
            super(itemView);
            nombrepuesto = itemView.findViewById(R.id.nombrepuesto);
            categoriapuesto= itemView.findViewById(R.id.categoriapuesto);
            preciopromedio = itemView.findViewById(R.id.preciopromedio);
            btnverproductos = itemView.findViewById(R.id.btnverproductos);
            imgpuesto = itemView.findViewById(R.id.imgpuesto);
        }
    }
}
