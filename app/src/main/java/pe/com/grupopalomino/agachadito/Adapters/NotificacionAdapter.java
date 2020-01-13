package pe.com.grupopalomino.agachadito.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pe.com.grupopalomino.agachadito.Models.NotificacionBean;
import pe.com.grupopalomino.agachadito.Models.ProductoBean;
import pe.com.grupopalomino.agachadito.R;
import pe.com.grupopalomino.agachadito.UCarritoActivity;
import pe.com.grupopalomino.agachadito.UFormPagoActivity;
import pe.com.grupopalomino.agachadito.UMenuActivity;
import pe.com.grupopalomino.agachadito.UPedidosFragment;
import pe.com.grupopalomino.agachadito.Utils.data.Utils;
import pe.com.grupopalomino.agachadito.VMenuActivity;

public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionAdapter.XMLNotificacion> {

    ArrayList<NotificacionBean> notificacionBeans;
    Context context;

    public NotificacionAdapter(Context con) {
        this.notificacionBeans = new ArrayList<>();
        this.context = con;
    }

    public void setNotificacionBeans(ArrayList<NotificacionBean> notificacionBeans) {
        //this.notificacionBeans.clear();
        this.notificacionBeans = notificacionBeans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public XMLNotificacion onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View vi = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemnotificacion, viewGroup, false);
        return new XMLNotificacion(vi);
    }

    @Override
    public void onBindViewHolder(@NonNull XMLNotificacion xmlNotificacion, int i) {
        final NotificacionBean notificacionBean = notificacionBeans.get(i);
        xmlNotificacion.comercionombre.setText(notificacionBean.getNombrenegocio());
        xmlNotificacion.preciototalcomercio.setText("Total : S /" + notificacionBean.getPreciototal());
        xmlNotificacion.fechapedido.setText(notificacionBean.getFechapedido());
        Glide.with(context).load(notificacionBean.getUrlfotonoti()).centerCrop().fitCenter().into(xmlNotificacion.imgnotifi);
        if (notificacionBean.getEstadoApp().equals("P"))//Pendiente
        {
            xmlNotificacion.btnestado.setVisibility(View.GONE);
            xmlNotificacion.btnestado.setText("Pendiente");
            xmlNotificacion.btnestado.setBackgroundColor(context.getResources().getColor(R.color.colordivider));
            xmlNotificacion.btnpagar.setVisibility(View.GONE);
            xmlNotificacion.btnesperar.setVisibility(View.VISIBLE);
        } else if (notificacionBean.getEstadoApp().equals("A")) {//Cancelado
            xmlNotificacion.btnestado.setVisibility(View.GONE);
            xmlNotificacion.btnestado.setText("Cancelado");
            xmlNotificacion.btnestado.setBackgroundColor(context.getResources().getColor(R.color.colorAccentLigth));
            xmlNotificacion.btnesperar.setVisibility(View.GONE);
            xmlNotificacion.btnpagar.setVisibility(View.VISIBLE);
            xmlNotificacion.btnesperar.setVisibility(View.GONE);
            xmlNotificacion.btncancelar.setVisibility(View.GONE);
        } else if (notificacionBean.getEstadoApp().equals("C")) {//Cancelado
            xmlNotificacion.btnestado.setVisibility(View.VISIBLE);
            xmlNotificacion.btnestado.setText("Cancelado");
            xmlNotificacion.btnestado.setBackgroundColor(context.getResources().getColor(R.color.colorAccentLigth));
            xmlNotificacion.btnesperar.setVisibility(View.VISIBLE);
            xmlNotificacion.btnpagar.setVisibility(View.GONE);
            xmlNotificacion.btnesperar.setVisibility(View.GONE);
            xmlNotificacion.btncancelar.setVisibility(View.GONE);
        } else if (notificacionBean.getEstadoApp().equals("E"))//Pagado | Entregado
        {
            xmlNotificacion.btnestado.setVisibility(View.VISIBLE);
            xmlNotificacion.btnestado.setText("Pagado");
            xmlNotificacion.btnestado.setBackgroundColor(context.getResources().getColor(R.color.colormoneyclaro));
            xmlNotificacion.btnesperar.setVisibility(View.VISIBLE);
            xmlNotificacion.btnpagar.setVisibility(View.GONE);
            xmlNotificacion.btnesperar.setVisibility(View.GONE);
            xmlNotificacion.btncancelar.setVisibility(View.GONE);
        }
        xmlNotificacion.btncancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("¿Está Seguro de Cancelar su pedido?")
                        .setContentText("perderá los datos del pedido")
                        .setConfirmText("Si ,cancelar!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                CancelarVenta(notificacionBean.getId_venta(), sDialog);
                            }
                        }).show();
            }
        });

        xmlNotificacion.btnpagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UFormPagoActivity.class);
                intent.putExtra("id_venta", notificacionBean.getId_venta());
                intent.putExtra("comercio", notificacionBean.getNombrenegocio());
                context.startActivity(intent);
            }
        });
    }

    private void CancelarVenta(final int id_venta, final SweetAlertDialog sDialog) {
        RequestQueue queue = Volley.newRequestQueue(context);
        Map<String, Object> params = new HashMap<>();
        SharedPreferences preferences = context.getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        int id_cliente = preferences.getInt("id_cliente", 0);
        params.put("id_venta", id_venta);
        params.put("id_cliente", id_cliente);
        JSONObject parameters = new JSONObject(params);
        String url;
        url = Utils.URLBASE + "Ventas/cancelar";
        Log.i("urlventa", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msgserver = response.getString("msgserver");
                    //String mensaje = response.getString("mensaje");
                    if (msgserver.equals("SUCCESS")) {
                        sDialog.setTitleText("Su pedido , fue cancelado")
                                .setContentText("Lamentamos el Inconveniente,\n Trabajamos para mejorar su experiencia")
                                .setConfirmText("Continuar Comprando")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        //context.startActivity(new Intent(context, VMenuActivity.class));
                        for (NotificacionBean item : notificacionBeans) {
                            if (item.getId_venta() == id_venta) {
                                item.setEstadoApp("C");
                            }
                        }
                        UPedidosFragment.notificacionAdapter.setNotificacionBeans(notificacionBeans);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });
        queue.add(request);
    }

    @Override
    public int getItemCount() {
        return notificacionBeans.size();
    }

    public class XMLNotificacion extends RecyclerView.ViewHolder {

        ImageView imgnotifi;
        TextView comercionombre;
        TextView preciototalcomercio;
        TextView fechapedido;

        Button btnpagar;
        Button btncancelar;
        Button btnesperar;
        TextView btnestado;

        public XMLNotificacion(@NonNull View itemView) {
            super(itemView);
            imgnotifi = itemView.findViewById(R.id.fotonoti);
            comercionombre = itemView.findViewById(R.id.tvcomercionoti);
            fechapedido = itemView.findViewById(R.id.tvfechanoti);
            preciototalcomercio = itemView.findViewById(R.id.tvtotalnoti);
            btnpagar = itemView.findViewById(R.id.btnpagar);
            btncancelar = itemView.findViewById(R.id.btncancelar);
            btnesperar = itemView.findViewById(R.id.btnespere);
            btnestado = itemView.findViewById(R.id.btnestado);
        }
    }
}
