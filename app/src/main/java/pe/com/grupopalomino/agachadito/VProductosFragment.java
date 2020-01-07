package pe.com.grupopalomino.agachadito;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class VProductosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match


    FloatingActionButton GoAgregarproducto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vproductos, container, false);
    GoAgregarproducto =view.findViewById(R.id.GoagregarProducto);
    GoAgregarproducto.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getActivity(),VAgregarProductoActivity.class));
        }
    });
        return view;
    }

}
