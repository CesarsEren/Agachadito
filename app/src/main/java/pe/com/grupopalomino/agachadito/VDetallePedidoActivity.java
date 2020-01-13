package pe.com.grupopalomino.agachadito;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pe.com.grupopalomino.agachadito.Adapters.CarritoPagoAdapter;
import pe.com.grupopalomino.agachadito.Models.ProductoBean;
import pe.com.grupopalomino.agachadito.Models.VPedidoBean;
import pe.com.grupopalomino.agachadito.Models.Ventas;
import pe.com.grupopalomino.agachadito.Utils.data.Utils;

public class VDetallePedidoActivity extends AppCompatActivity {

    RecyclerView rvdetallepvendedor;
    CarritoPagoAdapter adapter;
    ArrayList<VPedidoBean> vPedidoBeans;
    ArrayList<ProductoBean> productoBeans;
    RequestQueue queue;
    TextView preciototalpagoven;
    Button btnaceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vdetalle_pedido);
        rvdetallepvendedor = findViewById(R.id.rvdetallepvendedor);
        preciototalpagoven = findViewById(R.id.preciototalpagoven);
        queue = Volley.newRequestQueue(this);
        vPedidoBeans = new ArrayList<>();
        productoBeans = new ArrayList<>();
        adapter = new CarritoPagoAdapter(this);
        RecibirDatos(savedInstanceState);

        rvdetallepvendedor.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        btnaceptar = findViewById(R.id.btnaceptarpedido);
        btnaceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AceptarPedido();
            }
        });
    }

    private void AceptarPedido() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(VDetallePedidoActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Aceptando pedido ...");
        pDialog.setCancelable(true);
        pDialog.show();
        Map<String, Object> params = new HashMap<>();
        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        final int id_cliente = preferences.getInt("id_cliente", 0);
        params.put("id_venta", id_venta);
        params.put("id_cliente", id_cliente);
        JSONObject parameters = new JSONObject(params);
        String url;
        url = Utils.URLBASE + "Ventas/aceptar";
        Log.i("urlventa", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msgserver = response.getString("msgserver");
                    //String mensaje = response.getString("mensaje");
                    if (msgserver.equals("SUCCESS")) {
                        pDialog.hide();
                        startActivity(new Intent(VDetallePedidoActivity.this, VMenuActivity.class));
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
        queue.add(request);
    }

    int id_venta;

    public void RecibirDatos(Bundle extras) {
        extras = getIntent().getExtras();
        extras = getIntent().getExtras();
        id_venta = extras.getInt("id_venta");
        String url;
        url = Utils.URLBASE + "Ventas/getdetalle/" + id_venta;
        Log.i("urldetalle",url);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String msgserver = response.getString("msgserver");
                            JSONObject header = response.getJSONObject("cabecera");
                            JSONArray body = response.getJSONArray("body");
                            double totalpago = 0;
                            for (int i = 0; i < body.length(); i++) {
                                JSONObject objitem = body.getJSONObject(i);
                                ProductoBean item = new ProductoBean();
                                item.setFoto(objitem.getString("fotoproducto"));
                                item.setCantidad(objitem.getInt("cantidad"));
                                item.setPrecio(objitem.getString("precioproducto"));
                                item.setNombre(objitem.getString("nombreproducto"));
                                totalpago += Double.parseDouble(item.getPrecio()) * item.getCantidad();
                                productoBeans.add(item);
                            }
                            adapter.setProductoBeans(productoBeans);
                            rvdetallepvendedor.setAdapter(adapter);

                            preciototalpagoven.setText("S / " + totalpago);
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

}
