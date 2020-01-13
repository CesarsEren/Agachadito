package pe.com.grupopalomino.agachadito;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;

import pe.com.grupopalomino.agachadito.Models.TarjetaBean;
import pe.com.grupopalomino.agachadito.Utils.data.Utils;


public class VDashboardFragment extends Fragment {

    RequestQueue queue;
TextView  gananciames,gananciadia,gananciahoy,clientes,revisionsalubridad,revisionseguridad;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vdashboard, container, false);
        queue = Volley.newRequestQueue(view.getContext());
        revisionsalubridad = view.findViewById(R.id.revisionsalubridad);
        revisionseguridad = view.findViewById(R.id.revisionseguridad);
        clientes = view.findViewById(R.id.clientes);
        gananciahoy = view.findViewById(R.id.gananciahoy);
        gananciadia = view.findViewById(R.id.gananciadia);
        gananciames = view.findViewById(R.id.gananciames);

        SharedPreferences preferences = view.getContext().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        int id_cliente = preferences.getInt("id_cliente", 0);
        String url;
        //url = "http://172.16.11.85:8090/JM/Tarjetas/";
        url = Utils.URLBASE + "Ventas/getreporte/" + id_cliente;
        Log.i("url", url);

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject reporte = response.getJSONObject("reporte");
                            DecimalFormat formateador = new DecimalFormat("####.##");
                            clientes.setText(""+reporte.getInt("clientes"));
                            gananciahoy.setText("S/ "+formateador.format(reporte.getDouble("gananciaHoy")));
                            gananciames.setText("S/ "+formateador.format(reporte.getDouble("gananciaTotal")/12));
                            gananciadia.setText("S/ "+formateador.format(reporte.getDouble("gananciaTotal")/30));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        queue.add(request);
        return view;
    }

}
