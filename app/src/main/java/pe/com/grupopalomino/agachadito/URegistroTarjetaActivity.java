package pe.com.grupopalomino.agachadito;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import pe.com.grupopalomino.agachadito.Models.Persona;
import pe.com.grupopalomino.agachadito.Utils.data.Utils;

public class URegistroTarjetaActivity extends AppCompatActivity {

    private RequestQueue mQueue;
    TextInputEditText nrotarjeta, fechavenc, codigoCVV, NombreTarjeta, ApellidoTarjeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uregistro_tarjeta);
        Button btnregistrartarjeta = findViewById(R.id.btnregistrartarjeta);

        nrotarjeta = findViewById(R.id.nrotarjeta);
        fechavenc = findViewById(R.id.fechavenc);
        codigoCVV = findViewById(R.id.codigoCVV);
        NombreTarjeta = findViewById(R.id.NombreTarjeta);
        ApellidoTarjeta = findViewById(R.id.ApellidoTarjeta);

        mQueue = Volley.newRequestQueue(this);
        btnregistrartarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrarTarjeta();
            }
        });

    }

    private void RegistrarTarjeta() {
        String[] tarjeta = new String[4];//nrotarjeta.getText().toString().split("-");

        tarjeta[0] = nrotarjeta.getText().toString().substring(0, 4);
        tarjeta[1] = nrotarjeta.getText().toString().substring(4, 8);
        tarjeta[2] = nrotarjeta.getText().toString().substring(8, 12);
        tarjeta[3] = nrotarjeta.getText().toString().substring(12, 16);

        Map<String, Object> params = new HashMap<>();
        SharedPreferences preferences = getApplication().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        int id_cliente = preferences.getInt("id_cliente", 0);
        params.put("id_cliente", id_cliente);
        params.put("nroTarjeta", tarjeta[0] + " " + tarjeta[1] + " " + tarjeta[2] + " " + tarjeta[3]);
        params.put("fechaVenc", fechavenc.getText().toString());
        params.put("ccv", codigoCVV.getText().toString());
        params.put("nombre", NombreTarjeta.getText().toString());
        params.put("apellido", ApellidoTarjeta.getText().toString());

        JSONObject parameters = new JSONObject(params);
        //String url = null;
        String url = Utils.URLBASE;
        //url = "http://172.16.11.85:8090/JM/Tarjetas/";
        url = url + "Tarjetas/RegistrarTarjeta";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msgserver = response.getString("msgserver");
                    if (msgserver.equals("aceptado")) {
                        startActivity(new Intent(URegistroTarjetaActivity.this, UMenuActivity.class));
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
}
