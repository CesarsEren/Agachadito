package pe.com.grupopalomino.agachadito;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pe.com.grupopalomino.agachadito.Adapters.ProductoAdapter;
import pe.com.grupopalomino.agachadito.Models.ProductoBean;
import pe.com.grupopalomino.agachadito.Utils.data.Utils;

public class UDetalleComercioActivity extends AppCompatActivity {
    RequestQueue queue;
    RecyclerView rvproductos;
    ProductoAdapter productoAdapter;
    String id_vendedor, nombrepuesto, fotopuesto, categoriapuesto;
    ArrayList<ProductoBean> productoBeans;
    ImageView foto;
    Toolbar mToolBar;
    CollapsingToolbarLayout collapseActionView;

    public static TextView cartcantidad,cartmontotal;
    /*
    public   void setCartcantidad(String cartcantidad) {
        this.cartcantidad = findViewById(R.id.cartcantidad);
        this.cartcantidad.setText(cartcantidad);
    }

    public void setCartmontotal(String cartmontotal) {
        this.cartmontotal = findViewById(R.id.cartmontotal);
        this.cartmontotal.setText(cartmontotal);
    }*/

    Button btnvercarrito;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udetalle_comercio);
        mToolBar = findViewById(R.id.toolbardetalle);
        foto = findViewById(R.id.ivfotopuesto);
        collapseActionView = findViewById(R.id.collapseActionView);
        mToolBar.setNavigationIcon(R.drawable.iconback);
        setSupportActionBar(mToolBar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cartcantidad = findViewById(R.id.cartcantidad);
        cartmontotal = findViewById(R.id.cartmontotal);
        UDetalleComercioActivity.cartmontotal.setText("" + Utils.getPrecioTotalCarrito());
        UDetalleComercioActivity.cartcantidad.setText("" + Utils.getCantidadCarrito());

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UMenuActivity.class));
            }
        });

        rvproductos = findViewById(R.id.rvproductos);
        productoAdapter = new ProductoAdapter(this);
        queue = Volley.newRequestQueue(this);
        btnvercarrito = findViewById(R.id.gocart);
        btnvercarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UCarritoActivity.class));
            }
        });
        RecibirDatos(savedInstanceState);
        getProductos(id_vendedor);

    }

    String url = Utils.URLBASE;

    private void getProductos(String id_vendedor) {
        productoBeans = new ArrayList<>();
        url += "Productos/lista/" + id_vendedor;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("productos");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                ProductoBean productoBean = new ProductoBean();
                                productoBean.setId_producto(obj.getInt("id_producto"));
                                productoBean.setId_vendedor(obj.getInt("idvendedor"));
                                productoBean.setNombre(obj.getString("nombreproducto"));
                                productoBean.setPrecio("" + obj.getDouble("precio"));
                                productoBean.setFoto(obj.getString("foto"));
                                productoBeans.add(productoBean);
                            }
                            productoAdapter.setProductoBeans(productoBeans);
                            //rvproductos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                            rvproductos.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                            rvproductos.setAdapter(productoAdapter);
                           /*LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            rvproductos.setLayoutManager(layoutManager);*/

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


    public void RecibirDatos(Bundle extras) {
        extras = getIntent().getExtras();
        id_vendedor = ""+extras.getInt("id_vendedor");
        nombrepuesto = extras.getString("nombrepuesto");
        fotopuesto = extras.getString("fotopuesto");
        categoriapuesto = extras.getString("categoriapuesto");
        collapseActionView.setTitle(nombrepuesto);

        mToolBar.setSubtitle(categoriapuesto);
        Glide.with(getApplicationContext()).load(fotopuesto).centerCrop().fitCenter().into(foto);
    }
}
