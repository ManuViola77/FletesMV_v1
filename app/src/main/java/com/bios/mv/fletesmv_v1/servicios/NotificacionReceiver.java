package com.bios.mv.fletesmv_v1.servicios;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bios.mv.fletesmv_v1.MainActivity;
import com.bios.mv.fletesmv_v1.Procedimientos;
import com.bios.mv.fletesmv_v1.R;
import com.bios.mv.fletesmv_v1.bd.Constantes;
import com.bios.mv.fletesmv_v1.bd.Transporte;
import com.bios.mv.fletesmv_v1.bd.converter.TransporteConverter;

import org.json.JSONArray;

import java.util.List;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

public class NotificacionReceiver extends BroadcastReceiver {

    private RequestQueue requestQueue;
    private Context contexto;
    private List<Transporte> transportes;

    @Override
    public void onReceive(Context context, Intent intent) {
        contexto = context;
        buscarSiMandoNotificacion();
    }

    public void buscarSiMandoNotificacion() {
        // Obtengo los traslados y me fijo si por la fecha corresponde mandar notificacion o no

        // Inicializo cola de solicitudes
        requestQueue = Volley.newRequestQueue(contexto);

        // Crear solicitud
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                Constantes.URL_TRANSPORTES,
                null, // codigo para datos del post
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        manejarRespuesta(response);
                    } // codigo de success
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        manejarError(error);
                    } // codigo de error
                }
        );

        requestQueue.add(request);
    }

    private void manejarRespuesta(JSONArray respuesta) {
        transportes = TransporteConverter.convertFromJsonObject(respuesta);

        // Obtengo el valor actual de días de anticipación para mandar notificacion
        SharedPreferences defaultSharedPreference = PreferenceManager.getDefaultSharedPreferences(contexto);
        String diasAnticipacion = defaultSharedPreference.getString(Constantes.conf_dias_noti_key,Constantes.conf_dias_noti_defecto);

        for (Transporte traslado : transportes) {

            String fechaString = traslado.getFecha();

            boolean isTrasladoParaNotificar = Procedimientos.correspondeMandarNotificacion(fechaString, diasAnticipacion);

            if (isTrasladoParaNotificar){
                mandarNotificacion(diasAnticipacion, traslado);
            }
        }
    }

    private void manejarError(VolleyError volleyError) {
        Toast.makeText(contexto,
                "No se pudo obtener los datos de los transportes, "+volleyError.getMessage(),
                Toast.LENGTH_LONG).show();
    }

    public void mandarNotificacion(String diasAnticipacion, Transporte traslado) {
        Intent intent = new Intent(contexto, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                contexto,
                Constantes.NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(contexto, Constantes.CANAL_SERVICIO_NOTIFICACION);
        builder.setContentTitle("Notificación de Traslado Próximo");
        builder.setSmallIcon(R.mipmap.fletesmv_logo_round);
        builder.setLargeIcon(BitmapFactory.decodeResource(contexto.getResources(), R.mipmap.fletesmv_logo_round));
        builder.setContentText("En "+diasAnticipacion+" días comienza el traslado "+traslado.getId());
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        Notification notification = builder.build();

        NotificationManager manager = (NotificationManager) contexto.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify((int) System.currentTimeMillis(), notification);
    }
}