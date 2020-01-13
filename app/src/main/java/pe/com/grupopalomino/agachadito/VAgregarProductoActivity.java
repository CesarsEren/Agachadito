package pe.com.grupopalomino.agachadito;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.renderscript.Script;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pe.com.grupopalomino.agachadito.Utils.data.Utils;

public class VAgregarProductoActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    ImageView foto_gallery;


    Spinner spcategorias;
    ImageView btnchange, btnchange2;
    TextInputLayout etlCategoria;
    TextInputEditText etCategoria;
    int activo = 0;

    Button btnagregar;

    StorageReference mStorage;


    TextInputEditText etNombre;
    TextInputEditText etPrecio;
    RequestQueue mQueue;
    String URL;

    TextView Actnombreproducto, Atcprecioproducto;


    public void RecibirDatos(Bundle extras) {
        extras = getIntent().getExtras();
        String[] subcategorias = extras.getStringArray("sub_categorias");
        spcategorias.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, subcategorias));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vagregar_producto);
        foto_gallery = (ImageView) findViewById(R.id.Atcimageproducto);
        btnchange = findViewById(R.id.btnchange);
        btnchange2 = findViewById(R.id.btnchange2);
        spcategorias = findViewById(R.id.spcategorias);
        etCategoria = findViewById(R.id.etCategoria);
        etlCategoria = findViewById(R.id.etlCategoria);
        btnagregar = findViewById(R.id.agregarProducto);
        mStorage = FirebaseStorage.getInstance().getReference();
        mQueue = Volley.newRequestQueue(this);
        etNombre = findViewById(R.id.etNombre);
        etPrecio = findViewById(R.id.etPrecio);
        etCategoria = findViewById(R.id.etCategoria);

        Actnombreproducto = findViewById(R.id.Actnombreproducto);
        Atcprecioproducto = findViewById(R.id.Atcprecioproducto);
        pDialog = new SweetAlertDialog(VAgregarProductoActivity.this, SweetAlertDialog.PROGRESS_TYPE);


        RecibirDatos(savedInstanceState);
        etNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Actnombreproducto.setText(etNombre.getText().toString().trim());
            }
        });
        etPrecio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String textprecio = "Precio :  $ " + etPrecio.getText().toString();
                Atcprecioproducto.setText(textprecio);
            }
        });


        btnagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String categorianame = etCategoria.getText().toString();
                String precio = etPrecio.getText().toString();
                String nombre = etNombre.getText().toString();
                if (activo == 2 || activo == 0&& (categorianame.isEmpty() || categorianame.trim().isEmpty())) {
                    etCategoria.setError("Campo Incorrecto");
                } else if (Double.parseDouble(precio) <= 0) {
                    etPrecio.setError("Campo Vacio");
                } else if (nombre.isEmpty()) {
                    etNombre.setError("Campo Vacio");
                } else {
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Subiendo Datos ...");
                    pDialog.setCancelable(true);
                    pDialog.show();
                    //RegistrarProducto();
                    StorageReference ubicacion = mStorage.child("fotos").child(imageUri.getLastPathSegment());
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
                                            RegistrarProducto();
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
        });
        btnchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                spcategorias.setVisibility(View.GONE);
                etlCategoria.setVisibility(View.VISIBLE);
                btnchange.setVisibility(View.GONE);
                btnchange2.setVisibility(View.VISIBLE);
                activo = 2;
            }
        });

        btnchange2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                spcategorias.setVisibility(View.VISIBLE);
                etlCategoria.setVisibility(View.GONE);
                btnchange2.setVisibility(View.GONE);
                btnchange.setVisibility(View.VISIBLE);
                activo = 1;
            }
        });

        Button buscarImg = findViewById(R.id.buscarfotoproducto);
        buscarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        startActivityForResult(gallery, PICK_IMAGE);
    }

    SweetAlertDialog pDialog;

    private void RegistrarProducto() {
        Map<String, Object> params = new HashMap<>();
        SharedPreferences preferences = getApplication().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        int id_cliente = preferences.getInt("id_cliente", 0);
        params.put("idvendedor", id_cliente);
        params.put("categoriaproducto", activo == 0 || activo == 1 ? spcategorias.getSelectedItem().toString().trim() : etCategoria.getText().toString().trim());
        params.put("nombreproducto", etNombre.getText().toString().trim());
        params.put("precio", Double.parseDouble(etPrecio.getText().toString().trim()));
        params.put("foto", URL);
        //params.put("foto", "AAAA");
        JSONObject parameters = new JSONObject(params);
        //String url = null;
        String url = Utils.URLBASE;
        url = url + "Productos/agregar";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msgserver = response.getString("msgserver");
                    if (msgserver.equals("SUCCESS")) {
                        pDialog.hide();
                        finish();
                        startActivity(new Intent(VAgregarProductoActivity.this, VMenuActivity.class));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            foto_gallery.setImageURI(imageUri);
        }
    }
}

