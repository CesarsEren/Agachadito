package pe.com.grupopalomino.agachadito.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pe.com.grupopalomino.agachadito.Models.TarjetaBean;
import pe.com.grupopalomino.agachadito.R;
import pe.com.grupopalomino.agachadito.UMenuActivity;
import pe.com.grupopalomino.agachadito.URegistroP2Activity;
import pe.com.grupopalomino.agachadito.UTarjetasFragment;
import pe.com.grupopalomino.agachadito.Utils.data.Utils;

public class TarjetaAdapter extends RecyclerView.Adapter<TarjetaAdapter.Tarjeta> {

    Context con;
    ArrayList<TarjetaBean> tarjetaBeanList;
    RequestQueue mQueue;

    public TarjetaAdapter(Context con) {
        this.tarjetaBeanList = new ArrayList<>();
        this.con = con;
        mQueue = Volley.newRequestQueue(con);
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
                DeleteTarjeta(bean.getId_mediopago());
                //Toast.makeText(con, "Código a Eliminar " + bean.getId_mediopago(), Toast.LENGTH_LONG).show();
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


    public void DeleteTarjeta(final int id_mediopago) {

        //apodo = etapodo.getText().toString();
        SharedPreferences preferences = con.getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        int id_persona = preferences.getInt("id_cliente", 0);
        Map<String, Object> params = new HashMap<>();
        params.put("id_cliente", id_persona);
        params.put("id_mediopago", id_mediopago);

        JSONObject parameters = new JSONObject(params);
        /*String url = null;
          url = "http://172.16.11.85:8090/JM/Rest/";*/
        String url = Utils.URLBASE+"Tarjetas/DeleteTarjeta";
        //url = url + "Tarjetas/DeleteTarjeta";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msgserver = response.getString("msgserver");
                    //Toast.makeText(getApplicationContext(),"MENSAJE SERVR "+msgserver,Toast.LENGTH_LONG).show();
                    if (msgserver.equals("Aceptado")) {
                        for(int i = 0;i<tarjetaBeanList.size();i++)
                        {//TarjetaBean item;
                            if(tarjetaBeanList.get(i).getId_mediopago()==id_mediopago)
                            {
                                tarjetaBeanList.remove(i);
                                UTarjetasFragment.adapter.SetData(tarjetaBeanList);
                                break;
                            }
                        }
                        Toast.makeText(con, "Eliminado Correctamente" , Toast.LENGTH_LONG).show();
                    }else
                    {
                        Toast.makeText(con, "Error" , Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
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
