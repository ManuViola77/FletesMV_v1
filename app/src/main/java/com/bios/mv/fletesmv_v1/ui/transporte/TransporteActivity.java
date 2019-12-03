package com.bios.mv.fletesmv_v1.ui.transporte;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bios.mv.fletesmv_v1.Procedimientos;
import com.bios.mv.fletesmv_v1.R;
import com.bios.mv.fletesmv_v1.bd.Constantes;
import com.bios.mv.fletesmv_v1.bd.Recepcion;
import com.bios.mv.fletesmv_v1.bd.Transporte;
import com.bios.mv.fletesmv_v1.bd.Vehiculo;
import com.bios.mv.fletesmv_v1.bd.converter.TransporteConverter;

import org.json.JSONObject;

import java.text.ParseException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class TransporteActivity extends AppCompatActivity {

    public static final String TRANSPORTE_KEY = Constantes.getTransporteKey();

    private TextView titulo;
    private TextView fecha;
    private TextView origen;
    private TextView destino;

    private TextView marca;
    private TextView modelo;
    private TextView matricula;
    private TextView chofer;

    private TextView receptor;
    private TextView observacion;
    private TextView rec_fecha;

    private CardView transporte_cv;

    private TextView txt_vehiculo;
    private CardView vehiculo_cv;

    private TextView txt_recepcion;
    private CardView recepcion_cv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transporte);

        titulo = findViewById(R.id.transporte_info_titulo);
        fecha = findViewById(R.id.transporte_info_fecha);
        origen = findViewById(R.id.transporte_info_origen_direccion);
        destino = findViewById(R.id.transporte_info_destino_direccion);

        marca = findViewById(R.id.transporte_info_vehiculo_marca);
        modelo = findViewById(R.id.transporte_info_vehiculo_modelo);
        matricula = findViewById(R.id.transporte_info_vehiculo_matricula);
        chofer = findViewById(R.id.transporte_info_vehiculo_chofer);

        receptor = findViewById(R.id.transporte_info_vehiculo_recepcion_nombre);
        observacion = findViewById(R.id.transporte_info_recepcion_observacion);
        rec_fecha = findViewById(R.id.transporte_info_recepcion_fecha);

        transporte_cv = findViewById(R.id.transporte_info_transporte_cv);

        txt_vehiculo = findViewById(R.id.transporte_info_vehiculo);
        vehiculo_cv = findViewById(R.id.transporte_info_vehiculo_cv);

        txt_recepcion = findViewById(R.id.transporte_info_recepcion);
        recepcion_cv = findViewById(R.id.transporte_info_recepcion_cv);

        String idTransporteString = getIntent().getStringExtra(TRANSPORTE_KEY);

        String URL_transporte_info = Constantes.getUrlTransportes()+"/"+idTransporteString;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest solicitud = new JsonObjectRequest(
                Request.Method.GET,
                URL_transporte_info,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        manejarRespuesta(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        manejarError(error);

                    }
                }
        );

        requestQueue.add(solicitud);
    }

    private void manejarError(VolleyError error) {
        Log.e(Constantes.getTagLog(),error.getMessage());
        Toast.makeText(this,"Error obteniendo datos del transporte",Toast.LENGTH_LONG).show();

        finish();
    }

    private void manejarRespuesta(JSONObject respuesta) {
        Transporte transporte = TransporteConverter.convertTransporte(respuesta, true);

        String fechaString;

        if (transporte != null) {
            titulo.setText(getResources().getString(R.string.transporte_info_titulo,String.valueOf(transporte.getId()),transporte.getEstado()));

            try {
                fechaString = Procedimientos.formatearFecha(transporte.getFecha());
                fecha.setText(fechaString);
            } catch (ParseException e) {
                fecha.setText(transporte.getFecha());
            }
            origen.setText(transporte.getOrigen_direccion());
            destino.setText(transporte.getDestino_direccion());

            if (transporte.getVehiculo() != null){
                Vehiculo vehiculo = transporte.getVehiculo();

                marca.setText(vehiculo.getMarca());
                modelo.setText(vehiculo.getModelo());
                matricula.setText(vehiculo.getMatricula());
                chofer.setText(vehiculo.getChofer());

                txt_vehiculo.setVisibility(View.VISIBLE);
                vehiculo_cv.setVisibility(View.VISIBLE);
            }

            if (transporte.getRecepcion() != null){
                Recepcion recepcion = transporte.getRecepcion();

                receptor.setText(recepcion.getNombre_receptor());
                observacion.setText(recepcion.getObservacion());

                try {
                    fechaString = Procedimientos.formatearFecha(recepcion.getFecha());
                    rec_fecha.setText(fechaString);
                } catch (ParseException e) {
                    rec_fecha.setText(recepcion.getFecha());
                }

                txt_recepcion.setVisibility(View.VISIBLE);
                recepcion_cv.setVisibility(View.VISIBLE);
            }

            transporte_cv.setVisibility(View.VISIBLE);
        } else
            titulo.setText("Fallo en el convertidor de Transporte, retornó NULL");
    }

}
