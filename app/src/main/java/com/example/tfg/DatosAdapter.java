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

        TextView nameDatosLista = convertView.findViewById(R.id.nameDatosLista);
        TextView dniDatosLista = convertView.findViewById(R.id.dniDatosLista);
        TextView nExpedienteDatosLista = convertView.findViewById(R.id.nExpedienteDatosLista);
        TextView eurosDatosLista = convertView.findViewById(R.id.eurosDatosLista);

        nameDatosLista.setText(datos.getNombre());
        dniDatosLista.setText(datos.getDni());
        nExpedienteDatosLista.setText(datos.getnExpediente());
        eurosDatosLista.setText("€ " + datos.getEuros());

        return convertView;
    }

}
