package pe.com.grupopalomino.agachadito;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pe.com.grupopalomino.agachadito.Adapters.TarjetaAdapter;
import pe.com.grupopalomino.agachadito.Models.TarjetaBean;
import pe.com.grupopalomino.agachadito.Utils.data.Utils;

public class UTarjetasFragment extends Fragment {


    RecyclerView rvtarjetas;
    TarjetaAdapter adapter;
    ArrayList<TarjetaBean> tarjetas = new ArrayList<>();
    RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_utarjetas, container, false);
        FloatingActionButton addTarjeta = view.findViewById(R.id.fabtarjeta);
        addTarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), URegistroTarjetaActivity.class));
            }
        });
        adapter = new TarjetaAdapter(view.getContext());
        queue = Volley.newRequestQueue(view.getContext());
        rvtarjetas = view.findViewById(R.id.rvtarjetas);
        rvtarjetas.setLayoutManager(new LinearLayoutManager(getContext()));
        getData(view.getContext());
        rvtarjetas.setAdapter(adapter);
        //rvtarjetas.setHasFixedSize(true);

        return view;
    }

    private void getData(Context cn) {
        tarjetas.clear();
        SharedPreferences preferences = cn.getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String id_cliente = preferences.getString("id_cliente", "id_cliente");
        String url = Utils.URLBASE;
        //url = "http://172.16.11.85:8090/JM/Tarjetas/";
        url = url + "Tarjetas/lista/" + id_cliente;
        Log.i("url",url);

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("Tarjetas");
                            for (int i = 0; i < array.length(); i++) {
                                TarjetaBean bean = new TarjetaBean();
                                JSONObject obj = array.getJSONObject(i);
                                String dueño = obj.getString("nombre").toString()+" "+obj.getString("apellido").toString();
                                Log.i("dueño",dueño);
                                Log.i("nroTarjeta",obj.getString("nroTarjeta"));

                                bean.setTvdueño(dueño);
                                bean.setTvtarjeta(obj.getString("nroTarjeta"));
                                bean.setId_mediopago(obj.getInt("id_mediopago"));
                                tarjetas.add(bean);
                            }
                            adapter.SetData(tarjetas);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //hideProgressDialog();
                    }
                });
        queue.add(request);
    }
}
