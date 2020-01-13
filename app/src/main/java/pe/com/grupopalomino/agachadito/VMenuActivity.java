package pe.com.grupopalomino.agachadito;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import java.util.ArrayList;
import java.util.List;

import pe.com.grupopalomino.agachadito.Utils.spacetablayout.SpaceTabLayout;

public class VMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private ResideMenu resideMenu;
    private Context mContext;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemPedidos;
    private ResideMenuItem itemProfile;
    //private ResideMenuItem itemCalendar;
    private ResideMenuItem itemSettings;
    //private ResideMenuItem itemHome2;
    //private ResideMenuItem itemProfile2;
    //private ResideMenuItem itemCalendar2;
    //private ResideMenuItem itemSettings2;

    TextView tvcomercio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmenu);
        mContext = this;
        setUpMenu();
        tvcomercio = findViewById(R.id.tvcomercio);
        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        //int id_persona = preferences.getInt("id_persona", 0);
        String detalle = preferences.getString("detalle", "detalle");
        tvcomercio.setText(detalle);
        if (savedInstanceState == null)
            changeFragment(new VPedidosFragment());
    }

    private void setUpMenu() {

        resideMenu = new ResideMenu(this);

        resideMenu.setBackground(R.drawable.vendedor_background);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome = new ResideMenuItem(this, R.drawable.menu_home, "Inicio");
        itemProfile = new ResideMenuItem(this, R.drawable.menu_perfil, "Perfil");
        itemPedidos = new ResideMenuItem(this,R.drawable.menu_hamburguesa,"Pedidos");
        itemSettings = new ResideMenuItem(this, R.drawable.menu_conf, "Configuración");

        itemHome.setOnClickListener(this);
        itemProfile.setOnClickListener(this);
        itemSettings.setOnClickListener(this);
        itemPedidos.setOnClickListener(this);
        //itemSettings2.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemPedidos, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_RIGHT);

        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        findViewById(R.id.title_bar_right_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
            }
        });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {

        if (view == itemHome) {
            changeFragment(new VDashboardFragment());
        } else if (view == itemProfile) {
            changeFragment(new UPerfilFragment());
        } else if (view == itemSettings) {
            changeFragment(new VProductosFragment());
        }else if(view == itemPedidos)
        {
            changeFragment(new VPedidosFragment());
        }
        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            //   Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
//            Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    private void changeFragment(Fragment targetFragment) {
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu() {
        return resideMenu;
    }

}
