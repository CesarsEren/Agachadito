package pe.com.grupopalomino.agachadito;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pe.com.grupopalomino.agachadito.Adapters.PuestosAdapter;
import pe.com.grupopalomino.agachadito.Models.PuestosBean;
import pe.com.grupopalomino.agachadito.Utils.data.Utils;


public class UComercioFragment extends Fragment {

    RequestQueue queue;

    ArrayList<PuestosBean> puestosBeanArrayList;
    RecyclerView rvpuestos;
    PuestosAdapter adapter;
    FloatingActionButton fabQr;
    Set<String>categorias;
    LinearLayout myRoot;
    LinearLayout a;

    boolean cargarcategorias=true;

    LinearLayout lossconection;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ucomercio, container, false);
        cargarcategorias = true;
        myRoot = view.findViewById(R.id.LLcategorias);
        a = new LinearLayout(view.getContext());
        rvpuestos = view.findViewById(R.id.rvpuestos);
        queue = Volley.newRequestQueue(getContext());
        puestosBeanArrayList = new ArrayList<>();
        categorias = new HashSet<>();
        categorias.add("Todo");
        lossconection = view.findViewById(R.id.lossconection);
        dataPuestos("Todo");

        fabQr = view.findViewById(R.id.fabQr);
        fabQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(view.getContext(), UURecogerconQrActivity.class));
            }
        });
        //Drawable image  = ContextCompat.getDrawable(view.getContext(), R.drawable.categoriaredondeada);

        return view;
    }


    public void dataPuestos(String categoria) {
        lossconection.setVisibility(View.GONE);
        adapter = new PuestosAdapter(getContext());
        puestosBeanArrayList.clear();
        //categorias.clear();
        String url = Utils.URLBASE + "Zonas/lista" + (categoria.equals("Todo") ? "" : "/" + categoria);
        Log.i("urlzonas",url);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("puntos");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                //LatLng posicion = new LatLng(Double.parseDouble(obj.getString("latitud")),Double.parseDouble(obj.getString("longitud")));
                                PuestosBean bean = new PuestosBean();
                                bean.setCategoriapuesto(obj.getString("referencia"));
                                if (cargarcategorias) {
                                    categorias.add(obj.getString("referencia").trim());
                                }
                                bean.setId_vendedor(obj.getInt("id_vendedor"));
                                bean.setNombrepuesto(obj.getString("detalle"));
                                bean.setPreciopromedio(obj.getDouble("preciopromedio"));
                                bean.setUrlimg(obj.getString("foto"));
                                Log.i("zona",bean.toString());
                                puestosBeanArrayList.add(bean);

                            }
                            adapter.setPuestos(puestosBeanArrayList);
                            rvpuestos.setAdapter(adapter);
                            rvpuestos.setLayoutManager(new LinearLayoutManager(getContext()));
                            if (cargarcategorias) {
                                a.setOrientation(LinearLayout.HORIZONTAL);
                                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                param.rightMargin = 10;
                                param.leftMargin = 10;

                                for (String categoria :categorias) {
                                    final TextView x = new TextView(getContext());
                                    x.setBackground(getResources().getDrawable(R.drawable.categoriaredondeada));
                                    x.setTextColor(getResources().getColor(R.color.coloricons));
                                    x.setPadding(45, 20, 45, 20);
                                    x.setText(categoria.trim());
                                    a.addView(x);
                                    x.setLayoutParams(param);
                                    x.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dataPuestos(x.getText().toString());
                                        }
                                    });
                                }
                                myRoot.addView(a);
                                cargarcategorias = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        lossconection.setVisibility(View.VISIBLE);
                    //hideProgressDialog();
                    }
                });
        queue.add(request);
    }
}
