package com.bios.mv.fletesmv_v1;

/**
*
* Esta clase se creó para agrupar procedimientos que se usan en varios lugares, haciendo que su código esté en uno solo.
*
* */


import android.content.Context;
import android.content.SharedPreferences;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

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

       // SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        String fechaFormateada = dateFormat.format(date);

        return fechaFormateada;

    }

}
