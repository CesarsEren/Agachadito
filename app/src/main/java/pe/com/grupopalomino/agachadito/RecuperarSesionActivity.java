package pe.com.grupopalomino.agachadito;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RecuperarSesionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_sesion);
        Button btnsesion = findViewById(R.id.btnsesion);

        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String nom= preferences.getString("nombres", "no");
        String ape= preferences.getString("apellidos", "no");
        TextView nombre = findViewById(R.id.tvnombresesion);
        nombre.setText(nom+" "+ape);
        btnsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(RecuperarSesionActivity.this,UMenuActivity.class));
                startActivity(new Intent(RecuperarSesionActivity.this,UMenuActivity.class));
            }
        });
    }
}
