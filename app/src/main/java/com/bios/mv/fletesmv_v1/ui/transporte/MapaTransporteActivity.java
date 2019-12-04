package com.bios.mv.fletesmv_v1.ui.transporte;

import android.os.Bundle;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaTransporteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mapa_transporte_info;

    private String extra_transporte_origen_latitud = Constantes.getExtra_transporte_origen_latitud();
    private String extra_transporte_origen_longitud = Constantes.getExtra_transporte_origen_longitud();
    private String extra_transporte_destino_latitud = Constantes.getExtra_transporte_destino_latitud();
    private String extra_transporte_destino_longitud = Constantes.getExtra_transporte_destino_longitud();
    private String extra_transporte_modo = Constantes.getExtra_transporte_modo();

    private double origen_latitud;
    private double origen_longitud;
    private double destino_latitud;
    private double destino_longitud;
    private String modo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mapa_transporte);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            origen_latitud = extras.getDouble(extra_transporte_origen_latitud);
            origen_longitud = extras.getDouble(extra_transporte_origen_longitud);
            destino_latitud = extras.getDouble(extra_transporte_destino_latitud);
            destino_longitud = extras.getDouble(extra_transporte_destino_longitud);
            modo = extras.getString(extra_transporte_modo);
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

        mapa_transporte_info.addMarker(marker);

        if (modo.equals("Origen"))
            mapa_transporte_info.animateCamera(CameraUpdateFactory.newLatLngZoom(lugarEnMapa,20));

        lugarEnMapa = new LatLng(destino_latitud, destino_longitud);

        marker = new MarkerOptions();
        marker.position(lugarEnMapa);
        marker.title("Destino");

        mapa_transporte_info.addMarker(marker);

        if (modo.equals("Destino"))
            mapa_transporte_info.animateCamera(CameraUpdateFactory.newLatLngZoom(lugarEnMapa,20));
    }
}
