package pe.com.grupopalomino.agachadito;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.pushbots.push.Pushbots;
//import com.google.firebase.functions.FirebaseFunctions;
//import com.google.firebase.functions.HttpsCallableResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pe.com.grupopalomino.agachadito.Models.Persona;
import pe.com.grupopalomino.agachadito.Utils.data.Utils;


public class LoginActivity extends AppCompatActivity {

    RequestQueue mRequestQueue;
    //private FirebaseFunctions mFunctions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        AppCompatButton button = findViewById(R.id.btningresar);

        mRequestQueue = Volley.newRequestQueue(this);

        TextView olvidastepassword = findViewById(R.id.forgotpassword);
        TextView registrate = findViewById(R.id.registrate);

// ...
//        mFunctions = FirebaseFunctions.getInstance();


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

   /* private Task<String> addMessage(String text) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("text", text);
        data.put("push", true);

        return mFunctions
                .getHttpsCallable("addMessage")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String) task.getResult().getData();
                        return result;
                    }
                });
    }*/
    String ROL = "ROL";

    public String iniciarsesion(String usuario, String password) {

        String url = Utils.URLBASE;
        url = url + "Rest/loginApp/" + usuario + "/" + password;
        Log.i("info", url);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String msgserver = response.getString("msgserver");
                            if (msgserver.contains("Bienvenido ")) {
                                Class clase = Persona.class;
                                SharedPreferences settings = getApplicationContext().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
                                settings.edit().clear().commit();
                                Field[] campos = clase.getDeclaredFields();
                                SharedPreferences.Editor editor = getSharedPreferences("Credenciales", MODE_PRIVATE).edit();
                                JSONObject persona = response.getJSONObject("Persona");
                                editor.putInt("id_cliente", persona.getInt("id_cliente"));
                                editor.putInt("id_persona", persona.getInt("id_persona"));
                                editor.putString("fechaNacimiento", persona.getString("fechaNacimiento").toString());
                                editor.putString("dni", persona.getString("dni").toString());
                                editor.putString("direccion", persona.getString("direccion").toString());
                                editor.putString("fechaIngreso", persona.getString("fechaIngreso").toString());
                                editor.putString("nombres", persona.getString("nombres").toString());
                                editor.putString("apellidos", persona.getString("apellidos").toString());
                                editor.putBoolean("estado", persona.getBoolean("estado"));

                                ROL = response.getString("Rol");
                                editor.putString("Rol", ROL);
                                if (ROL.equals("Cliente")) {
                                    editor.putString("foto", response.getString("foto"));
                                    editor.commit();
                                    new SweetAlertDialog(LoginActivity.this)
                                            .setTitleText(msgserver)
                                            .setConfirmText("Entrar")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    finish();
                                                    startActivity(new Intent(LoginActivity.this, UMenuActivity.class));
                                                }
                                            }).show();

                                } else if (ROL.equals("Vendedor")) {
                                    JSONArray puesto = response.getJSONArray("Puesto");
                                    editor.putString("detalle", puesto.getJSONObject(0).getString("detalle"));
                                    editor.putString("foto", puesto.getJSONObject(0).getString("foto"));
                                    editor.commit();
                                    finish();
                                    new SweetAlertDialog(LoginActivity.this)
                                            .setTitleText(msgserver)
                                            .show();
                                    startActivity(new Intent(LoginActivity.this, VMenuActivity.class));
                                }
                            } else {
                                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText(msgserver)
                                        .show();
                            }

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
