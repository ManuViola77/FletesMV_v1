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
            Log.e(Constantes.TAG_LOG, e.getMessage());
        }

        return transportes;
    }

    public static Transporte stringToTransporte(String traslado_string) {
        JSONObject traslado_json = null;
        try {
            traslado_json = new JSONObject(traslado_string);
            Log.i(Constantes.TAG_LOG,"TransporteConverter, me quedo traslado_json: "+traslado_json);
            return convertTransporte(traslado_json,true);
        } catch (JSONException e) {
            Log.e(Constantes.TAG_LOG,"TransporteConverter, error al convertir String a JSON "+e.getMessage());
            return null;
        }
    }

    public static Transporte convertTransporte(JSONObject jsonObject, boolean isInfo) {
        Transporte transporte = new Transporte();
        try {
            transporte.setId(Integer.parseInt(jsonObject.getString("id")));
            transporte.setEstado(jsonObject.getString("estado"));
            transporte.setFecha(jsonObject.getString("fecha"));
            transporte.setOrigen_direccion(jsonObject.getString("origen_direccion").replaceAll("\n", ""));
            transporte.setOrigen_latitud(Double.valueOf(jsonObject.getString("origen_latitud")));
            transporte.setOrigen_longitud(Double.valueOf(jsonObject.getString("origen_longitud")));

            if (isInfo) {

                Log.i(Constantes.TAG_LOG,jsonObject.toString());

                transporte.setDestino_direccion(jsonObject.getString("destino_direccion").replaceAll("\n", ""));
                transporte.setDestino_latitud(Double.valueOf(jsonObject.getString("destino_latitud")));
                transporte.setDestino_longitud(Double.valueOf(jsonObject.getString("destino_longitud")));

                if ((!jsonObject.getString("vehiculo_marca").equals("null"))
                        || (!jsonObject.getString("vehiculo_modelo").equals("null"))
                        || (!jsonObject.getString("vehiculo_matricula").equals("null"))
                        || (!jsonObject.getString("vehiculo_chofer").equals("null"))) {

                    Vehiculo vehiculo = new Vehiculo();

                    if (!jsonObject.getString("vehiculo_marca").equals("null"))
                        vehiculo.setMarca(jsonObject.getString("vehiculo_marca"));

                    if (!jsonObject.getString("vehiculo_modelo").equals("null"))
                        vehiculo.setModelo(jsonObject.getString("vehiculo_modelo"));

                    if (!jsonObject.getString("vehiculo_matricula").equals("null"))
                        vehiculo.setMatricula(jsonObject.getString("vehiculo_matricula"));

                    if (!jsonObject.getString("vehiculo_chofer").equals("null"))
                        vehiculo.setChofer(jsonObject.getString("vehiculo_chofer"));

                    transporte.setVehiculo(vehiculo);
                }

                if ((!jsonObject.getString("recepcion_nombre_receptor").equals("null"))
                        || (!jsonObject.getString("recepcion_observacion").equals("null"))
                        || (!jsonObject.getString("recepcion_fecha").equals("null"))
                        || (!jsonObject.getString("recepcion_latitud").equals("null"))
                        || (!jsonObject.getString("recepcion_longitud").equals("null"))) {

                    Recepcion recepcion = new Recepcion();

                    if (!jsonObject.getString("recepcion_nombre_receptor").equals("null"))
                        recepcion.setNombre_receptor(jsonObject.getString("recepcion_nombre_receptor"));

                    if (!jsonObject.getString("recepcion_observacion").equals("null"))
                        recepcion.setObservacion(jsonObject.getString("recepcion_observacion"));

                    if (!jsonObject.getString("recepcion_fecha").equals("null"))
                        recepcion.setFecha(jsonObject.getString("recepcion_fecha"));

                    if (!jsonObject.getString("recepcion_latitud").equals("null"))
                        recepcion.setLatitud(Double.valueOf(jsonObject.getString("recepcion_latitud")));

                    if (!jsonObject.getString("recepcion_longitud").equals("null"))
                        recepcion.setLongitud(Double.valueOf(jsonObject.getString("recepcion_longitud")));

                    transporte.setRecepcion(recepcion);
                }
            }

            return transporte;
        } catch (JSONException ex) {
            Log.e(Constantes.TAG_LOG,ex.getMessage());

            return null;
        }
    }

    public static JSONObject convertTransporteToJSONOBject(Transporte transporte) {
        Map<String, String> params = new HashMap();

        params.put("id", Integer.toString(transporte.getId()));
        params.put("estado", transporte.getEstado());
        params.put("fecha", transporte.getFecha());
        params.put("origen_direccion", transporte.getOrigen_direccion());
        params.put("origen_latitud", Double.toString(transporte.getOrigen_latitud()));
        params.put("origen_longitud", Double.toString(transporte.getOrigen_longitud()));

        params.put("destino_direccion", transporte.getDestino_direccion());
        params.put("destino_latitud", Double.toString(transporte.getDestino_latitud()));
        params.put("destino_longitud", Double.toString(transporte.getDestino_longitud()));

        Vehiculo vehiculo = transporte.getVehiculo();

        if (vehiculo != null){
            params.put("vehiculo_marca", vehiculo.getMarca());
            params.put("vehiculo_modelo", vehiculo.getModelo());
            params.put("vehiculo_matricula", vehiculo.getMatricula());
            params.put("vehiculo_chofer", vehiculo.getChofer());
        } else {
            params.put("vehiculo_marca", "null");
            params.put("vehiculo_modelo", "null");
            params.put("vehiculo_matricula", "null");
            params.put("vehiculo_chofer", "null");
        }

        Recepcion recepcion = transporte.getRecepcion();

        if (recepcion != null) {
            params.put("recepcion_nombre_receptor", recepcion.getNombre_receptor());
            params.put("recepcion_observacion", recepcion.getObservacion());
            params.put("recepcion_fecha", recepcion.getFecha());
            params.put("recepcion_latitud", Double.toString(recepcion.getLatitud()));
            params.put("recepcion_longitud", Double.toString(recepcion.getLongitud()));
        } else {
            params.put("recepcion_nombre_receptor", "null");
            params.put("recepcion_observacion", "null");
            params.put("recepcion_fecha", "null");
            params.put("recepcion_latitud", "null");
            params.put("recepcion_longitud", "null");
        }

        JSONObject transporteJsonObject = new JSONObject(params);

        return transporteJsonObject;
    }
}
