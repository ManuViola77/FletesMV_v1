package com.bios.mv.fletesmv_v1.ui.transporte;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bios.mv.fletesmv_v1.Procedimientos;
import com.bios.mv.fletesmv_v1.R;
import com.bios.mv.fletesmv_v1.bd.Constantes;
import com.bios.mv.fletesmv_v1.bd.Transporte;
import com.bios.mv.fletesmv_v1.bd.converter.TransporteConverter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MapaTransporteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mapa_transporte_info;

    private Context contexto;

    private int transporteId;
    private String estado;

    private String origen_direccion;
    private double origen_latitud;
    private double origen_longitud;

    private String destino_direccion;
    private double destino_latitud;
    private double destino_longitud;

    private double latitud_actual;
    private double longitud_actual;

    private String modo;

    private FusedLocationProviderClient flpClient;

    private Marker marcador_lugar_actual;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mapa_transporte);

        contexto = this;

        // Mantengo activa la pantalla para que no tengan que estar presionando para que no se vaya.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String traslado_string = extras.getString(Constantes.extra_transporte_traslado_string);

            Transporte transporte = TransporteConverter.stringToTransporte(traslado_string);

            transporteId = transporte.getId();
            estado = transporte.getEstado();

            origen_direccion  = transporte.getOrigen_direccion();
            origen_latitud    = transporte.getOrigen_latitud();
            origen_longitud   = transporte.getOrigen_longitud();
            destino_direccion = transporte.getDestino_direccion();
            destino_latitud   = transporte.getDestino_latitud();
            destino_longitud  = transporte.getDestino_longitud();
            latitud_actual    = extras.getDouble(Constantes.extra_transporte_ultima_latitud);
            longitud_actual   = extras.getDouble(Constantes.extra_transporte_ultima_longitud);
            modo = extras.getString(Constantes.extra_transporte_modo);

            setTitle(String.format(
                    getResources().getString(R.string.title_mapa_transporte_con_id),
                    Integer.toString(transporteId),
                    estado));

            if (estado.equals(Constantes.iniciado) || estado.equals(Constantes.viajando)){
                setearGPS();
            }
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapa_traslado_info);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa_transporte_info = googleMap;

        configureMap();

        agregarOrigenDestinoAMapa();

        agregarLugarActualAMapa();
    }

    private void configureMap() {
        UiSettings uiSettings = mapa_transporte_info.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
    }

    private void agregarOrigenDestinoAMapa() {
        LatLng lugarEnMapa = new LatLng(origen_latitud, origen_longitud);

        MarkerOptions marker = new MarkerOptions();
        marker.position(lugarEnMapa);
        marker.title("Origen");
        marker.snippet(origen_direccion);
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        if (modo.equals("Origen")) {

            mapa_transporte_info.animateCamera(CameraUpdateFactory.newLatLngZoom(lugarEnMapa, 20));
            mapa_transporte_info.addMarker(marker).showInfoWindow();
        } else {
            mapa_transporte_info.addMarker(marker);
        }

        lugarEnMapa = new LatLng(destino_latitud, destino_longitud);

        marker = new MarkerOptions();
        marker.position(lugarEnMapa);
        marker.title("Destino");
        marker.snippet(destino_direccion);
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        if (modo.equals("Destino")){
            mapa_transporte_info.animateCamera(CameraUpdateFactory.newLatLngZoom(lugarEnMapa,20));
            mapa_transporte_info.addMarker(marker).showInfoWindow();
        } else {
            mapa_transporte_info.addMarker(marker);
        }
    }

    private void agregarLugarActualAMapa() {
        if (latitud_actual != 0 && longitud_actual != 0) {
            LatLng lugarEnMapa = new LatLng(latitud_actual, longitud_actual);

            MarkerOptions marker = new MarkerOptions();
            marker.position(lugarEnMapa);
            marker.title("Actual");
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.camion));

            marcador_lugar_actual = mapa_transporte_info.addMarker(marker);
        }
    }

    private void quitarLugarActualDeMapa(){
        marcador_lugar_actual.remove();
    }

    /*********************************************
     *  OBTENGO LA UBICACION GPS DEL TELEFONO
     *********************************************/

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
                Log.e(Constantes.TAG_LOG,"MapaTransporteActivity, addOnFailureListener, error: "+e.getMessage());
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
        if (flpClient != null)
            flpClient.removeLocationUpdates(locationCallback);
    }

    public LocationCallback locationCallback = new LocationCallback()  {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            //super.onLocationResult(locationResult);

            List<Location> locations = locationResult.getLocations();

            for (Location location : locations) {
                String fecha = Procedimientos.getFechaActual();

                if ((location.getLongitude() != Math.round(longitud_actual)) || (location.getLatitude() != latitud_actual)) {
                    // cambio la ubicacion entonces actualizo y mando
                    longitud_actual = location.getLongitude();
                    latitud_actual = location.getLatitude();

                    //ubicaciones.add(location);

                    quitarLugarActualDeMapa();
                    agregarLugarActualAMapa();

                    Procedimientos.InvocarServicios.mandarUbicacion(contexto,transporteId,latitud_actual,longitud_actual);
                }

                String texto = String.format("\n\n--ACTUALIZO\n\nLat:%s\nLon:%s\nFecha:%s",
                        latitud_actual,
                        longitud_actual,
                        fecha);

                Log.i(Constantes.TAG_LOG,"MapaTransporteActivity, onLocationResult: "+texto);
            }

        }
    };


    private void obtenerLocalizacion(Location location) {
        Date date = new Date(location.getTime());

        String fecha = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(date);

        longitud_actual = location.getLongitude();
        latitud_actual = location.getLatitude();

        //ubicaciones.add(location);

        String texto = String.format("\n\n--PRIMERA\n\nLat:%s\nLon:%s\nFecha:%s",
                latitud_actual,
                longitud_actual,
                fecha);

        Log.i(Constantes.TAG_LOG,"MapaTransporteActivity, obtenerLocalizacion: "+texto);
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (transporteId != 0) {
            if (estado.equals(Constantes.iniciado) || estado.equals(Constantes.viajando)) {
                pararGPS();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (transporteId != 0) {
            if (estado.equals(Constantes.iniciado) || estado.equals(Constantes.viajando)) {
                pararGPS();
            }
        }
    }

}
