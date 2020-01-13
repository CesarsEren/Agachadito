package pe.com.grupopalomino.agachadito;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pe.com.grupopalomino.agachadito.Utils.data.Utils;

public class UAgregarUbicacionActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextInputEditText direccion1;
    TextInputEditText apodo;
    Button agregar;
    RequestQueue mQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ubicacion);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapubicacion);
        mapFragment.getMapAsync(this);
        direccion1 = findViewById(R.id.direccionubi);
        apodo = findViewById(R.id.apodoubi);
        agregar = findViewById(R.id.btnregistrarubicacion);
        mQueue = Volley.newRequestQueue(this);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrarUbicacion(getApplicationContext());
            }
        });
    }
    String url = "";
    private void RegistrarUbicacion(Context context) {

        //url = "http://172.16.11.85:8090/JM/Ubicaciones/";
          url = Utils.URLBASE;
        url = url + "Ubicaciones/RegistrarUbicacion";

        Map<String, Object> params = new HashMap<>();
        SharedPreferences preferences = getApplication().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        int id_cliente = preferences.getInt("id_cliente", 0);
        params.put("id_cliente", id_cliente);
        params.put("apodo", apodo.getText().toString());
        params.put("longitud", longitud );
        params.put("latitud", latitud);
        params.put("direccion", direccion1.getText().toString());
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msgserver = response.getString("msgserver");
                    if (msgserver.equals("aceptado")) {
                        Utils.backTo="Mapa";
                        finish();
                        startActivity(new Intent(UAgregarUbicacionActivity.this, UMenuActivity.class));
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
        mQueue.add(request);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    String nombre, apellido, documento, correo, fechanac, pass;
    Double latitud, longitud;
    String direccion;
    String ROL = "ROL";

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng lima = new LatLng(-12.074270, -77.048648);
        latitud = lima.latitude;
        longitud = lima.longitude;
        mMap.addMarker(new MarkerOptions()
                // .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_position))
                .anchor(0.0f, 1.0f)
                .draggable(true)
                .position(lima)
                .title("Mi Ubicacion")
                .snippet(getDireccion(lima)));

        CameraPosition liberty = CameraPosition.builder().target(lima).zoom(16).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(liberty));
        //new MarkerOptions().position(sydney).title("Lima").draggable(true));
        direccion1.setText(getDireccion(lima));
        direccion = direccion1.getText().toString();

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng position = marker.getPosition();
                //Toast.makeText(getApplicationContext(), "Posicion" + position, Toast.LENGTH_SHORT).show();
                direccion1.setText(getDireccion(marker.getPosition()));
                direccion = direccion1.getText().toString();
                latitud = marker.getPosition().latitude;
                longitud = marker.getPosition().longitude;
            }
        });
    }

    public String getDireccion(LatLng position) {
        Geocoder geocoder;
        List<Address> address;
        String city = "Lima";
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            address = geocoder.getFromLocation(position.latitude, position.longitude, 1);
            city = address.get(0).getAddressLine(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return city;
    }
}
