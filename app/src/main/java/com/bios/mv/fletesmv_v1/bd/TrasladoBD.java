package com.bios.mv.fletesmv_v1.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TrasladoBD {

    private Context context;

    public TrasladoBD(Context context) {
        this.context = context;
    }

    public List<Transporte> getTraslados() {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String[] columnas = new String[] {
                "id",
                "estado",
                "fecha",
                "origen_direccion",
                "origen_latitud",
                "origen_longitud"};

        Cursor cursor = database.query(Constantes.NOMBRE_TABLA_TRASLADOS,
                columnas,
                null,
                null,
                null,
                null,
                null);

        List<Transporte> transportes = new ArrayList<>();

        while (cursor.moveToNext()) {

            Transporte transporte = new Transporte();
            transporte.setId(cursor.getInt(0));
            transporte.setEstado(cursor.getString(1));
            transporte.setFecha(cursor.getString(2));
            transporte.setOrigen_direccion(cursor.getString(3));
            transporte.setOrigen_latitud(cursor.getDouble(4));
            transporte.setOrigen_longitud(cursor.getDouble(5));

            transportes.add(transporte);
        }

        cursor.close();
        database.close();
        databaseHelper.close();

        return transportes;
    }

    public void setTraslados(List<Transporte> traslados) {
        // primero borro todos los taslados que habian
        deleteTraslados();

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        // despues lleno todos de nuevo
        for (Transporte transporte : traslados) {
            ContentValues values = new ContentValues();
            values.put("id", transporte.getId());
            values.put("estado", transporte.getEstado());
            values.put("fecha", transporte.getFecha());
            values.put("origen_direccion", transporte.getOrigen_direccion());
            values.put("origen_latitud", transporte.getOrigen_latitud());
            values.put("origen_longitud", transporte.getOrigen_longitud());

            database.insert(Constantes.NOMBRE_TABLA_TRASLADOS, null, values);
        }

        database.close();
        databaseHelper.close();
    }

    public void deleteTraslados() {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        database.execSQL("delete from "+ Constantes.NOMBRE_TABLA_TRASLADOS);

        database.close();
        databaseHelper.close();
    }
}
