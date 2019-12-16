package com.bios.mv.fletesmv_v1.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TrasladoInfoBD {

    private Context context;

    public TrasladoInfoBD(Context context) {
        this.context = context;
    }

    public List<Transporte> getTrasladosInfo() {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String[] columnas = new String[] {
                "id",
                "estado",
                "fecha",
                "origen_direccion",
                "origen_latitud",
                "origen_longitud",

                "destino_direccion",
                "destino_latitud",
                "destino_longitud",

                "vehiculo_marca",
                "vehiculo_modelo",
                "vehiculo_matricula",
                "vehiculo_chofer",

                "recepcion_nombre_receptor",
                "recepcion_observacion",
                "recepcion_fecha",
                "recepcion_latitud",
                "recepcion_longitud"};

        Cursor cursor = database.query(Constantes.NOMBRE_TABLA_TRASLADOS_INFO,
                columnas,
                null,
                null,
                null,
                null,
                null);

        List<Transporte> transportes = new ArrayList<>();
        Transporte transporte = null;

        while (cursor.moveToNext()) {
            transporte = getTrasladoByCursor(cursor);
            transportes.add(transporte);
        }

        cursor.close();
        database.close();
        databaseHelper.close();

        return transportes;
    }

    public Transporte getTrasladoInfo(int id) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String[] columnas = new String[] {
                "id",
                "estado",
                "fecha",
                "origen_direccion",
                "origen_latitud",
                "origen_longitud",

                "destino_direccion",
                "destino_latitud",
                "destino_longitud",

                "vehiculo_marca",
                "vehiculo_modelo",
                "vehiculo_matricula",
                "vehiculo_chofer",

                "recepcion_nombre_receptor",
                "recepcion_observacion",
                "recepcion_fecha",
                "recepcion_latitud",
                "recepcion_longitud"};

        String[] argumentos = new String[]{String.valueOf(id)};

        Cursor cursor = database.query(Constantes.NOMBRE_TABLA_TRASLADOS_INFO,
                columnas,
                "id = ?",
                argumentos,
                null,
                null,
                null);

        Transporte transporte = null;

        if (cursor.moveToFirst()) {
            transporte = getTrasladoByCursor(cursor);
        }

        cursor.close();
        database.close();
        databaseHelper.close();

        return transporte;
    }

    public Transporte getTrasladoByCursor(Cursor cursor) {
        Transporte transporte = new Transporte();
        transporte.setId(cursor.getInt(0));
        transporte.setEstado(cursor.getString(1));
        transporte.setFecha(cursor.getString(2));
        transporte.setOrigen_direccion(cursor.getString(3));
        transporte.setOrigen_latitud(cursor.getDouble(4));
        transporte.setOrigen_longitud(cursor.getDouble(5));

        transporte.setDestino_direccion(cursor.getString(6));
        transporte.setDestino_latitud(cursor.getDouble(7));
        transporte.setDestino_longitud(cursor.getDouble(8));

        // si esta pendiente no tiene vehiculo asociado aun
        if (!transporte.getEstado().equals(Constantes.pendiente)) {
            Vehiculo vehiculo = new Vehiculo();
            vehiculo.setMarca(cursor.getString(9));
            vehiculo.setModelo(cursor.getString(10));
            vehiculo.setMatricula(cursor.getString(11));
            vehiculo.setChofer(cursor.getString(12));

            transporte.setVehiculo(vehiculo);
        }

        // Solo si esta finalizado tiene recepcion seteada
        if (transporte.getEstado().equals(Constantes.finalizado)) {
            Recepcion recepcion = new Recepcion();
            recepcion.setNombre_receptor(cursor.getString(13));
            recepcion.setObservacion(cursor.getString(14));
            recepcion.setFecha(cursor.getString(15));
            recepcion.setLatitud(cursor.getDouble(16));
            recepcion.setLongitud(cursor.getDouble(17));

            transporte.setRecepcion(recepcion);
        }
        return transporte;
    }

    public void setTrasladosInfo(Transporte transporte) {
        // primero borro la informacion del taslado actual
        deleteTrasladoInfo(transporte);

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", transporte.getId());
        values.put("estado", transporte.getEstado());
        values.put("fecha", transporte.getFecha());
        values.put("origen_direccion", transporte.getOrigen_direccion());
        values.put("origen_latitud", transporte.getOrigen_latitud());
        values.put("origen_longitud", transporte.getOrigen_longitud());

        values.put("destino_direccion", transporte.getDestino_direccion());
        values.put("destino_latitud", transporte.getDestino_latitud());
        values.put("destino_longitud", transporte.getDestino_longitud());

        Vehiculo vehiculo = transporte.getVehiculo();

        if (vehiculo != null) {
            values.put("vehiculo_marca", vehiculo.getMarca());
            values.put("vehiculo_modelo", vehiculo.getModelo());
            values.put("vehiculo_matricula", vehiculo.getMatricula());
            values.put("vehiculo_chofer", vehiculo.getChofer());
        }

        Recepcion recepcion = transporte.getRecepcion();

        if (recepcion != null) {
            values.put("recepcion_nombre_receptor", recepcion.getNombre_receptor());
            values.put("recepcion_observacion", recepcion.getObservacion());
            values.put("recepcion_fecha", recepcion.getFecha());
            values.put("recepcion_latitud", recepcion.getLatitud());
            values.put("recepcion_longitud", recepcion.getLongitud());
        }

        database.insert(Constantes.NOMBRE_TABLA_TRASLADOS_INFO, null, values);

        database.close();
        databaseHelper.close();
    }

    public void deleteTrasladoInfo(Transporte traslado) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        database.execSQL("delete from " +
                         Constantes.NOMBRE_TABLA_TRASLADOS_INFO +
                         " where id = " +
                         traslado.getId());

        database.close();
        databaseHelper.close();
    }

}
