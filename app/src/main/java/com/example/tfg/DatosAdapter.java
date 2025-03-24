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

        TextView nombre = convertView.findViewById(R.id.tvNombre);
        TextView dni = convertView.findViewById(R.id.tvDni);
        TextView nExpediente = convertView.findViewById(R.id.tvExpediente);
        TextView euros = convertView.findViewById(R.id.tvEuros);

        nombre.setText("Nombre: " + datos.getNombre());
        dni.setText("DNI: " + datos.getDni());
        nExpediente.setText("Expediente: " + datos.getnExpediente());
        euros.setText("Euros: €" + datos.getEuros());

        return convertView;
    }
}
