package pe.com.grupopalomino.agachadito;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
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
    private ResideMenuItem itemProfile;
    private ResideMenuItem itemCalendar;
    private ResideMenuItem itemSettings;
    private ResideMenuItem itemHome2;
    private ResideMenuItem itemProfile2;
    private ResideMenuItem itemCalendar2;
    private ResideMenuItem itemSettings2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmenu);
        mContext = this;
        setUpMenu();
        if (savedInstanceState == null)
            changeFragment(new UComercioFragment());
    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);

        resideMenu.setBackground(R.drawable.vendedor_background);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome = new ResideMenuItem(this, R.drawable.menu_home, "Inicio");
        itemProfile = new ResideMenuItem(this, R.drawable.menu_perfil, "Perfil");
        itemCalendar = new ResideMenuItem(this, R.drawable.menu_map, "Mapa");
        itemSettings = new ResideMenuItem(this, R.drawable.menu_conf, "Configuración");
        itemHome2 = new ResideMenuItem(this, R.drawable.menu_home, "Inicio");
        itemProfile2 = new ResideMenuItem(this, R.drawable.menu_perfil, "Perfil");
        itemCalendar2 = new ResideMenuItem(this, R.drawable.menu_map, "Mapa");
        itemSettings2 = new ResideMenuItem(this, R.drawable.menu_conf, "Configuración");

        itemHome.setOnClickListener(this);
        itemProfile.setOnClickListener(this);
        itemCalendar.setOnClickListener(this);
        itemSettings.setOnClickListener(this);
        itemHome2.setOnClickListener(this);
        itemProfile2.setOnClickListener(this);
        itemCalendar2.setOnClickListener(this);
        itemSettings2.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemCalendar, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemHome2, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemProfile2, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemCalendar2, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemSettings2, ResideMenu.DIRECTION_RIGHT);

        // You can disable a direction by setting ->
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

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
            changeFragment(new UComercioFragment());
        } else if (view == itemProfile) {
            changeFragment(new UPerfilFragment());
        } else if (view == itemCalendar) {
            changeFragment(new UUbicacionesFragment());
        } else if (view == itemSettings) {
            changeFragment(new UPedidosFragment());
        } else if (view == itemHome2) {
            changeFragment(new UComercioFragment());
        } else if (view == itemProfile2) {
            changeFragment(new UPerfilFragment());
        } else if (view == itemCalendar2) {
            changeFragment(new UUbicacionesFragment());
        } else if (view == itemSettings2) {
            changeFragment(new UPedidosFragment());
        }

        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
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
