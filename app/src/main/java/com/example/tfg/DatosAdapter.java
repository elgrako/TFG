package com.example.tfg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DatosAdapter extends ArrayAdapter<Datos> {

    private Context context;
    private List<Datos> listaDatos;

    public DatosAdapter(Context context, List<Datos> listaDatos) {
        super(context, R.layout.datos_lista_layout, listaDatos);
        this.context = context;
        this.listaDatos = listaDatos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.datos_lista_layout, parent, false);
        }

        Datos datos = listaDatos.get(position);

        TextView tvNombre = convertView.findViewById(R.id.tvNombre);
        TextView tvDni = convertView.findViewById(R.id.tvDni);
        TextView tvExpediente = convertView.findViewById(R.id.tvExpediente);
        TextView tvEuros = convertView.findViewById(R.id.tvEuros);

        tvNombre.setText(datos.getNombre());
        tvDni.setText(datos.getDni());
        tvExpediente.setText(datos.getnExpediente());
        tvEuros.setText("€" + datos.getEuros());

        return convertView;
    }

}
