package pe.com.grupopalomino.agachadito;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String ROL= preferences.getString("Rol", "norol");
        if(ROL.equals("Cliente")){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,RecuperarSesionActivity.class));
                finish();
            }
        },2000);
        }else if(ROL.equals("Vendedor"))
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this,VMenuActivity.class));
                    finish();
                }
            },2000);
        }else
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                }
            },2000);

        }
    }
}
