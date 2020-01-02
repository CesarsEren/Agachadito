package pe.com.grupopalomino.agachadito;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class UPerfilFragment extends Fragment {

    static TextInputEditText perfil[] = new TextInputEditText[5];
    static Context con;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uperfil, container, false);
        con = view.getContext();

        Button cbtncerrarsesion = view.findViewById(R.id.Cbtncerrarsesion);
        cbtncerrarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = view.getContext().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
                settings.edit().clear().commit();
                getActivity().finish();
                startActivity(new Intent(getActivity(),LoginActivity.class));
            }
        });
        perfil[0] = view.findViewById(R.id.etdocumentoper);
        perfil[1] = view.findViewById(R.id.etnombresper);
        perfil[2] = view.findViewById(R.id.etapellidoper);
        //perfil[3] = view.findViewById(R.id.etcorreoper);
        perfil[3] = view.findViewById(R.id.etfechanacper);
        perfil[4] = view.findViewById(R.id.etfechaingper);


        SharedPreferences preferences = con.getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String id_persona = preferences.getString("id_persona", "id_persona");
        String fechaNacimiento = preferences.getString("fechaNacimiento", "fechaNacimiento");
        String dni = preferences.getString("dni", "dni");
        String direccion = preferences.getString("direccion", "direccion");
        String fechaIngreso = preferences.getString("fechaIngreso", "fechaIngreso");
        String nombres = preferences.getString("nombres", "nombres");
        String apellidos = preferences.getString("apellidos", "apellidos");
        //String estado = preferences.getString("estado", "estado");
        perfil[0].setText(dni);
        perfil[1].setText(nombres);
        perfil[2].setText(apellidos);
        perfil[3].setText(fechaNacimiento);
        perfil[4].setText(fechaIngreso);
        //"Persona", MODE_PRIVATE);
        //Toast.makeText(getContext(), "datos :" + dni, Toast.LENGTH_LONG).show();
        //String correo = prefs.getString("email", "por_defecto@email.com");

        return view;
        //inflater.inflate(R.layout.fragment_uperfil, container, false);
    }
}
