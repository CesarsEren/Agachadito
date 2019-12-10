package pe.com.grupopalomino.agachadito;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

import pe.com.grupopalomino.agachadito.HttpClients.HttpLogin;
import pe.com.grupopalomino.agachadito.Models.Persona;


public class LoginActivity extends HttpLogin {

    RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        AppCompatButton button = findViewById(R.id.btningresar);

        mRequestQueue = Volley.newRequestQueue(this);

        TextView olvidastepassword = findViewById(R.id.forgotpassword);
        TextView registrate = findViewById(R.id.registrate);

        /*
        *
        * SharedPreferences pref = getApplicationContext().getSharedPreferences("Credenciales", MODE_PRIVATE);
          String rol = pref.getString("ROL","Cliente");*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText user = findViewById(R.id.etusuario);
                TextInputEditText password = findViewById(R.id.etpass);


                iniciarsesion(user.getText().toString(), password.getText().toString());


            }
        });

        olvidastepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, URecuperarPasswordActivity.class));
            }
        });

        registrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, URegistrarClienteActivity.class));
            }
        });
    }
      String ROL="ROL";
    public String iniciarsesion(String usuario, String password) {

        String url = "http://172.16.11.85:8090/JM/Rest/";
        url = url + "loginApp/" + usuario + "/" + password;
        Log.i("info", url);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Class<?> clase = Persona.class;
                            Field[] campos = clase.getDeclaredFields();
                            SharedPreferences.Editor editor = getSharedPreferences("Credenciales", MODE_PRIVATE).edit();
                            JSONObject persona = response.getJSONObject("Persona");

                            for (Field field : campos) {
                                switch (comprobar(field)) {
                                    case "String":
                                        editor.putString(String.valueOf(field), persona.getString(field.getName()));
                                        break;
                                    case "double":
                                        editor.putFloat(String.valueOf(field),Float.parseFloat(String.valueOf(persona.getDouble(field.getName()))));
                                        break;
                                    case "boolean":
                                        editor.putBoolean(String.valueOf(field), persona.getBoolean(field.getName()));
                                        break;
                                    case "long":
                                        editor.putLong(String.valueOf(field), persona.getLong(String.valueOf(Long.parseLong(field.getName()))));
                                        break;
                                }
                            }

                            ROL = response.getString("Rol");
                            if (ROL.equals("Cliente")) {
                                startActivity(new Intent(LoginActivity.this, UMenuActivity.class));
                            } else if (ROL.equals("Vendedor")) {
                                startActivity(new Intent(LoginActivity.this, VMenuActivity.class));
                            }
                            /*editor.putString("ROL",ROL);
                            editor.apply();
                            editor.commit();*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Response", error.getMessage());
                    }
                }
        );
        mRequestQueue.add(getRequest);
        return ROL;
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
}
