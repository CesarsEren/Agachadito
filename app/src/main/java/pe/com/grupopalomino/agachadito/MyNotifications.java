package pe.com.grupopalomino.agachadito;

import android.app.Application;

import com.pushbots.push.Pushbots;

public class MyNotifications extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Pushbots Library
        Pushbots.sharedInstance().init(this);
    }
}
