package pe.com.grupopalomino.agachadito;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.util.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pe.com.grupopalomino.agachadito.Models.PuestosBean;
import pe.com.grupopalomino.agachadito.Models.TarjetaBean;
import pe.com.grupopalomino.agachadito.Utils.data.Utils;

public class UUbicacionesFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap googleMap;
    MapView mapView;
    View view;
    RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_uubicaciones, container, false);
        FloatingActionButton addUbicacion = view.findViewById(R.id.fabmaps);
        addUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.backTo="Mapa";
                Intent intentMap = new Intent().setClass(view.getContext(), UAgregarUbicacionActivity.class);
                startActivity(intentMap);
            }
        });
        queue = Volley.newRequestQueue(view.getContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.ubicaciones);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        //final LatLng posicion ;//= new LatLng(-12.074270, -77.048648);
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); //-12.0730088,-77.0485728

        SharedPreferences preferences = view.getContext().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        int ID_CLIENTE= preferences.getInt("id_cliente", 0);

        String url = Utils.URLBASE;
        url += "Zonas/lista/" + ID_CLIENTE + "/" + UMenuActivity.Spubicaciones.getSelectedItem();
        Log.i("ubicacion",url);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("puntos");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                LatLng posicion = new LatLng(Double.parseDouble(obj.getString("latitud")), Double.parseDouble(obj.getString("longitud")));
                                googleMap.addMarker(new MarkerOptions()
                                        .zIndex(Float.parseFloat(obj.getString("id_vendedor")))
                                        .title(obj.getString("detalle"))
                                        .snippet(obj.getString("referencia"))
                                        .position(posicion)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.eatpoint)));

                                PuestosBean bean = new PuestosBean();
                                bean.setCategoriapuesto(obj.getString("referencia"));
                                bean.setId_vendedor(obj.getInt("id_vendedor"));
                                bean.setNombrepuesto(obj.getString("detalle"));
                                bean.setPreciopromedio(obj.getDouble("preciopromedio"));
                                bean.setUrlimg(obj.getString("foto"));
                                Log.i("zona", bean.toString());

                                Utils.ubicaciones.add(bean);
                            }
                            JSONObject object = response.getJSONObject("ubicacion");
                            LatLng posicion = new LatLng(Double.parseDouble(object.getString("latitud")), Double.parseDouble(object.getString("longitud")));
                            googleMap.addMarker(new MarkerOptions().position(posicion).title(object.getString("apodo")).snippet(object.getString("direccion")));
                            CameraPosition liberty = CameraPosition.builder().target(posicion).zoom(16).bearing(0).tilt(45).build();
                            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(liberty));



                            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    if(Utils.zindextemp!=0 && Utils.zindextemp==(int)marker.getZIndex()){
                                        Intent intentDetalle = new Intent(view.getContext(), UDetalleComercioActivity.class);
                                        for (PuestosBean x : Utils.ubicaciones) {
                                            if (x.getId_vendedor() == (int) marker.getZIndex() && marker.getZIndex()!=0) {
                                                Utils.backTo="Mapa";
                                                intentDetalle.putExtra("id_vendedor", x.getId_vendedor());
                                                intentDetalle.putExtra("fotopuesto", x.getUrlimg());
                                                intentDetalle.putExtra("nombrepuesto", x.getNombrepuesto());
                                                intentDetalle.putExtra("categoriapuesto", x.getCategoriapuesto());
                                                view.getContext().startActivity(intentDetalle);
                                            }
                                        }
                                    }else
                                    {
                                        Utils.zindextemp=(int)marker.getZIndex();
                                    }

                                    return false;
                                }
                            });
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
}
