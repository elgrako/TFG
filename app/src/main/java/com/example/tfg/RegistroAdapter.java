package com.example.tfg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RegistroAdapter extends ArrayAdapter<Registro> {

    private Context context;
    private List<Registro> listaRegistro;

    public RegistroAdapter(Context context, List<Registro> listaRegistro) {
        super(context, R.layout.datos_lista_layout, listaRegistro);
        this.context = context;
        this.listaRegistro = listaRegistro;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.datos_lista_layout, parent, false);
        }

        Registro Registro = listaRegistro.get(position);

        TextView nameRegistroLista = convertView.findViewById(R.id.nameDatosLista);
        TextView dniRegistroLista = convertView.findViewById(R.id.dniDatosLista);
        TextView nExpedienteRegistroLista = convertView.findViewById(R.id.nExpedienteDatosLista);
        TextView eurosRegistroLista = convertView.findViewById(R.id.eurosDatosLista);

        nameRegistroLista.setText(Registro.getNombre());
        dniRegistroLista.setText(Registro.getDni());
        nExpedienteRegistroLista.setText(Registro.getnExpediente());
        eurosRegistroLista.setText("€ " + Registro.getEuros());

        return convertView;
    }

}
