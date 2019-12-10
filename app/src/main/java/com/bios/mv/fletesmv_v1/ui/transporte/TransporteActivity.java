package com.bios.mv.fletesmv_v1.ui.transporte;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class TransporteActivity extends AppCompatActivity {

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

    private Button boton_iniciar;

    private Transporte transporte;

    private String extra_transporte_origen_latitud = Constantes.extra_transporte_origen_latitud;
    private String extra_transporte_origen_longitud = Constantes.extra_transporte_origen_longitud;
    private String extra_transporte_destino_latitud = Constantes.extra_transporte_destino_latitud;
    private String extra_transporte_destino_longitud = Constantes.extra_transporte_destino_longitud;
    private String extra_transporte_modo = Constantes.extra_transporte_modo;

    private String extra_iniciar_traslado = Constantes.extra_iniciar_traslado;

    private String idTransporteString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transporte);

        // Mantengo activa la pantalla para que no tengan que estar presionando para que no se vaya.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        titulo = findViewById(R.id.transporte_info_titulo);
        fecha = findViewById(R.id.transporte_info_fecha);
        origen = findViewById(R.id.transporte_info_origen_direccion);
        destino = findViewById(R.id.transporte_info_destino_direccion);

        // Subrayo origen y destino para que se sepa que son clickeables
        origen.setPaintFlags(origen.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        destino.setPaintFlags(destino.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        marca = findViewById(R.id.transporte_info_vehiculo_marca);
        modelo = findViewById(R.id.transporte_info_vehiculo_modelo);
        matricula = findViewById(R.id.transporte_info_vehiculo_matricula);
        chofer = findViewById(R.id.transporte_info_vehiculo_chofer);

        receptor = findViewById(R.id.transporte_info_vehiculo_recepcion_nombre);
        observacion = findViewById(R.id.transporte_info_recepcion_observacion);
        rec_fecha = findViewById(R.id.transporte_info_recepcion_fecha);

        boton_iniciar = findViewById(R.id.transporte_info_boton_iniciar);

        transporte_cv = findViewById(R.id.transporte_info_transporte_cv);

        txt_vehiculo = findViewById(R.id.transporte_info_vehiculo);
        vehiculo_cv = findViewById(R.id.transporte_info_vehiculo_cv);

        txt_recepcion = findViewById(R.id.transporte_info_recepcion);
        recepcion_cv = findViewById(R.id.transporte_info_recepcion_cv);

        idTransporteString = getIntent().getStringExtra(Constantes.TRANSPORTE_KEY);

        boton_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boton_iniciar.setEnabled(false);
                String estado = transporte.getEstado();
                String estadoNuevo;
                switch (estado) {
                    case Constantes.pendiente:
                        // Paso de Pendiente a Iniciado pero paso por el pedido de los datos del vehiculo
                        iniciarTraslado(view);
                        break;

                    case Constantes.iniciado:
                        // Paso de iniciado a cargando
                        estadoNuevo = "cargando";
                        cambiarEstadoTraslado(view, estadoNuevo);
                        break;

                    case Constantes.cargando:
                        // Paso de cargando a viajando
                        estadoNuevo = "viajando";
                        cambiarEstadoTraslado(view, estadoNuevo);
                        break;

                    case Constantes.viajando:
                        // Paso de viajando a descargando
                        estadoNuevo = "descargando";
                        cambiarEstadoTraslado(view, estadoNuevo);
                        break;

                    case Constantes.descargando:
                        // Paso de descargando a finalizado pero paso por el pedido de los datos de la recepcion
                        estadoNuevo = "finalizado";
                        cambiarEstadoTraslado(view, estadoNuevo);
                        break;

                    case Constantes.finalizado:
                        // En este estado no pasa nada. El botón ni siquiera está visible.
                        break;
                }
            }
        });
    }

    private void iniciarTraslado(View view){
        Intent intent = new Intent(view.getContext(), IniciarTrasladoActivity.class);
        intent.putExtra(extra_iniciar_traslado,transporte.getId());
        startActivity(intent);
    }

    private void cambiarEstadoTraslado(View view, String estado){
        String URL_transporte_info = Constantes.URL_TRANSPORTES+"/"+transporte.getId();

        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());

        Map<String, String> params = new HashMap();
        params.put("estado", estado);
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest solicitud = new JsonObjectRequest(
                Request.Method.POST,
                URL_transporte_info,
                parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        manejarRespuestaCambiarEstado(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        manejarErrorCambiarEstado(error);
                    }
                }
        );

        requestQueue.add(solicitud);
    }

    private void manejarErrorCambiarEstado(VolleyError error) {
        Log.e(Constantes.TAG_LOG,"error "+error.getMessage(),error);
        Toast.makeText(this,"Error cambiando de estado el traslado",Toast.LENGTH_LONG).show();
    }

    private void manejarRespuestaCambiarEstado(JSONObject respuesta) {
        requestTraslado();
    }

    private void requestTraslado() {

        String URL_transporte_info = Constantes.URL_TRANSPORTES+"/"+idTransporteString;

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
        Log.e(Constantes.TAG_LOG,error.getMessage());
        Toast.makeText(this,"Error obteniendo datos del traslado",Toast.LENGTH_LONG).show();

        finish();
    }

    private void manejarRespuesta(JSONObject respuesta) {
        transporte = TransporteConverter.convertTransporte(respuesta, true);

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

            switch (transporte.getEstado()) {
                case Constantes.pendiente:
                    boton_iniciar.setVisibility(View.VISIBLE);
                    boton_iniciar.setText(getResources().getString(R.string.transporte_info_boton_iniciar));
                    boton_iniciar.setEnabled(true);
                    break;

                case Constantes.iniciado:
                    boton_iniciar.setVisibility(View.VISIBLE);
                    boton_iniciar.setText(getResources().getString(R.string.transporte_info_boton_cargar));
                    boton_iniciar.setEnabled(true);
                    break;

                case Constantes.cargando:
                    boton_iniciar.setVisibility(View.VISIBLE);
                    boton_iniciar.setText(getResources().getString(R.string.transporte_info_boton_viajar));
                    boton_iniciar.setEnabled(true);
                    break;

                case Constantes.viajando:
                    boton_iniciar.setVisibility(View.VISIBLE);
                    boton_iniciar.setText(getResources().getString(R.string.transporte_info_boton_descargar));
                    boton_iniciar.setEnabled(true);
                    break;

                case Constantes.descargando:
                    boton_iniciar.setVisibility(View.VISIBLE);
                    boton_iniciar.setText(getResources().getString(R.string.transporte_info_boton_finalizar));
                    boton_iniciar.setEnabled(true);
                    break;

                case Constantes.finalizado:
                    boton_iniciar.setVisibility(View.GONE);
                    break;
            }

        } else
            titulo.setText("Fallo en el convertidor de Transporte, retornó NULL");
    }


    public void abrirMapaEnOrigen(View view) {
        abrirMapa("Origen");

    }

    public void abrirMapaEnDestino(View view) {
        abrirMapa("Destino");
    }

    private void abrirMapa(String modo) {
        Intent intent = new Intent(this, MapaTransporteActivity.class);
        intent.putExtra(extra_transporte_origen_latitud,transporte.getOrigen_latitud());
        intent.putExtra(extra_transporte_origen_longitud,transporte.getOrigen_longitud());
        intent.putExtra(extra_transporte_destino_latitud,transporte.getDestino_latitud());
        intent.putExtra(extra_transporte_destino_longitud,transporte.getDestino_longitud());
        intent.putExtra(extra_transporte_modo,modo);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestTraslado();
    }
}
