package pe.com.grupopalomino.agachadito.HttpClients;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

    private static VolleySingleton mVolleyS = null;
    //http://gpmess.com/blog/2014/05/28/volley-usando-webservices-en-android-de-manera-sencilla/
    private RequestQueue mRequestQueue;

    private VolleySingleton(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static VolleySingleton getInstance(Context context) {
        if (mVolleyS == null) {
            mVolleyS = new VolleySingleton(context);
        }
        return mVolleyS;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

}
