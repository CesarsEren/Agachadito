package pe.com.grupopalomino.agachadito;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import pe.com.grupopalomino.agachadito.Adapters.CarritoAdapter;
import pe.com.grupopalomino.agachadito.Utils.data.Utils;

public class UCarritoActivity extends AppCompatActivity {

    public static CarritoAdapter adapter;
    public static TextView preciototal;
    public static RecyclerView rcart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ucarrito);
        adapter = new CarritoAdapter(this);
        rcart = findViewById(R.id.rvcarrito);
        preciototal = findViewById(R.id.preciototal);
        preciototal.setText("$ "+Utils.getPrecioTotalCarrito());
        TextView textback = findViewById(R.id.textback);
        if(Utils.getCantidadCarrito()>0)
        {
            textback.setVisibility(View.GONE);
            rcart.setVisibility(View.VISIBLE);
        }else
        {
            textback.setVisibility(View.VISIBLE);
        }
        adapter.setProductoBeans(Utils.carrito);
        rcart.setAdapter(adapter);
        rcart.setLayoutManager(new LinearLayoutManager(this));
    }
}
