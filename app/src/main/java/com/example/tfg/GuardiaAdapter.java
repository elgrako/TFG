package com.example.tfg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tfg.entities.Guardia;

import java.util.ArrayList;

public class GuardiaAdapter extends ArrayAdapter<Guardia> {

    private final Context context;
    private final ArrayList<Guardia> guardias;

    public GuardiaAdapter(@NonNull Context context, @NonNull ArrayList<Guardia> guardias) {
        super(context, 0, guardias);
        this.context = context;
        this.guardias = guardias;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Guardia guardia = guardias.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.guardia_lista_layout, parent, false);
        }

        TextView nombreView = convertView.findViewById(R.id.itemNombreAsistido);
        TextView diaView = convertView.findViewById(R.id.itemDiaActuacion);
        TextView juzgadoView = convertView.findViewById(R.id.itemPasaJuzgado);
        TextView cobradoView = convertView.findViewById(R.id.itemHaCobrado);

        nombreView.setText(guardia.getNombreAsistido());
        diaView.setText(guardia.getDiaActuacion());
        if (guardia.isPorJuzgado()) {
            juzgadoView.setText("Sí");
        } else {
            juzgadoView.setText("No");
        }

        if (guardia.isCobrado()) {
            cobradoView.setText("Sí");
        } else {
            cobradoView.setText("No");
        }
        return convertView;
    }
}
