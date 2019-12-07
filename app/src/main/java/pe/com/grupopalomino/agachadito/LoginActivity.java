package pe.com.grupopalomino.agachadito;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

import pe.com.grupopalomino.agachadito.HttpClients.HttpLogin;

public class LoginActivity extends HttpLogin {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        AppCompatButton button = findViewById(R.id.btningresar);

        TextView olvidastepassword = findViewById(R.id.forgotpassword);
        TextView registrate = findViewById(R.id.registrate);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText user = findViewById(R.id.etusuario);
                TextInputEditText password = findViewById(R.id.etpass);

                if (iniciarsesion(user.getText().toString(), password.getText().toString())) {
                    startActivity(new Intent(LoginActivity.this, UMenuActivity.class));
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
}
