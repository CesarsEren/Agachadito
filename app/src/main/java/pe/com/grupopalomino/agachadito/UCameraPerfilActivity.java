package pe.com.grupopalomino.agachadito;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventCallback;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pe.com.grupopalomino.agachadito.Utils.data.Utils;

public class UCameraPerfilActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    ImageView foto_gallerychange;

    static TextInputEditText perfil[] = new TextInputEditText[5];
    ImageView GoGallerychange ;
    StorageReference mStorage;
    RequestQueue mQueue;
Button actualizar;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            foto_gallerychange.setImageURI(imageUri);
        }
    }
    String URL;
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        startActivityForResult(gallery, PICK_IMAGE);
    }
    SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pDialog=new SweetAlertDialog(UCameraPerfilActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        setContentView(R.layout.activity_ucamera_perfil);
        foto_gallerychange = findViewById(R.id.foto_gallerychange);
        mStorage = FirebaseStorage.getInstance().getReference();
        mQueue = Volley.newRequestQueue(this);
        GoGallerychange = findViewById(R.id.GoGallerychange);
        //Glide//foto_gallerychange

        actualizar = findViewById(R.id.actualizarPerfil);

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Actualizando Perfil...");
                pDialog.setCancelable(true);
                pDialog.show();
                //ActualizarPerfil();
                StorageReference ubicacion = mStorage.child("perfil").child(imageUri.getLastPathSegment());
                ubicacion.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        //Toast.makeText(getApplicationContext(), "Se Subio exitosamente el Producto" + URL, Toast.LENGTH_LONG).show();
                                        URL = uri.toString();
                                        ActualizarPerfil();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });

        perfil[0] = findViewById(R.id.etdocumentoperchange);
        perfil[1] = findViewById(R.id.etnombresperchange);
        perfil[2] = findViewById(R.id.etapellidoperchange);
        //perfil[3] = view.findViewById(R.id.etcorreoper);
        perfil[3] = findViewById(R.id.etfechanacperchange);
        perfil[4] = findViewById(R.id.etfechaingperchange);
        GoGallerychange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });


        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        int id_persona = preferences.getInt("id_persona", 0);
        String fechaNacimiento = preferences.getString("fechaNacimiento", "fechaNacimiento");
        String dni = preferences.getString("dni", "dni");
        String direccion = preferences.getString("direccion", "direccion");
        String fechaIngreso = preferences.getString("fechaIngreso", "fechaIngreso");
        String nombres = preferences.getString("nombres", "nombres");
        String apellidos = preferences.getString("apellidos", "apellidos");
        String foto= preferences.getString("foto", "foto");
        //String estado = preferences.getString("estado", "estado");
        Glide.with(this).load(foto).centerCrop().fitCenter().into(foto_gallerychange);
        perfil[0].setText(dni);
        perfil[1].setText(nombres);
        perfil[2].setText(apellidos);
        perfil[3].setText(fechaNacimiento);
        perfil[4].setText(fechaIngreso);
    }

    private void ActualizarPerfil() {

        Map<String, Object> params = new HashMap<>();
        SharedPreferences preferences = getApplication().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        int id_cliente = preferences.getInt("id_cliente", 0);
        int id_persona= preferences.getInt("id_persona", 0);
        params.put("id_cliente", id_cliente);
        params.put("id_persona", id_persona);
        params.put("nombres", perfil[1].getText().toString());
        params.put("apellidos", perfil[2].getText().toString());
        params.put("fechaNacimiento", perfil[3].getText().toString());
        params.put("foto", URL);
        //params.put("foto", "AAA");

        JSONObject parameters = new JSONObject(params);
        //String url = null;
        String url = Utils.URLBASE;
        url = url + "Rest/ActualizarCliente";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msgserver = response.getString("msgserver");
                    if (msgserver.equals("SUCCESS")) {
                        pDialog.hide();
                        SharedPreferences settings = getApplicationContext().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
                        settings.edit().clear().commit();
                        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("Credenciales", MODE_PRIVATE).edit();
                        JSONObject persona = response.getJSONObject("Persona");
                        editor.putInt("id_cliente",persona.getInt("id_cliente"));
                        editor.putInt("id_persona",persona.getInt("id_persona"));
                        editor.putString("fechaNacimiento",persona.getString("fechaNacimiento").toString());
                        editor.putString("dni",persona.getString("dni").toString());
                        editor.putString("direccion",persona.getString("direccion").toString());
                        editor.putString("fechaIngreso",persona.getString("fechaIngreso").toString());
                        editor.putString("nombres",persona.getString("nombres").toString());
                        editor.putString("apellidos",persona.getString("apellidos").toString());
                        editor.putBoolean("estado",persona.getBoolean("estado"));
                        editor.putString("foto",response.getString("foto"));
                        editor.commit();
                        editor.apply();
                        finish();
                        startActivity(new Intent(UCameraPerfilActivity.this, UMenuActivity.class));
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
