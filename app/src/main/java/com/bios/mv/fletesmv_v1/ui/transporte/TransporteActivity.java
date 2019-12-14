package com.bios.mv.fletesmv_v1.ui.transporte;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.location.Location;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class TransporteActivity extends AppCompatActivity {

    private Context contexto;

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

    private String idTransporteString;

    private FusedLocationProviderClient flpClient;

    private double ultimaLongitud = 0;
    private double ultimaLatitud  = 0;

    private List<Location> ubicaciones = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transporte);

        contexto = this;

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
                        finalizarTraslado(view);
                        break;

                    case Constantes.finalizado:
                        // En este estado no pasa nada. El botón ni siquiera está visible.
                        break;
                }
            }
        });

    }

    private void finalizarTraslado(View view) {
        Intent intent = new Intent(view.getContext(), FinalizarTrasladoActivity.class);
        intent.putExtra(Constantes.extra_finalizar_traslado,transporte.getId());
        startActivity(intent);
    }

    private void iniciarTraslado(View view){
        Intent intent = new Intent(view.getContext(), IniciarTrasladoActivity.class);
        intent.putExtra(Constantes.extra_iniciar_traslado,transporte.getId());
        startActivity(intent);
    }

    private void cambiarEstadoTraslado(View view, String estado){
        String URL_transporte_cambiar_estado = Constantes.URL_TRANSPORTES+"/"+transporte.getId();

        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());

        Map<String, String> params = new HashMap();
        params.put("estado", estado);
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest solicitud = new JsonObjectRequest(
                Request.Method.POST,
                URL_transporte_cambiar_estado,
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
        Log.e(Constantes.TAG_LOG,"error cambiando de estado el traslado "+error.getMessage(),error);
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
        Log.e(Constantes.TAG_LOG,"error: "+error.getMessage());
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

            personalizacionSegunEstado();

        } else
            titulo.setText("Fallo en el convertidor de Transporte, retornó NULL");
    }

    private void personalizacionSegunEstado(){

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

                setearGPS();
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

                setearGPS();
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

    }

    private void setearGPS() {
        /****************************************
         * Obtener localización GPS del telefono
         ****************************************/

        flpClient = LocationServices.getFusedLocationProviderClient(this);

        Task<Location> lastLocationTask = flpClient.getLastLocation();

        lastLocationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    obtenerLocalizacion(location);
                } else {

                }
            }
        });

        lastLocationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(Constantes.TAG_LOG,"error: "+e.getMessage());
            }
        });

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        flpClient.requestLocationUpdates(locationRequest, locationCallback, null);

        /************************************************
         * Fin de Obtener localización GPS del telefono
         ************************************************/
    }

    private void pararGPS() {
        flpClient.removeLocationUpdates(locationCallback);
    }


    public void abrirMapaEnOrigen(View view) {
        abrirMapa("Origen");

    }

    public void abrirMapaEnDestino(View view) {
        abrirMapa("Destino");
    }

    private void abrirMapa(String modo) {
        Intent intent = new Intent(this, MapaTransporteActivity.class);
        intent.putExtra(Constantes.extra_transporte_origen_latitud,transporte.getOrigen_latitud());
        intent.putExtra(Constantes.extra_transporte_origen_longitud,transporte.getOrigen_longitud());
        intent.putExtra(Constantes.extra_transporte_destino_latitud,transporte.getDestino_latitud());
        intent.putExtra(Constantes.extra_transporte_destino_longitud,transporte.getDestino_longitud());
        intent.putExtra(Constantes.extra_transporte_ultima_latitud,ultimaLatitud);
        intent.putExtra(Constantes.extra_transporte_ultima_longitud,ultimaLongitud);
        intent.putExtra(Constantes.extra_transporte_modo,modo);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestTraslado();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (transporte != null) {
            if (transporte.getEstado().equals(Constantes.iniciado) || transporte.getEstado().equals(Constantes.viajando)) {
                pararGPS();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (transporte.getEstado().equals(Constantes.iniciado) || transporte.getEstado().equals(Constantes.viajando)) {
            pararGPS();
        }
    }

    public LocationCallback locationCallback = new LocationCallback()  {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            //super.onLocationResult(locationResult);

            List<Location> locations = locationResult.getLocations();

            for (Location location : locations) {
                String fecha = Procedimientos.getFechaActual();

                if ((location.getLongitude() != Math.round(ultimaLongitud)) || (location.getLatitude() != ultimaLatitud)) {
                    // cambio la ubicacion entonces actualizo y mando
                    ultimaLongitud = location.getLongitude();
                    ultimaLatitud = location.getLatitude();

                    ubicaciones.add(location);

                    Procedimientos.InvocarServicios.mandarUbicacion(contexto,transporte.getId(),ultimaLatitud,ultimaLongitud);
                }

                String texto = String.format("\n\n--ACTUALIZO\n\nLat:%s\nLon:%s\nFecha:%s",
                        ultimaLatitud,
                        ultimaLongitud,
                        fecha);

                Log.i(Constantes.TAG_LOG,"En onLocationResult: "+texto);
            }

        }
    };


    private void obtenerLocalizacion(Location location) {
        Date date = new Date(location.getTime());

        String fecha = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(date);

        ultimaLongitud = location.getLongitude();
        ultimaLatitud = location.getLatitude();

        ubicaciones.add(location);

        String texto = String.format("\n\n--KNOWN\n\nLat:%s\nLon:%s\nFecha:%s",
                ultimaLatitud,
                ultimaLongitud,
                fecha);

        Log.i(Constantes.TAG_LOG,"En obtenerLocalizacion: "+texto);
    }

}
