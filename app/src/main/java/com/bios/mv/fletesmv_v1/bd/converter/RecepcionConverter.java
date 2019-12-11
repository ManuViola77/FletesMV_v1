package com.bios.mv.fletesmv_v1.bd.converter;

import com.bios.mv.fletesmv_v1.bd.Constantes;
import com.bios.mv.fletesmv_v1.bd.Recepcion;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RecepcionConverter {

    public static JSONObject convertRecepcionToJSONOBject(Recepcion recepcion) {

        Map<String, String> params = new HashMap();
        params.put("estado", Constantes.finalizado);
        params.put("recepcion_nombre_receptor", recepcion.getNombre_receptor());
        params.put("recepcion_observacion", recepcion.getObservacion());
        params.put("recepcion_fecha", recepcion.getFecha());
        params.put("recepcion_latitud", Double.toString(recepcion.getLatitud()));
        params.put("recepcion_longitud", Double.toString(recepcion.getLongitud()));

        JSONObject vehiculoJsonObject = new JSONObject(params);

        return vehiculoJsonObject;

    }
}
