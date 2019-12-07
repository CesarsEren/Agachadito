package pe.com.grupopalomino.agachadito.HttpClients;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import pe.com.grupopalomino.agachadito.Models.Cliente;

public class HttpLogin extends AppCompatActivity {

    private VolleySingleton volley;
    protected RequestQueue fRequestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        volley = VolleySingleton.getInstance(getApplicationContext());
        fRequestQueue = volley.getRequestQueue();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fRequestQueue != null) {
            fRequestQueue.cancelAll(this);
        }
    }

    public void addToQueue(Request request) {
        if (request != null) {
            request.setTag(this);
            if (fRequestQueue == null)
                fRequestQueue = volley.getRequestQueue();
            request.setRetryPolicy(new DefaultRetryPolicy(
                    60000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            onPreStartConnection();
            fRequestQueue.add(request);
        }
    }

    public void onPreStartConnection() {
        setProgressBarIndeterminateVisibility(true);
    }

    public void onConnectionFinished() {
        setProgressBarIndeterminateVisibility(false);
    }

    public void onConnectionFailed(String error) {
        setProgressBarIndeterminateVisibility(false);
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
    }

    protected boolean iniciarsesion(String usuario,String password)
    {

        return true;
    }

}
