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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pe.com.grupopalomino.agachadito.Adapters.CarritoAdapter;
import pe.com.grupopalomino.agachadito.Models.Detalle_venta;
import pe.com.grupopalomino.agachadito.Models.InsertVenta;
import pe.com.grupopalomino.agachadito.Models.ProductoBean;
import pe.com.grupopalomino.agachadito.Models.Ventas;
import pe.com.grupopalomino.agachadito.Utils.data.Utils;

public class UCarritoActivity extends AppCompatActivity {

    public static CarritoAdapter adapter;
    public static TextView preciototal;
    public static RecyclerView rcart;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ucarrito);
        adapter = new CarritoAdapter(this);
        rcart = findViewById(R.id.rvcarrito);
        preciototal = findViewById(R.id.preciototal);
        preciototal.setText("$ " + Utils.getPrecioTotalCarrito());
        pDialog = new SweetAlertDialog(UCarritoActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        queue = Volley.newRequestQueue(this);
        TextView textback = findViewById(R.id.textback);
        Button btnconsultar = findViewById(R.id.btnconsultar);
        btnconsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrarVenta();
            }
        });


        if (Utils.getCantidadCarrito() > 0) {
            textback.setVisibility(View.GONE);
            rcart.setVisibility(View.VISIBLE);
        } else {
            textback.setVisibility(View.VISIBLE);
        }
        adapter.setProductoBeans(Utils.carrito);
        rcart.setAdapter(adapter);
        rcart.setLayoutManager(new LinearLayoutManager(this));
    }

    SweetAlertDialog pDialog;

    private void RegistrarVenta() {
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Realizando Pedido ...");
        pDialog.show();

        Map<String, Object> params = new HashMap<>();
        SharedPreferences preferences = getApplication().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        int id_cliente = preferences.getInt("id_cliente", 0);

        Map<String, Object> cabecera = new HashMap<>();
        cabecera.put("id_cliente", preferences.getInt("id_cliente", 0));

        cabecera.put("dni", preferences.getString("dni", "DNI"));
        cabecera.put("nombreCompleto", preferences.getString("nombres", "nombres") + " " + preferences.getString("apellidos", "apellidos"));
        List<Map<String, Object>> body = new ArrayList<>();
        for (ProductoBean item : Utils.carrito) {
            cabecera.put("id_vendedor", item.getId_vendedor());
            Map<String, Object> itemcar = new HashMap<>();
            itemcar.put("cantidad", item.getCantidad());
            itemcar.put("fotoproducto", item.getFoto());
            itemcar.put("precioproducto", item.getPrecio());
            itemcar.put("nombreproducto", item.getNombre());
            itemcar.put("id_producto", item.getId_producto());
            body.add(itemcar);
        }
        params.put("cabecera", cabecera);
        params.put("body", body);
        JSONObject parameters = new JSONObject(params);
        //String url = null;
        String url;
        url = Utils.URLBASE + "Ventas/insertar";
        Log.i("urlventa", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msgserver = response.getString("msgserver");
                    String mensaje = response.getString("mensaje");
                    if (msgserver.equals("SUCCESS")) {
                        pDialog.hide();
                        new SweetAlertDialog(UCarritoActivity.this)
                                .setTitleText(mensaje)
                                .setConfirmText("Continuar")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Utils.carrito.clear();
                                        startActivity(new Intent(UCarritoActivity.this, UMenuActivity.class));
                                    }
                                }).show();
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
}
