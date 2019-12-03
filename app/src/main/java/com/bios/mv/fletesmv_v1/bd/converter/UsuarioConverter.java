package com.bios.mv.fletesmv_v1.bd.converter;

import android.util.Log;

import com.bios.mv.fletesmv_v1.bd.Constantes;
import com.bios.mv.fletesmv_v1.bd.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioConverter {

    public static List<Usuario> convertFromJsonObject(JSONObject object) {

        List<Usuario> usuarios = new ArrayList<>();

        try {
            JSONArray resultados = object.getJSONArray("results");

            for (int i = 0; i < resultados.length(); i++) {
                JSONObject result = (JSONObject) resultados.get(i);
                Usuario usuario = convertUsuario(result);
                usuarios.add(usuario);
            }
        } catch (JSONException e) {
            Log.e(Constantes.getTagLog(), e.getMessage());
        }

        return usuarios;
    }

    public static Usuario convertUsuario(JSONObject jsonObject) {
        Usuario usuario = new Usuario();
        try {
            usuario.setName(jsonObject.getString("name"));
            //usuario.setLast_name(jsonObject.getString("last_name"));
            usuario.setEmail(jsonObject.getString("email"));
            //usuario.setPassword(jsonObject.getString("password"));

            return usuario;
        } catch (JSONException ex) {
            Log.e(Constantes.getTagLog(),ex.getMessage());

            return null;
        }
    }

    public static JSONObject convertUsuarioToJSONOBject(Usuario usuario) {
        Map<String, String> params = new HashMap();
        if (usuario.getName() != null) {
            params.put("name", usuario.getName()+" "+usuario.getLast_name());
        }
        params.put("email", usuario.getEmail());
        params.put("password", usuario.getPassword());

        JSONObject usuarioJsonObject = new JSONObject(params);

        return usuarioJsonObject;
    }

}
