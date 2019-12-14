package pe.com.grupopalomino.agachadito;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class URegistrarClienteActivity extends AppCompatActivity {
    private final int PERMISO_GPS = 1;
    private boolean tienePermiso = false;


    Button btnregistrar;
    TextInputEditText campos[] = new TextInputEditText[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uregistrar_cliente);

        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.M) {
            validarUsoUbicacion();
        } else {
            tienePermiso = true;
        }
        btnregistrar = findViewById(R.id.btnregistrar);
        campos[0] = findViewById(R.id.etnombrereg);
        campos[1] = findViewById(R.id.etapellidoreg);
        campos[2] = findViewById(R.id.etdocumentoreg);
        campos[3] = findViewById(R.id.etcorreoreg);
        campos[4] = findViewById(R.id.etfechanacimientoreg);
        campos[5] = findViewById(R.id.etpassreg);
        campos[6] = findViewById(R.id.etrpassreg);


        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error = false;
                for (int i = 0; i < campos.length; i++) {
                    if (campos[i].getText().toString().equals("")) {
                        campos[i].setError("No puede estar vacio");
                    } else {
                        error = true;
                    }
                }
                if (error) {
                    if (!campos[5].getText().equals(campos[6].getText())) {
                        if (tienePermiso) {
                            Intent intentMap = new Intent().setClass(getBaseContext(), URegistroP2Activity.class);
                            //Intent intentMap = new Intent(URegistrarClienteActivity.this,URegistroP2Activity.class);
                            intentMap.putExtra("nombre", campos[0].getText().toString());
                            intentMap.putExtra("apellido", campos[1].getText().toString());
                            intentMap.putExtra("documento", campos[2].getText().toString());
                            intentMap.putExtra("correo", campos[3].getText().toString());
                            intentMap.putExtra("fechanac", campos[4].getText().toString());
                            intentMap.putExtra("pass", campos[6].getText().toString());
                            startActivity(intentMap);
                        } else {
                            Toast.makeText(getApplicationContext(), "No tiene permiso para acceder a su GPS", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        campos[5].setError("Contraseñas Diferentes");
                        campos[6].setError("Contraseñas Diferentes");
                    }
                }
            }
        });


    }

    @TargetApi(Build.VERSION_CODES.M)
    private void validarUsoUbicacion() {
        final int IGPS = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

        if (IGPS != PackageManager.PERMISSION_GRANTED) {
            String[] permisos = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            requestPermissions(permisos, PERMISO_GPS);
            ;
        } else {
            tienePermiso = true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISO_GPS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tienePermiso = true;
            } else {
                Toast.makeText(getApplicationContext(), "No podrá hacer uso del GPS", Toast.LENGTH_SHORT).show();
                tienePermiso = false;
            }
        }
    }
}
