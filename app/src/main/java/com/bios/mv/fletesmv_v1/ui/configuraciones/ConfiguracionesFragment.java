package com.bios.mv.fletesmv_v1.ui.configuraciones;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import com.bios.mv.fletesmv_v1.R;
import com.bios.mv.fletesmv_v1.bd.Constantes;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class ConfiguracionesFragment  extends PreferenceFragmentCompat {

    // guardo el contexto en variable global para poder usarla adentro de los listener
    Context contexto;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey){
        setPreferencesFromResource(R.xml.preferencias,rootKey);

        // obtengo la key y el valor por defecto del parámetro de configuración de días de anticipación de la notificación
        String conf_dias_noti_key = Constantes.getConf_dias_noti_key();
        String conf_dias_noti_defecto = Constantes.getConf_dias_noti_defecto();

        // Obtengo la Preference de cantidad de días de anticipación de la notificación
        final EditTextPreference conf_dias_noti = findPreference(conf_dias_noti_key);

        contexto = getContext();

        SharedPreferences defaultSharedPreference = PreferenceManager.getDefaultSharedPreferences(getContext());

        // Como es un EditText pero solo quiero ingresar números (días), utilizo el OnBindEditText para que solamente
        // pueda recibir números (aparece solo el teclado numérico en pantalla).
        conf_dias_noti.setOnBindEditTextListener(new androidx.preference.EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull EditText editText) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);

            }
        });

        // Obtengo el valor actual de días de anticipación
        String diasAnticipacion = defaultSharedPreference.getString(conf_dias_noti_key,conf_dias_noti_defecto);

        // Creo un Listener por si cambia la configuración, setearle el valor en el Summary.
        conf_dias_noti.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                preference.setSummary(contexto.getResources().getString(R.string.conf_dias_noti_summary,newValue));

                return true;
            }
        });

        // Inicializo el Summary con el valor que tiene actualmente la configuración.
        conf_dias_noti.setSummary(contexto.getResources().getString(R.string.conf_dias_noti_summary,diasAnticipacion));

    }
}
