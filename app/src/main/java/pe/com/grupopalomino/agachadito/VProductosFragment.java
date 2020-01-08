package pe.com.grupopalomino.agachadito;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import pe.com.grupopalomino.agachadito.Adapters.ProductoAdapter;
import pe.com.grupopalomino.agachadito.Adapters.ProductosVendedorAdapter;
import pe.com.grupopalomino.agachadito.Models.ProductoBean;
import pe.com.grupopalomino.agachadito.Utils.data.Utils;


public class VProductosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match


    FloatingActionButton GoAgregarproducto;

    LinearLayout lossconectionvproductos;
    //RecyclerView rvproductosvendedor;
    RequestQueue queue;

    //RequestQueue queue;
    RecyclerView rvproductosvendedor;
    ProductosVendedorAdapter productoAdapter;
    //String id_vendedor;//nombrepuesto, fotopuesto, categoriapuesto;
    ArrayList<ProductoBean> productoBeans;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vproductos, container, false);
        GoAgregarproducto = view.findViewById(R.id.GoagregarProducto);

        lossconectionvproductos = view.findViewById(R.id.lossconectionvproductos);
        lossconectionvproductos.setVisibility(View.GONE);
        rvproductosvendedor = view.findViewById(R.id.rvproductosvendedor);
        subcategorias = new HashSet<>();
        subcategorias.add("Todo");
        productoAdapter = new ProductosVendedorAdapter(view.getContext());

        queue = Volley.newRequestQueue(view.getContext());
        myRoot = view.findViewById(R.id.linearcategorias);


        a = new LinearLayout(view.getContext());
        getProductos("Todo");
        GoAgregarproducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), VAgregarProductoActivity.class);
                //
                // String[] subcategoriasar = new String[subcategorias.size()];
                //subcategorias.toArray();//subcategorias
                /*int x = 0;
                subcategoriasar = new String[subcategorias.size()];
                for (String i : subcategorias) {
                    if (!i.equals("Todo")) {
                        subcategoriasar[x] = i.toString().trim();
                        x++;
                    }
                } */
                Set<String>norepe = new HashSet<>();
                for(int i =0;i<productoBeans.size();i++)
                {
                    norepe.add(productoBeans.get(i).getCategoria());
                }
                String[]data = new String[norepe.size()];
                int x = 0;
                for(String item : norepe)
                {
                    data[x++]=item;
                }
                intent.putExtra("sub_categorias", data);
                startActivity(intent);
            }
        });

        return view;
    }

    String[] subcategoriasar;
    boolean cargarsubcategorias = true;

    String url;

    Set<String> subcategorias;

    LinearLayout myRoot;
    LinearLayout a;

    private void getProductos(final String subcate) {
        productoBeans = new ArrayList<>();
        SharedPreferences preferences = getContext().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        int id_cliente = preferences.getInt("id_cliente", 0);//id del comercio
        url = Utils.URLBASE + "Productos/lista/" + id_cliente + (subcate.equals("Todo") ? "" : "/" + subcate);
        Log.i("urlvendedor", url);
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
                                productoBean.setCategoria(obj.getString("categoriaproducto").trim());
                                if (cargarsubcategorias) {
                                    subcategorias.add(obj.getString("categoriaproducto").trim());
                                    //Log.i("categoria",obj.getString("categoriaproducto").trim());
                                }
                                productoBeans.add(productoBean);
                            }
                            productoAdapter.setProductoBeans(productoBeans);
                            //rvproductos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                            rvproductosvendedor.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                            rvproductosvendedor.setAdapter(productoAdapter);
                            if (cargarsubcategorias) {
                                a.setOrientation(LinearLayout.HORIZONTAL);
                                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                param.rightMargin = 10;
                                param.leftMargin = 10;
                                Iterator<String> it = subcategorias.iterator();

                                while (it.hasNext()) {
                                    final TextView x = new TextView(getContext());
                                    x.setBackground(getResources().getDrawable(R.drawable.categoriaredondeada));
                                    x.setTextColor(getResources().getColor(R.color.coloricons));
                                    x.setPadding(45, 20, 45, 20);
                                    String dato = it.next();
                                    x.setText(dato);
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
                        lossconectionvproductos.setVisibility(View.VISIBLE);
                    }
                });
        queue.add(request);
    }

}
