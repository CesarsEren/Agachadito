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

                if (user.getText().toString().isEmpty()) {
                    user.setError("No puede estar vacio");
                } else if (password.getText().toString().isEmpty()) {
                    password.setError("No puede estar vacio");
                } else {
                    iniciarsesion(user.getText().toString(), password.getText().toString());
                }
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

    String ROL = "ROL";

    public String iniciarsesion(String usuario, String password) {

        String url = "http://172.16.11.85:8090/JM/Rest/";
        url = url + "loginApp/" + usuario + "/" + password;
        Log.i("info", url);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String msgserver = response.getString("msgserver");
                            if (msgserver.contains("Bienvenido ")) {
                                Class clase = Persona.class;
                                Field[] campos = clase.getDeclaredFields();
                                SharedPreferences.Editor editor = getSharedPreferences("credenciales", MODE_PRIVATE).edit();
                                JSONObject persona = response.getJSONObject("Persona");
                                editor.putString("id_persona",persona.getString("id_persona").toString());
                                editor.putString("fechaNacimiento",persona.getString("dni").toString());
                                editor.putString("dni",persona.getString("dni").toString());
                                editor.putString("direccion",persona.getString("direccion").toString());
                                editor.putString("fechaIngreso",persona.getString("fechaIngreso").toString());
                                editor.putString("nombres",persona.getString("nombres").toString());
                                editor.putString("apellidos",persona.getString("apellidos").toString());
                                editor.putBoolean("estado",persona.getBoolean("estado"));

                                /*for (int i = 0; i < campos.length; i++) {
                                    String dato = comprobar(campos[i]);
                                    if (dato.equals("String")) {
                                        editor.putString(campos[i].getName(), persona.getString(campos[i].getName()));
                                        Log.i("String", campos[i].getName());
                                    } else if (dato.equals("double")) {
                                        editor.putFloat(campos[i].getName(), Float.parseFloat("" + persona.getDouble(campos[i].getName())));
                                        Log.i("String", campos[i].getName());
                                    } else if (dato.equals("boolean")) {
                                        editor.putBoolean(campos[i].getName(), persona.getBoolean(campos[i].getName()));
                                        Log.i("String", campos[i].getName());
                                    } else if (dato.equals("int")) {
                                        editor.putInt(campos[i].getName(), persona.getInt(campos[i].getName()));
                                        Log.i("String", campos[i].getName());
                                    }
                                }*/
                                //editor.apply();
                                editor.commit();

                                ROL = response.getString("Rol");
                                if (ROL.equals("Cliente")) {
                                    startActivity(new Intent(LoginActivity.this, UMenuActivity.class));
                                } else if (ROL.equals("Vendedor")) {
                                    startActivity(new Intent(LoginActivity.this, VMenuActivity.class));
                                }
                            }
                            Toast.makeText(getApplicationContext(), msgserver, Toast.LENGTH_LONG).show();
                            /*editor.putString("ROL",ROL);*/

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
        switch (tipo.getType().toString().toUpperCase()) {
            case "STRING":
                respuesta = "String";
                break;
            case "DOUBLE":
                respuesta = "double";
                break;
            case "BOOLEAN":
            case "BIT":
                respuesta = "boolean";
                break;
            case "INT":
            case "INTEGER":
                respuesta = "int";
                break;
            case "LONG":
                respuesta = "long";
                break;
            default:
                respuesta = tipo.getType().toString();
                break;
        }
        return respuesta;
    }
}
