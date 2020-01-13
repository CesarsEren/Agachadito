package pe.com.grupopalomino.agachadito;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pe.com.grupopalomino.agachadito.Adapters.CarritoPagoAdapter;
import pe.com.grupopalomino.agachadito.Adapters.SpinerAdapter;
import pe.com.grupopalomino.agachadito.Models.Detalle_venta;
import pe.com.grupopalomino.agachadito.Models.NotificacionBean;
import pe.com.grupopalomino.agachadito.Models.ProductoBean;
import pe.com.grupopalomino.agachadito.Models.SpinerMedioPago;
import pe.com.grupopalomino.agachadito.Models.TarjetaBean;
import pe.com.grupopalomino.agachadito.Models.Ventas;
import pe.com.grupopalomino.agachadito.Utils.data.Utils;

public class UFormPagoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    RequestQueue queue;
    ArrayList<ProductoBean> productoBeans;
    CarritoPagoAdapter adapter;
    RecyclerView rvdetalle;
    Spinner spmediopago;
    ArrayList<SpinerMedioPago> customList;

    Button btnpagartodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uform_pago);
        queue = Volley.newRequestQueue(this);
        adapter = new CarritoPagoAdapter(this);
        rvdetalle = findViewById(R.id.rvdetalle);
        fechaemision = findViewById(R.id.fechaemision);
        spmediopago = findViewById(R.id.spmediopago);
        dnipago = findViewById(R.id.dnipago);
        nombrepago = findViewById(R.id.nombrepago);
        spmediopago = findViewById(R.id.spmediopago);
        preciototalpago = findViewById(R.id.preciototalpago);
        btnpagartodo = findViewById(R.id.btnpagartodo);
        productoBeans = new ArrayList<>();
        comercio = findViewById(R.id.comercio);
        RecibirDatos(savedInstanceState);
        //venta
        rvdetalle.setLayoutManager(new LinearLayoutManager(this));


        customList = new ArrayList<>();
        CargarSpiner();

        btnpagartodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(UFormPagoActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("¿Desea Realizar el Pago?")
                        .setContentText("Se le Cobrará " + preciototalpago.getText())
                        .setConfirmText("Continuar")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Pagar(id_venta, sDialog);
                            }
                        }).show();
            }
        });

    }

    TextView comercio, fechaemision, dnipago, nombrepago;
    TextView preciototalpago;

    Ventas venta;

    private void Pagar(final int id_venta, final SweetAlertDialog sDialog) {
        Map<String, Object> params = new HashMap<>();
        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        final int id_cliente = preferences.getInt("id_cliente", 0);
        params.put("id_venta", id_venta);
        params.put("id_cliente", id_cliente);
        JSONObject parameters = new JSONObject(params);
        String url;
        url = Utils.URLBASE + "Ventas/pagar";
        Log.i("urlventa", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msgserver = response.getString("msgserver");
                    //String mensaje = response.getString("mensaje");
                    if (msgserver.equals("SUCCESS")) {
                        sDialog.setTitleText("Su compra fue Realizada ")
                                .setContentText("No se Olvide de leer el CodigoQr para \nRECOGER")
                                .setConfirmText("Ahora")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                             @Override
                                                             public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                 startActivity(new Intent(UFormPagoActivity.this, UURecogerconQrActivity.class));
                                                             }
                                                         }
                                )
                                .setCancelButton("Después ", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        startActivity(new Intent(UFormPagoActivity.this, UMenuActivity.class));
                                    }
                                })
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        //context.startActivity(new Intent(context, VMenuActivity.class));
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        SpinerMedioPago items = (SpinerMedioPago) adapterView.getSelectedItem();
        Toast.makeText(this, "" + items.getId_mediopago(), Toast.LENGTH_SHORT).show();
        id_mediopago = items.getId_mediopago();
    }

    SpinerAdapter customAdapter;

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    ArrayList<TarjetaBean> tarjetas = new ArrayList<>();
    int id_mediopago;

    public void CargarSpiner() {
        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        int id_cliente = preferences.getInt("id_cliente", 0);
        String url;
        //url = "http://172.16.11.85:8090/JM/Tarjetas/";
        url = Utils.URLBASE + "Tarjetas/lista/" + id_cliente;
        Log.i("url", url);

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("Tarjetas");
                            for (int i = 0; i < array.length(); i++) {
                                TarjetaBean bean = new TarjetaBean();
                                JSONObject obj = array.getJSONObject(i);
                                String dueño = obj.getString("nombre").toString() + " " + obj.getString("apellido").toString();

                                SpinerMedioPago spinerMedioPago = new SpinerMedioPago();
                                spinerMedioPago.setDueño(dueño);
                                spinerMedioPago.setId_mediopago(obj.getInt("id_mediopago"));
                                spinerMedioPago.setSpinerText(obj.getString("nroTarjeta"));
                                customList.add(spinerMedioPago);
                            }
                            customAdapter = new SpinerAdapter(UFormPagoActivity.this, customList);

                            if (spmediopago != null) {
                                spmediopago.setAdapter(customAdapter);
                                spmediopago.setOnItemSelectedListener(UFormPagoActivity.this);
                            }
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

    int id_venta;

    public void RecibirDatos(Bundle extras) {
        venta = new Ventas();
        extras = getIntent().getExtras();
        id_venta = extras.getInt("id_venta");
        String comercioname = extras.getString("comercio");
        comercio.setText(comercioname);
        String url;
        url = Utils.URLBASE + "Ventas/getdetalle/" + id_venta;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String msgserver = response.getString("msgserver");
                            JSONObject header = response.getJSONObject("cabecera");
                            JSONArray body = response.getJSONArray("body");
                            venta.setDNI(header.getString("dni"));
                            venta.setNombreCompleto(header.getString("nombreCompleto"));
                            venta.setFechaEmision(header.getString("fechaEmision"));
                            venta.setId_venta(header.getInt("id_venta"));
                            venta.setId_vendedor(header.getInt("id_vendedor"));
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
                            rvdetalle.setAdapter(adapter);

                            preciototalpago.setText("S / " + totalpago);
                            fechaemision.setText(venta.getFechaEmision());
                            dnipago.setText(venta.getDNI());
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
