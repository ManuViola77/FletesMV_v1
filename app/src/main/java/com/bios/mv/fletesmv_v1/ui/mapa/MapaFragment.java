package com.bios.mv.fletesmv_v1.ui.mapa;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bios.mv.fletesmv_v1.Procedimientos;
import com.bios.mv.fletesmv_v1.R;
import com.bios.mv.fletesmv_v1.bd.Constantes;
import com.bios.mv.fletesmv_v1.bd.Transporte;
import com.bios.mv.fletesmv_v1.bd.TrasladoBD;
import com.bios.mv.fletesmv_v1.bd.converter.TransporteConverter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;

import java.util.List;

public class MapaFragment extends Fragment implements OnMapReadyCallback {

    private MapaViewModel mapaViewModel;
    private View rootView;
    private GoogleMap mapa_transportes;

    private RequestQueue requestQueue;

    private TrasladoBD trasladoBD;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mapaViewModel =
                ViewModelProviders.of(this).get(MapaViewModel.class);
        rootView = inflater.inflate(R.layout.fragment_mapa, container, false);
        final TextView textView = rootView.findViewById(R.id.text_mapa);
        mapaViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        trasladoBD = new TrasladoBD(rootView.getContext());

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapa_traslados);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // intento obtener los datos por la API si tengo internet
        if (Procedimientos.tieneConexionInternet(rootView.getContext())) {
            // Inicializo cola de solicitudes
            requestQueue = Volley.newRequestQueue(rootView.getContext());

            // Crear solicitud
            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    Constantes.URL_TRANSPORTES,
                    null, // codigo para datos del post
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            manejarRespuesta(response);
                        } // codigo de success
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            manejarError(error);
                        } // codigo de error
                    }
            );

            requestQueue.add(request);
        } else {
            // Si no tengo interenet obtengo los datos de la base de datos sqlite
            List<Transporte> transportes = trasladoBD.getTraslados();

            if (transportes != null)
                setOrigenes(transportes);
            else
                Toast.makeText(rootView.getContext(),
                        "No se pudo consultar los datos de los traslados ",
                        Toast.LENGTH_LONG).show();
        }
    }

    private void manejarRespuesta(JSONArray respuesta) {
        List<Transporte> transportes = TransporteConverter.convertFromJsonObject(respuesta);
        if (transportes != null)
            setOrigenes(transportes);
        else
            Toast.makeText(rootView.getContext(),
                    "No se pudo consultar los datos de los traslados ",
                    Toast.LENGTH_LONG).show();
    }

    private void setOrigenes(List<Transporte> transportes) {
        if (Procedimientos.tieneConexionInternet(rootView.getContext() )) {
            for (Transporte transporte : transportes) {
                agregarOrigenAMapa(transporte);
            }
        } else {
            Toast.makeText(rootView.getContext(),
                    "No se puede mostrar el mapa debido a que no hay conexion a internet",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void manejarError(VolleyError volleyError) {
        Toast.makeText(rootView.getContext(),
                "No se pudo consultar los datos, "+volleyError.getMessage(),
                Toast.LENGTH_LONG).show();

        List<Transporte> transportes = trasladoBD.getTraslados();
        if (transportes != null)
            setOrigenes(transportes);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa_transportes = googleMap;

        configureMap();
    }

    private void configureMap() {
        UiSettings uiSettings = mapa_transportes.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
    }

    private void agregarOrigenAMapa(Transporte transporte) {

        double origen_latitud = transporte.getOrigen_latitud();
        double origen_longitud = transporte.getOrigen_longitud();

        LatLng lugarEnMapa = new LatLng(origen_latitud, origen_longitud);

        MarkerOptions marker = new MarkerOptions();
        marker.position(lugarEnMapa);
        marker.title("Traslado "+transporte.getId());

        if (mapa_transportes != null)
            mapa_transportes.addMarker(marker);
    }

}