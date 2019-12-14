package com.bios.mv.fletesmv_v1.ui.transporte;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.bios.mv.fletesmv_v1.R;
import com.bios.mv.fletesmv_v1.bd.Constantes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaTransporteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mapa_transporte_info;

    private double origen_latitud;
    private double origen_longitud;
    private double destino_latitud;
    private double destino_longitud;
    private double latitud_actual;
    private double longitud_actual;
    private String modo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mapa_transporte);

        // Mantengo activa la pantalla para que no tengan que estar presionando para que no se vaya.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            origen_latitud   = extras.getDouble(Constantes.extra_transporte_origen_latitud);
            origen_longitud  = extras.getDouble(Constantes.extra_transporte_origen_longitud);
            destino_latitud  = extras.getDouble(Constantes.extra_transporte_destino_latitud);
            destino_longitud = extras.getDouble(Constantes.extra_transporte_destino_longitud);
            latitud_actual   = extras.getDouble(Constantes.extra_transporte_ultima_latitud);
            longitud_actual  = extras.getDouble(Constantes.extra_transporte_ultima_longitud);
            modo = extras.getString(Constantes.extra_transporte_modo);
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
        marker.snippet("Population: 776733");
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

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
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

        if (modo.equals("Destino")){
            mapa_transporte_info.animateCamera(CameraUpdateFactory.newLatLngZoom(lugarEnMapa,20));
            mapa_transporte_info.addMarker(marker).showInfoWindow();
        } else {
            mapa_transporte_info.addMarker(marker);
        }

        if (latitud_actual != 0 && longitud_actual != 0) {
            lugarEnMapa = new LatLng(latitud_actual, longitud_actual);

            marker = new MarkerOptions();
            marker.position(lugarEnMapa);
            marker.title("Actual");
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.camion));

            mapa_transporte_info.addMarker(marker);
        }
    }
}
