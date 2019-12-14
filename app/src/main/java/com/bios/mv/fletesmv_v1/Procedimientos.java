package com.bios.mv.fletesmv_v1;

/**
*
* Esta clase se creó para agrupar procedimientos que se usan en varios lugares, haciendo que su código esté en uno solo.
*
* */


import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bios.mv.fletesmv_v1.bd.Constantes;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;

public class Procedimientos {

    // Se crea procedimiento para setear variables de sesión de tipo String
    public static void setVariableSesionString(Context contexto, String nombre, String codigo, String valor) {
        SharedPreferences preferences = contexto.getSharedPreferences(nombre, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(codigo, valor);
        editor.apply();
    }

    // Se crea procedimiento para obtener el valor de variables de sesión de tipo String
    public static String getVariableSesionString(Context contexto, String nombre, String codigo){
        SharedPreferences preferences = contexto.getSharedPreferences(nombre, Context.MODE_PRIVATE);

        return preferences.getString(codigo,"");
    }

    public static String formatearFecha(String fechaSinFormatear) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = format.parse ( fechaSinFormatear );


        Locale locale = new Locale("spa", "URY");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);

        String fechaFormateada = dateFormat.format(date);

        return fechaFormateada;

    }

    public static String getFechaActual() {

        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date());

    }
    public void prueba() {

    }

    public static class InvocarServicios {

        private static Context contexto;
        private static int transporteId;
        private static double latitud;
        private static double longitud;

        /*******************************************************************************
         *
         *                      INICIO SERVICIO MANDAR UBICACION
         *
         *      mandarUbicacion(contexto, transporteId, latitud, longitud)
         *
         *      Invoca la API para mandar la ubicacion actual.
         *
         *      Se invoca en los estados:   iniciado
         *                                  viajando
         *
         *******************************************************************************/
        public static void mandarUbicacion(Context param_contexto, int transporteId, double param_latitud, double param_longitud) {

            contexto = param_contexto;
            latitud = param_latitud;
            longitud = param_longitud;

            String URL_transporte_gps = Constantes.URL_GPS + "/" + transporteId + "/location";

            RequestQueue requestQueue = Volley.newRequestQueue(contexto);

            Map<String, String> params = new HashMap();
            params.put("latitud", Double.toString(latitud));
            params.put("longitud", Double.toString(longitud));
            JSONObject parameters = new JSONObject(params);

            Log.i(Constantes.TAG_LOG, "mandando mi ubicacion actual con params: " + parameters);
            Log.i(Constantes.TAG_LOG, "URL_transporte_gps: " + URL_transporte_gps);

            JsonObjectRequest solicitud = new JsonObjectRequest(
                    Request.Method.POST,
                    URL_transporte_gps,
                    parameters,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            manejarRespuestaMandarUbicacion(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            manejarErrorMandarUbicacion(error);
                        }
                    }
            );

            requestQueue.add(solicitud);
        }

        private static void manejarErrorMandarUbicacion(VolleyError error) {
            Log.e(Constantes.TAG_LOG, "error mandando ubicacion: " + error.getMessage());
            Toast.makeText(contexto, "Error mandando ubicación", Toast.LENGTH_LONG).show();
        }

        private static void manejarRespuestaMandarUbicacion(JSONObject response) {
            Log.i(Constantes.TAG_LOG, "Usted está en: " + latitud + ": " + longitud);
        }

        /*******************************************************************************
         *
         *                      FIN SERVICIO MANDAR UBICACION
         *
         ********************************************************************************/

    }
}
