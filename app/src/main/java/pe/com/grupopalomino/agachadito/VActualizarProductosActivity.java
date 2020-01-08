package pe.com.grupopalomino.agachadito;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;


import cn.pedant.SweetAlert.SweetAlertDialog;
import pe.com.grupopalomino.agachadito.Utils.data.Utils;

public class VActualizarProductosActivity extends AppCompatActivity {


    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    ImageView foto_gallery;


    int activo = 0;


    StorageReference mStorage;

    String URL;

    RequestQueue queue;
    Button buscarfotoproductoupdate;

    Button actualizarProducto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vactualizar_productos);


        Atcimageproductoupdate = findViewById(R.id.Atcimageproductoupdate);
        Actnombreproductoupdate = findViewById(R.id.Actnombreproductoupdate);
        Atcprecioproductoupdate = findViewById(R.id.Atcprecioproductoupdate);
        buscarfotoproductoupdate =findViewById(R.id.buscarfotoproductoupdate);
        etNombreupdate = findViewById(R.id.etNombreupdate);

        etlCategoriaupdate = findViewById(R.id.etlCategoriaupdate);
        etCategoriaupdate = findViewById(R.id.etCategoriaupdate);
        spcategoriasupdate = findViewById(R.id.spcategoriasupdate);
        btnchangeupdate = findViewById(R.id.btnchangeupdate);
        mStorage = FirebaseStorage.getInstance().getReference();
        queue = Volley.newRequestQueue(this);
        etPrecioUpdate = findViewById(R.id.etPrecioupdate);
        btnchangeupdate2 = findViewById(R.id.btnchange2update);
        actualizarProducto = findViewById(R.id.actualizarProducto);
        getDetalle(savedInstanceState);
        buscarfotoproductoupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        etNombreupdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Actnombreproductoupdate.setText(etNombreupdate.getText().toString().trim());
            }
        });
        etPrecioUpdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String textprecio = "Precio :  $ " + etPrecioUpdate.getText().toString();
                Atcprecioproductoupdate.setText(textprecio);
            }
        });
        actualizarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                                        URL = uri.toString();
                                        ActualizarProducto();
                                    }
                                });
                            }
                        }

                    }
                });


            }
        });

        btnchangeupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                spcategoriasupdate.setVisibility(View.GONE);
                etlCategoriaupdate.setVisibility(View.VISIBLE);
                btnchangeupdate.setVisibility(View.GONE);
                btnchangeupdate2.setVisibility(View.VISIBLE);
                activo = 1;
            }
        });

        btnchangeupdate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                spcategoriasupdate.setVisibility(View.VISIBLE);
                etlCategoriaupdate.setVisibility(View.GONE);
                btnchangeupdate2.setVisibility(View.GONE);
                btnchangeupdate.setVisibility(View.VISIBLE);
                activo = 2;
            }
        });
    }
    public void ActualizarProducto()
    {
        Map<String, Object> params = new HashMap<>();
        SharedPreferences preferences = getApplication().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        int id_cliente = preferences.getInt("id_cliente", 0);
        params.put("idvendedor", id_cliente);
        params.put("categoriaproducto", activo == 0 || activo == 1 ? etCategoriaupdate.getText().toString().trim() : spcategoriasupdate.getSelectedItem().toString().trim());
        params.put("nombreproducto", etNombreupdate.getText().toString().trim());
        params.put("precio", Double.parseDouble(etPrecioUpdate.getText().toString().trim()));
        params.put("foto", URL);
        //params.put("foto", "AAAA");
        JSONObject parameters = new JSONObject(params);
        //String url = null;
        String url = Utils.URLBASE;
        url = url + "Productos/actualizar";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msgserver = response.getString("msgserver");
                    if (msgserver.equals("Aceptado")) {
                        Toast.makeText(getApplicationContext(), "Se Actualizaron los datos del Producto, Exitosamente " + URL, Toast.LENGTH_LONG).show();

                        startActivity(new Intent(VActualizarProductosActivity.this, VMenuActivity.class));
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
        queue.add(request);
    }
    TextView Actnombreproductoupdate;
    ImageView Atcimageproductoupdate;
    TextView Atcprecioproductoupdate;

    TextInputEditText etNombreupdate;
    TextInputEditText etPrecioUpdate;
    TextInputLayout etlCategoriaupdate;
    TextInputEditText etCategoriaupdate;
    Spinner spcategoriasupdate;

    ImageView btnchangeupdate;
    ImageView btnchangeupdate2;

    private void getDetalle(Bundle extras) {
        extras = getIntent().getExtras();

        String[] detalle = extras.getStringArray("producto");
        String[]subcategorias = extras.getStringArray("categorias");

        String id_producto = detalle[0];
        String nombrepuesto = detalle[1];
        String precio = detalle[2];
        String foto = detalle[3];

        Actnombreproductoupdate.setText(nombrepuesto);
        Atcprecioproductoupdate.setText("Precio $ "+precio);
        Glide.with(getApplicationContext()).load(foto).centerCrop().fitCenter().into(Atcimageproductoupdate);
        spcategoriasupdate.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, subcategorias));
    }

    /*String[]data = {productoBean.getId_producto()+"",productoBean.getNombre(),productoBean.getPrecio().trim(),productoBean.getFoto()};
                intent.putExtra("producto",data);*/
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            foto_gallery.setImageURI(imageUri);
        }
    }
}
