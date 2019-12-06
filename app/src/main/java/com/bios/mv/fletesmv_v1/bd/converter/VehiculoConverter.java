package com.bios.mv.fletesmv_v1.bd.converter;

import android.util.Log;

import com.bios.mv.fletesmv_v1.bd.Constantes;
import com.bios.mv.fletesmv_v1.bd.Vehiculo;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VehiculoConverter {

    public static JSONObject convertVehiculoToJSONOBject(Vehiculo vehiculo) {
        Map<String, String> params = new HashMap();
        params.put("estado", "iniciado");
        params.put("vehiculo_marca", vehiculo.getMarca());
        params.put("vehiculo_modelo", vehiculo.getModelo());
        params.put("vehiculo_matricula", vehiculo.getMatricula());
        params.put("vehiculo_chofer", vehiculo.getChofer());

        JSONObject vehiculoJsonObject = new JSONObject(params);

        return vehiculoJsonObject;
    }
}
