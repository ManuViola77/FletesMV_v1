package com.bios.mv.fletesmv_v1.ui.transporte;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bios.mv.fletesmv_v1.Procedimientos;
import com.bios.mv.fletesmv_v1.R;
import com.bios.mv.fletesmv_v1.bd.Constantes;
import com.bios.mv.fletesmv_v1.bd.Transporte;
import com.bios.mv.fletesmv_v1.bd.adapter.TransporteAdapter;
import com.bios.mv.fletesmv_v1.bd.converter.TransporteConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TransporteFragment extends Fragment {
    private TransporteViewModel transporteViewModel;
    private View root;
    private RequestQueue requestQueue;
    private TransporteAdapter transporteAdapter;

    private static String URL_TRANSPORTES = Constantes.getUrlTransportes();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        transporteViewModel =
                ViewModelProviders.of(this).get(TransporteViewModel.class);
        root = inflater.inflate(R.layout.fragment_transporte, container, false);

        final TextView textView = root.findViewById(R.id.text_transporte);
        transporteViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        transporteAdapter = new TransporteAdapter();
        transporteAdapter.setTransporteSelectedListener(new TransporteAdapter.TransporteSelectedListener() {
            @Override
            public void onTransporteSelected(Transporte transporte) {
                // evento que ocurre cuando se selecciona un traslado
                manejarTransporteSelecionado(transporte);
            }
        });

        RecyclerView rv = root.findViewById(R.id.transportes_rv);
        rv.setAdapter(transporteAdapter);
        rv.setLayoutManager(new LinearLayoutManager(root.getContext(), RecyclerView.VERTICAL, false));
        rv.setItemAnimator(new DefaultItemAnimator());

        // Inicializo cola de solicitudes
        requestQueue = Volley.newRequestQueue(root.getContext());

        // Crear solicitud
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                URL_TRANSPORTES,
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
    }

    private void manejarTransporteSelecionado(Transporte transporte) {
        // cuando selecciono un traslado abro la actividad de transporte mediante un intent
        // para saber el transporte seleccionado le mando el id del transporte como dato extra al intent
        Intent intent = new Intent(root.getContext(),TransporteActivity.class);
        intent.putExtra(TransporteActivity.TRANSPORTE_KEY,Integer.toString(transporte.getId()));
        startActivity(intent);
    }

    private void manejarRespuesta(JSONArray respuesta) {
        List<Transporte> transportes =
                TransporteConverter.convertFromJsonObject(respuesta);

        transporteAdapter.setTransportes(transportes);
        transporteAdapter.notifyDataSetChanged();
    }

    private void manejarError(VolleyError volleyError) {
        Toast.makeText(root.getContext(),
                "No se pudo consultar los datos, "+volleyError.getMessage(),
                Toast.LENGTH_LONG).show();
    }
}
