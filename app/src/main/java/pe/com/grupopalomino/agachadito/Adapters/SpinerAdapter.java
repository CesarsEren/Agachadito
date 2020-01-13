package pe.com.grupopalomino.agachadito.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pe.com.grupopalomino.agachadito.Models.SpinerMedioPago;
import pe.com.grupopalomino.agachadito.R;

public class SpinerAdapter extends ArrayAdapter<SpinerMedioPago> {


    public SpinerAdapter(Context context ,List<SpinerMedioPago> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return SpinerAdapter(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return SpinerAdapter(position, convertView, parent);
    }
    public View SpinerAdapter(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.customspiner, parent, false);
        }
        SpinerMedioPago items = getItem(position);
        ImageView spinnerImage = convertView.findViewById(R.id.ivCustomSpinner);
        TextView spinnerName = convertView.findViewById(R.id.tvCustomSpinner);
        TextView dueño= convertView.findViewById(R.id.tvCustomDueño);
        if (items != null) {
            if (items.getSpinerText().substring(0, 1).equals("4")) {
                spinnerImage.setImageResource(R.drawable.menu_visa);
            } else {
                spinnerImage.setImageResource(R.drawable.iconmastercard);
            }
            spinnerName.setText(items.getSpinerText());
            dueño.setText(items.getDueño());

        }
        return convertView;
    }
}
