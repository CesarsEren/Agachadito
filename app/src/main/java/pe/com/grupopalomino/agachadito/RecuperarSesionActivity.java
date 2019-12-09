package pe.com.grupopalomino.agachadito;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RecuperarSesionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_sesion);
        Button btnsesion = findViewById(R.id.btnsesion);

        btnsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(RecuperarSesionActivity.this,UMenuActivity.class));
                startActivity(new Intent(RecuperarSesionActivity.this,LoginActivity.class));
            }
        });
    }
}
