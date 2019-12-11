package pe.com.grupopalomino.agachadito;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class URegistrarClienteActivity extends AppCompatActivity {
    private final int PERMISO_GPS = 1;
    private boolean tienePermiso = false;

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


        Button btnregistrar = findViewById(R.id.btnregistrar);
        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tienePermiso) {
                    Intent intentMap = new Intent().setClass(getBaseContext(), URegistroP2Activity.class);
                    startActivity(intentMap);
                } else {
                    Toast.makeText(getApplicationContext(), "No tiene permiso para acceder a su GPS", Toast.LENGTH_SHORT).show();
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
