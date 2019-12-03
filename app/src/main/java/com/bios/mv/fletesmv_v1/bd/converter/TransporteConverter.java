package com.bios.mv.fletesmv_v1.bd.converter;

import android.util.Log;

import com.bios.mv.fletesmv_v1.bd.Constantes;
import com.bios.mv.fletesmv_v1.bd.Recepcion;
import com.bios.mv.fletesmv_v1.bd.Transporte;
import com.bios.mv.fletesmv_v1.bd.Vehiculo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransporteConverter {
    public static List<Transporte> convertFromJsonObject(JSONArray resultados) {

        List<Transporte> transportes = new ArrayList<>();

        try {
            for (int i = 0; i < resultados.length(); i++) {
                JSONObject result = (JSONObject) resultados.get(i);
                Transporte transporte = convertTransporte(result, false);
                transportes.add(transporte);
            }
        } catch (JSONException e) {
            Log.e(Constantes.getTagLog(), e.getMessage());
        }

        return transportes;
    }

    public static Transporte convertTransporte(JSONObject jsonObject, boolean isInfo) {
        Transporte transporte = new Transporte();
        try {
            transporte.setId(Integer.parseInt(jsonObject.getString("id")));
            transporte.setEstado(jsonObject.getString("estado"));
            transporte.setFecha(jsonObject.getString("fecha"));
            transporte.setOrigen_direccion(jsonObject.getString("origen_direccion"));
            transporte.setOrigen_latitud(Double.valueOf(jsonObject.getString("origen_latitud")));
            transporte.setOrigen_longitud(Double.valueOf(jsonObject.getString("origen_longitud")));

            if (isInfo) {

                Log.i(Constantes.getTagLog(),jsonObject.toString());

                transporte.setDestino_direccion(jsonObject.getString("destino_direccion"));
                transporte.setDestino_latitud(Double.valueOf(jsonObject.getString("destino_latitud")));
                transporte.setDestino_longitud(Double.valueOf(jsonObject.getString("destino_longitud")));

                if ((jsonObject.getString("vehiculo_marca") != "null")
                        || (jsonObject.getString("vehiculo_modelo") != "null")
                        || (jsonObject.getString("vehiculo_matricula") != "null")
                        || (jsonObject.getString("vehiculo_chofer") != "null")) {

                    Vehiculo vehiculo = new Vehiculo();

                    if (jsonObject.getString("vehiculo_marca") != "null")
                        vehiculo.setMarca(jsonObject.getString("vehiculo_marca"));

                    if (jsonObject.getString("vehiculo_modelo") != "null")
                        vehiculo.setModelo(jsonObject.getString("vehiculo_modelo"));

                    if (jsonObject.getString("vehiculo_matricula") != "null")
                        vehiculo.setMatricula(jsonObject.getString("vehiculo_matricula"));

                    if (jsonObject.getString("vehiculo_chofer") != "null")
                        vehiculo.setChofer(jsonObject.getString("vehiculo_chofer"));

                    transporte.setVehiculo(vehiculo);
                }

                if ((jsonObject.getString("recepcion_nombre_receptor") != "null")
                        || (jsonObject.getString("recepcion_observacion") != "null")
                        || (jsonObject.getString("recepcion_fecha") != "null")
                        || (jsonObject.getString("recepcion_latitud") != "null")
                        || (jsonObject.getString("recepcion_longitud") != "null")) {

                    Recepcion recepcion = new Recepcion();

                    if (jsonObject.getString("recepcion_nombre_receptor") != "null")
                        recepcion.setNombre_receptor(jsonObject.getString("recepcion_nombre_receptor"));

                    if (jsonObject.getString("recepcion_observacion") != "null")
                        recepcion.setObservacion(jsonObject.getString("recepcion_observacion"));

                    if (jsonObject.getString("recepcion_fecha") != "null")
                        recepcion.setFecha(jsonObject.getString("recepcion_fecha"));

                    if (jsonObject.getString("recepcion_latitud") != "null")
                        recepcion.setLatitud(Double.valueOf(jsonObject.getString("recepcion_latitud")));

                    if (jsonObject.getString("recepcion_longitud") != "null")
                        recepcion.setLongitud(Double.valueOf(jsonObject.getString("recepcion_longitud")));

                    transporte.setRecepcion(recepcion);
                }
            }

            // DESPUES BORRAR, AHORA DEJO PARA PROBAR LOS DISTINTOS ESTADOS
            switch (transporte.getId()) {
                case 47:
                    transporte.setEstado("iniciado");
                    break;

                case 9:
                    transporte.setEstado("cargando");
                    break;

                case 35:
                    transporte.setEstado("viajando");
                    break;

                case 32:
                    transporte.setEstado("descargado");
                    break;

                case 23:
                    transporte.setEstado("finalizado");
                    break;
            }

            return transporte;
        } catch (JSONException ex) {
            Log.e(Constantes.getTagLog(),ex.getMessage());

            return null;
        }
    }

    /*public static JSONObject convertTransporteToJSONOBject(Transporte transporte) {
        Map<String, String> params = new HashMap();

        params.put("id", Integer.toString(transporte.getId()));
        params.put("estado", transporte.getEstado());
        params.put("fecha", transporte.getFecha());
        params.put("origen_direccion", transporte.getOrigen_direccion());
        params.put("origen_latitud", Integer.toString(transporte.getOrigen_latitud()));
        params.put("origen_longitud", Integer.toString(transporte.getOrigen_longitud()));

        JSONObject transporteJsonObject = new JSONObject(params);

        return transporteJsonObject;
    }*/
}
