package pe.com.grupopalomino.agachadito;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import pe.com.grupopalomino.agachadito.Adapters.VPedidosAdapter;
import pe.com.grupopalomino.agachadito.Models.ProductoBean;
import pe.com.grupopalomino.agachadito.Models.VPedidoBean;
import pe.com.grupopalomino.agachadito.Utils.data.Utils;

public class VPedidosFragment extends Fragment {

    RecyclerView pedidosvendedor;
    RequestQueue queue;
    VPedidosAdapter vPedidosAdapter;
    ArrayList<VPedidoBean> VPedidoBeans;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vpedidos, container, false);
        context = view.getContext();
        queue = Volley.newRequestQueue(view.getContext());
        pedidosvendedor = view.findViewById(R.id.pedidosvendedor);
        vPedidosAdapter = new VPedidosAdapter(view.getContext());
        VPedidoBeans = new ArrayList<>();
        CargarPedidos();
        return view;
    }

    private void CargarPedidos() {
        SharedPreferences preferences = context.getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        int id_cliente = preferences.getInt("id_cliente", 0);
        String url;
        url = Utils.URLBASE + "Ventas/getPedidosporVendedor/" + id_cliente;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String msgserver = response.getString("msgserver");
                            JSONArray pedidos = response.getJSONArray("pedidos");
                            for (int i = 0; i < pedidos.length(); i++) {
                                JSONObject itemobj = pedidos.getJSONObject(i);
                                VPedidoBean item = new VPedidoBean();
                                item.setFotourl(itemobj.getString("foto"));
                                item.setId_venta(itemobj.getInt("id_venta"));
                                item.setHorapedido(itemobj.getString("fechaEmision"));
                                item.setNombrecliente(itemobj.getString("nombreCompleto"));
                                item.setPreciototal("" + itemobj.getDouble("total"));
                                VPedidoBeans.add(item);
                            }
                            vPedidosAdapter.setArrayList(VPedidoBeans);
                            pedidosvendedor.setAdapter(vPedidosAdapter);
                            pedidosvendedor.setLayoutManager(new GridLayoutManager(context, 4));
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
    }

    /*
    * {
"msgserver": "SUCCESS",
"pedidos": [
  {
"id_venta": 5,
"foto": "https://firebasestorage.googleapis.com/v0/b/agachadito.appspot.com/o/perfil%2F613207948?alt=media&token=b6c83fab-fca3-45f6-8490-0d42008b9aee",
"total": 4,
"dni": "71807093",
"fechaEmision": "2020-01-09 20:50:14.937",
"nombreCompleto": "Almendra Castro Sanches"
}
],
    * */
}
