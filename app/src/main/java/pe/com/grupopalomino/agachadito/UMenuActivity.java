package pe.com.grupopalomino.agachadito;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pe.com.grupopalomino.agachadito.Utils.spacetablayout.SpaceTabLayout;

public class UMenuActivity extends AppCompatActivity {

    SpaceTabLayout tabLayout;
    private final int PERMISO_GPS=1;
    private boolean tienePermiso = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_umenu);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new UPedidosFragment());
        fragmentList.add(new UUbicacionesFragment());
        fragmentList.add(new UComercioFragment());
        fragmentList.add(new UTarjetasFragment());
        fragmentList.add(new UPerfilFragment());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (SpaceTabLayout) findViewById(R.id.spaceTabLayout);

        tabLayout.initialize(viewPager, getSupportFragmentManager(),
                fragmentList);

        int currentApiVersion = Build.VERSION.SDK_INT;
        if(currentApiVersion >= Build.VERSION_CODES.M)
        {
            validarUsoUbicacion();
        }else
        {
            tienePermiso = true;
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void validarUsoUbicacion() {
        final int IGPS = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

        if(IGPS!= PackageManager.PERMISSION_GRANTED)
        {
            String[]permisos = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            requestPermissions(permisos,PERMISO_GPS);;
        }else
        {
            tienePermiso = true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==PERMISO_GPS)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                tienePermiso = true;
            }
            else
            {
                Toast.makeText(getApplicationContext(),"No podrá hacer uso del GPS",Toast.LENGTH_SHORT).show();
                tienePermiso=false;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //tabLayout.saveState(outState);
        super.onSaveInstanceState(outState);

    }
}
