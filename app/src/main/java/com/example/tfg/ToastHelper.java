package com.example.tfg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ToastHelper {

    public static final int ICON_INFO = android.R.drawable.ic_dialog_info;
    public static final int ICON_ERROR = android.R.drawable.ic_delete;

    public static void show(Context context, String mensaje, int iconResId) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast, null);

        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(mensaje);

        ImageView icon = layout.findViewById(R.id.toast_icon);
        icon.setImageResource(iconResId);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public static void info(Context context, String mensaje) {
        show(context, mensaje, ICON_INFO);
    }

    public static void error(Context context, String mensaje) {
        show(context, mensaje, ICON_ERROR);
    }
}
