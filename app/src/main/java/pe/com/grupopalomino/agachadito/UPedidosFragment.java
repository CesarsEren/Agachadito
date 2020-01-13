package pe.com.grupopalomino.agachadito;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

import pe.com.grupopalomino.agachadito.Adapters.NotificacionAdapter;
import pe.com.grupopalomino.agachadito.Models.NotificacionBean;
import pe.com.grupopalomino.agachadito.Models.TarjetaBean;
import pe.com.grupopalomino.agachadito.Utils.data.Utils;

public class UPedidosFragment extends Fragment {

    RecyclerView rvpedidos;
    RequestQueue queue;
    ArrayList<NotificacionBean> notificaciones;
    LinearLayout lossconectionnot;
    public static NotificacionAdapter notificacionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upedidos, container, false);
        queue = Volley.newRequestQueue(view.getContext());
        rvpedidos = view.findViewById(R.id.rvpedidos);
        lossconectionnot = view.findViewById(R.id.lossconectionnot);
        notificaciones = new ArrayList<>();
        notificacionAdapter = new NotificacionAdapter(view.getContext());
        rvpedidos.setAdapter(notificacionAdapter);
        rvpedidos.setLayoutManager(new LinearLayoutManager(getContext()));
        cargardata(view.getContext());
        return view;
    }

    private void cargardata(Context cn) {
        lossconectionnot.setVisibility(View.GONE);

        SharedPreferences preferences = cn.getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        int id_cliente = preferences.getInt("id_cliente", 0);
        String url;
        //url = "http://172.16.11.85:8090/JM/Tarjetas/";
        url = Utils.URLBASE + "Ventas/getHistoriaPedidos/" + id_cliente;
        Log.i("url", url);

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("ventas");
                            for (int i = 0; i < array.length(); i++) {
                                NotificacionBean notify = new NotificacionBean();
                                JSONObject obj = array.getJSONObject(i);
                                notify.setId_venta(obj.getInt("id_venta"));
                                notify.setNombrenegocio(obj.getString("nombrecomercio"));
                                notify.setFechapedido(obj.getString("fechaEmision"));
                                notify.setPreciototal(obj.getDouble("total"));
                                notify.setUrlfotonoti(obj.getString("foto"));
                                notify.setEstadoApp(obj.getString("estadoApp"));
                                notificaciones.add(notify);
                            }
                            notificacionAdapter.setNotificacionBeans(notificaciones);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        lossconectionnot.setVisibility(View.VISIBLE);
                    }
                });
        queue.add(request);
    }

}
