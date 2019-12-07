package pe.com.grupopalomino.agachadito;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import pe.com.grupopalomino.agachadito.Utils.spacetablayout.SpaceTabLayout;

public class UMenuActivity extends AppCompatActivity {

    SpaceTabLayout tabLayout;
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
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //tabLayout.saveState(outState);
        super.onSaveInstanceState(outState);

    }
}
