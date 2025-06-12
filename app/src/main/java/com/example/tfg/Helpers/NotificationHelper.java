package com.example.tfg.Helpers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.Manifest;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.tfg.R;

public class NotificationHelper {

    private static final String CHANNEL_ID = "tfg-notificaciones";
    private static final String CHANNEL_NAME = "Notificaciones TFG";
    private static final String CHANNEL_DESC = "Canal de notificaciones para la aplicación TFG";
    private static final int NOTIFICATION_ID = 1001;

    public static void Notification(Context context, String titulo, String mensaje) {
        crearCanal(context);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ToastHelper.info(context, "No se pueden mostrar notificaciones. Acepta los permisos en la configuración");
                return;
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.iconlogo)
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }


    private static void crearCanal(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            canal.setDescription(CHANNEL_DESC);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(canal);
            }
        }
    }
}
