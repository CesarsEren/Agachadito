package pe.com.grupopalomino.agachadito;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pe.com.grupopalomino.agachadito.Models.Persona;

public class URegistroP2Activity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private RequestQueue mQueue;
    TextView direccion1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uregistro_p2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        direccion1 = findViewById(R.id.direccion);
        RecibirDatos(savedInstanceState);
        Button btnTerminar = findViewById(R.id.btnterminarregistro);

        mQueue = Volley.newRequestQueue(this);

        btnTerminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrarCliente();
            }
        });
    }

    String nombre, apellido, documento, correo, fechanac, pass;
    Double latitud, longitud;
    String direccion;
    String ROL = "ROL";

    public void RegistrarCliente() {
        Map<String, Object> params = new HashMap<>();
        params.put("nombres", nombre);
        params.put("apellidos", apellido);
        params.put("documento", documento);
        params.put("correo", correo);
        params.put("fechaNacimiento", fechanac);
        params.put("password", pass);
        params.put("longitud", longitud);
        params.put("latitud", latitud);
        params.put("direccion", direccion);

        JSONObject parameters = new JSONObject(params);
        String url = null;
          url = "http://172.16.11.85:8090/JM/Rest/";
        url = url + "RegistrarCliente";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msgserver = response.getString("msgserver");
                    if (msgserver.equals("Aceptado")) {

                        Class<?> clase = Persona.class;
                        Field[] campos = clase.getDeclaredFields();
                        SharedPreferences.Editor editor = getSharedPreferences("Credenciales", MODE_PRIVATE).edit();
                        JSONObject persona = response.getJSONObject("Persona");

                        editor.putString("id_persona",persona.getString("id_persona").toString());
                        editor.putString("fechaNacimiento",persona.getString("dni").toString());
                        editor.putString("dni",persona.getString("dni").toString());
                        editor.putString("direccion",persona.getString("direccion").toString());
                        editor.putString("fechaIngreso",persona.getString("fechaIngreso").toString());
                        editor.putString("nombres",persona.getString("nombres").toString());
                        editor.putString("apellidos",persona.getString("apellidos").toString());
                        editor.putBoolean("estado",persona.getBoolean("estado"));
                        editor.putBoolean("Rol",persona.getBoolean("Cliente"));
                        editor.commit();

                        ROL = response.getString("Rol");
                        startActivity(new Intent(URegistroP2Activity.this, UMenuActivity.class));
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


    public void RecibirDatos(Bundle extras) {
        extras = getIntent().getExtras();
        nombre = extras.getString("nombre");
        apellido = extras.getString("apellido");
        documento = extras.getString("documento");
        correo = extras.getString("correo");
        fechanac = extras.getString("fechanac");
        pass = extras.getString("pass");
        //Toast.makeText(getApplicationContext(), nombre + " " + apellido + " " + documento + " " + correo + " " + fechanac + " " + pass, Toast.LENGTH_LONG).show();


    }

    public String comprobar(Field tipo) {
        String respuesta = "AÃ±ada la condiciÃ³n por favor";
        switch (tipo.getName().toUpperCase()) {
            case "DATE":
            case "DATETIME":
            case "VARCHAR":
            case "NCHAR":
            case "STRING":
                respuesta = "String";
                break;
            case "DOUBLE":
            case "DECIMAL":
                respuesta = "double";
                break;
            case "BIT":
                respuesta = "boolean";
                break;
            case "BOOLEAN":
                respuesta = "boolean";
                break;
            case "INT":
            case "INTEGER":
                respuesta = "int";
                break;
            case "LONG":
                respuesta = "long";
                break;
        }
        return respuesta;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng lima = new LatLng(-12.0262676, -77.1278632);
        latitud = lima.latitude;
        longitud = lima.longitude;
        mMap.addMarker(new MarkerOptions()
                // .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_position))
                .anchor(0.0f, 1.0f)
                .draggable(true)
                .position(lima)
                .title("Mi Ubicacion")
                .snippet(getDireccion(lima)));
        //new MarkerOptions().position(sydney).title("Lima").draggable(true));
        direccion1.setText(getDireccion(lima));
        direccion = direccion1.getText().toString();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(lima));

       /* mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_position))
                        .anchor(0.0f,1.0f)
                        .draggable(true)
                        .position(latLng));
            }
        });*/
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
                Toast.makeText(getApplicationContext(), "Posicion" + position, Toast.LENGTH_SHORT).show();
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
