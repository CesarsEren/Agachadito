package pe.com.grupopalomino.agachadito;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import pe.com.grupopalomino.agachadito.Utils.data.Utils;
import pe.com.grupopalomino.agachadito.Utils.spacetablayout.SpaceTabLayout;

public class UMenuActivity extends AppCompatActivity {
    Spinner Spubicaciones;
    SpaceTabLayout tabLayout;
    private final int PERMISO_GPS=1;
    private boolean tienePermiso = false;
    ImageView iconcart;
    //String url = "http://172.16.11.85:8090/JM/";
    String url = Utils.URLBASE+"Ubicaciones/lista/";
    String[] apodosArray;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_umenu);
        Spubicaciones = findViewById(R.id.Spubicaciones);
        iconcart = findViewById(R.id.iconcart);
        queue = Volley.newRequestQueue(this);
        llenarSpinner();
        iconcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UCarritoActivity.class));
            }
        });
        SharedPreferences preferences = getApplication().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        /*String id_cliente = preferences.getString("id_cliente", "id_cliente");
        Toast.makeText(this,id_cliente,Toast.LENGTH_LONG).show();*/
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new UPedidosFragment());
        fragmentList.add(new UUbicacionesFragment());
        fragmentList.add(new UComercioFragment());
        fragmentList.add(new UTarjetasFragment());
        fragmentList.add(new UPerfilFragment());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (SpaceTabLayout) findViewById(R.id.spaceTabLayout);

        tabLayout.initialize(viewPager, getSupportFragmentManager(),
                fragmentList);

        int currentApiVersion = Build.VERSION.SDK_INT;
        if(currentApiVersion >= Build.VERSION_CODES.M)
        {
            validarUsoUbicacion();
        }else
        {
            tienePermiso = true;
        }

    }
    private void llenarSpinner() {
        //Spubicaciones
        //final List<String> apodos = new ArrayList<>();

        SharedPreferences preferences = getApplication().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String id_cliente = preferences.getString("id_cliente", "id_cliente");
        url +=id_cliente;
        Log.i("url",url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray= response.getJSONArray("ubicaciones");
                    apodosArray = new String[jsonArray.length()];
                    for(int i = 0;i <jsonArray.length();i++)
                    {
                        apodosArray[i]=jsonArray.getJSONObject(i).getString("apodo");
                        Log.i("apodo",jsonArray.getJSONObject(i).getString("apodo"));
                        /*JSONObject object = jsonArray.getJSONObject(i);
                        apodos.add(object.getString("apodo").toString());*/
                    }
                    Spubicaciones.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,apodosArray));
                    //apodosArray = apodos.toArray(apodosArray);
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

    @TargetApi(Build.VERSION_CODES.M)
    private void validarUsoUbicacion() {
        final int IGPS = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

        if(IGPS!= PackageManager.PERMISSION_GRANTED)
        {
            String[]permisos = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            requestPermissions(permisos,PERMISO_GPS);;
        }else
        {
            tienePermiso = true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==PERMISO_GPS)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                tienePermiso = true;
            }
            else
            {
                Toast.makeText(getApplicationContext(),"No podrá hacer uso del GPS",Toast.LENGTH_SHORT).show();
                tienePermiso=false;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //tabLayout.saveState(outState);
        super.onSaveInstanceState(outState);

    }
}
