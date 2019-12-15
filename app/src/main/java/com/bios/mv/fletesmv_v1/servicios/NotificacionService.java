package com.bios.mv.fletesmv_v1.servicios;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.bios.mv.fletesmv_v1.MainActivity;
import com.bios.mv.fletesmv_v1.Procedimientos;
import com.bios.mv.fletesmv_v1.R;
import com.bios.mv.fletesmv_v1.bd.Constantes;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class NotificacionService extends IntentService {

    public NotificacionService() {
        super("NotificacionService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String fecha_ultima_noti = Procedimientos.getVariableSesionString(this, Constantes.NOMBRE, Constantes.FECHA_ULTIMA_NOTI);
        String fechaAcual = Procedimientos.getFechaActualSinHora();

        // TODO: SOLO PARA PROBAR; DESPUES BORRAR!!!!!!!!!!!!
        fechaAcual = Procedimientos.getFechaActual();

        Log.i(Constantes.TAG_LOG,"fecha_ultima_noti: "+fecha_ultima_noti+" fechaAcual: "+fechaAcual);

        int comparacion = fechaAcual.compareTo(fecha_ultima_noti);
        Log.i(Constantes.TAG_LOG,"comparacion: "+comparacion);

        if (fecha_ultima_noti.isEmpty() || fechaAcual.compareTo(fecha_ultima_noti) > 0) {
            // Si estoy en una fecha superior a la ultima vez que mande notificacion
            // entonces mando notificacion de nuevo
            mandarNotificacion();
            Procedimientos.setVariableSesionString(this, Constantes.NOMBRE, Constantes.FECHA_ULTIMA_NOTI,fechaAcual);
        }

    }

    public void mandarNotificacion() {
        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                Constantes.NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, Constantes.CANAL_SERVICIO_NOTIFICACION);
        builder.setContentTitle("Notificación de Traslado Próximo");
        builder.setSmallIcon(R.mipmap.fletesmv_logo_round);
        builder.setContentText("En x días comienza el traslado y");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        Notification notification = builder.build();

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(Constantes.NOTIFICATION_ID, notification);
    }

}
