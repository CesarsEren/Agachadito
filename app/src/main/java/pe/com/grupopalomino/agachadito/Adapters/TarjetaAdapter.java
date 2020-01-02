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

import java.util.ArrayList;
import java.util.List;

import pe.com.grupopalomino.agachadito.Models.TarjetaBean;
import pe.com.grupopalomino.agachadito.R;

public class TarjetaAdapter extends RecyclerView.Adapter<TarjetaAdapter.Tarjeta> {

    Context con;
    List<TarjetaBean> tarjetaBeanList;

    public TarjetaAdapter(Context con) {
        this.tarjetaBeanList = new ArrayList<>();
        this.con = con;
    }

    public void SetData(ArrayList<TarjetaBean> tarjetaBeanList) {
        this.tarjetaBeanList.clear();
        this.tarjetaBeanList.addAll(tarjetaBeanList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Tarjeta onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View vi = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemtarjeta, viewGroup, false);
        return new Tarjeta(vi);
    }

    @Override
    public void onBindViewHolder(@NonNull Tarjeta tarjeta, int i) {
        final TarjetaBean bean = tarjetaBeanList.get(i);
        tarjeta.tvduenio.setText(bean.getTvdueño());
        tarjeta.btndelete.setImageResource(R.drawable.icontrash);
        tarjeta.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(con, "Código a Eliminar " + bean.getId_mediopago(), Toast.LENGTH_LONG).show();
            }
        });
        tarjeta.tvduenio.setText(bean.getTvdueño());
        tarjeta.tvtarjeta.setText(bean.getTvtarjeta());

        if (bean.getTvtarjeta().substring(0, 1).equals("4")) {
            tarjeta.imgtarjeta.setImageResource(R.drawable.menu_visa);
        } else {
            tarjeta.imgtarjeta.setImageResource(R.drawable.iconmastercard);
        }

    }

    @Override
    public int getItemCount() {
        return tarjetaBeanList.size();
    }

    public class Tarjeta extends RecyclerView.ViewHolder {
        ImageView imgtarjeta;
        TextView tvduenio;
        TextView tvtarjeta;
        ImageView btndelete;

        public Tarjeta(@NonNull View itemView) {
            super(itemView);
            imgtarjeta = itemView.findViewById(R.id.imgtarjeta);
            tvduenio = itemView.findViewById(R.id.tvduenio);
            tvtarjeta = itemView.findViewById(R.id.tvtarjeta);
            btndelete = itemView.findViewById(R.id.btndelete);
        }
    }

}
