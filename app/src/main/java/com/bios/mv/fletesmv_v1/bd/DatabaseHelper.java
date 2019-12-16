package com.bios.mv.fletesmv_v1.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context contexto) {
        super(contexto, Constantes.NOMBRE_BASE, null, Constantes.VERSION_V1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.i(Constantes.TAG_LOG, "onCreate: creando tablas");

        // Tengo una tabla para traslados con la informacion basica nomas
        String sqlCreate = "CREATE TABLE Traslados (" +
                "id INTEGER, " +
                "estado TEXT, " +
                "fecha TEXT, " +
                "origen_direccion TEXT, " +
                "origen_latitud REAL, " +
                "origen_longitud REAL)";

        db.execSQL(sqlCreate);

        // Tengo una tabla para la informacion de los traslados
        sqlCreate = "CREATE TABLE TrasladosInfo (" +
                "id INTEGER, " +
                "estado TEXT, " +
                "fecha TEXT, " +
                "origen_direccion TEXT, " +
                "origen_latitud REAL, " +
                "origen_longitud REAL, " +

                "destino_direccion TEXT, " +
                "destino_latitud REAL, " +
                "destino_longitud REAL, " +

                "vehiculo_marca TEXT, " +
                "vehiculo_modelo TEXT, " +
                "vehiculo_matricula TEXT, " +
                "vehiculo_chofer TEXT, " +

                "recepcion_nombre_receptor TEXT, " +
                "recepcion_observacion TEXT, " +
                "recepcion_fecha TEXT, " +
                "recepcion_latitud REAL, " +
                "recepcion_longitud REAL)";

        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int currentVersion, int newVersion) {
        // no tengo actualizaciones por ahora
    }
}
