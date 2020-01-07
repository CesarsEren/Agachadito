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
import android.view.ViewGroup;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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


    public static TextView cartcantidad, cartmontotal;
    /*
    public   void setCartcantidad(String cartcantidad) {
        this.cartcantidad = findViewById(R.id.cartcantidad);
        this.cartcantidad.setText(cartcantidad);
    }

    public void setCartmontotal(String cartmontotal) {
        this.cartmontotal = findViewById(R.id.cartmontotal);
        this.cartmontotal.setText(cartmontotal);
    }*/
    Set<String> subcategorias;
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
                startActivity(new Intent(getApplicationContext(), UCarritoActivity.class));
            }
        });
        myRoot = findViewById(R.id.DDcategorias);
        subcategorias = new HashSet<>();
        subcategorias.add("Todo");
        a = new LinearLayout(this);
        RecibirDatos(savedInstanceState);
        getProductos("Todo");

    }

    boolean cargarsubcategorias = true;
    ;


    String url;

    LinearLayout myRoot;
    LinearLayout a;

    private void getProductos(final String subcate) {
        productoBeans = new ArrayList<>();
        url = Utils.URLBASE+"Productos/lista/" + id_vendedor + (subcate.equals("Todo") ? "" : "/" + subcate);
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
                                if (cargarsubcategorias) {
                                    subcategorias.add(obj.getString("categoriaproducto").trim());
                                }
                                productoBeans.add(productoBean);
                            }
                            productoAdapter.setProductoBeans(productoBeans);
                            //rvproductos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                            rvproductos.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

                            rvproductos.setAdapter(productoAdapter);
                            if (cargarsubcategorias) {
                                a.setOrientation(LinearLayout.HORIZONTAL);
                                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                param.rightMargin = 10;
                                param.leftMargin = 10;
                                Iterator<String> it = subcategorias.iterator();
                                while(it.hasNext()){
                                        final TextView x = new TextView(getApplicationContext());
                                        x.setBackground(getResources().getDrawable(R.drawable.categoriaredondeada));
                                        x.setTextColor(getResources().getColor(R.color.coloricons));
                                        x.setPadding(45, 20, 45, 20);
                                        x.setText(it.next());
                                        a.addView(x);
                                        x.setLayoutParams(param);
                                        x.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                getProductos(x.getText().toString());
                                            }
                                        });
                                }
                                myRoot.addView(a);
                                cargarsubcategorias = false;
                            }
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
        id_vendedor = "" + extras.getInt("id_vendedor");
        nombrepuesto = extras.getString("nombrepuesto");
        fotopuesto = extras.getString("fotopuesto");
        categoriapuesto = extras.getString("categoriapuesto");
        collapseActionView.setTitle(nombrepuesto);

        mToolBar.setSubtitle(categoriapuesto);
        Glide.with(getApplicationContext()).load(fotopuesto).centerCrop().fitCenter().into(foto);
    }
}
